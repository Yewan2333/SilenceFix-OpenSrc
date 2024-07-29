package dev.xinxin.module.modules.player;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.rendering.EventRender3D;
import dev.xinxin.event.world.EventPacketReceive;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.event.world.EventUpdate;
import dev.xinxin.gui.notification.NotificationManager;
import dev.xinxin.gui.notification.NotificationType;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.ModeValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.BlinkUtils;
import dev.xinxin.utils.client.PacketUtil;
import dev.xinxin.utils.client.TimeUtil;
import dev.xinxin.utils.render.RenderUtil;
import java.awt.Color;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C16PacketClientStatus;
import org.lwjgl.opengl.GL11;

public class Blink
extends Module {
    private final ModeValue<PacketModeValues> packetModeValue = new ModeValue("PacketMode", PacketModeValues.values(), PacketModeValues.All);
    private final BoolValue pulseValue = new BoolValue("Pulse", false);
    private final NumberValue pulseDelayValue = new NumberValue("PulseDelay", 1000.0, 100.0, 5000.0, 100.0);
    private final BoolValue noC00C16 = new BoolValue("NoC00-C16", false);
    private final BoolValue packetKickBypass = new BoolValue("PacketKickBypass", false);
    private final TimeUtil pulseTimer = new TimeUtil();
    private static EntityOtherPlayerMP fakePlayer = null;
    private final LinkedList<double[]> positions = new LinkedList();
    private final LinkedBlockingQueue<Packet<INetHandlerPlayClient>> packets = new LinkedBlockingQueue();

    public Blink() {
        super("Blink", Category.Player);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onEnable() {
        NotificationManager.post(NotificationType.INFO,"","这个功能有bug");
        if (Blink.mc.thePlayer == null) {
            return;
        }
        if (this.packetModeValue.getValue() == PacketModeValues.OutGoing || this.packetModeValue.getValue() == PacketModeValues.All) {
            BlinkUtils.setBlinkState(false, false, true, false, false, false, false, false, false, false, false);
            if (!this.pulseValue.getValue().booleanValue()) {
                fakePlayer = new EntityOtherPlayerMP(Blink.mc.theWorld, Blink.mc.thePlayer.gameProfile);
                fakePlayer.clonePlayer(Blink.mc.thePlayer, true);
                fakePlayer.copyLocationAndAnglesFrom(Blink.mc.thePlayer);
                Blink.fakePlayer.rotationYawHead = Blink.mc.thePlayer.rotationYawHead;
                Blink.mc.theWorld.addEntityToWorld(-1337, fakePlayer);
            }
        }
        this.packets.clear();
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Blink.mc.thePlayer.posX, Blink.mc.thePlayer.getEntityBoundingBox().minY + (double)(Blink.mc.thePlayer.getEyeHeight() / 2.0f), Blink.mc.thePlayer.posZ});
            this.positions.add(new double[]{Blink.mc.thePlayer.posX, Blink.mc.thePlayer.getEntityBoundingBox().minY, Blink.mc.thePlayer.posZ});
        }
        this.pulseTimer.reset();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @Override
    public void onDisable() {
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.clear();
        }
        if (Blink.mc.thePlayer == null) {
            return;
        }
        BlinkUtils.setBlinkState(true, true, false, false, false, false, false, false, false, false, false);
        BlinkUtils.clearPacket(null, false, -1);
        this.packets.clear();
        if (this.packetModeValue.getValue() == PacketModeValues.InBound || this.packetModeValue.getValue() == PacketModeValues.All) {
            this.clearPackets();
        }
        if (fakePlayer != null) {
            Blink.mc.theWorld.removeEntityFromWorld(fakePlayer.getEntityId());
            fakePlayer = null;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onUpdate(EventUpdate event) {
        this.setSuffix(BlinkUtils.bufferSize(null));
        setState(false);
        LinkedList<double[]> linkedList = this.positions;
        synchronized (linkedList) {
            this.positions.add(new double[]{Blink.mc.thePlayer.posX, Blink.mc.thePlayer.getEntityBoundingBox().minY, Blink.mc.thePlayer.posZ});
        }
        if (this.packetKickBypass.getValue().booleanValue() && Blink.mc.thePlayer.ticksExisted % 2 == 1) {
            PacketUtil.sendPacketC0F(true);
        }
        if (this.pulseValue.getValue().booleanValue() && this.pulseTimer.hasReached(this.pulseDelayValue.getValue().longValue())) {
            linkedList = this.positions;
            synchronized (linkedList) {
                this.positions.clear();
            }
            BlinkUtils.releasePacket(null, false, -1, 0);
            if (this.packetModeValue.getValue() == PacketModeValues.InBound || this.packetModeValue.getValue() == PacketModeValues.All) {
                this.clearPackets();
            }
            this.pulseTimer.reset();
        }
    }

    private void clearPackets() {
        while (!this.packets.isEmpty()) {
            try {
                PacketUtil.handlePacket(this.packets.take());
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        Packet<?> packet = event.getPacket();
        if ((this.packetModeValue.getValue() == PacketModeValues.InBound || this.packetModeValue.getValue() == PacketModeValues.All) && packet.getClass().getSimpleName().startsWith("S")) {
            if (Blink.mc.thePlayer.ticksExisted < 20) {
                return;
            }
            event.setCancelled(true);
            this.packets.add((Packet<INetHandlerPlayClient>) packet);
        }
    }

    @EventTarget
    public void onPacketSend(EventPacketSend event) {
        if ((this.packetModeValue.getValue() == PacketModeValues.OutGoing || this.packetModeValue.getValue() == PacketModeValues.All) && (event.getPacket() instanceof C16PacketClientStatus || event.getPacket() instanceof C00PacketKeepAlive && this.noC00C16.getValue().booleanValue())) {
            event.setCancelled();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (this.packetModeValue.getValue() == PacketModeValues.OutGoing || this.packetModeValue.getValue() == PacketModeValues.All) {
            LinkedList<double[]> linkedList = this.positions;
            synchronized (linkedList) {
                GL11.glPushMatrix();
                GL11.glDisable(3553);
                GL11.glBlendFunc(770, 771);
                GL11.glEnable(2848);
                GL11.glEnable(3042);
                GL11.glDisable(2929);
                Blink.mc.entityRenderer.disableLightmap();
                GL11.glLineWidth(2.0f);
                GL11.glBegin(3);
                RenderUtil.glColor(new Color(68, 131, 123, 255).getRGB());
                double renderPosX = Blink.mc.getRenderManager().viewerPosX;
                double renderPosY = Blink.mc.getRenderManager().viewerPosY;
                double renderPosZ = Blink.mc.getRenderManager().viewerPosZ;
                for (double[] pos : this.positions) {
                    GL11.glVertex3d(pos[0] - renderPosX, pos[1] - renderPosY, pos[2] - renderPosZ);
                }
                GL11.glColor4d(1.0, 1.0, 1.0, 1.0);
                GL11.glEnd();
                GL11.glEnable(2929);
                GL11.glDisable(2848);
                GL11.glDisable(3042);
                GL11.glEnable(3553);
                GL11.glPopMatrix();
            }
        }
    }

    public enum PacketModeValues {
        All,
        InBound,
        OutGoing

    }
}

