package com.google.android.systemui.smartspace;

import java.util.Objects;

/* loaded from: classes2.dex */
public final class SmallHash {
    public static int hash(String str) {
        return hash(Objects.hashCode(str));
    }

    public static int hash(int i) {
        return Math.abs(Math.floorMod(i, 8192));
    }
}
