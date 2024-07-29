package dev.xinxin.module.modules.combat;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.attack.EventAttack;
import dev.xinxin.event.world.EventMotion;
import dev.xinxin.event.world.EventStep;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.player.Blink;
import dev.xinxin.module.values.ModeValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.client.MathUtil;
import dev.xinxin.utils.client.TimeUtil;
import dev.xinxin.utils.player.PlayerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class Criticals
extends Module {
    private final TimeUtil timer = new TimeUtil();
    private final TimeUtil prevent = new TimeUtil();
    private final ModeValue<modeEnums> modeValue = new ModeValue("Mode", modeEnums.values(), modeEnums.Hypixel);
    private final NumberValue hurtTimeValue = new NumberValue("HurtTime", 15.0, 0.0, 20.0, 1.0);
    private final NumberValue delayValue = new NumberValue("Delay", 3.0, 0.0, 10.0, 0.5);
    private final NumberValue timervalue = new NumberValue("TimerValue", 1F, 0.1F, 1F, 0.1);
    private final NumberValue jumpHeight = new NumberValue("JumpHeight", 0.42F, 0.1F, 1.0F, 0.01F);
    private final NumberValue airTime = new NumberValue("AirTime", 10, 5, 20, 1);
    private int groundTicks;

    private int blinkTick = 0;
    private int jump = 0;
    private int airTick = 0;
    private boolean isCriticalHitTimerActive = false;

    public Criticals() {
        super("Criticals", Category.Combat);
    }

    @Override
    public void onEnable() {
        this.timer.reset();
        this.prevent.reset();
        this.groundTicks = 0;
    }

    @EventTarget
    void onUpdate(EventMotion event) {
        this.setSuffix(this.modeValue.getValue().toString());
        this.groundTicks = PlayerUtil.isOnGround(0.01) ? ++this.groundTicks : 0;
        if (this.groundTicks > 20) {
            this.groundTicks = 20;
        }
        if (this.modeValue.getValue() == modeEnums.NoGround) {
            event.setOnGround(false);
        }

        if (this.modeValue.getValue() == modeEnums.Grim) {
            handleGrimMode();
        }
    }

    private void handleGrimMode() {
        Module blink = getModule(Blink.class);
        if (!mc.thePlayer.onGround) {
            airTick++;
        } else {
            airTick = 0;
        }

        if (blink.getState()) {
            blinkTick = 100;
        } else {
            blinkTick--;
        }

        if (isCriticalHitTimerActive && getModule(KillAura.class).getState()) {
            if (airTick == 1) {
                mc.gameSettings.keyBindJump.pressed = false;
            }

            if (airTick >= 6 && airTick <= airTime.getValue().intValue() && !mc.thePlayer.onGround) {
                mc.timer.timerSpeed = timervalue.getValue().floatValue();
            }

            if (airTick > airTime.getValue().intValue() || airTick == 0 || mc.thePlayer.onGround) {
                mc.timer.timerSpeed = 1F;
                isCriticalHitTimerActive = false;
            }
        }

        jump++;
    }

    @EventTarget
    void onStep(EventStep event) {
        if (!event.isPre()) {
            this.prevent.reset();
        }
    }

    @EventTarget
    void onAttack(EventAttack event) {
        boolean isEligibleForCriticalHit = checkCriticalHitConditions(event);

        if (event.isPre() && isEligibleForCriticalHit) {
            performCriticalHit(event);
        }
    }

    private boolean checkCriticalHitConditions(EventAttack event) {
        return this.groundTicks > 3
                && Criticals.mc.theWorld.getBlockState(new BlockPos(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY - 1.0, Criticals.mc.thePlayer.posZ)).getBlock().isFullBlock()
                && !PlayerUtil.isInLiquid()
                && !PlayerUtil.isOnLiquid()
                && !Criticals.mc.thePlayer.isOnLadder()
                && Criticals.mc.thePlayer.ridingEntity == null
                && Criticals.mc.thePlayer.onGround
                && event.getTarget().hurtResistantTime <= this.hurtTimeValue.getValue().intValue()
                && this.prevent.hasPassed(300L)
                && this.timer.hasPassed((long) this.delayValue.getValue().intValue() * 100L);
    }

    private void performCriticalHit(EventAttack event) {
        switch (this.modeValue.getValue()) {
            case Hypixel:
                performHypixelCrit();
                break;
            case HvH:
                performHvHCrit();
                break;
            case Packet:
            case Visual:
                Criticals.mc.thePlayer.onCriticalHit(event.getTarget());
                break;
            case Jump:
                Criticals.mc.thePlayer.jump();
                break;
            case Hop:
                performHopCrit();
                break;
            case Grim:
                performGrimCrit(event);
                break;
        }
    }

    private void performHypixelCrit() {
        double[] values = {0.0625 + Math.random() / 100.0, 0.03125 + Math.random() / 100.0};
        for (double value : values) {
            mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + value, Criticals.mc.thePlayer.posZ, false));
        }
    }

    private void performHvHCrit() {
        double[] offsets = {0.06253453, 0.02253453, 0.001253453, 1.135346E-4};
        for (double offset : offsets) {
            Criticals.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(Criticals.mc.thePlayer.posX, Criticals.mc.thePlayer.posY + offset, Criticals.mc.thePlayer.posZ, false));
        }
    }

    private void performPacketCrit() {

    }

    private void performHopCrit() {
        Criticals.mc.thePlayer.motionY = 0.1;
        Criticals.mc.thePlayer.fallDistance = 0.1f;
        Criticals.mc.thePlayer.onGround = false;
    }

    private void performGrimCrit(EventAttack event) {
        Module blink = getModule(Blink.class);
        if (!blink.getState() && blinkTick <= 0 && getModule(KillAura.class).getState()) {
            if (mc.thePlayer.onGround && jump > 10 && airTick == 0 && !isCriticalHitTimerActive && mc.thePlayer.hurtTime == 0) {
                mc.gameSettings.keyBindJump.pressed = true;
                jump = 0;
                isCriticalHitTimerActive = true;
                mc.timer.timerSpeed = 2f - timervalue.getValue().floatValue();
            }
        } else {
            if (mc.thePlayer.onGround && jump > 10) {
                mc.thePlayer.motionY = jumpHeight.getValue().floatValue();
                jump = 0;
            }
        }
    }

    private enum modeEnums {
        Packet,
        Hypixel,
        HvH,
        Hop,
        Jump,
        Visual,
        Grim,
        NoGround
    }
}