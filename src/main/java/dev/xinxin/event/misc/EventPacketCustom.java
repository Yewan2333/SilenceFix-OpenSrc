package dev.xinxin.event.misc;

import dev.xinxin.event.api.events.Event;
import lombok.Getter;
import net.minecraft.network.Packet;

@Getter
public class EventPacketCustom implements Event {
    Packet packet;

    public EventPacketCustom(Packet packet) {
        this.packet = packet;
    }

}
