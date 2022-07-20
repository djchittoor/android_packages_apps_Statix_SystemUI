package com.google.android.systemui.smartspace;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* compiled from: KeyguardZenAlarmViewController.kt */
/* loaded from: classes2.dex */
/* synthetic */ class KeyguardZenAlarmViewController$showNextAlarm$1 extends FunctionReferenceImpl implements Function0<Unit> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public KeyguardZenAlarmViewController$showNextAlarm$1(KeyguardZenAlarmViewController keyguardZenAlarmViewController) {
        super(0, keyguardZenAlarmViewController, KeyguardZenAlarmViewController.class, "showAlarm", "showAlarm()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: avoid collision after fix types in other method */
    public final void invoke2() {
        ((KeyguardZenAlarmViewController) this.receiver).showAlarm();
    }
}
