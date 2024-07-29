package dev.xinxin.module.modules.misc;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.utils.DebugUtil;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;

public class PacketDebugger
extends Module {
    private final BoolValue c07Value = new BoolValue("C07-StopBlocking", true);
    private final BoolValue c08Value = new BoolValue("C08-StartBlocking", true);

    public PacketDebugger() {
        super("PacketDebugger", Category.Misc);
    }

    @EventTarget
    public void onPacketSend(EventPacketSend eventPacketSend) {
        Packet packet = eventPacketSend.getPacket();
        if (packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM && ((Boolean)this.c07Value.getValue()).booleanValue()) {
            DebugUtil.log("[PacketDebugger]C07-StopBlocking");
        }
        if (packet instanceof C08PacketPlayerBlockPlacement && PacketDebugger.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && ((Boolean)this.c08Value.getValue()).booleanValue()) {
            DebugUtil.log("[PacketDebugger]C08-StartBlocking");
        }
    }
}

