package dev.xinxin.module.modules.combat.velocity;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.attack.EventAttack;
import dev.xinxin.event.world.EventPacketReceive;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.event.world.EventTick;
import dev.xinxin.event.world.EventUpdate;
import dev.xinxin.event.world.EventWorldLoad;
import dev.xinxin.module.Category;

public class JumpResetVelocity
extends VelocityMode {
    public JumpResetVelocity() {
        super("JumpReset", Category.Combat);
    }

    @Override
    public String getTag() {
        return "JumpReset";
    }

    @Override
    public void onAttack(EventAttack event) {
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onPacketSend(EventPacketSend event) {
    }

    @Override
    public void onWorldLoad(EventWorldLoad event) {
    }

    @Override
    public void onUpdate(EventUpdate event) {
        if (this.mc.thePlayer.onGround && this.mc.thePlayer.hurtTime > 0) {
            this.mc.thePlayer.setSprinting(false);
            this.mc.thePlayer.movementInput.jump = true;
        }
    }

    @Override
    public void onTick(EventTick event) {
    }

    @Override
    @EventTarget
    public void onPacketReceive(EventPacketReceive e) {
    }
}

