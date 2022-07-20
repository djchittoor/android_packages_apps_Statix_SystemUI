package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadata;
import android.os.UserHandle;
import android.text.TextUtils;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.R.string;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.settings.CurrentUserTracker;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: KeyguardMediaViewController.kt */
public final class KeyguardMediaViewController {
    private CharSequence artist;
    private final BroadcastDispatcher broadcastDispatcher;
    private final Context context;
    private final ComponentName mediaComponent;
    private final KeyguardMediaViewController$mediaListener$1 mediaListener = new NotificationMediaManager.MediaListener() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1
        @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
        public void onPrimaryMetadataOrStateChanged(final MediaMetadata mediaMetadata, final int i) {
            DelayableExecutor uiExecutor = KeyguardMediaViewController.getUiExecutor();
            final KeyguardMediaViewController keyguardMediaViewController = KeyguardMediaViewController.this;
            uiExecutor.execute(new Runnable() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1$onPrimaryMetadataOrStateChanged$1
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardMediaViewController.updateMediaInfo(mediaMetadata, i);
                }
            });
        }
    };
    private final NotificationMediaManager mediaManager;
    private final BcSmartspaceDataPlugin plugin;
    private BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    private CharSequence title;
    private final DelayableExecutor uiExecutor;
    private CurrentUserTracker userTracker;

    /* JADX WARN: Type inference failed for: r3v1, types: [com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1] */
    public KeyguardMediaViewController(Context context, BcSmartspaceDataPlugin plugin, DelayableExecutor uiExecutor, NotificationMediaManager mediaManager, BroadcastDispatcher broadcastDispatcher) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(plugin, "plugin");
        Intrinsics.checkNotNullParameter(uiExecutor, "uiExecutor");
        Intrinsics.checkNotNullParameter(mediaManager, "mediaManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        context = context;
        plugin = plugin;
        uiExecutor = uiExecutor;
        mediaManager = mediaManager;
        broadcastDispatcher = broadcastDispatcher;
        mediaComponent = new ComponentName(context, KeyguardMediaViewController.class);
    }

    public final DelayableExecutor getUiExecutor() {
        return uiExecutor;
    }

    public final BcSmartspaceDataPlugin.SmartspaceView getSmartspaceView() {
        return smartspaceView;
    }

    public final void setSmartspaceView(BcSmartspaceDataPlugin.SmartspaceView smartspaceView) {
        smartspaceView = smartspaceView;
    }

    public final void init() {
        plugin.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View v) {
                NotificationMediaManager notificationMediaManager;
                KeyguardMediaViewController$mediaListener$1 keyguardMediaViewController$mediaListener$1;
                Intrinsics.checkNotNullParameter(v, "v");
                KeyguardMediaViewController.setSmartspaceView((BcSmartspaceDataPlugin.SmartspaceView) v);
                notificationMediaManager = KeyguardMediaViewController.mediaManager;
                keyguardMediaViewController$mediaListener$1 = KeyguardMediaViewController.mediaListener;
                notificationMediaManager.addCallback(keyguardMediaViewController$mediaListener$1);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View v) {
                NotificationMediaManager notificationMediaManager;
                KeyguardMediaViewController$mediaListener$1 keyguardMediaViewController$mediaListener$1;
                Intrinsics.checkNotNullParameter(v, "v");
                KeyguardMediaViewController.setSmartspaceView(null);
                notificationMediaManager = KeyguardMediaViewController.mediaManager;
                keyguardMediaViewController$mediaListener$1 = KeyguardMediaViewController.mediaListener;
                notificationMediaManager.removeCallback(keyguardMediaViewController$mediaListener$1);
            }
        });
        final BroadcastDispatcher broadcastDispatcher = broadcastDispatcher;
        userTracker = new CurrentUserTracker(broadcastDispatcher) { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$init$2
            @Override // com.android.systemui.settings.CurrentUserTracker
            public void onUserSwitched(int i) {
                KeyguardMediaViewController.reset();
            }
        };
    }

    public final void updateMediaInfo(MediaMetadata mediaMetadata, int i) {
        CharSequence charSequence;
        if (!NotificationMediaManager.isPlayingState(i)) {
            reset();
            return;
        }
        Unit unit = null;
        if (mediaMetadata == null) {
            charSequence = null;
        } else {
            charSequence = mediaMetadata.getText("android.media.metadata.TITLE");
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = context.getResources().getString(R.string.music_controls_no_title);
            }
        }
        CharSequence text = mediaMetadata == null ? null : mediaMetadata.getText("android.media.metadata.ARTIST");
        if (TextUtils.equals(title, charSequence) && TextUtils.equals(artist, text)) {
            return;
        }
        title = charSequence;
        artist = text;
        if (charSequence != null) {
            SmartspaceAction build = new SmartspaceAction.Builder("deviceMediaTitle", charSequence.toString()).setSubtitle(artist).setIcon(mediaManager.getMediaIcon()).build();
            CurrentUserTracker currentUserTracker = userTracker;
            if (currentUserTracker == null) {
                Intrinsics.throwUninitializedPropertyAccessException("userTracker");
                throw null;
            }
            SmartspaceTarget build2 = new SmartspaceTarget.Builder("deviceMedia", mediaComponent, UserHandle.of(currentUserTracker.getCurrentUserId())).setFeatureType(41).setHeaderAction(build).build();
            BcSmartspaceDataPlugin.SmartspaceView smartspaceView = getSmartspaceView();
            if (smartspaceView != null) {
                smartspaceView.setMediaTarget(build2);
                unit = Unit.INSTANCE;
            }
        }
        if (unit != null) {
            return;
        }
        reset();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void reset() {
        title = null;
        artist = null;
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = smartspaceView;
        if (smartspaceView == null) {
            return;
        }
        smartspaceView.setMediaTarget(null);
    }
}
