package dev.xinxin.event.misc;

import dev.xinxin.event.api.events.callables.EventCancellable;

public class EventKey
extends EventCancellable {
    private int key;

    public EventKey(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }
}

