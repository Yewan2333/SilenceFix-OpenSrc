package dev.xinxin.module.modules.render;

import dev.xinxin.event.EventManager;
import dev.xinxin.event.rendering.EventShader;
import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.NumberValue;
import dev.xinxin.utils.render.RenderUtil;
import dev.xinxin.utils.render.shader.KawaseBloom;
import dev.xinxin.utils.render.shader.KawaseBlur;
import net.minecraft.client.shader.Framebuffer;

public class PostProcessing
extends Module {
    public final BoolValue blur = new BoolValue("Blur", true);
    private final NumberValue iterations = new NumberValue("Blur Iterations", 2.0, 1.0, 8.0, 1.0);
    private final NumberValue offset = new NumberValue("Blur Offset", 3.0, 1.0, 10.0, 1.0);
    private final BoolValue bloom = new BoolValue("Bloom", true);
    private final NumberValue shadowRadius = new NumberValue("Bloom Iterations", 3.0, 1.0, 8.0, 1.0);
    private final NumberValue shadowOffset = new NumberValue("Bloom Offset", 1.0, 1.0, 10.0, 1.0);
    private String currentMode;
    private Framebuffer stencilFramebuffer = new Framebuffer(1, 1, false);

    public PostProcessing() {
        super("PostProcessing", Category.Render);
    }

    public void blurScreen() {
        EventShader eventShader;
        if (!this.getState()) {
            return;
        }
        if (((Boolean)this.blur.getValue()).booleanValue()) {
            this.stencilFramebuffer = RenderUtil.createFrameBuffer(this.stencilFramebuffer);
            this.stencilFramebuffer.framebufferClear();
            this.stencilFramebuffer.bindFramebuffer(false);
            eventShader = new EventShader(false);
            EventManager.call(eventShader);
            RenderUtil.resetColor();
            this.drawBlur();
            RenderUtil.resetColor();
            this.stencilFramebuffer.unbindFramebuffer();
            KawaseBlur.renderBlur(this.stencilFramebuffer.framebufferTexture, ((Double)this.iterations.getValue()).intValue(), ((Double)this.offset.getValue()).intValue());
        }
        if (((Boolean)this.bloom.getValue()).booleanValue()) {
            this.stencilFramebuffer = RenderUtil.createFrameBuffer(this.stencilFramebuffer);
            this.stencilFramebuffer.framebufferClear();
            this.stencilFramebuffer.bindFramebuffer(false);
            eventShader = new EventShader(true);
            EventManager.call(eventShader);
            RenderUtil.resetColor();
            this.drawBloom();
            RenderUtil.resetColor();
            this.stencilFramebuffer.unbindFramebuffer();
            KawaseBloom.renderBlur(this.stencilFramebuffer.framebufferTexture, ((Double)this.shadowRadius.getValue()).intValue(), ((Double)this.shadowOffset.getValue()).intValue());
        }
    }

    private void drawBloom() {
    }

    private void drawBlur() {
    }
}

