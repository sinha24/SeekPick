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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.solipsism.seekpick.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

import static com.solipsism.seekpick.Search.MapsActivity.resultItem;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {

    TextView bsName, bsPrice, bsShopName, bsAddress, bsNumber;
    Button bsCall, bsPing;
    String shoid,name;

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

        shoid=resultItem.getShopid();
        name=resultItem.getName();

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
                doPing("https://seekpick.herokuapp.com/ping");
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

    public void doPing(String Uri){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String success = "";
                        String message = "";
                        try {
                            JSONObject obje = new JSONObject(response);
                            success = obje.getString("success");
                            message = obje.getString("message");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (success.equals("true")) {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Unable to ping right now, try later.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new Hashtable<>();
                params.put("shopId", shoid);
                params.put("name", name);

                //returning parameters
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
}