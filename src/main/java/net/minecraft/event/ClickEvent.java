package net.minecraft.event;

import com.google.common.collect.Maps;
import java.util.Map;

public class ClickEvent {
    private final Action action;
    private final String ba;

    public ClickEvent(Action theAction, String theValue) {
        this.action = theAction;
        this.ba = theValue;
    }

    public Action getAction() {
        return this.action;
    }

    public String getValue() {
        return this.ba;
    }

    public boolean equals(Object p_equals_1_) {
        if (this == p_equals_1_) {
            return true;
        }
        if (p_equals_1_ != null && this.getClass() == p_equals_1_.getClass()) {
            ClickEvent clickevent = (ClickEvent)p_equals_1_;
            if (this.action != clickevent.action) {
                return false;
            }
            return !(this.ba != null ? !this.ba.equals(clickevent.ba) : clickevent.ba != null);
        }
        return false;
    }

    public String toString() {
        return "ClickEvent{action=" + (Object)((Object)this.action) + ", value='" + this.ba + '\'' + '}';
    }

    public int hashCode() {
        int i = this.action.hashCode();
        i = 31 * i + (this.ba != null ? this.ba.hashCode() : 0);
        return i;
    }

    public static enum Action {
        OPEN_URL("open_url", true),
        OPEN_FILE("open_file", false),
        RUN_COMMAND("run_command", true),
        TWITCH_USER_INFO("twitch_user_info", false),
        SUGGEST_COMMAND("suggest_command", true),
        CHANGE_PAGE("change_page", true);

        private static final Map<String, Action> nameMapping;
        private final boolean allowedInChat;
        private final String canonicalName;

        private Action(String canonicalNameIn, boolean allowedInChatIn) {
            this.canonicalName = canonicalNameIn;
            this.allowedInChat = allowedInChatIn;
        }

        public boolean shouldAllowInChat() {
            return this.allowedInChat;
        }

        public String getCanonicalName() {
            return this.canonicalName;
        }

        public static Action getValueByCanonicalName(String canonicalNameIn) {
            return nameMapping.get(canonicalNameIn);
        }

        static {
            nameMapping = Maps.newHashMap();
            for (Action clickevent$action : Action.values()) {
                nameMapping.put(clickevent$action.getCanonicalName(), clickevent$action);
            }
        }
    }
}

