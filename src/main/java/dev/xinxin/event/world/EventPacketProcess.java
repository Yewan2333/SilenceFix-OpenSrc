package dev.xinxin.event.world;

import dev.xinxin.event.api.events.callables.EventCancellable;
import net.minecraft.network.Packet;

public class EventPacketProcess
extends EventCancellable {
    public Packet packet;

    public EventPacketProcess(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return this.packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }
}

