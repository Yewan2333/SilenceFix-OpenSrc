package net.minecraft.client.gui.inventory;

import dev.xinxin.module.modules.player.ChestStealer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class GuiChest
extends GuiContainer {
    private static final ResourceLocation CHEST_GUI_TEXTURE = new ResourceLocation("textures/gui/container/generic_54.png");
    private final IInventory upperChestInventory;
    public final IInventory lowerChestInventory;
    private final int inventoryRows;

    public GuiChest(IInventory upperInv, IInventory lowerInv) {
        super(new ContainerChest(upperInv, lowerInv, Minecraft.getMinecraft().thePlayer));
        this.upperChestInventory = upperInv;
        this.lowerChestInventory = lowerInv;
        this.allowUserInput = false;
        int i = 222;
        int j2 = i - 108;
        this.inventoryRows = lowerInv.getSizeInventory() / 9;
        this.ySize = j2 + this.inventoryRows * 18;
        String chestTitle = this.lowerChestInventory.getDisplayName().getUnformattedText();
        ChestStealer.isChest = chestTitle.equals(StatCollector.translateToLocal("container.chest")) || chestTitle.equals("Chest") || chestTitle.equals(StatCollector.translateToLocal("container.chestDouble")) || chestTitle.equals("LOW");
        ChestStealer.timer.reset();
        ChestStealer.openChestTimer.reset();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(this.lowerChestInventory.getDisplayName().getUnformattedText(), 8, 6, 0x404040);
        this.fontRendererObj.drawString(this.upperChestInventory.getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 0x404040);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.mc.getTextureManager().bindTexture(CHEST_GUI_TEXTURE);
        int i = (this.width - this.xSize) / 2;
        int j2 = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j2, 0, 0, this.xSize, this.inventoryRows * 18 + 17);
        this.drawTexturedModalRect(i, j2 + this.inventoryRows * 18 + 17, 0, 126, this.xSize, 96);
    }
}

