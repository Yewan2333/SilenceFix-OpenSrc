package dev.xinxin.event.world;

import dev.xinxin.event.api.events.Event;
import net.minecraft.entity.Entity;

public class EventLivingUpdate
implements Event {
    public Entity entity;

    public EventLivingUpdate(Entity targetEntity) {
        this.entity = targetEntity;
    }

    public Entity getEntity() {
        return this.entity;
    }
}

