package dev.xinxin;

import dev.xinxin.command.CommandManager;
import dev.xinxin.config.ConfigManager;
import dev.xinxin.event.EventManager;
import dev.xinxin.gui.altmanager.AltManager;
import dev.xinxin.gui.ui.UiManager;
import dev.xinxin.module.Module;
import dev.xinxin.module.ModuleManager;
import dev.xinxin.module.values.Value;
import dev.xinxin.utils.component.*;
import dev.xinxin.utils.RotationComponent;
import dev.xinxin.utils.SlotSpoofManager;
import dev.xinxin.utils.YawPitchHelper;
import dev.xinxin.utils.client.menu.BetterMainMenu;
import dev.xinxin.utils.novoshader.BackgroundShader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.compatibility.display.Display;
import sun.misc.Unsafe;

public class Client {
    @Getter
    private ExecutorService executor;
    public static Minecraft mc = Minecraft.getMinecraft();
    public static Client instance;
    public static String NAME = "SilenceFix";
    public static String VERSION = "9.50";
    public static ResourceLocation cape;
    public String USER;
    private static boolean logged;
    public String commandPrefix = ".";
    public ConfigManager configManager;
    @Getter
    public AltManager altManager;
    public ModuleManager moduleManager;
    public CommandManager commandManager;
    public UiManager uiManager;
    @Getter
    public SlotSpoofManager slotSpoofManager;
    @Getter
    public YawPitchHelper yawPitchHelper;
    public List<Float> cGUIPosX = new ArrayList<>();
    public List<Float> cGUIPosY = new ArrayList<>();
    public List<Module> cGUIInSetting = new ArrayList<>();
    public List<Value<?>> cGUIInMode = new ArrayList();
    public static Unsafe theUnsafe;
    public BackgroundShader blobShader;

    public String getUser() {
        return this.USER;
    }

    public String getVersion() {
        return VERSION;
    }

    public boolean isLogged() {
        return logged;
    }

    public void setLogged(boolean state) {
        logged = state;
    }

    public Client() {
        logged = false;
    }

    public void init() {
        this.USER = "Jinliang Xu";
        Client.logged = true;
        try {
            try {
                //SplashScreen.setProgress(10, "ModuleManager");
                Client.instance = this;
                //SplashScreen.setProgress(11, "EventManager");
                this.altManager = new AltManager();
                this.commandManager = new CommandManager();
                this.configManager = new ConfigManager();
                this.uiManager = new UiManager();
                this.slotSpoofManager = new SlotSpoofManager();
                this.yawPitchHelper = new YawPitchHelper();
                this.setWindowIcon();
                try {
                    Client.instance.setLogged(true);

                    Client.instance.moduleManager = new ModuleManager();
                    EventManager.register(Client.instance);
                    EventManager.register(new RotationComponent());
                    EventManager.register(new FallDistanceComponent());
                    EventManager.register(new InventoryClickFixComponent());
                    EventManager.register(new PingSpoofComponent());
                    EventManager.register(new BadPacketsComponent());
                    Client.instance.moduleManager.init();
                    Client.instance.commandManager.init();
                    Client.instance.uiManager.init();
                    Client.instance.configManager.loadAllConfig();
                    mc.displayGuiScreen(new BetterMainMenu());
                }
                catch (Exception ignored) {}
            }
            catch (Exception ignored) {
            }
        }
        catch (Throwable ignored) {
        }
    }


    public static void displayGuiScreen(GuiScreen guiScreenIn) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void setWindowIcon() {
        Util.EnumOS util$enumos = Util.getOSType();
        if (util$enumos != Util.EnumOS.OSX) {
            InputStream inputstream1;
            InputStream inputstream;
            block5: {
                inputstream = null;
                inputstream1 = null;
                try {
                    inputstream = Client.mc.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("/assets/minecraft/express/icon/bh16.png"));
                    inputstream1 = Client.mc.mcDefaultResourcePack.getInputStreamAssets(new ResourceLocation("/assets/minecraft/express/icon/bh32.png"));
                    if (inputstream == null || inputstream1 == null) break block5;
                    Display.setIcon(new ByteBuffer[]{mc.readImageToBuffer(inputstream), mc.readImageToBuffer(inputstream1)});
                }
                catch (IOException ioexception) {
                    try {
                        Minecraft.logger.error("Couldn't set icon", ioexception);
                    }
                    catch (Throwable throwable) {
                        IOUtils.closeQuietly(inputstream);
                        IOUtils.closeQuietly(inputstream1);
                        throw throwable;
                    }
                    IOUtils.closeQuietly(inputstream);
                    IOUtils.closeQuietly(inputstream1);
                }
            }
            IOUtils.closeQuietly(inputstream);
            IOUtils.closeQuietly(inputstream1);
        }
    }
}

