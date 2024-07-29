package dev.xinxin.utils.client.menu;

import dev.xinxin.gui.altmanager.ColorUtils;
import dev.xinxin.gui.altmanager.GuiAltManager;
import dev.xinxin.gui.clickgui.book.RippleAnimation;
import dev.xinxin.utils.client.menu.button.MenuButton;
import dev.xinxin.utils.misc.MinecraftInstance;
import dev.xinxin.utils.misc.MouseUtils;
import dev.xinxin.utils.render.RenderUtil;
import dev.xinxin.utils.render.RoundedUtils;
import dev.xinxin.utils.render.animation.Animation;
import dev.xinxin.utils.render.animation.Direction;
import dev.xinxin.utils.render.animation.impl.DecelerateAnimation;
import dev.xinxin.utils.render.fontRender.FontManager;
import java.awt.Color;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;

public class BetterMainMenu
        extends GuiScreen
        implements MinecraftInstance {
    public boolean hoverSwitch;
    public boolean hoverFlushed;
    public RippleAnimation a3 = new RippleAnimation();
    public Animation a = new DecelerateAnimation(500, 1.0);
    public Animation a2 = new DecelerateAnimation(500, 1.0);
    private final List<MenuButton> buttons = Arrays.asList(new MenuButton("Singleplayer"), new MenuButton("Multiplayer"), new MenuButton("AltManager"), new MenuButton("Settings"), new MenuButton("Exit"));
    private List<String> bgNames = Arrays.asList("mainmenu", "login", "load", "mtf");
    int bg = 0;

    public void initGui() {
        this.buttons.forEach(MenuButton::initGui);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        String switchBGText = "Click to switch background.";
        this.a.setDirection(this.hoverSwitch ? Direction.FORWARDS : Direction.BACKWARDS);
        this.a2.setDirection(this.hoverFlushed ? Direction.FORWARDS : Direction.BACKWARDS);
        this.hoverSwitch = MouseUtils.isHovering(8.0f, 8.0f, 25.0f + (float)this.a.getOutput() * (float)(FontManager.lettuceFont20.getStringWidth(switchBGText) + 5), 25.0f, mouseX, mouseY);
        this.hoverFlushed = MouseUtils.isHovering(this.width - 128, 8.0f, 120.0f, 60.0f, mouseX, mouseY);
        this.renderBackground();
        FontManager.lettuceFont20.drawStringWithShadow("Made with     by XinXin dev team", 1.0f, this.height - 10, Color.WHITE.getRGB());
        FontManager.icontestFont20.drawStringWithShadow("Q", 1 + FontManager.lettuceFont20.getStringWidth("Made with "), this.height - 8, Color.RED.getRGB());
        RenderUtil.scissorStart(7.0, 6.0, 25.0f + (float)this.a.getOutput() * (float)(FontManager.lettuceFont20.getStringWidth(switchBGText) + 10) + 3.0f, 30.0);
        FontManager.icontestFont40.drawCenteredString("k", 20.5f, FontManager.icontestFont20.getMiddleOfBox(25.0f) + 3.5f, Color.WHITE.getRGB());
        FontManager.lettuceFont20.drawString(switchBGText, 35.0f, FontManager.icontestFont40.getMiddleOfBox(25.0f) + 14.0f, Color.WHITE.getRGB());
        RenderUtil.scissorEnd();
        MinecraftInstance.mc.getTextureManager().bindTexture(new ResourceLocation("express/icon/banner.png"));
        RoundedUtils.drawRoundTextured(this.width - 128, 8.0f, 120.0f, 60.0f, 12.5f, 1.0f);
        RoundedUtils.drawRound((float)this.width - 128.2f, 8.0f, 120.2f, 60.8f, 12.5f, ColorUtils.interpolateColorC(new Color(0, 0, 0, 100), new Color(0, 0, 0, 150), (float)this.a2.getOutput()));
        FontManager.lettuceBoldFont26.drawCenteredStringWithShadow("Get Free", (float)this.width - 69.5f, 25.0f, ColorUtils.interpolateColor(Color.WHITE, Color.WHITE.darker(), (float)this.a2.getOutput()));
        FontManager.lettuceBoldFont26.drawCenteredStringWithShadow("xinxin.cfd", (float)this.width - 69.5f, 40.0f, ColorUtils.interpolateColor(Color.WHITE, Color.WHITE.darker(), (float)this.a2.getOutput()));
        float buttonWidth = 140.0f;
        float buttonHeight = 25.0f;
        int count = 0;
        for (MenuButton button : this.buttons) {
            button.x = (float)this.width / 2.0f - buttonWidth / 2.0f;
            button.y = (float)this.height / 2.0f - buttonHeight / 2.0f - 25.0f + (float)count;
            button.width = buttonWidth;
            button.height = buttonHeight;
            button.clickAction = () -> {
                switch (button.text) {
                    case "Singleplayer": {
                        MinecraftInstance.mc.displayGuiScreen((GuiScreen)new GuiSelectWorld((GuiScreen)this));
                        break;
                    }
                    case "Multiplayer": {
                        MinecraftInstance.mc.displayGuiScreen((GuiScreen)new GuiMultiplayer((GuiScreen)this));
                        break;
                    }
                    case "Settings": {
                        MinecraftInstance.mc.displayGuiScreen((GuiScreen)new GuiOptions((GuiScreen)this, MinecraftInstance.mc.gameSettings));
                        break;
                    }
                    case "AltManager": {
                        MinecraftInstance.mc.displayGuiScreen((GuiScreen)new GuiAltManager(this));
                        break;
                    }
                    case "Exit": {
                        MinecraftInstance.mc.shutdown();
                    }
                }
            };
            button.drawScreen(mouseX, mouseY);
            count = (int)((float)count + (buttonHeight + 5.0f));
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        this.a3.mouseClicked(mouseX, mouseY);
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.hoverFlushed && mouseButton == 0) {
            try {
                Desktop.getDesktop().browse(new URI("https://xinxin.cfd"));
            }
            catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean doesGuiPauseGame() {
        return false;
    }
}
