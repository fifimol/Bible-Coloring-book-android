package com.biblestory.color;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.adapters.MyArtworkAdapter;
import com.adapters.MyArtworkAdapter.OnDeleteClickListener;
import com.application.AdApplication;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.utils.Constants;
import com.utils.GridSpacingItemDecoration;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MyArtworksActivity extends AppBaseActivity {

    //jangan ubah script
    private MyArtworksActivity context;
    RecyclerView mRecyclerView;
    private AdApplication myApp;
    private ArrayList<String> myArtWorks;
    private InterstitialAd interstitial;
    private AdView mAdView;

    public View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.activity_my_artworks, this.parent);
        this.mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.myApp = (AdApplication) getApplicationContext();
        this.context = this;
        this.myArtWorks = new ArrayList(Arrays.asList(Constants.getMyArtworksDirectory(this).list()));
        this.mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        this.mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, Constants.dpToPx(this, 10), true));
        this.mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        final MyArtworkAdapter myArtworkAdapter = new MyArtworkAdapter(this.myArtWorks);
        this.mRecyclerView.setAdapter(myArtworkAdapter);
        myArtworkAdapter.setOnDeleteListener(new OnDeleteClickListener() {
            public void onDeleteClicked(int position) {
                File fileToDelete = new File(Constants.getMyArtworksDirectory(MyArtworksActivity.this.context) + File.separator + ((String) MyArtworksActivity.this.myArtWorks.get(position)));
                Log.d(getClass().getSimpleName(), fileToDelete.getAbsolutePath());
                if (fileToDelete.exists()) {
                    fileToDelete.delete();
                }
                MyArtworksActivity.this.myArtWorks.remove(position);
                myArtworkAdapter.notifyItemRemoved(position);
            }
        });
        try {
            ((TextView) findViewById(R.id.aMyDrawing_tvTitle)).setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAdView = (AdView) findViewById(R.id.adView);
        interstitial = new InterstitialAd(MyArtworksActivity.this);
        mAdView.loadAd(new AdRequest.Builder().build());


        interstitial.setAdUnitId(getString(R.string.INTERSTITIAL_UNIT_ID));
        AdRequest adRequest = new AdRequest.Builder().build();

        interstitial.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            public void onAdLoaded() {

                displayInterstitial();
            }
        });
    }

    public void onBackClicked(View view) {
        finish();
    }

    protected void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
        loadAd();
    }

    void loadAd() {

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

        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
