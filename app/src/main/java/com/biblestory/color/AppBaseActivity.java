package com.biblestory.color;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.RelativeLayout;

import com.support.permission.PermissionUtils;

public class AppBaseActivity extends AppCompatActivity {
    public RelativeLayout parent;

    public  View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public  View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        hideNavigation();
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_app_base);
        getWindow().addFlags(128);
        this.parent = (RelativeLayout) findViewById(R.id.baseView);
    }

    private void hideNavigation() {
        if (VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(5894);
            final View decorView = getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & 4) == 0) {
                        decorView.setSystemUiVisibility(5894);
                    }
                }
            });
        }
    }

    protected void onResume() {
        super.onResume();
        Log.e(getClass().getSimpleName(), "onResume()");
        PermissionUtils.verifyPermission(this);
        hideNavigation();
    }
}
