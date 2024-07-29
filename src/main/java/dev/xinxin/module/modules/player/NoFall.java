package dev.xinxin.module.modules.player;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventMotion;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.ModeValue;

public class NoFall
extends Module {
    ModeValue<modeEnum> mode = new ModeValue("Mode", modeEnum.values(), modeEnum.HypixelSpoof);

    public NoFall() {
        super("NoFall", Category.Player);
    }

    @EventTarget
    public void onUpdate(EventMotion e) {
        this.setSuffix(this.mode.getValue().toString());
        switch (this.mode.getValue()) {
            case HypixelSpoof: {
                if (NoFall.mc.thePlayer.ticksExisted <= 50 || !(NoFall.mc.thePlayer.fallDistance > 3.0f)) break;
                e.setOnGround(true);
                break;
            }
            case GroundSpoof: {
                if (NoFall.mc.thePlayer.onGround) break;
                e.setOnGround(true);
            }
        }
    }

    enum modeEnum {
        HypixelSpoof,
        GroundSpoof

    }
}

