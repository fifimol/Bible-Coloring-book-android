package com.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.biblestory.color.R;
import com.nostra13.universalimageloader.BuildConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;


public class AdApplication extends Application {
    private static AdView admobBannerAdView;
    private static String deviceId = BuildConfig.FLAVOR;
    private static InterstitialAd interstitial;
    LinearLayout adLayout;
    AdLoadedListener adLoadListener;
    View bannerAdView;
    int bannerHeight;
    int bannerWidth;
    Context context;
    boolean isFromMoreButton = false;
    boolean isMainAdShown = false;
    public boolean shouldShowAlert = false;

    public interface AdLoadedListener {
        void onAdLoaded(boolean z);
    }

    class C05331 extends AdListener {
        C05331() {
        }

        public void onAdLoaded() {
            super.onAdLoaded();
            if (AdApplication.this.adLoadListener != null) {
                AdApplication.this.adLoadListener.onAdLoaded(true);
            }
            AdApplication.this.bannerAdView = AdApplication.admobBannerAdView;
            AdApplication.this.bannerWidth = -1;
            AdApplication.this.bannerHeight = AdSize.SMART_BANNER.getHeightInPixels(AdApplication.this.context);
            if (AdApplication.this.adLayout != null) {
                AdApplication.this.setAdToLayout(AdApplication.this.adLayout, AdApplication.this.context);
            }
        }

        public void onAdFailedToLoad(int errorCode) {
            super.onAdFailedToLoad(errorCode);
        }
    }

    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    public void loadPromotion(Context con) {
        this.context = con;
        requestForAdmobBanner(getString(R.string.AD_UNIT_ID));
        requestForAdMobInterstitial(getString(R.string.INTERSTITIAL_UNIT_ID));
    }

    private void requestForAdmobBanner(String adMobAdUnitId) {
        try {
            admobBannerAdView = new AdView(this.context);
            admobBannerAdView.setAdSize(AdSize.SMART_BANNER);
            admobBannerAdView.setAdUnitId(adMobAdUnitId);
            admobBannerAdView.loadAd(new Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).addTestDevice(deviceId).build());
            admobBannerAdView.setAdListener(new C05331());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void requestForAdMobInterstitial(final String interstitialId) {
        try {
            interstitial = new InterstitialAd(this.context);
            interstitial.setAdUnitId(interstitialId);
            interstitial.loadAd(new Builder().addTestDevice(deviceId).build());
            interstitial.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    super.onAdLoaded();
                }

                public void onAdFailedToLoad(int errorCode) {
                    super.onAdFailedToLoad(errorCode);
                    AdApplication.interstitial = null;
                }

                public void onAdClosed() {
                    super.onAdClosed();
                    AdApplication.interstitial = null;
                    AdApplication.this.requestForAdMobInterstitial(interstitialId);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showMoreApps(Context context) {
        this.context = context;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://play.google.com/store/apps/developer?id=ebenezer+softwares"));
        this.context.startActivity(intent);
    }

    public void rateApp(Context context) {
        this.context = context;
        String linkToOpen = context.getString(R.string.rate_app_play) + context.getPackageName();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(linkToOpen));
        context.startActivity(intent);
    }

    public static void initImageLoader(Context context) {
        ImageLoader.getInstance().init(new ImageLoaderConfiguration.Builder(context).threadPriority(3).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).diskCacheSize(52428800).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build());
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
    }

    public void setAdToLayout(ViewGroup adviewLayout, Context activityContext) {
        this.context = activityContext;
        this.adLayout = (LinearLayout) adviewLayout;
        if (this.bannerAdView == null) {
            Log.e("setAdToLayout ", "bannerAdView null");
            return;
        }
        Log.e("setAdToLayout ", "bannerAdView not null");
        if (this.bannerAdView.getParent() != null) {
            ((ViewGroup) this.bannerAdView.getParent()).removeAllViews();
        }
        this.adLayout.removeAllViews();
        this.bannerAdView.setBackgroundResource(R.drawable.ad_bg);
        this.adLayout.addView(this.bannerAdView, new LayoutParams(this.bannerWidth, this.bannerHeight));
        this.adLayout.invalidate();
    }

    public void loadInterstitial() {
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    public void destroy() {
        this.bannerAdView = null;
        this.shouldShowAlert = false;
        this.isFromMoreButton = false;
        this.isMainAdShown = false;
        if (admobBannerAdView != null) {
            admobBannerAdView.destroy();
        }
    }

    public void setOnAdLoadedListener(AdLoadedListener adLoadedListener) {
        this.adLoadListener = adLoadedListener;
    }
}
