package dev.xinxin.module.modules.combat;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventMotion;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.client.PacketUtil;
import dev.xinxin.utils.client.TimeUtil;
import dev.xinxin.utils.player.PlayerUtil;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.apache.commons.lang3.RandomUtils;

public class AutoSoup
extends Module {
    private final BoolValue postValue = new BoolValue("Post", false);
    private final BoolValue sendPostC0FFix = new BoolValue("SendPostC0FFix", true);
    private final NumberValue health = new NumberValue("Health", 15.0, 0.0, 20.0, 1.0);
    private final NumberValue minDelay = new NumberValue("Min Delay", 300.0, 0.0, 1000.0, 1.0);
    private final NumberValue maxDelay = new NumberValue("Max Delay", 500.0, 0.0, 1000.0, 1.0);
    private final BoolValue dropBowl = new BoolValue("Drop Bowl", true);
    private final BoolValue Legit = new BoolValue("Legit", false);
    private final TimeUtil timer = new TimeUtil();
    private boolean switchBack;
    private long decidedTimer;
    private int soup = -37;

    public AutoSoup() {
        super("AutoSoup", Category.Combat);
    }

    @Override
    public void onDisable() {
        this.switchBack = false;
        this.soup = -37;
    }

    @EventTarget
    public void onMotion(EventMotion event) {
        if (this.postValue.getValue() && event.isPost() || !this.postValue.getValue() && event.isPre()) {
            if (this.switchBack) {
                if (this.sendPostC0FFix.getValue() && this.postValue.getValue()) {
                    PacketUtil.sendPacketC0F();
                }
                PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                if (this.dropBowl.getValue()) {
                    if (this.sendPostC0FFix.getValue() && this.postValue.getValue()) {
                        PacketUtil.sendPacketC0F();
                    }
                    PacketUtil.send(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.DROP_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                }
                if (this.Legit.getValue()) {
                    AutoSoup.mc.playerController.updateController();
                } else {
                    PacketUtil.send(new C09PacketHeldItemChange(AutoSoup.mc.thePlayer.inventory.currentItem));
                }
                this.switchBack = false;
                return;
            }
            if (this.timer.hasPassed(this.decidedTimer) && AutoSoup.mc.thePlayer.ticksExisted > 10 && AutoSoup.mc.thePlayer.getHealth() < (float) this.health.getValue().intValue()) {
                this.soup = PlayerUtil.findSoup() - 36;
                if (this.soup != -37) {
                    if (this.Legit.getValue()) {
                        AutoSoup.mc.thePlayer.inventory.currentItem = this.soup;
                        AutoSoup.mc.gameSettings.keyBindUseItem.setPressed(true);
                    } else {
                        PacketUtil.send(new C09PacketHeldItemChange(this.soup));
                        if (this.sendPostC0FFix.getValue() && this.postValue.getValue()) {
                            PacketUtil.sendPacketC0F();
                        }
                        PacketUtil.send(new C08PacketPlayerBlockPlacement(AutoSoup.mc.thePlayer.inventory.getStackInSlot(this.soup)));
                    }
                    this.switchBack = true;
                } else {
                    int soupInInventory = PlayerUtil.findItem(9, 36, Items.mushroom_stew);
                    if (soupInInventory != -1 && PlayerUtil.hasSpaceHotbar()) {
                        boolean openInventory;
                        boolean bl = openInventory = !(AutoSoup.mc.currentScreen instanceof GuiInventory);
                        if (openInventory) {
                            AutoSoup.mc.thePlayer.setSprinting(false);
                            mc.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
                        }
                        AutoSoup.mc.playerController.windowClick(0, soupInInventory, 0, 1, AutoSoup.mc.thePlayer);
                        if (openInventory) {
                            mc.getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
                        }
                    }
                }
                int delayFirst = (int)Math.floor(Math.min(this.minDelay.getValue().intValue(), this.maxDelay.getValue().intValue()));
                int delaySecond = (int)Math.ceil(Math.max(this.minDelay.getValue().intValue(), this.maxDelay.getValue().intValue()));
                this.decidedTimer = RandomUtils.nextInt(delayFirst, delaySecond);
                this.timer.reset();
            }
        }
    }
}

