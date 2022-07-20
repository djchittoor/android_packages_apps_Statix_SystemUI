package com.google.android.systemui.smartspace;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import androidx.viewpager.widget.ViewPager;

/* loaded from: classes2.dex */
public class InterceptingViewPager extends ViewPager {
    private boolean mHasPerformedLongPress;
    private boolean mHasPostedLongPress;
    private final EventProxy mSuperOnTouch = new EventProxy() { // from class: com.google.android.systemui.smartspace.InterceptingViewPager$$ExternalSyntheticLambda1
        @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
        public final boolean delegateEvent(MotionEvent motionEvent) {
            return InterceptingViewPager.m752$r8$lambda$Kinb8UkpjhBhKntCOQxRMNdiw(InterceptingViewPager.this, motionEvent);
        }
    };
    private final EventProxy mSuperOnIntercept = new EventProxy() { // from class: com.google.android.systemui.smartspace.InterceptingViewPager$$ExternalSyntheticLambda0
        @Override // com.google.android.systemui.smartspace.InterceptingViewPager.EventProxy
        public final boolean delegateEvent(MotionEvent motionEvent) {
            return InterceptingViewPager.$r8$lambda$3SWsnYuFjnqtbymqfR4U1UuMdzc(InterceptingViewPager.this, motionEvent);
        }
    };
    private final Runnable mLongPressCallback = new Runnable() { // from class: com.google.android.systemui.smartspace.InterceptingViewPager$$ExternalSyntheticLambda2
        @Override // java.lang.Runnable
        public final void run() {
            InterceptingViewPager.m751$r8$lambda$8ptLCUmemqW7BFR05TfzQ3Fpqk(InterceptingViewPager.this);
        }
    };

    /* loaded from: classes2.dex */
    public interface EventProxy {
        boolean delegateEvent(MotionEvent motionEvent);
    }

    public static /* synthetic */ boolean $r8$lambda$3SWsnYuFjnqtbymqfR4U1UuMdzc(InterceptingViewPager interceptingViewPager, MotionEvent motionEvent) {
        return interceptingViewPager.lambda$new$1(motionEvent);
    }

    /* renamed from: $r8$lambda$8ptLC-UmemqW7BFR05TfzQ3Fpqk */
    public static /* synthetic */ void m751$r8$lambda$8ptLCUmemqW7BFR05TfzQ3Fpqk(InterceptingViewPager interceptingViewPager) {
        interceptingViewPager.triggerLongPress();
    }

    /* renamed from: $r8$lambda$Kinb8UkpjhBhKntC-OQxRM-Ndiw */
    public static /* synthetic */ boolean m752$r8$lambda$Kinb8UkpjhBhKntCOQxRMNdiw(InterceptingViewPager interceptingViewPager, MotionEvent motionEvent) {
        return interceptingViewPager.lambda$new$0(motionEvent);
    }

    public /* synthetic */ boolean lambda$new$0(MotionEvent motionEvent) {
        return super.onTouchEvent(motionEvent);
    }

    public /* synthetic */ boolean lambda$new$1(MotionEvent motionEvent) {
        return super.onInterceptTouchEvent(motionEvent);
    }

    public InterceptingViewPager(Context context) {
        super(context);
    }

    public InterceptingViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return handleTouchOverride(motionEvent, this.mSuperOnIntercept);
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return handleTouchOverride(motionEvent, this.mSuperOnTouch);
    }

    private boolean handleTouchOverride(MotionEvent motionEvent, EventProxy eventProxy) {
        int action = motionEvent.getAction();
        if (action == 0) {
            this.mHasPerformedLongPress = false;
            if (isLongClickable()) {
                cancelScheduledLongPress();
                this.mHasPostedLongPress = true;
                postDelayed(this.mLongPressCallback, ViewConfiguration.getLongPressTimeout());
            }
        } else if (action == 1 || action == 3) {
            cancelScheduledLongPress();
        }
        if (this.mHasPerformedLongPress) {
            cancelScheduledLongPress();
            return true;
        } else if (!eventProxy.delegateEvent(motionEvent)) {
            return false;
        } else {
            cancelScheduledLongPress();
            return true;
        }
    }

    private void cancelScheduledLongPress() {
        if (this.mHasPostedLongPress) {
            this.mHasPostedLongPress = false;
            removeCallbacks(this.mLongPressCallback);
        }
    }

    public void triggerLongPress() {
        this.mHasPerformedLongPress = true;
        if (performLongClick()) {
            getParent().requestDisallowInterceptTouchEvent(true);
        }
    }
}
