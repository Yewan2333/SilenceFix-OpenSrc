package dev.xinxin.module.modules.misc;

import dev.xinxin.Client;
import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventMotion;
import dev.xinxin.event.world.EventPacketReceive;
import dev.xinxin.event.world.EventWorldLoad;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.combat.KillAura;
import dev.xinxin.module.modules.player.ChestStealer;
import dev.xinxin.module.modules.player.InvCleaner;
import dev.xinxin.module.modules.world.ChestAura;
import dev.xinxin.module.modules.world.Scaffold;
import dev.xinxin.module.values.BoolValue;
import net.minecraft.network.play.server.S01PacketJoinGame;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.world.WorldSettings;

public class ModuleHelper
extends Module {
    private final BoolValue lagBackCheckValue = new BoolValue("LagBackCheck", false);

    public ModuleHelper() {
        super("ModuleHelper", Category.Misc);
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        if (event.getPacket() instanceof S01PacketJoinGame || event.getPacket() instanceof S08PacketPlayerPosLook && (Boolean) this.lagBackCheckValue.getValue()) {
            this.disableModule();
        }
    }
    private void handleChatPacket(S02PacketChat chatPacket) {
        String text = chatPacket.chatComponent.getUnformattedText();

        if (text.contains("开始倒计时: 1 秒")) {
            skenableModule();
        } else if (text.contains("你在地图") && text.contains("赢得了")) {
            handleWin();
        } else if (text.contains("[起床战争] Game 结束！感谢您的参与！") || text.contains("喜欢 一般 不喜欢")) {
            disableModule();
        } else if (text.contains("玩家") && text.contains("在本局游戏中行为异常")) {
            mc.thePlayer.sendChatMessage("陈安建写的外挂打不过又在手动ban人了呢");
        }
    }
    private void handleWin() {
        disableModule();
    }
    @EventTarget
    public void onWorld(EventWorldLoad event) {
        this.disableModule();
    }

    public void skenableModule() {
        if (Client.instance.moduleManager.getModule(KillAura.class).state) {
            Client.instance.moduleManager.getModule(KillAura.class).setState(true);
        }
        if (Client.instance.moduleManager.getModule(InvCleaner.class).state) {
            Client.instance.moduleManager.getModule(InvCleaner.class).setState(true);
        }
        if (Client.instance.moduleManager.getModule(ChestStealer.class).state) {
            Client.instance.moduleManager.getModule(ChestStealer.class).setState(true);
        }
        if (Client.instance.moduleManager.getModule(Scaffold.class).state) {
            Client.instance.moduleManager.getModule(Scaffold.class).setState(true);
        }
    }



    public void disableModule() {
        if (Client.instance.moduleManager.getModule(KillAura.class).state) {
            Client.instance.moduleManager.getModule(KillAura.class).setState(false);
        }
        if (Client.instance.moduleManager.getModule(InvCleaner.class).state) {
            Client.instance.moduleManager.getModule(InvCleaner.class).setState(false);
        }
        if (Client.instance.moduleManager.getModule(ChestStealer.class).state) {
            Client.instance.moduleManager.getModule(ChestStealer.class).setState(false);
        }
        if (Client.instance.moduleManager.getModule(Scaffold.class).state) {
            Client.instance.moduleManager.getModule(Scaffold.class).setState(false);
        }
        if (Client.instance.moduleManager.getModule(ChestAura.class).state) {
            Client.instance.moduleManager.getModule(ChestAura.class).setState(false);
        }
    }

    @EventTarget
    public void onMotion(EventMotion event) {
        if (ModuleHelper.mc.playerController.getCurrentGameType() == WorldSettings.GameType.SPECTATOR) {
            if (Client.instance.moduleManager.getModule(InvCleaner.class).state) {
                Client.instance.moduleManager.getModule(InvCleaner.class).setState(false);
            }
            if (Client.instance.moduleManager.getModule(ChestStealer.class).state) {
                Client.instance.moduleManager.getModule(ChestStealer.class).setState(false);
            }
            if (Client.instance.moduleManager.getModule(ChestAura.class).state) {
                Client.instance.moduleManager.getModule(ChestAura.class).setState(false);
            }
        }
    }
}

