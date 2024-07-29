package dev.xinxin.module.modules.render;

import dev.xinxin.Client;
import dev.xinxin.event.EventTarget;
import dev.xinxin.event.misc.EventKey;
import dev.xinxin.event.rendering.EventRender2D;
import dev.xinxin.event.rendering.EventShader;
import dev.xinxin.gui.notification.Notification;
import dev.xinxin.gui.notification.NotificationManager;
import dev.xinxin.gui.ui.UiModule;
import dev.xinxin.gui.ui.modules.*;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.ModuleManager;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.ColorValue;
import dev.xinxin.module.values.ModeValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.render.AnimationUtil;
import dev.xinxin.utils.render.RenderUtil;
import dev.xinxin.utils.render.RoundedUtils;
import dev.xinxin.utils.render.animation.Animation;
import dev.xinxin.utils.render.animation.Direction;
import dev.xinxin.utils.render.fontRender.FontManager;
import dev.xinxin.utils.render.fontRender.RapeMasterFontManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HUD extends Module {

    public final ModeValue<HUDmode> hudModeValue = new ModeValue("HUD Mode", (Enum[]) HUDmode.values(), (Enum) HUDmode.Neon);
    public static BoolValue arraylist = new BoolValue("Arraylist", true);
    public static final BoolValue importantModules = new BoolValue("Arraylist-Important", false, () -> (Boolean) arraylist.getValue());
    public static final BoolValue hLine = new BoolValue("Arraylist-HLine", true, () -> (Boolean) arraylist.getValue());
    public static final NumberValue height = new NumberValue("Arraylist-Height", 11.0, 9.0, 20.0, 1.0, () -> (Boolean) arraylist.getValue());
    public static final ModeValue<ModuleList.ANIM> animation = new ModeValue("Arraylist-Animation", (Enum[]) ModuleList.ANIM.values(), (Enum) ModuleList.ANIM.ScaleIn, () -> (Boolean) arraylist.getValue());
    public static final BoolValue background = new BoolValue("Arraylist-Background", true, () -> (Boolean) arraylist.getValue());
    public static final NumberValue backgroundAlpha = new NumberValue("Arraylist-Background Alpha", 0.35, 0.01, 1.0, 0.01, () -> (Boolean) arraylist.getValue());
    public static BoolValue tab = new BoolValue("TabGUI", false);
    public static BoolValue notifications = new BoolValue("Notification", false);
    public static BoolValue Debug = new BoolValue("Debug", false);
    public static BoolValue potionInfo = new BoolValue("Potion", false);
    public static BoolValue targetHud = new BoolValue("TargetHUD", true);
    public static ModeValue<THUDMode> thudmodeValue = new ModeValue("THUD Style", (Enum[]) THUDMode.values(), (Enum) THUDMode.Neon);
    public static BoolValue multi_targetHUD = new BoolValue("Multi TargetHUD", true);
    public static BoolValue titleBar = new BoolValue("TitleBar", true);
    private final ModeValue<TitleMode> modeValue = new ModeValue("Title Mode", (Enum[]) TitleMode.values(), (Enum) TitleMode.Simple);
    public static BoolValue Information = new BoolValue("Information", false);
    public static NumberValue animationSpeed = new NumberValue("Animation Speed", 4.0, 1.0, 10.0, 0.1);
    public static NumberValue scoreBoardHeightValue = new NumberValue("Scoreboard Height", 0.0, 0.0, 300.0, 1.0);
    public static ColorValue mainColor = new ColorValue("First Color", Color.white.getRGB());
    public static ColorValue mainColor2 = new ColorValue("Second Color", Color.white.getRGB());
    private final TabGUI tabGUI = new TabGUI();
    private float leftY;
    public int offsetValue = 0;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    public HUD() {
        super("HUD", Category.Render);
        this.setState(true);
    }

    public static Color color(int tick) {
        return new Color(RenderUtil.colorSwitch(mainColor.getColorC(), mainColor2.getColorC(), 2000.0F, -(tick * 200) / 40, 75L, 2.0D));
    }

    public static RapeMasterFontManager getFont() {
        return FontManager.arial18;
    }

    @EventTarget
    public void onShader(EventShader e) {
        String fps = String.valueOf(Minecraft.getDebugFPS());
        if (((Boolean) titleBar.getValue()).booleanValue()) {
            String var3 = ((HUD.TitleMode) this.modeValue.getValue()).toString().toLowerCase();
            byte var4 = -1;
            switch (var3.hashCode()) {
                case -902286926:
                    if (var3.equals("simple")) {
                        var4 = 0;
                    }
                    break;
                case 3327403:
                    if (var3.equals("logo")) {
                        var4 = 1;
                    }
            }

            label29:
            switch (var4) {
                case 0:
                    switch ((HUD.HUDmode) this.hudModeValue.getValue()) {
                        case Shit:
                            EnumChatFormatting var10000 = EnumChatFormatting.DARK_GRAY;
                            EnumChatFormatting var10001 = EnumChatFormatting.WHITE;
                            String var10002 = Client.instance.USER;
                            EnumChatFormatting var10003 = EnumChatFormatting.DARK_GRAY;
                            EnumChatFormatting var10004 = EnumChatFormatting.WHITE;
                            Minecraft.getDebugFPS();
                            EnumChatFormatting var10006 = EnumChatFormatting.DARK_GRAY;
                            EnumChatFormatting var10007 = EnumChatFormatting.WHITE;
                            String str = mc.isSingleplayer() ? "SinglePlayer" : mc.getCurrentServerData().serverIP;
                            RoundedUtils.drawRound(6.0F, 6.0F, (float) (FontManager.arial16.getStringWidth(str) + 8 + FontManager.bold22.getStringWidth(Client.NAME.toUpperCase())), 15.0F, 0.0F, new Color(0, 0, 0));
                            RoundedUtils.drawRound(6.0F, 6.0F, (float) (FontManager.arial16.getStringWidth(str) + 8 + FontManager.bold22.getStringWidth(Client.NAME.toUpperCase())), 1.0F, 1.0F, new Color(0, 0, 0));
                            break label29;
                        case Neon:
                            String title = String.format("| v%s | %s | %sfps", Client.instance.getVersion(), Client.instance.USER, Minecraft.getDebugFPS());
                            String mark = Client.NAME;
                            float width = (float) (getFont().getStringWidth(title.toUpperCase()) + FontManager.bold22.getStringWidth(mark.toUpperCase()) + 6);
                            Gui.drawRect3(4.0D, 4.0D, (double) (width + 6.0F), (double) (FontManager.bold22.getHeight() + 4), (new Color(0, 0, 0, 255)).getRGB());
                        default:
                            break label29;
                    }
                case 1:
                    GlStateManager.resetColor();
                    GlStateManager.enableAlpha();
                    GlStateManager.disableBlend();
                    RenderUtil.drawImage(10.0F, 10.0F, 66, 66, new ResourceLocation("express/ico.png"), mainColor.getColorC());
            }
        }

        this.drawNotificationsEffects(e.isBloom());
        if (((Boolean) tab.getValue()).booleanValue()) {
            this.tabGUI.blur(this, this.leftY);
        }

    }

    @EventTarget
    public void onKey(EventKey e) {
        if (((Boolean) tab.getValue()).booleanValue()) {
            this.tabGUI.onKey(e.getKey());
        }

    }

    @EventTarget
    public void onRender(EventRender2D e) {
        String name = Client.instance.USER;
        String fps = String.valueOf(Minecraft.getDebugFPS());
        int lengthName = FontManager.Tahoma16.getStringWidth(name);
        int nlRectX = lengthName + 74;
        UiModule DebugHud = Client.instance.uiManager.getModule(Debug.class);
        DebugHud.setState(((Boolean) Debug.getValue()).booleanValue());
        UiModule Arraylist = Client.instance.uiManager.getModule(ModuleList.class);
        Arraylist.setState(((Boolean) arraylist.getValue()).booleanValue());
        UiModule InformationHUD = Client.instance.uiManager.getModule(Information.class);
        InformationHUD.setState(((Boolean) Information.getValue()).booleanValue());
        UiModule potionHUD = Client.instance.uiManager.getModule(PotionsInfo.class);
        potionHUD.setState(((Boolean) potionInfo.getValue()).booleanValue());
        this.drawNotifications();
        if (((Boolean) titleBar.getValue()).booleanValue()) {
            String Thud = ((HUD.TitleMode) this.modeValue.getValue()).toString().toLowerCase();
            byte var11 = -1;
            switch (Thud.hashCode()) {
                case -1012420739:
                    if (Thud.equals("onetap")) {
                        var11 = 3;
                    }
                    break;
                case -969905247:
                    if (Thud.equals("neverlose")) {
                        var11 = 1;
                    }
                    break;
                case -902286926:
                    if (Thud.equals("simple")) {
                        var11 = 0;
                    }
                    break;
                case 3327403:
                    if (Thud.equals("logo")) {
                        var11 = 2;
                    }
            }

            switch (var11) {
                case 0:
                    switch ((HUD.HUDmode) this.hudModeValue.getValue()) {
                        case Shit:
                            EnumChatFormatting var31 = EnumChatFormatting.DARK_GRAY;
                            EnumChatFormatting var10001 = EnumChatFormatting.WHITE;
                            String var10002 = Client.instance.USER;
                            EnumChatFormatting var32 = EnumChatFormatting.DARK_GRAY;
                            EnumChatFormatting var10004 = EnumChatFormatting.WHITE;
                            Minecraft.getDebugFPS();
                            EnumChatFormatting var10006 = EnumChatFormatting.DARK_GRAY;
                            EnumChatFormatting var10007 = EnumChatFormatting.WHITE;
                            String str = mc.isSingleplayer() ? "SinglePlayer" : mc.getCurrentServerData().serverIP;
                            RoundedUtils.drawRound(6.0F, 6.0F, (float) (FontManager.arial16.getStringWidth(str) + 8 + FontManager.bold22.getStringWidth(Client.NAME.toUpperCase())), 15.0F, 0.0F, new Color(19, 19, 19, 230));
                            RoundedUtils.drawRound(6.0F, 6.0F, (float) (FontManager.arial16.getStringWidth(str) + 8 + FontManager.bold22.getStringWidth(Client.NAME.toUpperCase())), 1.0F, 1.0F, color(8));
                            FontManager.arial16.drawString(str, (float) (11 + FontManager.bold22.getStringWidth(Client.NAME.toUpperCase())), 11.5F, Color.WHITE.getRGB());
                            FontManager.bold22.drawString(Client.NAME.toUpperCase(), 9.5F, 12.0F, color(8).getRGB());
                            FontManager.bold22.drawString(Client.NAME.toUpperCase(), 10.0F, 12.5F, Color.WHITE.getRGB());
                            break;
                        case Neon:
                            String title = String.format("| v%s | %s | %sfps", Client.instance.getVersion(), Client.instance.USER, Minecraft.getDebugFPS());
                            String mark = Client.NAME;
                            float width = (float) (getFont().getStringWidth(title.toUpperCase()) + FontManager.bold22.getStringWidth(mark.toUpperCase()) + 6);
                            RenderUtil.drawRect(4.0D, 4.0D, (double) (width + 10.0F), (double) (FontManager.bold22.getHeight() + 8), (new Color(0, 0, 0, 100)).getRGB());
                            FontManager.bold22.drawStringDynamic(mark.toUpperCase(), 8.0D, 10.0D, 1, 6);
                            getFont().drawString(title.toUpperCase(), (float) (12 + FontManager.bold22.getStringWidth(mark.toUpperCase())), 9.0F, -1);
                    }

                    this.leftY = AnimationUtil.animate(this.leftY, 24.0F, 0.3F);
                    break;
                case 1:
                    int bgColor = (new Color(30, 144, 255, 190)).getRGB();
                    int textColor = (new Color(255, 255, 255)).getRGB();
                    double x = 5.0D;
                    double y = 5.0D;
                    double width = (double) nlRectX;
                    double height = 20.0D;
                    double cornerRadius = 5.0D;
                    RenderUtil.drawRoundedRect((float) x, (float) y, (float) width, (float) height, (int) cornerRadius, bgColor);
                    FontManager.Tahoma18.drawString(Client.NAME, 10.0F, 10.0F, textColor);
                    FontManager.Tahoma16.drawString("|", 43.0F, 10.0F, textColor);
                    FontManager.Tahoma16.drawString(fps, 48.5F, 10.0F, textColor);
                    FontManager.Tahoma16.drawString("|", 64.0F, 10.0F, textColor);
                    FontManager.Tahoma16.drawString(name, 70.0F, 10.0F, textColor);
                    RenderUtil.drawShadow(5.0F, 5.0F, (float) nlRectX, 20.0F);
                    break;
                case 2:
                    GlStateManager.resetColor();
                    GlStateManager.enableAlpha();
                    GlStateManager.disableBlend();
                    RenderUtil.drawImage(10.0F, 10.0F, 60, 60, new ResourceLocation("express/ico.png"), color(1));
                    break;
                case 3:
                    String var10000 = Client.NAME;
                    int var10003 = (int) mc.thePlayer.rotationYaw % 360;
                    String text = String.valueOf(mc.thePlayer.rotationPitch);
                    int otRectX = FontManager.Tahoma14.getStringWidth(text) + 11;
                    RenderUtil.drawRoundedRect(5.0F, 6.0F, (float) otRectX, 19.0F, 4, (new Color(0, 0, 0, 200)).getRGB());
                    RenderUtil.drawRoundedRect(5.0F, 5.0F, (float) otRectX, 7.0F, 4, mainColor.getColor());
                    FontManager.Tahoma14.drawStringWithShadow(text, 8.0F, 11.0F, (new Color(255, 255, 255)).getRGB());
            }
        }

        UiModule Thud = Client.instance.uiManager.getModule(TargetHud.class);
        Thud.setState(((Boolean) targetHud.getValue()).booleanValue());
        if (((Boolean) tab.getValue()).booleanValue()) {
            this.tabGUI.renderTabGUI(this, this.leftY);
        }

    }

    public void drawNotifications() {
        ScaledResolution sr = new ScaledResolution(mc);
        float yOffset = 0.0F;
        NotificationManager.setToggleTime(2.0F);

        for (Notification notification : NotificationManager.getNotifications()) {
            Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimerUtil().hasTimeElapsed((long) notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);
            if (animation.finished(Direction.BACKWARDS)) {
                NotificationManager.getNotifications().remove(notification);
            } else {
                animation.setDuration(200);
                int actualOffset = 3;
                int notificationHeight = 23;
                int notificationWidth = FontManager.arial20.getStringWidth(notification.getDescription()) + 25;
                float x2 = (float) ((double) sr.getScaledWidth() - (double) (notificationWidth + 5) * animation.getOutput());
                float y2 = (float) sr.getScaledHeight() - (yOffset + 18.0F + (float) this.offsetValue + (float) notificationHeight + 15.0F);
                notification.drawLettuce(x2, y2, (float) notificationWidth, (float) notificationHeight);
                yOffset = (float) ((double) yOffset + (double) (notificationHeight + actualOffset) * animation.getOutput());
            }
        }

    }

    public void drawNotificationsEffects(boolean bloom) {
        ScaledResolution sr = new ScaledResolution(mc);
        float yOffset = 0.0F;

        for (Notification notification : NotificationManager.getNotifications()) {
            Animation animation = notification.getAnimation();
            animation.setDirection(notification.getTimerUtil().hasTimeElapsed((long) notification.getTime()) ? Direction.BACKWARDS : Direction.FORWARDS);
            if (animation.finished(Direction.BACKWARDS)) {
                NotificationManager.getNotifications().remove(notification);
            } else {
                animation.setDuration(200);
                int actualOffset = 3;
                int notificationHeight = 23;
                int notificationWidth = FontManager.arial20.getStringWidth(notification.getDescription()) + 25;
                float x2 = (float) ((double) sr.getScaledWidth() - (double) (notificationWidth + 5) * animation.getOutput());
                float y2 = (float) sr.getScaledHeight() - (yOffset + 18.0F + (float) this.offsetValue + (float) notificationHeight + 15.0F);
                notification.blurLettuce(x2, y2, (float) notificationWidth, (float) notificationHeight, bloom);
                yOffset = (float) ((double) yOffset + (double) (notificationHeight + actualOffset) * animation.getOutput());
            }
        }

    }


    static {
        // $FF: Couldn't be decompiled
    }

    public static enum HUDmode {
        Neon,
        Shit;

        private HUDmode() {
        }

        // $FF: synthetic method
        private static HUD.HUDmode[] $values() {
            return new HUD.HUDmode[]{Neon, Shit};
        }
    }

    public static enum Section {
        TYPES,
        MODULES;

        private Section() {
        }

        // $FF: synthetic method
        private static HUD.Section[] $values() {
            return new HUD.Section[]{TYPES, MODULES};
        }
    }

    public static enum THUDMode {
        Neon,
        Novoline,
        Exhibition,
        ThunderHack,
        Raven,
        Sils,
        WTFNovo,
        Exire,
        Moon,
        RiseNew;

        private THUDMode() {
        }

        // $FF: synthetic method
        private static HUD.THUDMode[] $values() {
            return new HUD.THUDMode[]{Neon, Novoline, Exhibition, ThunderHack, Raven, Sils, WTFNovo, Exire, Moon, RiseNew};
        }
    }

    public static class TabGUI {
        private HUD.Section section = HUD.Section.TYPES;
        private Category selectedType = Category.values()[0];
        private Module selectedModule = null;
        private int maxType;
        private int maxModule;
        private float horizonAnimation = 0.0F;
        private int currentType = 0;
        private int currentModule = 0;

        public TabGUI() {
            super();
        }

        public void init() {
            Category[] arrCategory = Category.values();
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

            for (Category category : arrCategory) {
                int categoryWidth = fontRenderer.getStringWidth(category.name().toUpperCase()) + 4;
                this.maxType = Math.max(this.maxType, categoryWidth);
            }

            ModuleManager moduleManager = Client.instance.moduleManager;

            for (Module module : Client.instance.moduleManager.getModules()) {
                int moduleWidth = fontRenderer.getStringWidth(module.getName().toUpperCase()) + 4;
                this.maxModule = Math.max(this.maxModule, moduleWidth);
            }

            this.maxModule += 12;
            this.maxType = Math.max(this.maxType, this.maxModule);
            this.maxModule += this.maxType;
        }

        public void blur(HUD hud, float y2) {
            float categoryY;
            float moduleY = categoryY = y2 + 80.0F;
            int moduleWidth = 60;
            int moduleX = moduleWidth + 1;
            Gui.drawRect(4.0D, (double) categoryY, (double) (moduleWidth - 3), (double) (categoryY + (float) (12 * Category.values().length)), (new Color(0, 0, 0, 255)).getRGB());

            for (Category category : Category.values()) {
                boolean isSelected = this.selectedType == category;
                if (isSelected) {
                    Gui.drawRect(5.0D, (double) (categoryY + 2.0F), 6.5D, (double) ((float) ((double) (categoryY + (float) HUD.getFont().getHeight()) + 1.5D)), HUD.color(0).getRGB());
                    moduleY = categoryY;
                }

                categoryY += 12.0F;
            }

            if (this.section == HUD.Section.MODULES || this.horizonAnimation > 1.0F) {
                int moduleHeight = 12 * Client.instance.moduleManager.getModsByCategory(this.selectedType).size();
                if (this.horizonAnimation < (float) moduleWidth) {
                    this.horizonAnimation = (float) ((double) this.horizonAnimation + (double) ((float) moduleWidth - this.horizonAnimation) / 20.0D);
                }

                Gui.drawRect((double) moduleX, (double) moduleY, (double) ((float) moduleX + this.horizonAnimation), (double) (moduleY + (float) moduleHeight), (new Color(0, 0, 0, 255)).getRGB());

                for (Module module : Client.instance.moduleManager.getModsByCategory(this.selectedType)) {
                    boolean isSelected = this.selectedModule == module;
                    if (isSelected) {
                        Gui.drawRect((double) ((float) moduleX + 1.0F), (double) (moduleY + 2.0F), (double) ((float) moduleX + 2.5F), (double) (moduleY + (float) HUD.getFont().getHeight() + 1.0F), (new Color(0, 0, 0, 255)).getRGB());
                    }

                    moduleY += 12.0F;
                }
            }

            if (this.horizonAnimation > 0.0F && this.section != HUD.Section.MODULES) {
                this.horizonAnimation -= 5.0F;
            }

        }

        public void renderTabGUI(HUD hud, float y2) {
            float categoryY;
            float moduleY = categoryY = y2 + 80.0F;
            int moduleWidth = 60;
            int moduleX = moduleWidth + 1;
            Gui.drawRect(4.0D, (double) categoryY, (double) (moduleWidth - 3), (double) (categoryY + (float) (12 * Category.values().length)), (new Color(0, 0, 0, 100)).getRGB());

            for (Category category : Category.values()) {
                boolean isSelected = this.selectedType == category;
                int color = isSelected ? -1 : (new Color(150, 150, 150)).getRGB();
                HUD.getFont().drawString(category.name(), 8.0F, categoryY + 2.0F, color);
                if (isSelected) {
                    Gui.drawRect(5.0D, (double) (categoryY + 2.0F), 6.5D, (double) ((float) ((double) (categoryY + (float) HUD.getFont().getHeight()) + 1.5D)), HUD.color(0).getRGB());
                    moduleY = categoryY;
                }

                categoryY += 12.0F;
            }

            if (this.section == HUD.Section.MODULES || this.horizonAnimation > 1.0F) {
                int moduleHeight = 12 * Client.instance.moduleManager.getModsByCategory(this.selectedType).size();
                if (this.horizonAnimation < (float) moduleWidth) {
                    this.horizonAnimation = (float) ((double) this.horizonAnimation + (double) ((float) moduleWidth - this.horizonAnimation) / 20.0D);
                }

                Gui.drawRect((double) moduleX, (double) moduleY, (double) ((float) moduleX + this.horizonAnimation), (double) (moduleY + (float) moduleHeight), (new Color(0, 0, 0, 100)).getRGB());

                for (Module module : Client.instance.moduleManager.getModsByCategory(this.selectedType)) {
                    boolean isSelected = this.selectedModule == module;
                    int color = isSelected ? (new Color(-1)).getRGB() : (module.getState() ? -1 : 11184810);
                    HUD.getFont().drawString(module.getName(), (float) (moduleX + 3), moduleY + 2.0F, color);
                    if (isSelected) {
                        Gui.drawRect((double) ((float) moduleX + 1.0F), (double) (moduleY + 2.0F), (double) ((float) moduleX + 2.5F), (double) (moduleY + (float) HUD.getFont().getHeight() + 1.0F), HUD.color(0).getRGB());
                    }

                    moduleY += 12.0F;
                }
            }

            if (this.horizonAnimation > 0.0F && this.section != HUD.Section.MODULES) {
                this.horizonAnimation -= 5.0F;
            }

        }

        public void onKey(int key) {
            Minecraft mc = Minecraft.getMinecraft();
            ModuleManager moduleManager = Client.instance.moduleManager;
            Category[] values = Category.values();
            if (!mc.gameSettings.showDebugInfo) {
                int KEY_DOWN = 208;
                int KEY_UP = 200;
                int KEY_RIGHT = 205;
                int KEY_RETURN = 28;
                int KEY_LEFT = 203;
                switch (key) {
                    case 28:
                        if (this.section == HUD.Section.MODULES) {
                            this.selectedModule.toggle();
                        } else if (this.section == HUD.Section.TYPES) {
                            this.currentModule = 0;
                            this.section = HUD.Section.MODULES;
                            this.selectedModule = (Module) moduleManager.getModsByCategory(this.selectedType).get(0);
                        }
                        break;
                    case 200:
                        if (this.section == HUD.Section.TYPES) {
                            this.currentType = (this.currentType + values.length - 1) % values.length;
                            this.selectedType = values[this.currentType];
                        } else if (this.section == HUD.Section.MODULES) {
                            List modulesByCategory = moduleManager.getModsByCategory(this.selectedType);
                            this.currentModule = (this.currentModule + modulesByCategory.size() - 1) % modulesByCategory.size();
                            this.selectedModule = (Module) modulesByCategory.get(this.currentModule);
                        }
                        break;
                    case 203:
                        if (this.section == HUD.Section.MODULES) {
                            this.section = HUD.Section.TYPES;
                            this.currentModule = 0;
                        }
                        break;
                    case 205:
                        if (this.section == HUD.Section.TYPES) {
                            this.currentModule = 0;
                            this.selectedModule = (Module) moduleManager.getModsByCategory(this.selectedType).get(0);
                            this.section = HUD.Section.MODULES;
                            this.horizonAnimation = 0.0F;
                        }
                        break;
                    case 208:
                        if (this.section == HUD.Section.TYPES) {
                            this.currentType = (this.currentType + 1) % values.length;
                            this.selectedType = values[this.currentType];
                        } else if (this.section == HUD.Section.MODULES) {
                            List<Module> modulesByCategory = moduleManager.getModsByCategory(this.selectedType);
                            this.currentModule = (this.currentModule + 1) % modulesByCategory.size();
                            this.selectedModule = (Module) modulesByCategory.get(this.currentModule);
                        }
                }

            }
        }
    }

    public static enum TitleMode {
        Simple,
        NeverLose,
        OneTap,
        Logo;

        private TitleMode() {
        }

        // $FF: synthetic method
        private static HUD.TitleMode[] $values() {
            return new HUD.TitleMode[]{Simple, NeverLose, OneTap, Logo};
        }
    }
}
