package net.minecraft.client.settings;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.xinxin.event.EventManager;
import dev.xinxin.event.misc.EventKey;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.IntHashMap;

public class KeyBinding
implements Comparable<KeyBinding> {
    private static final List<KeyBinding> keybindArray = Lists.newArrayList();
    private static final IntHashMap<KeyBinding> hash = new IntHashMap();
    private static final Set<String> keybindSet = Sets.newHashSet();
    private final String keyDescription;
    private final int keyCodeDefault;
    private final String keyCategory;
    private int keyCode;
    public boolean pressed;
    private int pressTime;

    public static void onTick(int keyCode) {
        KeyBinding keybinding;
        if (keyCode != 0 && (keybinding = hash.lookup(keyCode)) != null) {
            ++keybinding.pressTime;
        }
    }

    public static void setKeyBindState(int keyCode, boolean pressed) {
        KeyBinding keybinding;
        if (keyCode != 0 && (keybinding = hash.lookup(keyCode)) != null) {
            keybinding.pressed = pressed;
        }
    }

    public static void unPressAllKeys() {
        for (KeyBinding keybinding : keybindArray) {
            keybinding.unpressKey(keybinding.getKeyCode());
        }
    }

    public static void resetKeyBindingArrayAndHash() {
        hash.clearMap();
        for (KeyBinding keybinding : keybindArray) {
            hash.addKey(keybinding.keyCode, keybinding);
        }
    }

    public static Set<String> getKeybinds() {
        return keybindSet;
    }

    public KeyBinding(String description, int keyCode, String category) {
        this.keyDescription = description;
        this.keyCode = keyCode;
        this.keyCodeDefault = keyCode;
        this.keyCategory = category;
        keybindArray.add(this);
        hash.addKey(keyCode, this);
        keybindSet.add(category);
    }

    public boolean isKeyDown() {
        return this.pressed;
    }

    public String getKeyCategory() {
        return this.keyCategory;
    }

    public boolean isPressed() {
        if (this.pressTime == 0) {
            return false;
        }
        --this.pressTime;
        return true;
    }

    private void unpressKey(int key) {
        if (this.isKeyDown()) {
            EventKey keyPressEvent = new EventKey(key);
            EventManager.call(keyPressEvent);
            if (!keyPressEvent.isCancelled()) {
                this.pressTime = 0;
                this.pressed = false;
            }
        }
    }

    public String getKeyDescription() {
        return this.keyDescription;
    }

    public int getKeyCodeDefault() {
        return this.keyCodeDefault;
    }

    public int getKeyCode() {
        return this.keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    @Override
    public int compareTo(KeyBinding p_compareTo_1_) {
        int i = I18n.format(this.keyCategory, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyCategory, new Object[0]));
        if (i == 0) {
            i = I18n.format(this.keyDescription, new Object[0]).compareTo(I18n.format(p_compareTo_1_.keyDescription, new Object[0]));
        }
        return i;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }
}

