package dev.xinxin.gui.verify;

import dev.xinxin.gui.altmanager.ColorUtils;
import dev.xinxin.gui.clickgui.book.RippleAnimation;
import dev.xinxin.utils.render.RenderUtil;
import dev.xinxin.utils.render.RoundedUtils;
import dev.xinxin.utils.render.animation.Animation;
import dev.xinxin.utils.render.animation.Direction;
import dev.xinxin.utils.render.animation.impl.DecelerateAnimation;
import dev.xinxin.utils.render.fontRender.FontManager;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class LoginButton extends GuiScreen {
    public final String text;
    public RippleAnimation a = new RippleAnimation();
    public Animation a2 = new DecelerateAnimation(500, 1.0);
    public float x;
    public float y;
    public float width;
    public float height;
    public Runnable clickAction;

    public LoginButton(String text){
        this.text = text;
    }
    @Override
    public void keyTyped(char typedChar, int keyCode) {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float ticks) {
        boolean hovered = RenderUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        a2.setDirection(hovered ? Direction.FORWARDS : Direction.BACKWARDS);
        RoundedUtils.drawRound(x, y, width, height, 12, ColorUtils.interpolateColorC(new Color(1, 244, 244), new Color(2, 198, 255),(float) a2.getOutput()));
        a.draw(() -> RoundedUtils.drawRound(x, y, width, height, 12, ColorUtils.interpolateColorC(new Color(1, 244, 244), new Color(2, 198, 255),(float) a2.getOutput())));
        FontManager.arial20.drawCenteredString(text, x + width / 2, y + height / 2 - 4, -1);
    }
    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hovered = RenderUtil.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY);
        a.mouseClicked(mouseX, mouseY);
        if (hovered) {
            this.clickAction.run();
        }
    }
    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
    }

}
