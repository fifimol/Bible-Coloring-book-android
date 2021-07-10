package com.biblestory.color;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.adapters.DrawingAdapter;
import com.adapters.DrawingAdapter.OnItemClickListener;
import com.application.AdApplication;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.utils.Constants;
import java.io.IOException;

public class SelectDrawingActivity extends AppBaseActivity {
    //jangan ubah script
    private String[] artworks;
    private SelectDrawingActivity context;
    private DrawingAdapter drawingAdapter;
    RecyclerView mRecyclerView;
    private AdApplication myApp;
    private AdView mAdView;
    private InterstitialAd interstitial;
    class C07101 implements OnItemClickListener {
        C07101() {
        }

        public void onItemClick(int position) {
            if (interstitial != null && interstitial.isLoaded()) {
                interstitial.show();

            }

            SelectDrawingActivity.this.startActivity(new Intent(SelectDrawingActivity.this, SketchActivity.class).putExtra(Constants.KEY_DRAWING_NAME, SelectDrawingActivity.this.artworks[position]));

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
        LayoutInflater.from(this).inflate(R.layout.activity_select_new_drawing, this.parent);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());
         this.mRecyclerView = (RecyclerView) findViewById(R.id.rv_drawing);

        this.context = this;
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.INTERSTITIAL_UNIT_ID));
        interstitial.loadAd(new AdRequest.Builder().build());
       // Intent intent=getIntent();
     //   String category=intent.getStringExtra("constans");
        setupDrawings("drawings");
     //   Toast.makeText(SelectDrawingActivity.this,category,Toast.LENGTH_LONG).show();
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        this.drawingAdapter = new DrawingAdapter(this.artworks);
        this.mRecyclerView.setAdapter(this.drawingAdapter);
        this.drawingAdapter.setOnItemClickListener(new C07101());
        try {
            ((TextView) findViewById(R.id.tvTitle)).setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onBackClicked(View view) {
        finish();
    }

    private boolean setupDrawings(String constans) {
        try {
            this.artworks = getAssets().list(constans);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }


  }
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }



    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }
    public void displayInterstitial() {
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();

        }
    }
    public void myads(){
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(getResources().getString(R.string.INTERSTITIAL_UNIT_ID));
        interstitial.loadAd(new AdRequest.Builder().build());

    }
}
