package com.app.mobiledev.apphris.test;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.TextView;


import com.app.mobiledev.apphris.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class TestFCM extends AppCompatActivity {

    TextView tvToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fcm);

        //fm.subscribeToTopic("memo");
        //String fmToken = fm.getToken().toString();

        //Log.d("TAG_TOKEN_FBM", "onCreate: "+fmToken);

        tvToken = findViewById(R.id.tvToken);

        //FirebaseMessaging.getInstance().subscribeToTopic("memo");


/*
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        tvToken.setText(token);

                        // Log and toast
                        String msg = token;
                        Log.d("TAG_TOKEN0", msg);
                        Toast.makeText(TestFCM.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });

 */

    }

}

