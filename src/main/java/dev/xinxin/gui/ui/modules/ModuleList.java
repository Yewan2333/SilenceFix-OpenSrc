package dev.xinxin.gui.ui.modules;

import dev.xinxin.Client;
import dev.xinxin.event.EventTarget;
import dev.xinxin.event.rendering.EventRender2D;
import dev.xinxin.event.rendering.EventShader;
import dev.xinxin.gui.ui.UiModule;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.modules.render.HUD;
import dev.xinxin.utils.render.ColorUtil;
import dev.xinxin.utils.render.RenderUtil;
import dev.xinxin.utils.render.animation.Animation;
import dev.xinxin.utils.render.animation.Direction;
import dev.xinxin.utils.render.fontRender.FontManager;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;

public class ModuleList
        extends UiModule {
    public List<Module> modules;

    public ModuleList() {
        super("ModuleList", RenderUtil.width(), 0.0, 100.0, 0.0);
    }

    @EventTarget
    public void blur(EventShader event) {
        double yOffset = 0.0;
        ScaledResolution sr = new ScaledResolution(mc);
        for (Module module : this.modules) {
            if (((Boolean)HUD.importantModules.getValue()).booleanValue() && module.getCategory() == Category.Render) continue;
            Animation moduleAnimation = module.getAnimation();
            if (!module.getState() && moduleAnimation.finished(Direction.BACKWARDS)) continue;
            String displayText = this.formatModule(module);
            double textWidth = FontManager.arial22.getStringWidth(displayText);
            double xValue = sr.getScaledWidth() - 10;
            boolean flip = xValue <= (double)((float)sr.getScaledWidth() / 2.0f);
            double x2 = flip ? xValue : (double)sr.getScaledWidth() - (textWidth + 3.0);
            double y2 = yOffset + 4.0;
            double heightVal = (Double)HUD.height.getValue() + 1.0;
            switch (((ANIM)((Object)HUD.animation.getValue())).name()) {
                case "MoveIn": {
                    if (flip) {
                        x2 -= Math.abs((moduleAnimation.getOutput() - 1.0) * ((double)sr.getScaledWidth() - (2.0 + textWidth)));
                        break;
                    }
                    x2 += Math.abs((moduleAnimation.getOutput() - 1.0) * (2.0 + textWidth));
                    break;
                }
                case "ScaleIn": {
                    RenderUtil.scaleStart((float)(x2 + (double)((float)FontManager.arial22.getStringWidth(displayText) / 2.0f)), (float)(y2 + heightVal / 2.0 - (double)((float)FontManager.arial26.getHeight() / 2.0f)), (float)moduleAnimation.getOutput());
                }
            }
            if (((Boolean)HUD.background.getValue()).booleanValue()) {
                Gui.drawRect3((float)(x2 - 2.0), (float)(y2 - 3.0), FontManager.arial22.getStringWidth(displayText) + 5, (float)heightVal, new Color(0, 0, 0).getRGB());
            }
            if (((ANIM)((Object)HUD.animation.getValue())).name() == "ScaleIn") {
                RenderUtil.scaleEnd();
            }
            yOffset += moduleAnimation.getOutput() * heightVal;
        }
    }

    private String formatModule(Module module) {
        String name = module.getName();
        name = name.replaceAll(" ", "");
        String formatText = "%s %s%s";
        String suffix = module.getSuffix();
        if (suffix == null || suffix.isEmpty()) {
            return name;
        }
        return String.format(formatText, new Object[]{name, EnumChatFormatting.GRAY, suffix});
    }

    public List<Module> getModules() {
        Stream<Module> stream = Client.instance.moduleManager.getModules().stream();
        stream = stream.sorted((mod1, mod2) -> Long.compare(FontManager.arial26.getStringWidth(this.formatModule((Module)mod2)), FontManager.arial26.getStringWidth(this.formatModule((Module)mod1))));
        return stream.collect(Collectors.toList());
    }

    @EventTarget
    public void onRender2D(EventRender2D event) {
        ArrayList moduleList = new ArrayList();
        this.modules = this.getModules().stream().filter(Module::getState).collect(Collectors.toList());
        double yOffset = 0.0;
        ScaledResolution sr = new ScaledResolution(mc);
        int count = 0;
        for (Module module2 : this.modules) {
            if (((Boolean)HUD.importantModules.getValue()).booleanValue() && module2.getCategory() == Category.Render) continue;
            Animation moduleAnimation = module2.getAnimation();
            moduleAnimation.setDirection(module2.getState() ? Direction.FORWARDS : Direction.BACKWARDS);
            if (!module2.getState() && moduleAnimation.finished(Direction.BACKWARDS)) continue;
            String displayText = this.formatModule(module2);
            double textWidth = FontManager.arial22.getStringWidth(displayText);
            double xValue = sr.getScaledWidth() - 10;
            boolean flip = xValue <= (double)((float)sr.getScaledWidth() / 2.0f);
            double x = flip ? xValue : (double)sr.getScaledWidth() - (textWidth + 3.0);
            float alphaAnimation = 1.0f;
            double y = yOffset + 4.0;
            double heightVal = (Double)HUD.height.getValue() + 1.0;
            switch (((ANIM)((Object)HUD.animation.getValue())).name()) {
                case "MoveIn": {
                    if (flip) {
                        x -= Math.abs((moduleAnimation.getOutput() - 1.0) * ((double)sr.getScaledWidth() - (2.0 - textWidth)));
                        break;
                    }
                    x += Math.abs((moduleAnimation.getOutput() - 1.0) * (2.0 + textWidth));
                    break;
                }
                case "ScaleIn": {
                    RenderUtil.scaleStart((float)(x + (double)((float)FontManager.arial22.getStringWidth(displayText) / 2.0f)), (float)(y + heightVal / 2.0 - (double)((float)FontManager.arial26.getHeight() / 2.0f)), (float)moduleAnimation.getOutput());
                    alphaAnimation = (float)moduleAnimation.getOutput();
                }
            }
            if (((Boolean)HUD.background.getValue()).booleanValue()) {
                Gui.drawRect3((float)(x - 2.0), (float)(y - 3.0), FontManager.arial22.getStringWidth(displayText) + 5, (float)heightVal, ColorUtil.applyOpacity(new Color(20, 20, 20), ((Double)HUD.backgroundAlpha.getValue()).floatValue() * alphaAnimation).getRGB());
            }
            if (((Boolean)HUD.hLine.getValue()).booleanValue()) {
                Gui.drawRect3((float)RenderUtil.width() - 1.0f, (float)(y - 3.0), 1.0, (float)heightVal, HUD.color(count).getRGB());
            }
            int textcolor = HUD.color(count).getRGB();
            FontManager.arial22.drawStringWithShadow(displayText, (float)x, (float)(y - 1.0 + (double)FontManager.arial22.getMiddleOfBox((float)heightVal)), ColorUtil.applyOpacity(textcolor, alphaAnimation));
            if (((ANIM)((Object)HUD.animation.getValue())).name() == "ScaleIn") {
                RenderUtil.scaleEnd();
            }
            yOffset += moduleAnimation.getOutput() * heightVal;
            ++count;
        }
    }
    public static enum ANIM {
        MoveIn,
        ScaleIn;

        private ANIM() {
        }

        // $FF: synthetic method
        private static ModuleList.ANIM[] $values() {
            return new ModuleList.ANIM[]{MoveIn, ScaleIn};
        }
    }
}
