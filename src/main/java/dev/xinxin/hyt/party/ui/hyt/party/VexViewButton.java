/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package dev.xinxin.hyt.party.ui.hyt.party;

public class VexViewButton
extends VexViewComponent {
    private final String name;

    public VexViewButton(String name, String id) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}

