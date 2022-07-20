package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ComponentName;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.android.internal.graphics.ColorUtils;
import com.android.launcher3.icons.GraphicsUtils;
import com.android.systemui.bcsmartspace.R.layout;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggerUtil;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CardPagerAdapter extends PagerAdapter {
    private int mCurrentTextColor;
    private BcSmartspaceDataPlugin mDataProvider;
    private int mPrimaryTextColor;
    private final View mRoot;
    private List<SmartspaceTarget> mSmartspaceTargets = new ArrayList();
    private final List<SmartspaceTarget> mTargetsExcludingMediaAndHolidayAlarms = new ArrayList();
    private final List<SmartspaceTarget> mMediaTargets = new ArrayList();
    private boolean mHasOnlyDefaultDateCard = false;
    private final SparseArray<ViewHolder> mHolders = new SparseArray<>();
    private float mDozeAmount = 0.0f;
    private int mDozeColor = -1;
    private String mDndDescription = null;
    private Drawable mDndImage = null;
    private String mNextAlarmDescription = null;
    private Drawable mNextAlarmImage = null;
    private SmartspaceTarget mHolidayAlarmsTarget = null;

    /* renamed from: $r8$lambda$S-RMUIR888hx7Olx-M4EEkQ_BFA */
    public static /* synthetic */ void m749$r8$lambda$SRMUIR888hx7OlxM4EEkQ_BFA(CardPagerAdapter cardPagerAdapter, Parcelable parcelable) {
        cardPagerAdapter.lambda$setTargets$0(parcelable);
    }

    public CardPagerAdapter(View view) {
        this.mRoot = view;
        int attrColor = GraphicsUtils.getAttrColor(view.getContext(), 16842806);
        this.mPrimaryTextColor = attrColor;
        this.mCurrentTextColor = attrColor;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.mSmartspaceTargets.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view, Object obj) {
        return view == ((ViewHolder) obj).card;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        viewGroup.removeView(viewHolder.card);
        if (this.mHolders.get(i) == viewHolder) {
            this.mHolders.remove(i);
        }
    }

    public BcSmartspaceCard getCardAtPosition(int i) {
        ViewHolder viewHolder = this.mHolders.get(i);
        if (viewHolder == null) {
            return null;
        }
        return viewHolder.card;
    }

    public SmartspaceTarget getTargetAtPosition(int i) {
        if (this.mSmartspaceTargets.isEmpty() || i < 0 || i >= this.mSmartspaceTargets.size()) {
            return null;
        }
        return this.mSmartspaceTargets.get(i);
    }

    public List<SmartspaceTarget> getTargets() {
        return this.mSmartspaceTargets;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getItemPosition(Object obj) {
        ViewHolder viewHolder = (ViewHolder) obj;
        SmartspaceTarget targetAtPosition = getTargetAtPosition(viewHolder.position);
        if (viewHolder.target == targetAtPosition) {
            return -1;
        }
        if (targetAtPosition == null || getFeatureType(targetAtPosition) != getFeatureType(viewHolder.target) || !Objects.equals(targetAtPosition.getSmartspaceTargetId(), viewHolder.target.getSmartspaceTargetId())) {
            return -2;
        }
        viewHolder.target = targetAtPosition;
        onBindViewHolder(viewHolder);
        return -1;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public ViewHolder instantiateItem(ViewGroup viewGroup, int i) {
        SmartspaceTarget smartspaceTarget = this.mSmartspaceTargets.get(i);
        BcSmartspaceCard createBaseCard = createBaseCard(viewGroup, getFeatureType(smartspaceTarget));
        ViewHolder viewHolder = new ViewHolder(i, createBaseCard, smartspaceTarget);
        onBindViewHolder(viewHolder);
        viewGroup.addView(createBaseCard);
        this.mHolders.put(i, viewHolder);
        return viewHolder;
    }

    private int getFeatureType(SmartspaceTarget smartspaceTarget) {
        List actionChips = smartspaceTarget.getActionChips();
        int featureType = smartspaceTarget.getFeatureType();
        return (actionChips == null || actionChips.isEmpty()) ? featureType : (featureType == 13 && actionChips.size() == 1) ? -2 : -1;
    }

    private BcSmartspaceCard createBaseCard(ViewGroup viewGroup, int i) {
        int i2;
        int i3;
        if (i == -2) {
            i2 = R.layout.smartspace_card_at_store;
        } else if (i == 1) {
            i2 = R.layout.smartspace_card_date;
        } else if (i == 20) {
            i2 = R.layout.smartspace_base_card_package_delivery;
        } else if (i == 30) {
            i2 = R.layout.smartspace_base_card_doorbell;
        } else {
            i2 = R.layout.smartspace_card;
        }
        LayoutInflater from = LayoutInflater.from(viewGroup.getContext());
        BcSmartspaceCard bcSmartspaceCard = (BcSmartspaceCard) from.inflate(i2, viewGroup, false);
        if (i == -2) {
            i3 = R.layout.smartspace_card_combination_at_store;
        } else if (i == -1) {
            i3 = R.layout.smartspace_card_combination;
        } else {
            if (i != 3) {
                if (i == 4) {
                    i3 = R.layout.smartspace_card_flight;
                } else if (i == 9) {
                    i3 = R.layout.smartspace_card_sports;
                } else if (i == 10) {
                    i3 = R.layout.smartspace_card_weather_forecast;
                } else if (i == 13) {
                    i3 = R.layout.smartspace_card_shopping_list;
                } else if (i == 14) {
                    i3 = R.layout.smartspace_card_loyalty;
                } else if (i != 18) {
                    i3 = (i == 20 || i == 30) ? R.layout.smartspace_card_doorbell : 0;
                }
            }
            i3 = R.layout.smartspace_card_generic_landscape_image;
        }
        if (i3 != 0) {
            bcSmartspaceCard.setSecondaryCard((BcSmartspaceCardSecondary) from.inflate(i3, (ViewGroup) bcSmartspaceCard, false));
        }
        return bcSmartspaceCard;
    }

    private void onBindViewHolder(ViewHolder viewHolder) {
        BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier;
        SmartspaceTarget smartspaceTarget = this.mSmartspaceTargets.get(viewHolder.position);
        BcSmartspaceCard bcSmartspaceCard = viewHolder.card;
        BcSmartspaceCardLoggingInfo build = new BcSmartspaceCardLoggingInfo.Builder().setInstanceId(InstanceId.create(smartspaceTarget)).setFeatureType(smartspaceTarget.getFeatureType()).setDisplaySurface(BcSmartSpaceUtil.getLoggingDisplaySurface(this.mRoot.getContext().getPackageName(), this.mDozeAmount)).setRank(viewHolder.position).setCardinality(this.mSmartspaceTargets.size()).setSubcardInfo(BcSmartspaceCardLoggerUtil.createSubcardLoggingInfo(smartspaceTarget)).build();
        final BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.mDataProvider;
        if (bcSmartspaceDataPlugin == null) {
            smartspaceEventNotifier = null;
        } else {
            Objects.requireNonNull(bcSmartspaceDataPlugin);
            smartspaceEventNotifier = new BcSmartspaceDataPlugin.SmartspaceEventNotifier() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda0
                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier
                public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
                    BcSmartspaceDataPlugin.this.notifySmartspaceEvent(smartspaceTargetEvent);
                }
            };
        }
        bcSmartspaceCard.setEventNotifier(smartspaceEventNotifier);
        BcSmartspaceCardLoggerUtil.forcePrimaryFeatureTypeAndInjectWeatherSubcard(build, smartspaceTarget, 39);
        boolean z = true;
        if (this.mSmartspaceTargets.size() <= 1) {
            z = false;
        }
        bcSmartspaceCard.setSmartspaceTarget(smartspaceTarget, build, z);
        bcSmartspaceCard.setPrimaryTextColor(this.mCurrentTextColor);
        bcSmartspaceCard.setDozeAmount(this.mDozeAmount);
        bcSmartspaceCard.setDnd(this.mDndImage, this.mDndDescription);
        bcSmartspaceCard.setNextAlarm(this.mNextAlarmImage, this.mNextAlarmDescription, this.mHolidayAlarmsTarget);
    }

    private boolean isHolidayAlarmsTarget(SmartspaceTarget smartspaceTarget) {
        return smartspaceTarget.getFeatureType() == 34;
    }

    public void setTargets(List<? extends Parcelable> list) {
        this.mTargetsExcludingMediaAndHolidayAlarms.clear();
        this.mHolidayAlarmsTarget = null;
        list.forEach(new Consumer() { // from class: com.google.android.systemui.smartspace.CardPagerAdapter$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CardPagerAdapter.m749$r8$lambda$SRMUIR888hx7OlxM4EEkQ_BFA(CardPagerAdapter.this, (Parcelable) obj);
            }
        });
        boolean z = true;
        if (this.mTargetsExcludingMediaAndHolidayAlarms.isEmpty()) {
            this.mTargetsExcludingMediaAndHolidayAlarms.add(new SmartspaceTarget.Builder("date_card_794317_92634", new ComponentName(this.mRoot.getContext(), CardPagerAdapter.class), this.mRoot.getContext().getUser()).setFeatureType(1).build());
        }
        if (this.mTargetsExcludingMediaAndHolidayAlarms.size() != 1 || this.mTargetsExcludingMediaAndHolidayAlarms.get(0).getFeatureType() != 1) {
            z = false;
        }
        this.mHasOnlyDefaultDateCard = z;
        updateTargetVisibility();
        notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$setTargets$0(Parcelable parcelable) {
        SmartspaceTarget smartspaceTarget = (SmartspaceTarget) parcelable;
        if (isHolidayAlarmsTarget(smartspaceTarget)) {
            this.mHolidayAlarmsTarget = smartspaceTarget;
        } else {
            this.mTargetsExcludingMediaAndHolidayAlarms.add(smartspaceTarget);
        }
    }

    public void setDataProvider(BcSmartspaceDataPlugin bcSmartspaceDataPlugin) {
        this.mDataProvider = bcSmartspaceDataPlugin;
    }

    public void setPrimaryTextColor(int i) {
        this.mPrimaryTextColor = i;
        setDozeAmount(this.mDozeAmount);
    }

    public void setDozeAmount(float f) {
        this.mCurrentTextColor = ColorUtils.blendARGB(this.mPrimaryTextColor, this.mDozeColor, f);
        this.mDozeAmount = f;
        updateTargetVisibility();
        refreshCardColors();
    }

    public float getDozeAmount() {
        return this.mDozeAmount;
    }

    public void setDnd(Drawable drawable, String str) {
        this.mDndImage = drawable;
        this.mDndDescription = str;
        refreshCards();
    }

    public void setNextAlarm(Drawable drawable, String str) {
        this.mNextAlarmImage = drawable;
        this.mNextAlarmDescription = str;
        refreshCards();
    }

    public void setMediaTarget(SmartspaceTarget smartspaceTarget) {
        this.mMediaTargets.clear();
        if (smartspaceTarget != null) {
            this.mMediaTargets.add(smartspaceTarget);
        }
        updateTargetVisibility();
    }

    public Drawable getNextAlarmImage() {
        return this.mNextAlarmImage;
    }

    public SmartspaceTarget getHolidayAlarmsTarget() {
        return this.mHolidayAlarmsTarget;
    }

    private void refreshCards() {
        for (int i = 0; i < this.mHolders.size(); i++) {
            onBindViewHolder(this.mHolders.get(i));
        }
    }

    private void refreshCardColors() {
        for (int i = 0; i < this.mHolders.size(); i++) {
            this.mHolders.get(i).card.setPrimaryTextColor(this.mCurrentTextColor);
            this.mHolders.get(i).card.setDozeAmount(this.mDozeAmount);
        }
    }

    private void updateTargetVisibility() {
        boolean z;
        if (this.mMediaTargets.isEmpty()) {
            this.mSmartspaceTargets = this.mTargetsExcludingMediaAndHolidayAlarms;
            notifyDataSetChanged();
            return;
        }
        float f = this.mDozeAmount;
        if (f == 0.0f || !(z = this.mHasOnlyDefaultDateCard)) {
            this.mSmartspaceTargets = this.mTargetsExcludingMediaAndHolidayAlarms;
            notifyDataSetChanged();
        } else if (f != 1.0f || !z) {
        } else {
            this.mSmartspaceTargets = this.mMediaTargets;
            notifyDataSetChanged();
        }
    }

    public static class ViewHolder {
        public final BcSmartspaceCard card;
        public final int position;
        public SmartspaceTarget target;

        ViewHolder(int i, BcSmartspaceCard bcSmartspaceCard, SmartspaceTarget smartspaceTarget) {
            this.position = i;
            this.card = bcSmartspaceCard;
            this.target = smartspaceTarget;
        }
    }
}
