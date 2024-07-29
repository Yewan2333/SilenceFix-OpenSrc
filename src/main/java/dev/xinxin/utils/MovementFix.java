package dev.xinxin.utils;

public enum MovementFix {
    OFF("Off"),
    NORMAL("Null"),
    TRADITIONAL("Traditional"),
    BACKWARDS_SPRINT("Backwards Sprint");

    String name;

    public String toString() {
        return this.name;
    }

    private MovementFix(String name) {
        this.name = name;
    }
}

