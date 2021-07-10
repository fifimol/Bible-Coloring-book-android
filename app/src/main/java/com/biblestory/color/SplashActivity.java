package com.biblestory.color;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;


public class SplashActivity extends AppBaseActivity {

    class C04741 implements Runnable {
        C04741() {
        }

        public void run() {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, HomeActivity.class));
            SplashActivity.this.finish();
        }
    }

    public  View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context;

        LayoutInflater.from(this).inflate(R.layout.activity_splash, this.parent);
        //lama nya loading splahs
        new Handler().postDelayed(new C04741(), 5000);
    }
}
