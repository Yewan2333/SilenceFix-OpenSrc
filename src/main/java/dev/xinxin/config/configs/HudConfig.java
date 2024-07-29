package dev.xinxin.config.configs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.xinxin.Client;
import dev.xinxin.config.Config;
import dev.xinxin.gui.ui.UiModule;
import dev.xinxin.utils.render.RenderUtil;

public class HudConfig
extends Config {
    public HudConfig() {
        super("hud.json");
    }

    @Override
    public JsonObject saveConfig() {
        JsonObject object = new JsonObject();
        for (UiModule hud : Client.instance.uiManager.getModules()) {
            JsonObject hudObject = new JsonObject();
            hudObject.addProperty("x", (Number)hud.posX);
            hudObject.addProperty("y", (Number)hud.posY);
            object.add(hud.getName(), (JsonElement)hudObject);
        }
        return object;
    }

    @Override
    public void loadConfig(JsonObject object) {
        for (UiModule hud : Client.instance.uiManager.getModules()) {
            if (!object.has(hud.getName())) continue;
            JsonObject hudObject = object.get(hud.getName()).getAsJsonObject();
            hud.setPosX(hudObject.get("x").getAsDouble() * (double)RenderUtil.width());
            hud.setPosY(hudObject.get("y").getAsDouble() * (double)RenderUtil.height());
        }
    }
}

