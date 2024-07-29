package dev.xinxin.module.modules.movement;

import dev.xinxin.Client;
import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventPacketReceive;
import dev.xinxin.event.world.EventUpdate;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.player.Bl1nk;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGlass;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S45PacketTitle;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public class AutoClip
extends Module {
    /*private final ModeValue<mode> modeValue = new ModeValue("Mode", mode.values(), mode.Tick);
    private final ModeValue<upmpdes> upMode = new ModeValue("UP Mode", upmpdes.values(), upmpdes.SetPosition);
    private final NumberValue high = new NumberValue("High", 2.0, 1.0, 20.0, 1.0);
    private final NumberValue flySpeed = new NumberValue("Fly Speed", 2.0, 1.0, 20.0, 1.0);
    private final NumberValue delay = new NumberValue("Delay", 50.0, 1.0, 2000.0, 1.0);
    private final BoolValue flyValue = new BoolValue("Fly", false);
    private final BoolValue reSize = new BoolValue("ReSize Window", false);
    private boolean teleporting = false;
    private boolean shouldFly = false;
    private int width;
    private int height;*/

    public AutoClip() {
        super("AutoClip", Category.Movement);
    }

    @EventTarget
    public void onUpdata(EventUpdate e){
        if (Client.instance.moduleManager.getModule(Bl1nk.class).getState()){
            for (Map.Entry<BlockPos, ?> block : searchBlocks(3).entrySet()) {
                BlockPos blockpos = block.getKey();
                if (block.getValue() instanceof BlockGlass) {
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, blockpos, EnumFacing.DOWN));
                    mc.getNetHandler().getNetworkManager().sendPacket(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.STOP_DESTROY_BLOCK, blockpos, EnumFacing.DOWN));
                    mc.theWorld.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
                }
            }
        }
    }

    @EventTarget
    public void onPacket(EventPacketReceive event){
        Packet<?> packet = event.getPacket();
        if (packet instanceof S45PacketTitle){
            S45PacketTitle s45 = (S45PacketTitle)packet;
            if (s45.getMessage().getUnformattedText().equals("\u00a7\u6218\u6597\u5f00\u59cb...")){
                Client.instance.moduleManager.getModule(Bl1nk.class).setState(false);
            }
        }
        if (packet instanceof S02PacketChat) {
            String text = ((S02PacketChat) packet).getChatComponent().getUnformattedText();
            if (text.contains("开始倒计时: 3 秒")){
                Client.instance.moduleManager.getModule(Bl1nk.class).setState(true);
            }
        }
    }

    public static Map<BlockPos, Block> searchBlocks(int radius) {
        Map<BlockPos, Block> blocks = new HashMap();

        for(int x = radius; x > -radius; --x) {
            for(int y = radius; y > -radius; --y) {
                for(int z = radius; z > -radius; --z) {
                    BlockPos blockPos = new BlockPos(mc.thePlayer.lastTickPosX + (double)x, mc.thePlayer.lastTickPosY + (double)y, mc.thePlayer.lastTickPosZ + (double)z);
                    Block block = getBlock(blockPos);
                    blocks.put(blockPos, block);
                }
            }
        }

        return blocks;
    }
    public static Block getBlock(BlockPos blockPos) {
        return mc.theWorld.getBlockState(blockPos).getBlock();
    }

    /*@Override
    public void onEnable() {
        this.teleporting = false;
        this.shouldFly = false;
    }

    @EventTarget
    public void onWorld(EventWorldLoad event) {
        this.shouldFly = false;
        this.width = AutoClip.mc.displayWidth;
        this.height = AutoClip.mc.displayHeight;
        try {
            Robot robot = new Robot();
            robot.keyPress(Keyboard.KEY_LMETA);
            robot.keyPress(Keyboard.KEY_UP);
            DebugUtil.log("release up");
            robot.keyRelease(Keyboard.KEY_UP);
            robot.keyPress(Keyboard.KEY_DOWN);
            DebugUtil.log("release down");
            robot.keyRelease(Keyboard.KEY_DOWN);
            DebugUtil.log("release m");
            robot.keyRelease(Keyboard.KEY_LMETA);
        }
        catch (AWTException e) {
            throw new RuntimeException(e);
        }
        this.teleporting = false;
    }

    @EventTarget(value=0)
    public void onMotion(EventMotion e) {
        if (e.isPre()) {
            if (this.modeValue.is("Tick") && AutoClip.mc.thePlayer.ticksExisted <= 2) {
                if (this.reSize.getValue().booleanValue()) {
                    AutoClip.mc.displayWidth = 800;
                    AutoClip.mc.displayHeight = 600;
                }
                BlockPos pos = new BlockPos(AutoClip.mc.thePlayer.posX, AutoClip.mc.thePlayer.posY + 2.0, AutoClip.mc.thePlayer.posZ);
                BlockPos higherPos = new BlockPos(AutoClip.mc.thePlayer.posX, AutoClip.mc.thePlayer.posY + 3.0, AutoClip.mc.thePlayer.posZ);
                if (AutoClip.mc.theWorld.getBlockState(pos).getBlock() == Blocks.glass || AutoClip.mc.theWorld.getBlockState(higherPos).getBlock() == Blocks.glass) {
                    this.teleporting = true;
                    this.shouldFly = true;
                    this.up();
                    NotificationManager.post(NotificationType.SUCCESS, "AutoClip", "Clip UP!", 5.0f);
                    this.teleporting = false;
                }
            }
            if (this.flyValue.getValue().booleanValue()) {
                if (AutoClip.mc.thePlayer.capabilities.allowFlying) {
                    if (this.shouldFly) {
                        double vanillaSpeed = this.flySpeed.getValue();
                        AutoClip.mc.thePlayer.motionY = 0.0;
                        AutoClip.mc.thePlayer.motionX = 0.0;
                        AutoClip.mc.thePlayer.motionZ = 0.0;
                        MoveUtil.setSpeed(vanillaSpeed);
                    }
                } else {
                    this.shouldFly = false;
                }
            }
        }
    }

    private void up() {
        BlinkUtils.setBlinkState(false, false, true, false, false, false, false, false, false, false, false);
        switch (this.upMode.get()) {
            case SetPosition: {
                AutoClip.mc.thePlayer.setPosition(AutoClip.mc.thePlayer.posX, AutoClip.mc.thePlayer.posY + this.high.getValue(), AutoClip.mc.thePlayer.posZ);
                break;
            }
            case SetPositionRotation: {
                AutoClip.mc.thePlayer.setPositionAndRotation(AutoClip.mc.thePlayer.posX, AutoClip.mc.thePlayer.posY + this.high.getValue(), AutoClip.mc.thePlayer.posZ, AutoClip.mc.thePlayer.rotationYaw, AutoClip.mc.thePlayer.rotationPitch);
                break;
            }
            case SetPositionRotation2: {
                AutoClip.mc.thePlayer.setPositionAndRotation2(AutoClip.mc.thePlayer.posX, AutoClip.mc.thePlayer.posY + this.high.getValue(), AutoClip.mc.thePlayer.posZ, AutoClip.mc.thePlayer.rotationYaw, AutoClip.mc.thePlayer.rotationPitch, 3, true);
            }
        }
        BlinkUtils.setBlinkState(true, true, false, false, false, false, false, false, false, false, false);
        BlinkUtils.clearPacket(null, false, -1);
    }

    @EventTarget
    public void onAABB(EventCollideWithBlock e) {
        if (this.teleporting) {
            e.setBoundingBox(null);
        }
    }

    @EventTarget(value=0)
    public void onPacketReceiveEvent(EventPacketReceive event) {
        String text;
        Packet<?> packet = event.getPacket();
        if (event.getPacket() instanceof S02PacketChat && (text = ((S02PacketChat)event.getPacket()).getChatComponent().getUnformattedText()).contains("\u5f00\u59cb\u5012\u8ba1\u65f6: 1 \u79d2")) {
            AutoClip.mc.displayWidth = this.width;
            AutoClip.mc.displayHeight = this.height;
        }
        if (event.getPacket() instanceof S45PacketTitle && this.modeValue.is("Delay")) {
            S45PacketTitle s45 = (S45PacketTitle)packet;
            if (s45.getMessage() == null) {
                return;
            }
            if (s45.getMessage().getUnformattedText().equals("\u00a7a\u6218\u6597\u5f00\u59cb...")) {
                if (this.reSize.getValue().booleanValue()) {
                    AutoClip.mc.displayWidth = 800;
                    AutoClip.mc.displayHeight = 600;
                }
                Timer timer = new Timer();
                TimerTask task = new TimerTask(){

                    @Override
                    public void run() {
                        AutoClip.this.teleporting = true;
                        AutoClip.this.up();
                        NotificationManager.post(NotificationType.SUCCESS, "AutoClip", "Clip UP!", 5.0f);
                        AutoClip.this.teleporting = false;
                    }
                };
                timer.schedule(task, this.delay.getValue().intValue());
            }
        }
    }

    public enum upmpdes {
        SetPosition,
        SetPositionRotation,
        SetPositionRotation2

    }

    public enum mode {
        Delay,
        Tick

    }*/
}

