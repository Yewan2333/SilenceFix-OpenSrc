package dev.xinxin.module.modules.misc;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.misc.EventPacketCustom;
import dev.xinxin.event.misc.EventSendChatMessage;
import dev.xinxin.hyt.HYTProvider;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.utils.client.HelperUtil;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;

public class Protocol extends Module {
    public Protocol() {
        super("Protocol", Category.Misc);
    }
    @EventTarget
    public void onPacketCustom(EventPacketCustom event) {
        HYTProvider.onPacket(event);
    }
    @EventTarget
    public void onSendChatMessage(EventSendChatMessage e) {
        if (e.getMsg().contains("/kh")) {
            HelperUtil.sendMessage("\u6253\u5f00\u7ec4\u961f\u9875\u9762");
            HYTProvider.sendOpenParty();
            e.setCancelled();
        }
    }
    private IChatComponent createClickableText(String text, String command) {
        ChatComponentText clickableText = new ChatComponentText(text);
        clickableText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        return clickableText;
    }
}
