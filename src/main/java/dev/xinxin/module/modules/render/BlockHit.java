package dev.xinxin.module.modules.render;

import dev.xinxin.module.Category;
import dev.xinxin.module.Module;
import dev.xinxin.module.values.BoolValue;
import dev.xinxin.module.values.ModeValue;
import dev.xinxin.module.values.NumberValue;

public class BlockHit
extends Module {
    public static final ModeValue<swords> Sword = new ModeValue("Style", (Enum[])swords.values(), (Enum)swords.Stella);
    public static final NumberValue slowdown = new NumberValue("Swing Slowdown", 1, 1, 15, 1);
    public static final BoolValue smallSwing = new BoolValue("Small Swing", false);
    public static final NumberValue x = new NumberValue("X", 0, -50, 50, 1);
    public static final NumberValue y = new NumberValue("Y", 0, -50, 50, 1);
    public static final NumberValue size = new NumberValue("Size", 0, -50, 50, 1);

    public BlockHit() {
        super("BlockAnimations", Category.Render);
    }

    public static enum swords {
        Stella,
        Middle,
        Minecraft,
        Exhi,
        Exhi2,
        Exhi3,
        Exhi4,
        Exhi5,
        Shred,
        Custom,
        Smooth,
        Sigma
    }
}

