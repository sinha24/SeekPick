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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.solipsism.seekpick.R;

import static com.solipsism.seekpick.Search.NearMapsActivity.resultShop;

public class NearMeBottomSheet extends BottomSheetDialogFragment {

    TextView bsName, bsPrice, bsShopName, bsAddress, bsNumber,bsStatus;
    Button bsCall, bsAllProducts;
    String shopId,name;

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
        bsName.setVisibility(View.GONE);
        bsPrice = (TextView) contentView.findViewById(R.id.bsPrice);
        bsPrice.setVisibility(View.GONE);
        bsShopName = (TextView) contentView.findViewById(R.id.bsShopName);
        bsAddress = (TextView) contentView.findViewById(R.id.bsAddress);
        bsNumber = (TextView) contentView.findViewById(R.id.bsNumber);
        bsStatus = (TextView) contentView.findViewById(R.id.bsStatus);
        bsStatus.setVisibility(View.GONE);
        bsCall = (Button) contentView.findViewById(R.id.bsCall);
        bsAllProducts = (Button) contentView.findViewById(R.id.bsPing);
        bsAllProducts.setText("All Products");

        shopId = resultShop.getShopid();

        bsShopName.setText(resultShop.getShopname());
        bsNumber.setText(resultShop.getPhone());
        bsAddress.setText(resultShop.getAddress() + " - " + resultShop.getPincode());
        bsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + resultShop.getPhone()));
                try {
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        bsAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),AllProductActivity.class);
                i.putExtra("shopId",shopId);
                startActivity(i);
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