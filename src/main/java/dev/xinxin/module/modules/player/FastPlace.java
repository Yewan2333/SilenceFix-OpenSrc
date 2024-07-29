package dev.xinxin.module.modules.player;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventTick;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.NumberValue;
import net.minecraft.item.ItemBlock;

public class FastPlace
extends Module {
    private final NumberValue ticks = new NumberValue("Ticks", 0.0, 4.0, 0.0, 1.0);

    public FastPlace() {
        super("FastPlace", Category.Player);
    }

    @EventTarget
    public void onTick(EventTick e) {
        if (FastPlace.mc.thePlayer.getHeldItem().getItem() instanceof ItemBlock) {
            FastPlace.mc.rightClickDelayTimer = Math.min(0, this.ticks.getValue().intValue());
        }
    }
}

