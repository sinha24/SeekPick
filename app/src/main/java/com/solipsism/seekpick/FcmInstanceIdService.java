package com.solipsism.seekpick;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.solipsism.seekpick.utils.PrefsHelper;

public class FcmInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String recentToken = FirebaseInstanceId.getInstance().getToken();
        Log.e("Token gen:-- ",recentToken);
        PrefsHelper.getPrefsHelper(this).savePref(PrefsHelper.FCM_TOKEN,recentToken);
    }
}