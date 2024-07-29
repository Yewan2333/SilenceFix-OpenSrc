package net.minecraft.client.gui;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.xinxin.Client;
import dev.xinxin.event.EventManager;
import dev.xinxin.event.misc.EventSendChatMessage;
import dev.xinxin.utils.client.menu.utils.MainMenuBackground;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import dev.xinxin.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityList;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class GuiScreen
extends Gui
implements GuiYesNoCallback {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Set<String> PROTOCOLS = Sets.newHashSet(Arrays.toString((Object[])new String[]{"http", "https"}));
    long initTime = System.currentTimeMillis();
    public MainMenuBackground shaderBackground;
    private static final Splitter NEWLINE_SPLITTER = Splitter.on((char)'\n');
    protected Minecraft mc;
    protected RenderItem itemRender;
    public int width;
    public int height;
    protected List<GuiButton> buttonList = Lists.newArrayList();
    protected List<GuiLabel> labelList = Lists.newArrayList();
    public boolean allowUserInput;
    protected FontRenderer fontRendererObj;
    private GuiButton selectedButton;
    private int eventButton;
    private long lastMouseEvent;
    private int touchValue;
    private URI clickedLinkURI;

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        for (int i = 0; i < this.buttonList.size(); ++i) {
            this.buttonList.get(i).drawButton(this.mc, mouseX, mouseY);
        }
        for (int j2 = 0; j2 < this.labelList.size(); ++j2) {
            this.labelList.get(j2).drawLabel(this.mc, mouseX, mouseY);
        }
        if (Client.instance.uiManager != null) {
            Client.instance.uiManager.mouseX = mouseX;
            Client.instance.uiManager.mouseY = mouseY;
        }
    }

    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            this.mc.displayGuiScreen(null);
            if (this.mc.currentScreen == null) {
                this.mc.setIngameFocus();
            }
        }
    }

    public static String getClipboardString() {
        try {
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                return (String)transferable.getTransferData(DataFlavor.stringFlavor);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        return "";
    }

    public static void setClipboardString(String copyText) {
        if (!StringUtils.isEmpty((CharSequence)copyText)) {
            try {
                StringSelection stringselection = new StringSelection(copyText);
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringselection, null);
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    protected void renderToolTip(ItemStack stack, int x2, int y2) {
        List<String> list = stack.getTooltip(this.mc.thePlayer, this.mc.gameSettings.advancedItemTooltips);
        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, (Object)((Object)stack.getRarity().rarityColor) + list.get(i));
                continue;
            }
            list.set(i, (Object)((Object)EnumChatFormatting.GRAY) + list.get(i));
        }
        this.drawHoveringText(list, x2, y2);
    }

    protected void drawCreativeTabHoveringText(String tabName, int mouseX, int mouseY) {
        this.drawHoveringText(Arrays.asList(tabName), mouseX, mouseY);
    }

    protected void drawHoveringText(List<String> textLines, int x2, int y2) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;
            for (String s2 : textLines) {
                int j2 = this.fontRendererObj.getStringWidth(s2);
                if (j2 <= i) continue;
                i = j2;
            }
            int l1 = x2 + 12;
            int i2 = y2 - 12;
            int k2 = 8;
            if (textLines.size() > 1) {
                k2 += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i > this.width) {
                l1 -= 28 + i;
            }
            if (i2 + k2 + 6 > this.height) {
                i2 = this.height - k2 - 6;
            }
            zLevel = 300.0f;
            this.itemRender.zLevel = 300.0f;
            int l2 = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l2, l2);
            this.drawGradientRect(l1 - 3, i2 + k2 + 3, l1 + i + 3, i2 + k2 + 4, l2, l2);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k2 + 3, l2, l2);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k2 + 3, l2, l2);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k2 + 3, l2, l2);
            int i1 = 0x505000FF;
            int j1 = (i1 & 0xFEFEFE) >> 1 | i1 & 0xFF000000;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k2 + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k2 + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i2 + k2 + 2, l1 + i + 3, i2 + k2 + 3, j1, j1);
            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = textLines.get(k1);
                this.fontRendererObj.drawStringWithShadow(s1, l1, i2, -1);
                if (k1 == 0) {
                    i2 += 2;
                }
                i2 += 10;
            }
            zLevel = 0.0f;
            this.itemRender.zLevel = 0.0f;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    protected void handleComponentHover(IChatComponent component, int x2, int y2) {
        if (component != null && component.getChatStyle().getChatHoverEvent() != null) {
            block21: {
                HoverEvent hoverevent = component.getChatStyle().getChatHoverEvent();
                if (hoverevent.getAction() == HoverEvent.Action.SHOW_ITEM) {
                    ItemStack itemstack = null;
                    try {
                        NBTTagCompound nbtbase = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                        if (nbtbase instanceof NBTTagCompound) {
                            itemstack = ItemStack.loadItemStackFromNBT(nbtbase);
                        }
                    }
                    catch (NBTException nbtbase) {
                        // empty catch block
                    }
                    if (itemstack != null) {
                        this.renderToolTip(itemstack, x2, y2);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Item!", x2, y2);
                    }
                } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ENTITY) {
                    if (this.mc.gameSettings.advancedItemTooltips) {
                        try {
                            NBTTagCompound nbtbase1 = JsonToNBT.getTagFromJson(hoverevent.getValue().getUnformattedText());
                            if (nbtbase1 instanceof NBTTagCompound) {
                                ArrayList list1 = Lists.newArrayList();
                                NBTTagCompound nbttagcompound = nbtbase1;
                                list1.add(nbttagcompound.getString("name"));
                                if (nbttagcompound.hasKey("type", 8)) {
                                    String s2 = nbttagcompound.getString("type");
                                    list1.add("Type: " + s2 + " (" + EntityList.getIDFromString(s2) + ")");
                                }
                                list1.add(nbttagcompound.getString("id"));
                                this.drawHoveringText(list1, x2, y2);
                                break block21;
                            }
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", x2, y2);
                        }
                        catch (NBTException var10) {
                            this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid Entity!", x2, y2);
                        }
                    }
                } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_TEXT) {
                    this.drawHoveringText(NEWLINE_SPLITTER.splitToList((CharSequence)hoverevent.getValue().getFormattedText()), x2, y2);
                } else if (hoverevent.getAction() == HoverEvent.Action.SHOW_ACHIEVEMENT) {
                    StatBase statbase = StatList.getOneShotStat(hoverevent.getValue().getUnformattedText());
                    if (statbase != null) {
                        IChatComponent ichatcomponent = statbase.getStatName();
                        ChatComponentTranslation ichatcomponent1 = new ChatComponentTranslation("stats.tooltip.type." + (statbase.isAchievement() ? "achievement" : "statistic"), new Object[0]);
                        ichatcomponent1.getChatStyle().setItalic(true);
                        String s1 = statbase instanceof Achievement ? ((Achievement)statbase).getDescription() : null;
                        ArrayList list = Lists.newArrayList((Object[])new String[]{ichatcomponent.getFormattedText(), ichatcomponent1.getFormattedText()});
                        if (s1 != null) {
                            list.addAll(this.fontRendererObj.listFormattedStringToWidth(s1, 150));
                        }
                        this.drawHoveringText(list, x2, y2);
                    } else {
                        this.drawCreativeTabHoveringText((Object)((Object)EnumChatFormatting.RED) + "Invalid statistic/achievement!", x2, y2);
                    }
                }
            }
            GlStateManager.disableLighting();
        }
    }

    protected void setText(String newChatText, boolean shouldOverwrite) {
    }

    protected boolean handleComponentClick(IChatComponent component) {
        if (component == null) {
            return false;
        }
        ClickEvent clickevent = component.getChatStyle().getChatClickEvent();
        if (GuiScreen.isShiftKeyDown()) {
            if (component.getChatStyle().getInsertion() != null) {
                this.setText(component.getChatStyle().getInsertion(), false);
            }
        } else if (clickevent != null) {
            block21: {
                if (clickevent.getAction() == ClickEvent.Action.OPEN_URL) {
                    if (!this.mc.gameSettings.chatLinks) {
                        return false;
                    }
                    try {
                        URI uri = new URI(clickevent.getValue());
                        String s2 = uri.getScheme();
                        if (s2 == null) {
                            throw new URISyntaxException(clickevent.getValue(), "Missing protocol");
                        }
                        if (!PROTOCOLS.contains(s2.toLowerCase())) {
                            throw new URISyntaxException(clickevent.getValue(), "Unsupported protocol: " + s2.toLowerCase());
                        }
                        if (this.mc.gameSettings.chatLinksPrompt) {
                            this.clickedLinkURI = uri;
                            this.mc.displayGuiScreen(new GuiConfirmOpenLink((GuiYesNoCallback)this, clickevent.getValue(), 31102009, false));
                            break block21;
                        }
                        this.openWebLink(uri);
                    }
                    catch (URISyntaxException urisyntaxexception) {
                        LOGGER.error("Can't open url for " + clickevent, (Throwable)urisyntaxexception);
                    }
                } else if (clickevent.getAction() == ClickEvent.Action.OPEN_FILE) {
                    URI uri1 = new File(clickevent.getValue()).toURI();
                    this.openWebLink(uri1);
                } else if (clickevent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                    this.setText(clickevent.getValue(), true);
                } else if (clickevent.getAction() == ClickEvent.Action.RUN_COMMAND) {
                    this.sendChatMessage(clickevent.getValue(), false);
                } else if (clickevent.getAction() == ClickEvent.Action.TWITCH_USER_INFO) {
                    LOGGER.error("Tried to handle twitch user but couldn't find them!");
                } else {
                    LOGGER.error("Don't know how to handle " + clickevent);
                }
            }
            return true;
        }
        return false;
    }

    public void sendChatMessage(String msg) {
        this.sendChatMessage(msg, true);
    }

    public void sendChatMessage(String msg, boolean addToChat) {
        if (addToChat) {
            this.mc.ingameGUI.getChatGUI().addToSentMessages(msg);
        }
        EventSendChatMessage event =new EventSendChatMessage(msg);
        EventManager.call(event);
        if (!event.isCancelled()){
            this.mc.thePlayer.sendChatMessage(msg);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            for (int i = 0; i < this.buttonList.size(); ++i) {
                GuiButton guibutton = this.buttonList.get(i);
                if (!guibutton.mousePressed(this.mc, mouseX, mouseY)) continue;
                this.selectedButton = guibutton;
                guibutton.playPressSound(this.mc.getSoundHandler());
                this.actionPerformed(guibutton);
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (this.selectedButton != null && state == 0) {
            this.selectedButton.mouseReleased(mouseX, mouseY);
            this.selectedButton = null;
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
    }

    protected void actionPerformed(GuiButton button) throws IOException {
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        this.mc = mc;
        this.itemRender = mc.getRenderItem();
        this.fontRendererObj = mc.fontRendererObj;
        this.width = width;
        this.height = height;
        this.buttonList.clear();
        this.initGui();
    }

    public void a(int p_a_1_, int p_a_2_) {
        this.width = p_a_1_;
        this.height = p_a_2_;
    }

    public void initGui() {
    }

    public void handleInput() throws IOException {
        if (Mouse.isCreated()) {
            while (Mouse.next()) {
                this.handleMouseInput();
            }
        }
        if (Keyboard.isCreated()) {
            while (Keyboard.next()) {
                this.handleKeyboardInput();
            }
        }
    }

    public void handleMouseInput() throws IOException {
        int i = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int j2 = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
        int k2 = Mouse.getEventButton();
        if (Mouse.getEventButtonState()) {
            if (this.mc.gameSettings.touchscreen && this.touchValue++ > 0) {
                return;
            }
            this.eventButton = k2;
            this.lastMouseEvent = Minecraft.getSystemTime();
            this.mouseClicked(i, j2, this.eventButton);
        } else if (k2 != -1) {
            if (this.mc.gameSettings.touchscreen && --this.touchValue > 0) {
                return;
            }
            this.eventButton = -1;
            this.mouseReleased(i, j2, k2);
        } else if (this.eventButton != -1 && this.lastMouseEvent > 0L) {
            long l2 = Minecraft.getSystemTime() - this.lastMouseEvent;
            this.mouseClickMove(i, j2, this.eventButton, l2);
        }
    }

    public void handleKeyboardInput() throws IOException {
        char c0 = Keyboard.getEventCharacter();
        if (Keyboard.getEventKey() == 0 && c0 >= ' ' || Keyboard.getEventKeyState()) {
            this.keyTyped(c0, Keyboard.getEventKey());
        }
        this.mc.dispatchKeypresses();
    }

    public void updateScreen() {
    }

    public void onGuiClosed() {
    }

    public void drawDefaultBackground() {
        this.drawWorldBackground(0);
    }

    public void drawWorldBackground(int tint) {
        if (this.mc.theWorld != null) {
            this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
        } else {
            this.drawBackground(tint);
        }
    }

    public void drawBackground(int tint) {
        GlStateManager.disableLighting();
        GlStateManager.disableFog();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        this.mc.getTextureManager().bindTexture(optionsBackground);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        float f = 32.0f;
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        worldrenderer.pos(0.0, this.height, 0.0).tex(0.0, (float)this.height / 32.0f + (float)tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(this.width, this.height, 0.0).tex((float)this.width / 32.0f, (float)this.height / 32.0f + (float)tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(this.width, 0.0, 0.0).tex((float)this.width / 32.0f, tint).color(64, 64, 64, 255).endVertex();
        worldrenderer.pos(0.0, 0.0, 0.0).tex(0.0, tint).color(64, 64, 64, 255).endVertex();
        tessellator.draw();
    }

    public boolean doesGuiPauseGame() {
        return true;
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        if (id == 31102009) {
            if (result) {
                this.openWebLink(this.clickedLinkURI);
            }
            this.clickedLinkURI = null;
            this.mc.displayGuiScreen(this);
        }
    }

    private void openWebLink(URI url) {
        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke(null, new Object[0]);
            oclass.getMethod("browse", URI.class).invoke(object, url);
        }
        catch (Throwable throwable) {
            LOGGER.error("Couldn't open link", throwable);
        }
    }

    public static boolean isCtrlKeyDown() {
        return Minecraft.isRunningOnMac ? Keyboard.isKeyDown((int)219) || Keyboard.isKeyDown((int)220) : Keyboard.isKeyDown((int)29) || Keyboard.isKeyDown((int)157);
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown((int)42) || Keyboard.isKeyDown((int)54);
    }

    public static boolean isAltKeyDown() {
        return Keyboard.isKeyDown((int)56) || Keyboard.isKeyDown((int)184);
    }

    public static boolean isKeyComboCtrlX(int keyID) {
        return keyID == 45 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlV(int keyID) {
        return keyID == 47 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlC(int keyID) {
        return keyID == 46 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public static boolean isKeyComboCtrlA(int keyID) {
        return keyID == 30 && GuiScreen.isCtrlKeyDown() && !GuiScreen.isShiftKeyDown() && !GuiScreen.isAltKeyDown();
    }

    public void onResize(Minecraft mcIn, int w2, int h) {
        this.setWorldAndResolution(mcIn, w2, h);
    }

    public void renderBackground() {
        this.drawDefaultBackground();
        RenderUtil.drawImage(new ResourceLocation("express/bg.png"), 0, 0, this.width, this.height);
    }
}

