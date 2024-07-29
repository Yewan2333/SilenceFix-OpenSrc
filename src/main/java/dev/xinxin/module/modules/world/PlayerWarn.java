package dev.xinxin.module.modules.world;

import dev.xinxin.event.EventTarget;
import dev.xinxin.event.world.EventPacketSend;
import dev.xinxin.event.world.EventTick;
import dev.xinxin.event.world.EventWorldLoad;
import dev.xinxin.gui.notification.NotificationManager;
import dev.xinxin.gui.notification.NotificationType;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.misc.Teams;
import dev.xinxin.utils.DebugUtil;
import dev.xinxin.utils.HYTUtils;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;

public class PlayerWarn
        extends Module {
    public static List<Entity> flaggedEntity = new ArrayList<Entity>();
    public static int banned = 0;
    public PlayerWarn() {
        super("PlayerTracker", Category.World);
    }

    @EventTarget
    public void onWorld(EventWorldLoad e) {
        flaggedEntity.clear();
    }

    @EventTarget
    public void onPacket(EventPacketSend e){
        Packet<?> packet = e.getPacket();
        if (packet instanceof S02PacketChat){
            S02PacketChat packetChat = (S02PacketChat) packet;
            String text = packetChat.chatComponent.getUnformattedText();
            if (text.contains("玩家") && text.contains("在本局游戏中行为异常")) {
                banned++;
            }
        }
    }

    @EventTarget
    public void onTick(EventTick e) {
        if (PlayerWarn.mc.theWorld == null || PlayerWarn.mc.theWorld.loadedEntityList.isEmpty()) {
            return;
        }
        if (HYTUtils.isInLobby()) {
            return;
        }
        if (PlayerWarn.mc.thePlayer.ticksExisted % 6 == 0) {
            for (Entity ent : PlayerWarn.mc.theWorld.loadedEntityList) {
                if (!(ent instanceof EntityPlayer player) || ent == PlayerWarn.mc.thePlayer) continue;
                if (HYTUtils.isStrength(player) > 0 && !flaggedEntity.contains(player) && !Teams.isSameTeam(player)) {
                    flaggedEntity.add(player);
                    DebugUtil.log("有新的傻逼", player.getName() + " 是傻逼力量狗，快去斩杀他的亲妈吧");
                    NotificationManager.post(NotificationType.WARNING, "有新的傻逼", player.getName() + " 是傻逼力量狗，快去斩杀他的亲妈吧", 20.0f);
                }
                if (HYTUtils.isRegen(player) > 0 && !flaggedEntity.contains(player) && !Teams.isSameTeam(player)) {
                    flaggedEntity.add(player);
                    DebugUtil.log("有新的傻逼", player.getName() + " 使用他的恢复，复活他亲妈，快去斩杀他的亲妈吧");
                    NotificationManager.post(NotificationType.WARNING, "有新的傻逼", player.getName() + " 是傻逼生命恢复狗，快去斩杀他的亲妈吧", 20.0f);
                }
                if (HYTUtils.isHoldingGodAxe(player) && !flaggedEntity.contains(player) && !Teams.isSameTeam(player)) {
                    flaggedEntity.add(player);
                    DebugUtil.log("有新的傻逼", player.getName() + " 正在使用秒人斧击杀他的母亲，主播小心点别被杀了");
                    NotificationManager.post(NotificationType.WARNING, "有新的傻逼", player.getName() + " 正在使用秒人斧击杀他的母亲，主播小心点别被杀了", 20.0f);
                }
                if (HYTUtils.isKBBall(player.getHeldItem()) && !flaggedEntity.contains(player) && !Teams.isSameTeam(player)) {
                    flaggedEntity.add(player);
                    DebugUtil.log("有新的傻逼", player.getName() + " 正在使用击退球把他的老母打到十里开外，主播小心点");
                    NotificationManager.post(NotificationType.WARNING, "有新的傻逼", player.getName() + " 正在使用击退球把他的老母打到十里开外，主播小心点", 20.0f);
                }
                if (HYTUtils.hasEatenGoldenApple(player) <= 0 || flaggedEntity.contains(player) || Teams.isSameTeam(player)) continue;
                flaggedEntity.add(player);
                DebugUtil.log("有新的傻逼", player.getName() + " 吃了附魔金苹果换取锁血和他的老妈子决斗，主播等他锁血没了再去殴打他吧");
                NotificationManager.post(NotificationType.WARNING, "有新的傻逼", player.getName() + " 吃了附魔金苹果换取锁血和他的老妈子决斗，主播等他锁血没了再去殴打他吧", 20.0f);
            }
        }
    }
}