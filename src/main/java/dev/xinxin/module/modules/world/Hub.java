package dev.xinxin.module.modules.world;

import dev.xinxin.Client;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.combat.KillAura;
import dev.xinxin.module.modules.player.ChestStealer;
import dev.xinxin.module.modules.player.InvCleaner;
import dev.xinxin.module.values.BoolValue;

public class Hub
        extends Module {
    public BoolValue SB = new BoolValue("谁用谁SB", true);

    public Hub() {
        super("Hub", Category.World);
    }

    @Override
    public void onEnable() {
        //DebugUtil.log("一位[ ", AutoDiYuQiShi.getLocation() + " 省人]-回到了大厅");
        Client.instance.moduleManager.getModule(InvCleaner.class).setState(false);
        Client.instance.moduleManager.getModule(ChestStealer.class).setState(false);
        Client.instance.moduleManager.getModule(KillAura.class).setState(false);
        Client.instance.moduleManager.getModule(ChestAura.class).setState(false);
        Client.instance.moduleManager.getModule(Scaffold.class).setState(false);
        mc.thePlayer.sendChatMessage("/hub");
        Client.instance.moduleManager.getModule(Hub.class).setState(false);
    }

}

