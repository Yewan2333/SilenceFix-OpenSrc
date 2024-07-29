package dev.xinxin.module.modules.misc;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventUpdate;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.client.MathUtil;
import dev.xinxin.utils.component.PingSpoofComponent;

public class PingSpoof
extends Module {
    private final NumberValue minDelay = new NumberValue("MinDelay", 50.0, 50.0, 30000.0, 50.0);
    private final NumberValue maxDelay = new NumberValue("MaxDelay", 50.0, 50.0, 30000.0, 50.0);
    private final BoolValue teleports = new BoolValue("DelayTeleports", false);
    private final BoolValue velocity = new BoolValue("DelayVelocity", false);
    private final BoolValue world = new BoolValue("DelayBlockUpdates", false);
    private final BoolValue entities = new BoolValue("DelayEntityMovements", false);

    public PingSpoof() {
        super("PingSpoof", Category.Misc);
    }

    @Override
    public void onEnable() {
        PingSpoofComponent.spoofing = true;
    }

    @Override
    public void onDisable() {
        PingSpoofComponent.spoofing = false;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        PingSpoofComponent.setSpoofing(MathUtil.getRandom(((Double)this.minDelay.getValue()).intValue(), ((Double)this.maxDelay.getValue()).intValue()), true, (Boolean)this.teleports.getValue(), (Boolean)this.velocity.getValue(), (Boolean)this.world.getValue(), (Boolean)this.entities.getValue());
    }
}

