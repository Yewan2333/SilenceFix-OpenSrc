package dev.xinxin.module.modules.player;

@FunctionalInterface
public interface Listener<Event> {
    void call(Event event);
}
