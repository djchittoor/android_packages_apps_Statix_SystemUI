package com.google.android.systemui.smartspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.icu.text.DateFormat;
import android.icu.text.DisplayContext;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import com.android.systemui.bcsmartspace.R.string;
import java.util.Locale;
import java.util.Objects;

public class IcuDateTextView extends DoubleShadowTextView {
    private DateFormat mFormatter;
    private Handler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    private String mText;
    private final Runnable mTicker;

    /* renamed from: $r8$lambda$Sd3yswBLpq1S8-i1GImy2Qrz2n0 */
    public static /* synthetic */ void m750$r8$lambda$Sd3yswBLpq1S8i1GImy2Qrz2n0(IcuDateTextView icuDateTextView) {
        icuDateTextView.onTimeTick();
    }

    public IcuDateTextView(Context context) {
        this(context, null);
    }

    public IcuDateTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
        mTicker = new Runnable() { // from class: com.google.android.systemui.smartspace.IcuDateTextView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                IcuDateTextView.m750$r8$lambda$Sd3yswBLpq1S8i1GImy2Qrz2n0(IcuDateTextView.this);
            }
        };
        mIntentReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.smartspace.IcuDateTextView.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                IcuDateTextView.onTimeChanged(!"android.intent.action.TIME_TICK".equals(intent.getAction()));
            }
        };
    }

    @Override // android.widget.TextView, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.TIME_SET");
        intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
        getContext().registerReceiver(mIntentReceiver, intentFilter);
        onTimeChanged(true);
        mHandler = new Handler();
    }

    @Override // android.view.View
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mHandler != null) {
            getContext().unregisterReceiver(mIntentReceiver);
            mHandler = null;
        }
    }

    public void onTimeTick() {
        onTimeChanged(false);
        if (mHandler != null) {
            long uptimeMillis = SystemClock.uptimeMillis();
            mHandler.postAtTime(mTicker, uptimeMillis + (1000 - (uptimeMillis % 1000)));
        }
    }

    @Override // android.view.View
    public void onVisibilityAggregated(boolean z) {
        super.onVisibilityAggregated(z);
        Handler handler = mHandler;
        if (handler != null) {
            handler.removeCallbacks(mTicker);
            if (!z) {
                return;
            }
            mTicker.run();
        }
    }

    public void onTimeChanged(boolean z) {
        if (!isShown()) {
            return;
        }
        if (mFormatter == null || z) {
            DateFormat instanceForSkeleton = DateFormat.getInstanceForSkeleton(getContext().getString(R.string.smartspace_icu_date_pattern), Locale.getDefault());
            mFormatter = instanceForSkeleton;
            instanceForSkeleton.setContext(DisplayContext.CAPITALIZATION_FOR_BEGINNING_OF_SENTENCE);
        }
        String format = mFormatter.format(Long.valueOf(System.currentTimeMillis()));
        if (Objects.equals(mText, format)) {
            return;
        }
        mText = format;
        setText(format);
        setContentDescription(format);
    }
}
