package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.bcsmartspace.R.id;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.smartspace.logging.BcSmartspaceCardLoggingInfo;

public class BcSmartspaceCardLoyalty extends BcSmartspaceCardGenericImage {
    private TextView mCardPromptView;
    private ImageView mLoyaltyProgramLogoView;
    private TextView mLoyaltyProgramNameView;

    public BcSmartspaceCardLoyalty(Context context) {
        super(context);
    }

    public BcSmartspaceCardLoyalty(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        super.setSmartspaceActions(smartspaceTarget, smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        Bundle extras = baseAction == null ? null : baseAction.getExtras();
        mImageView.setVisibility(8);
        mLoyaltyProgramLogoView.setVisibility(8);
        mLoyaltyProgramNameView.setVisibility(8);
        mCardPromptView.setVisibility(8);
        if (extras != null) {
            boolean containsKey = extras.containsKey("imageBitmap");
            if (extras.containsKey("cardPrompt")) {
                setCardPrompt(extras.getString("cardPrompt"));
                mCardPromptView.setVisibility(0);
                if (containsKey) {
                    mImageView.setVisibility(0);
                }
                return true;
            } else if (!extras.containsKey("loyaltyProgramName")) {
                if (containsKey) {
                    mLoyaltyProgramLogoView.setVisibility(0);
                }
                return containsKey;
            } else {
                setLoyaltyProgramName(extras.getString("loyaltyProgramName"));
                mLoyaltyProgramNameView.setVisibility(0);
                if (containsKey) {
                    mLoyaltyProgramLogoView.setVisibility(0);
                }
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        mLoyaltyProgramLogoView = (ImageView) findViewById(R.id.loyalty_program_logo);
        mLoyaltyProgramNameView = (TextView) findViewById(R.id.loyalty_program_name);
        mCardPromptView = (TextView) findViewById(R.id.card_prompt);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardGenericImage
    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
        mLoyaltyProgramLogoView.setImageBitmap(bitmap);
    }

    void setCardPrompt(String str) {
        TextView textView = mCardPromptView;
        if (textView == null) {
            Log.w("BcSmartspaceCardLoyalty", "No card prompt view to update");
        } else {
            textView.setText(str);
        }
    }

    void setLoyaltyProgramName(String str) {
        TextView textView = mLoyaltyProgramNameView;
        if (textView == null) {
            Log.w("BcSmartspaceCardLoyalty", "No loyalty program name view to update");
        } else {
            textView.setText(str);
        }
    }
}
