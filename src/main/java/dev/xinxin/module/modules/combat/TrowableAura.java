package dev.xinxin.module.modules.combat;

import dev.xinxin.Client;
import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.event.world.EventUpdate;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.misc.Teams;
import dev.xinxin.module.modules.player.Blink;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.ModeValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.RotationComponent;
import dev.xinxin.utils.client.MathUtil;
import dev.xinxin.utils.client.PacketUtil;
import dev.xinxin.utils.client.TimeUtil;
import dev.xinxin.utils.player.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import org.lwjgl.compatibility.util.vector.Vector2f;
import tech.skidonion.obfuscator.annotations.NativeObfuscation;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TrowableAura extends Module {
    public TrowableAura() {
        super("TrowableAura", Category.Combat);
    }
    public final ModeValue<attack> attackmode = new ModeValue("AttackMode", attack.values(), attack.Single);
    public final NumberValue range = new NumberValue("Max Range", 8, 0, 15, 0.1);
    public final NumberValue minRange = new NumberValue("Min Range", 3, 0, 4, 0.1);
    public final NumberValue switchtick = new NumberValue("SwitchTick", 10, 0, 1000, 10);

    private final NumberValue rotationSpeed = new NumberValue("Rotation speed", 10, 1, 10, 1);
    public final ModeValue<AutoBall> autoBall = new ModeValue("AutoBall", AutoBall.values(), AutoBall.Packet);

    private final NumberValue ticks = new NumberValue("Ticks", 20, 1, 50, 1);
    private final NumberValue ballnumber = new NumberValue("BallNumber", 1, 1, 16, 1);
    public final BoolValue player = new BoolValue("Player", true);
    public final BoolValue mobs = new BoolValue("Mobs", false);
    public final BoolValue invisibles = new BoolValue("Invisibles", false);
    public final BoolValue animals = new BoolValue("Animals", false);
    public final BoolValue villagers = new BoolValue("Villagers", false);
    public final BoolValue teams = new BoolValue("Teams", false);

    private final TimeUtil ticksTimer = new TimeUtil();
    private TimeUtil timer = new TimeUtil();
    int number;
    private List<Entity> targets;
    private long AttackTime = 0;
    public Entity target;
    public static int currentSlot, targetSlot;
    public static ItemStack currentStack, targetStack;
    public static boolean packet;

    @EventTarget
    private void onUpdate(EventUpdate event){
        if (isNull()) return;

        if (target != null) {
            this.setSuffix(target.getName());
        } else {
            this.setSuffix("None");
        }

        KillAura kill = Client.instance.moduleManager.getModule(KillAura.class);
        Blink blink = Client.instance.moduleManager.getModule(Blink.class);

        if (targetSlot != -1 && currentSlot != targetSlot) {
            if (packet) {
                PacketUtil.send(new C09PacketHeldItemChange(targetSlot));
            } else {
                switchToSlot(targetSlot);
            }
            targetStack = getStackInSlot(targetSlot);
            targetSlot = -1;
        }
        long delay = ticks.getValue().longValue() * 100L;
        if (target != null) {
            if (target.isDead || mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) > range.getValue() || mc.thePlayer.getDistance(target.posX, target.posY, target.posZ) <= 4) {
                target = null;
            }
        }
        if (target != null) {
            if (getItemSlot() < 0) {
                target = null;
            }
        }
        this.getTargets();

        if (this.targets.size() > 1 && attackmode.is("Switch")) {
            if (timer.hasTimeElapsed(switchtick.getValue().intValue())) {
                ticksTimer.reset();
                ++this.number;
            }
        }
        if(targets.isEmpty() && target == null){
            setSlot(mc.thePlayer.inventory.currentItem);
        }
        if (this.number >= this.targets.size()) {
            this.number = 0;
        }
        if (kill.target != null && blink.getState()){
            this.target = null;
        }

        if (!targets.isEmpty()) {
            target = targets.get(number);
        }
        if (getItemSlot() != -1 && target != null && kill.target == null && !targets.isEmpty() ){
            switch (autoBall.get()){
                case Switch:
                    setSlot(getItemSlot(), false);
                    break;
                case Packet:
                    setSlot(getItemSlot());
                    break;
            }
        }

        if (target != null && kill.target == null && getItemSlot() != -1) {
            if (timer.hasTimeElapsed(delay)) {
                attack();
                Rotation();
                timer.reset();
            }
        }
    }


    public static void setSlot(int slotIndex) {
        setSlot(slotIndex, true);
    }


    public static void setSlot(int slotIndex, boolean silent) {
        if (slotIndex >= 0 && slotIndex < 9) {
            targetSlot = slotIndex;
            packet = silent;
        }
    }

    @EventTarget
    private void onPacketSend(EventPacketSend e){
        if (isNull()) return;

        Packet<?> packet = e.getPacket();

        if (packet instanceof C09PacketHeldItemChange wrapper) {
            currentSlot = wrapper.getSlotId();
            currentStack = getStackInSlot(currentSlot);
        }
    }
    public ItemStack getStackInSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 9) {
            return mc.thePlayer.inventory.getStackInSlot(slotIndex);
        }
        return null;
    }
    @NativeObfuscation.Inline
    public static void switchToSlot(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 9) {
            mc.thePlayer.inventory.currentItem = slotIndex;
            mc.playerController.updateController();
        }
    }

    @NativeObfuscation.Inline
    private void Rotation(){
        Vector2f targetRotations = RotationUtil.calculate(target, false, range.getValue());
        if (target != null) {
            RotationUtil.calculate(target);
        }
        final double minRotationSpeed = this.rotationSpeed.getValue();
        final double maxRotationSpeed = this.rotationSpeed.getValue();
        final float rotationSpeed = (float) MathUtil.getRandom(minRotationSpeed, maxRotationSpeed);
        if (target != null) {
            if (target.getDistanceToEntity(mc.thePlayer) <= range.getValue()) {
                if (mc.thePlayer.onGround) {
                    if (target.getDistanceToEntity(mc.thePlayer) >= 8 && target.getDistanceToEntity(mc.thePlayer) <= 12) {
                        targetRotations.y -= 3;
                    } else if (target.getDistanceToEntity(mc.thePlayer) >= 13 && target.getDistanceToEntity(mc.thePlayer) <= 17) {
                        targetRotations.y -= 5;
                    } else if (target.getDistanceToEntity(mc.thePlayer) >= 18 && target.getDistanceToEntity(mc.thePlayer) <= 24) {
                        targetRotations.y -= 8;
                    } else if (target.getDistanceToEntity(mc.thePlayer) >= 25 && target.getDistanceToEntity(mc.thePlayer) <= range.getValue()) {
                        targetRotations.y -= 11;
                    }
                }
            }
        }

        if (target == null) {
            targetRotations.x = mc.thePlayer.rotationYaw;
            targetRotations.y = mc.thePlayer.rotationPitch;
        }
        RotationComponent.setRotations(targetRotations, rotationSpeed, true);
    }

    @NativeObfuscation.Inline
    private void getTargets() {
        targets = mc.theWorld.loadedEntityList.stream()
                .filter(entity -> entity instanceof EntityLivingBase && entity != mc.thePlayer)
                .filter(entity -> !entity.isDead && ((EntityLivingBase) entity).deathTime == 0)
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) <= range.getValue())
                .filter(entity -> mc.thePlayer.getDistanceToEntity(entity) >= minRange.getValue())
                .filter(entity -> {
                    if (entity.isInvisible() && !invisibles.getValue()) {
                        return false;
                    }
//                    if (antiBot.isBot(entity)) {
//                        return false;
//                    }
                    if (Teams.isSameTeam((EntityLivingBase) entity) && teams.getValue()) {
                        return false;
                    }
                    return isTargetTypeAllowed(entity);
                })
                .sorted(Comparator.comparingDouble(entity -> mc.thePlayer.getDistanceToEntity(entity)))
                .collect(Collectors.toList());

        target = targets.isEmpty() ? null : targets.get(0);
    }

    @NativeObfuscation.Inline
    private boolean isTargetTypeAllowed(Entity entity) {
        if (entity instanceof EntityPlayer) {
            return player.getValue();
        }
        if (entity instanceof EntityAnimal) {
            return animals.getValue();
        }
        if (entity instanceof EntityVillager) {
            return villagers.getValue();
        }
        if (entity instanceof EntitySquid) {
            return false;
        }
        if (entity instanceof EntityMob) {
            return mobs.getValue();
        }
        return false;
    }


    @NativeObfuscation.Inline
    private void attack(){
        long currentTime = System.currentTimeMillis();
        long tick = ticks.getValue().intValue() * 20L;

        if (currentTime - AttackTime < tick) return;
        for (int i = 0; i < ballnumber.getValue(); i++) {
            mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
        }
        AttackTime = currentTime;
    }
    @NativeObfuscation.Inline
    private int getItemSlot() {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.thePlayer.inventory.mainInventory[i];
            if (stack != null && (stack.getItem() instanceof ItemSnowball || stack.getItem() instanceof ItemEgg)) {
                return i;
            }
        }
        return -1;
    }
    public enum attack{
        Single,
        Switch
    }
    public enum AutoBall{
        Packet,
        Switch
    }
}
