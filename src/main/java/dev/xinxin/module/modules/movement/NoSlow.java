package dev.xinxin.module.modules.movement;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventMotion;
import dev.xinxin.event.world.EventPacketReceive;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.event.world.EventSlowDown;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.combat.KillAura;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.ModeValue;
import dev.xinxin.utils.client.PacketUtil;
import dev.xinxin.utils.client.TimeUtil;
import java.util.Arrays;
import java.util.List;

import dev.xinxin.utils.player.PlayerUtil;
import net.minecraft.block.Block;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0EPacketClickWindow;
import net.minecraft.network.play.server.S2FPacketSetSlot;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;

public class NoSlow
extends Module {
    public final List<Integer> blacklist = Arrays.asList(54, 146, 61, 62);
    boolean slow;
    boolean canCanCelC08;
    TimeUtil timer = new TimeUtil();
    long delay = 100L;
    boolean fasterDelay;
    private final ModeValue<NoSlowMode> mode = new ModeValue("Mode", NoSlowMode.values(), NoSlowMode.Vanilla);
    public static final BoolValue blockingDamageAmountDebug = new BoolValue("BlockingDamageAmountDebug", false);
    private boolean sent = false;

    public NoSlow() {
        super("NewFixSlow", Category.Movement);
    }

    @Override
    public void onEnable() {
        this.sent = false;
    }

    private boolean shouldCancelPlacement(MovingObjectPosition objectPosition) {
        return !this.blacklist.contains(Block.getIdFromBlock(NoSlow.mc.theWorld.getBlockState(objectPosition.getBlockPos()).getBlock()));
    }

    private boolean isHoldingPotionAndSword(ItemStack stack, boolean checkSword, boolean checkFood, boolean checkPotion, boolean checkBow) {
        return PlayerUtil.isHoldingPotionAndSword(stack, checkSword, checkFood, checkPotion, checkBow);
    }

    @EventTarget
    public void onSlowDown(EventSlowDown e) {
        if (e.getType() == EventSlowDown.Type.Item) {
            ItemStack itemStack = NoSlow.mc.thePlayer.getHeldItem();
            e.setCancelled(itemStack.getItem() instanceof ItemAppleGold && !itemStack.getItem().hasEffect(itemStack) && !this.slow || this.isHoldingPotionAndSword(NoSlow.mc.thePlayer.getHeldItem(), true, false, false, false));
            if (NoSlow.mc.thePlayer.isUsingItem() && NoSlow.mc.thePlayer.moveForward > 0.0f) {
                NoSlow.mc.thePlayer.setSprinting(true);
            }
        }
    }

    @EventTarget
    public void onPacketReceive(EventPacketReceive event) {
        S2FPacketSetSlot s2f;
        Packet<?> packet = event.getPacket();
        ItemStack itemStack = NoSlow.mc.thePlayer.getHeldItem();
        if (NoSlow.mc.thePlayer == null || NoSlow.mc.theWorld == null || !NoSlow.mc.theWorld.isRemote || NoSlow.mc.thePlayer.getHeldItem() == null) {
            return;
        }
        if (this.mode.is("Grim") && packet instanceof S2FPacketSetSlot && itemStack.getItem() instanceof ItemAppleGold && !itemStack.getItem().hasEffect(itemStack) && (s2f = (S2FPacketSetSlot)packet).func_149175_c() == 0 && s2f.func_149174_e().getItem() == NoSlow.mc.thePlayer.getHeldItem().getItem()) {
            NoSlow.mc.thePlayer.inventory.getCurrentItem().stackSize = s2f.func_149174_e().stackSize;
            event.setCancelled(true);
            this.slow = false;
        }
    }

    @EventTarget
    public void onPacketSend(EventPacketSend event) {
        Packet packet = event.getPacket();
        if (this.mode.is("Grim")) {
            if (NoSlow.mc.thePlayer == null || NoSlow.mc.theWorld == null || !NoSlow.mc.theWorld.isRemote || NoSlow.mc.thePlayer.getHeldItem() == null) {
                return;
            }
            ItemStack itemStack = NoSlow.mc.thePlayer.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof ItemAppleGold && !itemStack.getItem().hasEffect(itemStack)) {
                if (packet instanceof C08PacketPlayerBlockPlacement && ((C08PacketPlayerBlockPlacement)packet).getPosition().getY() == -1 && !this.slow) {
                    mc.getNetHandler().addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    this.slow = true;
                }
                if (packet instanceof C07PacketPlayerDigging && ((C07PacketPlayerDigging)packet).getStatus() == C07PacketPlayerDigging.Action.RELEASE_USE_ITEM) {
                    event.setCancelled(true);
                    this.slow = true;
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventMotion e) {
        this.setSuffix(this.mode.getValue().toString());
        switch ((NoSlowMode) this.mode.getValue()) {
            case Grim: {
                if (e.isPre() && NoSlow.mc.thePlayer.isUsingItem() ) {/*
                    if (this.isHoldingPotionAndSword(NoSlow.mc.thePlayer.getHeldItem(), true, true, true, true)) {
                        PacketUtil.send(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
                        PacketUtil.send(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                        PacketUtil.send(new C09PacketHeldItemChange((mc.thePlayer.inventory.currentItem)));
                    }*/
                    if (this.isHoldingPotionAndSword(NoSlow.mc.thePlayer.getHeldItem(), true, false, false, false)){
                        PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                        mc.thePlayer.sendQueue.addToSendQueue(new C0EPacketClickWindow(0, 36, 0, 2, new ItemStack(Block.getBlockById(166)), (short)0));
                    }
                }/*
                if (!e.isPost() && !NoSlow.mc.thePlayer.isUsingItem()) {
                    PacketUtil.send(new C08PacketPlayerBlockPlacement(NoSlow.mc.thePlayer.inventory.getCurrentItem()));
                }*/
                break;
            }
            case OldGrim: {
                /*if (!e.isPre() || !NoSlow.mc.thePlayer.isUsingItem() && !KillAura.isBlocking || !this.isHoldingPotionAndSword(NoSlow.mc.thePlayer.getHeldItem(), true, true)) break;
                PacketUtil.send(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem % 8 + 1));
                PacketUtil.send(new C09PacketHeldItemChange(NoSlow.mc.thePlayer.inventory.currentItem));*/
                break;
            }
            case AAC4: 
            case Packet: {
                if (e.isPre() && (NoSlow.mc.thePlayer.isUsingItem() || KillAura.isBlocking)) {
                    PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                if (!e.isPost() || !NoSlow.mc.thePlayer.isUsingItem() && !KillAura.isBlocking) break;
                NoSlow.mc.playerController.sendUseItem(NoSlow.mc.thePlayer, NoSlow.mc.theWorld, NoSlow.mc.thePlayer.getHeldItem(), false);
                break;
            }
            case Intave: {
                /*if (e.isPre() && (NoSlow.mc.thePlayer.isUsingItem() || KillAura.isBlocking) && this.isHoldingPotionAndSword(NoSlow.mc.thePlayer.getHeldItem(), true, false)) {
                    PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                if (!e.isPost() || !NoSlow.mc.thePlayer.isUsingItem() && !KillAura.isBlocking || !this.isHoldingPotionAndSword(NoSlow.mc.thePlayer.getHeldItem(), true, false) || !this.timer.hasTimeElapsed(this.delay)) break;
                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                this.delay = this.fasterDelay ? 100L : 200L;
                this.fasterDelay = !this.fasterDelay;
                this.timer.reset();
                break;*/
            }
            case AAC5: {
                if (!e.isPost() || !NoSlow.mc.thePlayer.isUsingItem() && !KillAura.isBlocking) break;
                NoSlow.mc.playerController.sendUseItem(NoSlow.mc.thePlayer, NoSlow.mc.theWorld, NoSlow.mc.thePlayer.getHeldItem(), false);
                break;
            }
        }
    }

    enum NoSlowMode {
        Vanilla,
        OldGrim,
        Grim,
        Packet,
        AAC5,
        AAC4,
        Intave

    }
}

