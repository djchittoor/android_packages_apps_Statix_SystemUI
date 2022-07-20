package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.bcsmartspace.R.layout;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class BcSmartspaceDataProvider implements BcSmartspaceDataPlugin {
    private final Set<BcSmartspaceDataPlugin.SmartspaceTargetListener> mSmartspaceTargetListeners = new HashSet();
    private final List<SmartspaceTarget> mSmartspaceTargets = new ArrayList();
    private Set<View> mViews = new HashSet();
    private Set<View.OnAttachStateChangeListener> mAttachListeners = new HashSet();
    private BcSmartspaceDataPlugin.SmartspaceEventNotifier mEventNotifier = null;
    private View.OnAttachStateChangeListener mStateChangeListener = new View.OnAttachStateChangeListener() { // from class: com.google.android.systemui.smartspace.BcSmartspaceDataProvider.1
        {
            BcSmartspaceDataProvider.this = this;
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
            BcSmartspaceDataProvider.mViews.add(view);
            for (View.OnAttachStateChangeListener onAttachStateChangeListener : BcSmartspaceDataProvider.mAttachListeners) {
                onAttachStateChangeListener.onViewAttachedToWindow(view);
            }
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            BcSmartspaceDataProvider.mViews.remove(view);
            view.removeOnAttachStateChangeListener(this);
            for (View.OnAttachStateChangeListener onAttachStateChangeListener : BcSmartspaceDataProvider.mAttachListeners) {
                onAttachStateChangeListener.onViewDetachedFromWindow(view);
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$CVMuLEb73PWV8fVSpB1qh1kx5WM(BcSmartspaceDataProvider bcSmartspaceDataProvider, BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        bcSmartspaceDataProvider.lambda$onTargetsAvailable$0(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void registerListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        mSmartspaceTargetListeners.add(smartspaceTargetListener);
        smartspaceTargetListener.onSmartspaceTargetsUpdated(mSmartspaceTargets);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void unregisterListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        mSmartspaceTargetListeners.remove(smartspaceTargetListener);
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void registerSmartspaceEventNotifier(BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier) {
        mEventNotifier = smartspaceEventNotifier;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
        BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier = mEventNotifier;
        if (smartspaceEventNotifier != null) {
            smartspaceEventNotifier.notifySmartspaceEvent(smartspaceTargetEvent);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public BcSmartspaceDataPlugin.SmartspaceView getView(ViewGroup viewGroup) {
        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.smartspace_enhanced, viewGroup, false);
        inflate.addOnAttachStateChangeListener(mStateChangeListener);
        return (BcSmartspaceDataPlugin.SmartspaceView) inflate;
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void addOnAttachStateChangeListener(View.OnAttachStateChangeListener onAttachStateChangeListener) {
        mAttachListeners.add(onAttachStateChangeListener);
        for (View view : mViews) {
            onAttachStateChangeListener.onViewAttachedToWindow(view);
        }
    }

    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin
    public void onTargetsAvailable(List<SmartspaceTarget> list) {
        mSmartspaceTargets.clear();
        for (SmartspaceTarget smartspaceTarget : list) {
            if (smartspaceTarget.getFeatureType() != 15) {
                mSmartspaceTargets.add(smartspaceTarget);
            }
        }
        mSmartspaceTargetListeners.forEach(new Consumer() { // from class: com.google.android.systemui.smartspace.BcSmartspaceDataProvider$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BcSmartspaceDataProvider.$r8$lambda$CVMuLEb73PWV8fVSpB1qh1kx5WM(BcSmartspaceDataProvider.this, (BcSmartspaceDataPlugin.SmartspaceTargetListener) obj);
            }
        });
    }

    public /* synthetic */ void lambda$onTargetsAvailable$0(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        smartspaceTargetListener.onSmartspaceTargetsUpdated(mSmartspaceTargets);
    }
}
