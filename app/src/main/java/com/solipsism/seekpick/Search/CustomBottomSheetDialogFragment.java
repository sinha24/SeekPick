package com.solipsism.seekpick.Search;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solipsism.seekpick.R;

import static com.solipsism.seekpick.Search.MapsActivity.resultItem;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    TextView bsName, bsPrice, bsShopName, bsAddress, bsNumber;
    Button bsCall, bsPing;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.bottom_sheet_result, null);
        bsName = (TextView) contentView.findViewById(R.id.bsName);
        bsPrice = (TextView) contentView.findViewById(R.id.bsPrice);
        bsShopName = (TextView) contentView.findViewById(R.id.bsShopName);
        bsAddress = (TextView) contentView.findViewById(R.id.bsAddress);
        bsNumber = (TextView) contentView.findViewById(R.id.bsNumber);
        bsCall = (Button) contentView.findViewById(R.id.bsCall);
        bsPing = (Button) contentView.findViewById(R.id.bsPing);
        bsName.setText(resultItem.getName());
        bsPrice.setText(resultItem.getPrice());
        bsShopName.setText(resultItem.getShopname());
        bsNumber.setText(resultItem.getPhone());
        bsAddress.setText(resultItem.getAddress() + " - " + resultItem.getPincode());
        bsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + resultItem.getPhone()));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bsPing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }
}