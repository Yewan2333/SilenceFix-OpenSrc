package dev.xinxin.event.world;

import dev.xinxin.event.api.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketReceive
extends EventCancellable {
    private Packet<?> packet;

    public EventPacketReceive(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return this.packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }
}

