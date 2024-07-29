package dev.xinxin.event.misc;

import dev.xinxin.event.api.events.Event;

public class EventMouseOver
implements Event {
    private double range;

    public double getRange() {
        return this.range;
    }

    public void setRange(double range) {
        this.range = range;
    }
}

