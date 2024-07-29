package dev.xinxin.module.modules.render;

import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.NumberValue;

public final class Camera
extends Module {
    public final BoolValue cameraClipValue = new BoolValue("CameraClip", false);
    public final BoolValue noHurtCameraValue = new BoolValue("NoHurtCamera", false);
    public final BoolValue betterBobbingValue = new BoolValue("BetterBobbing", false);
    public final BoolValue noFovValue = new BoolValue("NoFov", false);
    public final NumberValue fovValue = new NumberValue("Fov", 1.0, 0.0, 4.0, 0.1);

    public Camera() {
        super("Camera", Category.Render);
    }
}

