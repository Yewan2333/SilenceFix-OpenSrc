package dev.xinxin.utils.item;

import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.apache.commons.lang3.ArrayUtils;

public final class ItemUtils {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final int[] itemHelmet = new int[]{298, 302, 306, 310, 314};
    private static final int[] itemChestPlate = new int[]{299, 303, 307, 311, 315};
    private static final int[] itemLeggings = new int[]{300, 304, 308, 312, 316};
    private static final int[] itemBoots = new int[]{301, 305, 309, 313, 317};

    public static float getSwordDamage(ItemStack itemStack) {
        float damage = 0.0f;
        Optional attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();
        if (attributeModifier.isPresent()) {
            damage = (float)((AttributeModifier)attributeModifier.get()).getAmount();
        }
        return damage + EnchantmentHelper.getModifierForCreature(itemStack, EnumCreatureAttribute.UNDEFINED);
    }

    public static boolean isBestSword(ContainerChest c, ItemStack item) {
        float tempdamage;
        int i;
        float itemdamage1 = ItemUtils.getSwordDamage(item);
        float itemdamage2 = 0.0f;
        for (i = 0; i < 45; ++i) {
            if (!ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !((tempdamage = ItemUtils.getSwordDamage(ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack())) >= itemdamage2)) continue;
            itemdamage2 = tempdamage;
        }
        for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
            if (c.getLowerChestInventory().getStackInSlot(i) == null || !((tempdamage = ItemUtils.getSwordDamage(c.getLowerChestInventory().getStackInSlot(i))) >= itemdamage2)) continue;
            itemdamage2 = tempdamage;
        }
        return itemdamage1 == itemdamage2;
    }

    public static boolean isBestArmor(ContainerChest c, ItemStack item) {
        float temppro;
        int i;
        float itempro1 = ((ItemArmor)item.getItem()).damageReduceAmount;
        float itempro2 = 0.0f;
        if (ItemUtils.isContain(itemHelmet, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ItemUtils.isContain(itemHelmet, Item.getIdFromItem(ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ItemUtils.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ItemUtils.isContain(itemHelmet, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        if (ItemUtils.isContain(itemChestPlate, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ItemUtils.isContain(itemChestPlate, Item.getIdFromItem(ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ItemUtils.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ItemUtils.isContain(itemChestPlate, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        if (ItemUtils.isContain(itemLeggings, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ItemUtils.isContain(itemLeggings, Item.getIdFromItem(ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ItemUtils.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ItemUtils.isContain(itemLeggings, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        if (ItemUtils.isContain(itemBoots, Item.getIdFromItem(item.getItem()))) {
            for (i = 0; i < 45; ++i) {
                if (!ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getHasStack() || !ItemUtils.isContain(itemBoots, Item.getIdFromItem(ItemUtils.mc.thePlayer.inventoryContainer.getSlot(i).getStack().getItem())) || !((temppro = (float)((ItemArmor)ItemUtils.mc.thePlayer.inventoryContainer.getSlot((int)i).getStack().getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
            for (i = 0; i < c.getLowerChestInventory().getSizeInventory(); ++i) {
                if (c.getLowerChestInventory().getStackInSlot(i) == null || !ItemUtils.isContain(itemBoots, Item.getIdFromItem(c.getLowerChestInventory().getStackInSlot(i).getItem())) || !((temppro = (float)((ItemArmor)c.getLowerChestInventory().getStackInSlot((int)i).getItem()).damageReduceAmount) > itempro2)) continue;
                itempro2 = temppro;
            }
        }
        return itempro1 == itempro2;
    }

    public static boolean isContain(int[] arr, int targetValue) {
        return ArrayUtils.contains((int[])arr, (int)targetValue);
    }

    public static boolean isPotionNegative(ItemStack itemStack) {
        ItemPotion potion = (ItemPotion)itemStack.getItem();
        List<PotionEffect> potionEffectList = potion.getEffects(itemStack);
        return potionEffectList.stream().map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()]).anyMatch(Potion::isBadEffect);
    }

    public static int getEnchantment(ItemStack itemStack, Enchantment enchantment) {
        if (itemStack == null || itemStack.getEnchantmentTagList() == null || itemStack.getEnchantmentTagList().hasNoTags()) {
            return 0;
        }
        for (int i = 0; i < itemStack.getEnchantmentTagList().tagCount(); ++i) {
            NBTTagCompound tagCompound = itemStack.getEnchantmentTagList().getCompoundTagAt(i);
            if (tagCompound.getShort("ench") != enchantment.effectId && tagCompound.getShort("id") != enchantment.effectId) continue;
            return tagCompound.getShort("lvl");
        }
        return 0;
    }
}

