package com.biblestory.color;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog.Builder;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.application.AdApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;


import com.utils.Constants;

public class HomeActivity extends AppBaseActivity {

    //ubah bagian rate app dan app more
    private HomeActivity context;
    ImageView ivPlayBg;
    private AdApplication myApp;
    private ObjectAnimator rotation;
    private AdView mAdView;

    private static InterstitialAd mInterstitial;
    private InterstitialAd mInterstitial2;
    class C04741 implements Runnable {
        C04741() {
        }

        public void run() {
            HomeActivity.this.startActivity(new Intent(HomeActivity.this, SelectDrawingActivity.class));
            HomeActivity.this.finish();
        }
    }
    class C04611 implements OnClickListener {
        C04611() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();

            HomeActivity.this.finish();

        }
    }

    class C04622 implements OnClickListener {
        C04622() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C04633 implements OnClickListener {
        C04633() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {

        }
    }

    class C04644 implements OnClickListener {
        C04644() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_new_home, this.parent);
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());


        mInterstitial = new InterstitialAd(this);
        mInterstitial.setAdUnitId(getResources().getString(R.string.INTERSTITIAL_UNIT_ID));
        mInterstitial.loadAd(new AdRequest.Builder().build());

        this.ivPlayBg = (ImageView) findViewById(R.id.home_ivPlayBg);
    this.myApp = (AdApplication) HomeActivity.this.getApplicationContext();
        this.context = this;
        setFonts();
    }

    private void rotate(boolean isRotate) {
        if (isRotate) {
            if (this.rotation == null) {
                this.rotation = ObjectAnimator.ofFloat(this.ivPlayBg, "rotation", new float[]{360.0f}).setDuration(2000);
                this.rotation.setRepeatMode(ValueAnimator.RESTART);
                this.rotation.setRepeatCount(-1);
                this.rotation.setInterpolator(new LinearInterpolator());
            }
            this.rotation.start();
        } else if (this.rotation != null) {
            this.rotation.end();
            this.rotation.cancel();
        }
    }

    private void setFonts() {
        try {
            Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.FONT_NAME);
            ((TextView) findViewById(R.id.tvGallery)).setTypeface(typeface);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onStartClicked(View view) {


        if (mInterstitial != null && mInterstitial.isLoaded()) {
            mInterstitial.show();

        }

        Intent intent =new Intent(HomeActivity.this,SelectDrawingActivity.class);
        startActivity(intent);

    }
//wajib ubah
    public void onMoreClicked(View view) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Ebenezer+Softwares"));
        startActivity(intent);
    }

    public void myArtworksClicked(View view) {

        startActivity(new Intent(this, MyArtworksActivity.class));
    }
//wajib ubah
    public void rateAppClicked(View view) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+HomeActivity.this.getPackageName().toString()));
        this.context.startActivity(intent);
    }

    protected void onResume() {
        super.onResume();


        if (mAdView != null) {
            mAdView.resume();
        }
        loadAd();


        rotate(true);
    }

    protected void onPause() {
        super.onPause();
        if (mAdView != null) {
            mAdView.pause();
        }
        rotate(false);
    }

    void loadAd() {
       this.myApp.setAdToLayout((LinearLayout) findViewById(R.id.adLayout), this.context);
    }

    private void showSimpleExitDialog() {
        new Builder(this).setTitle((CharSequence) "Exit!").setMessage((CharSequence) "Would you like to exit?").setPositiveButton((CharSequence) "Yes", new C04611()).setNegativeButton((CharSequence) "No", new C04622()).create().show();
    }

    private void showSimplestartDialog() {
        new Builder(this).setTitle(getString(R.string.app_name)).setMessage((CharSequence) "Would you like to try a great new app?").setPositiveButton((CharSequence) "Yes", new C04633()).setNegativeButton((CharSequence) "No", new C04644()).create().show();
    }

    public void onBackPressed() {
        showSimpleExitDialog();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


}
