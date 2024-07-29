package dev.xinxin.module.modules.movement;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventStrafe;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;

public class Sprint
extends Module {
    public Sprint() {
        super("Sprint", Category.Movement);
        this.setState(true);
    }

    @EventTarget
    public void onUpdate(EventStrafe event) {
        Sprint.mc.gameSettings.keyBindSprint.pressed = true;
    }
}

