package com.google.android.systemui.smartspace;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Handler;
import android.text.format.DateFormat;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.policy.NextAlarmController;
import com.android.systemui.statusbar.policy.ZenModeController;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.reflect.KFunction;

/* compiled from: KeyguardZenAlarmViewController.kt */
public final class KeyguardZenAlarmViewController {
    private final Drawable alarmImage;
    private final AlarmManager alarmManager;
    private final Context context;
    private final Handler handler;
    private final NextAlarmController nextAlarmController;
    private final BcSmartspaceDataPlugin plugin;
    private final ZenModeController zenModeController;
    private Set<BcSmartspaceDataPlugin.SmartspaceView> smartspaceViews = new LinkedHashSet();
    //private final KFunction<Unit> showNextAlarm = new KeyguardZenAlarmViewController$showNextAlarm$1(this);
    private final ZenModeController.Callback zenModeCallback = new ZenModeController.Callback() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$zenModeCallback$1
        @Override // com.android.systemui.statusbar.policy.ZenModeController.Callback
        public void onZenChanged(int i) {
            updateDnd();
        }
    };
    private final NextAlarmController.NextAlarmChangeCallback nextAlarmCallback = new NextAlarmController.NextAlarmChangeCallback() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$nextAlarmCallback$1
        @Override // com.android.systemui.statusbar.policy.NextAlarmController.NextAlarmChangeCallback
        public final void onNextAlarmChanged(AlarmManager.AlarmClockInfo alarmClockInfo) {
            updateNextAlarm();
        }
    };
    private final Drawable dndImage = loadDndImage();

    /* JADX WARN: Type inference failed for: r3v3, types: [com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$zenModeCallback$1] */
    public KeyguardZenAlarmViewController(Context context, BcSmartspaceDataPlugin plugin, ZenModeController zenModeController, AlarmManager alarmManager, NextAlarmController nextAlarmController, Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(plugin, "plugin");
        Intrinsics.checkNotNullParameter(zenModeController, "zenModeController");
        Intrinsics.checkNotNullParameter(alarmManager, "alarmManager");
        Intrinsics.checkNotNullParameter(nextAlarmController, "nextAlarmController");
        Intrinsics.checkNotNullParameter(handler, "handler");
        context = context;
        plugin = plugin;
        zenModeController = zenModeController;
        alarmManager = alarmManager;
        nextAlarmController = nextAlarmController;
        handler = handler;
        alarmImage = context.getResources().getDrawable(R.drawable.ic_access_alarms_big, null);
    }

    public final Set<BcSmartspaceDataPlugin.SmartspaceView> getSmartspaceViews() {
        return smartspaceViews;
    }

    public final void init() {
        plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
                ZenModeController zenModeController;
                ZenModeController.Callback keyguardZenAlarmViewController;
                NextAlarmController nextAlarmController;
                NextAlarmController.NextAlarmChangeCallback nextAlarmChangeCallback;
                Intrinsics.checkNotNullParameter(v, "v");
                getSmartspaceViews().add((BcSmartspaceDataPlugin.SmartspaceView) v);
                if (getSmartspaceViews().size() == 1) {
                    zenModeController = zenModeController;
                    keyguardZenAlarmViewController = zenModeCallback;
                    zenModeController.addCallback(keyguardZenAlarmViewController;
                    nextAlarmController = nextAlarmController;
                    nextAlarmChangeCallback = nextAlarmCallback;
                    nextAlarmController.addCallback(nextAlarmChangeCallback);
                }
                refresh();
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
                ZenModeController zenModeController;
                ZenModeController.Callback keyguardZenAlarmViewController;
                NextAlarmController nextAlarmController;
                NextAlarmController.NextAlarmChangeCallback nextAlarmChangeCallback;
                Intrinsics.checkNotNullParameter(v, "v");
                getSmartspaceViews().remove((BcSmartspaceDataPlugin.SmartspaceView) v);
                if (getSmartspaceViews().isEmpty()) {
                    zenModeController = zenModeController;
                    keyguardZenAlarmViewController = zenModeCallback;
                    zenModeController.removeCallback(keyguardZenAlarmViewController);
                    nextAlarmController = nextAlarmController;
                    nextAlarmChangeCallback = nextAlarmCallback;
                    nextAlarmController.removeCallback(nextAlarmChangeCallback);
                }
            }
        });
        updateNextAlarm();
    }

    public final void refresh() {
        updateDnd();
        updateNextAlarm();
    }

    private final Drawable loadDndImage() {
        Drawable drawable = context.getResources().getDrawable(R.drawable.stat_sys_dnd, null);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.InsetDrawable");
        Drawable drawable2 = ((InsetDrawable) drawable).getDrawable();
        Intrinsics.checkNotNullExpressionValue(drawable2, "withInsets.getDrawable()");
        return drawable2;
    }

    @VisibleForTesting
    public final void updateDnd() {
        if (zenModeController.getZen() != 0) {
            String string = context.getResources().getString(R.string.accessibility_quick_settings_dnd);
            for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView : smartspaceViews) {
                smartspaceView.setDnd(dndImage, string);
            }
            return;
        }
        for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 : smartspaceViews) {
            smartspaceView2.setDnd(null, null);
        }
    }

    public final void updateNextAlarm() {
        AlarmManager alarmManager = alarmManager;
        //final Function0 function0 = (Function0) showNextAlarm;
        alarmManager.cancel(new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$sam$android_app_AlarmManager_OnAlarmListener$0
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                showAlarm();
            }
        });
        long nextAlarm = zenModeController.getNextAlarm();
        if (nextAlarm > 0) {
            long millis = nextAlarm - TimeUnit.HOURS.toMillis(12L);
            if (millis > 0) {
                AlarmManager alarmManager2 = alarmManager;
                //final Function0 function02 = (Function0) showNextAlarm;
                alarmManager2.setExact(1, millis, "lock_screen_next_alarm", new AlarmManager.OnAlarmListener() { // from class: com.google.android.systemui.smartspace.KeyguardZenAlarmViewController$sam$android_app_AlarmManager_OnAlarmListener$0
                    @Override // android.app.AlarmManager.OnAlarmListener
                    public final void onAlarm() {
                        showAlarm();
                    }
                }, handler);
            }
        }
        showAlarm();
    }

    @VisibleForTesting
    public final void showAlarm() {
        long nextAlarm = zenModeController.getNextAlarm();
        if (nextAlarm > 0 && withinNHours(nextAlarm, 12L)) {
            String obj = DateFormat.format(DateFormat.is24HourFormat(context, ActivityManager.getCurrentUser()) ? "HH:mm" : "h:mm", nextAlarm).toString();
            for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView : smartspaceViews) {
                smartspaceView.setNextAlarm(alarmImage, obj);
            }
            return;
        }
        for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView2 : smartspaceViews) {
            smartspaceView2.setNextAlarm(null, null);
        }
    }

    private final boolean withinNHours(long j, long j2) {
        return j <= System.currentTimeMillis() + TimeUnit.HOURS.toMillis(j2);
    }
}
