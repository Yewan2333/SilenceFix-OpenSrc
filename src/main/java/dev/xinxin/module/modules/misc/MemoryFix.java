package dev.xinxin.module.modules.misc;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventTick;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.client.TimeUtil;
import dev.xinxin.utils.system.MemoryUtils;

public class MemoryFix
extends Module {
    private final NumberValue cleanUpDelay = new NumberValue("CleanUpDelay", 120.0, 10.0, 600.0, 1.0);
    private final NumberValue cleanUpLimit = new NumberValue("CleanUpLimit", 80.0, 20.0, 95.0, 1.0);
    private final TimeUtil cleanUpDelayTime = new TimeUtil();

    public MemoryFix() {
        super("MemoryFix", Category.Misc);
    }

    @EventTarget
    public void onTick(EventTick event) {
        long maxMem = Runtime.getRuntime().maxMemory();
        long totalMem = Runtime.getRuntime().totalMemory();
        long freeMem = Runtime.getRuntime().freeMemory();
        long usedMem = totalMem - freeMem;
        if (this.cleanUpDelayTime.hasReached((Double)this.cleanUpDelay.getValue() * 1000.0) && (Double)this.cleanUpLimit.getValue() <= (double)(usedMem * 100L / maxMem)) {
            MemoryUtils.memoryCleanup();
            this.cleanUpDelayTime.reset();
        }
    }
}

