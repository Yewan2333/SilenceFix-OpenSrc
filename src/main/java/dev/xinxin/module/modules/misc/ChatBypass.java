package dev.xinxin.module.modules.misc;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;

public final class ChatBypass
extends Module {
    public ChatBypass() {
        super("ChatBypass", Category.Misc);
    }

    @EventTarget
    public void onPacketSend(EventPacketSend event) {
        if (ChatBypass.mc.thePlayer == null || ChatBypass.mc.theWorld == null) {
            return;
        }
        Packet packet = event.getPacket();
        if (packet instanceof C01PacketChatMessage) {
            String message = ((C01PacketChatMessage)packet).getMessage();
            if (message.startsWith("/")) {
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            for (char c : message.toCharArray()) {
                if (c == '@') {
                    stringBuilder.append(c);
                    continue;
                }
                if (c >= '!' && c <= '\u0080') {
                    stringBuilder.append(Character.toChars(c + 65248));
                    continue;
                }
                stringBuilder.append(c);
            }
            ((C01PacketChatMessage)packet).message = stringBuilder.toString();
        }
    }
}

