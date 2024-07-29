package dev.xinxin.module.modules.misc;

import dev.xinxin.Client;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.utils.player.PlayerUtil;
import java.util.Objects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class Teams
extends Module {
    private static final BoolValue armorValue = new BoolValue("ArmorColor", true);
    private static final BoolValue colorValue = new BoolValue("Color", true);
    private static final BoolValue scoreboardValue = new BoolValue("ScoreboardTeam", true);

    public Teams() {
        super("Teams", Category.Misc);
    }

    public static boolean isSameTeam(Entity entity) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer entityPlayer = (EntityPlayer)entity;
            if (Objects.requireNonNull(Client.instance.moduleManager.getModule("Teams")).getState()) {
                return (Boolean)armorValue.getValue() != false && PlayerUtil.armorTeam(entityPlayer) || (Boolean)colorValue.getValue() != false && PlayerUtil.colorTeam(entityPlayer) || (Boolean)scoreboardValue.getValue() != false && PlayerUtil.scoreTeam(entityPlayer);
            }
            return false;
        }
        return false;
    }
}

