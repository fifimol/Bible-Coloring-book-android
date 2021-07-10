package com.biblestory.color;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.ToggleButton;
import com.adapters.ColorAdapter;
import com.application.AdApplication;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import com.support.customviews.HorizontalListView;
import com.support.permission.PermissionUtils;
import com.support.permission.PermissionUtils.OnRequestedPermissionListener;
import com.utils.BrushView;
import com.utils.Constants;
import com.utils.DrawingPanel;
import com.utils.TextTrackStyle;
import com.utils.ZoomLayout;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class SketchActivity extends MusicActivity {

    //jangan ubah apapun di skrip ini
    BrushView brushView;
    private ColorAdapter colorAdapter;
    private SketchActivity context;
    float currentThickness = 8.0f;
    private String drawingName;
    ToggleButton eraser;
    HorizontalListView horizontalListView;
    private int lastSelectedColorIndex;
    private DrawingPanel mDrawingPanel;
    RelativeLayout mDrawingPanelLayout;
    Paint mPaint;
    private AdApplication myApp;
    ZoomLayout sketchViewContainer;
    Paint thicknessClear;
    SeekBar thicknessSlider;
    LinearLayout zoomChildLayout;
    ImageView zoomImage;
    LinearLayout zoomIntroLayout;
    boolean zoomIntroShown;
    ToggleButton zoomToggle;
    private AdView mAdView;
    ImageView img,img2,img3,img4,img5,img6,img7,img8,img9,img10,img11,img12,img13,img14,img15,img16,img17,img18,img19,img20,img21,img22,img23,img24,img25,img26;
    private InterstitialAd mInterstitial;
    class C04672 implements OnCheckedChangeListener {
        C04672() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                SketchActivity.this.zoomImage.setImageResource(R.drawable.zoom_out);
                SketchActivity.this.zoomIntroLayout.setVisibility(View.VISIBLE);
                SketchActivity.this.mDrawingPanel.setEnabled(false);
                if (SketchActivity.this.zoomIntroShown) {
                    SketchActivity.this.introOkClicked(null);
                    return;
                }
                SketchActivity.this.zoomIntroShown = true;
                SketchActivity.this.zoomIntroLayout.setEnabled(false);
                return;
            }
            SketchActivity.this.zoomImage.setImageResource(R.drawable.zoom_in);
            SketchActivity.this.zoomIntroLayout.setVisibility(View.GONE);
            SketchActivity.this.zoomIntroLayout.setEnabled(true);
            SketchActivity.this.zoomChildLayout.setVisibility(View.VISIBLE);
            SketchActivity.this.mDrawingPanel.setEnabled(true);
        }
    }

    class C04683 implements OnCheckedChangeListener {
        C04683() {
        }

        public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
            if (isChecked) {
                SketchActivity.this.colorAdapter.setSelected(0);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.mPaint.setColor(Constants.getColor(0));
                SketchActivity.this.brushView.setColor(Constants.getColor(0));
            } else {
                SketchActivity.this.colorAdapter.setSelected(SketchActivity.this.lastSelectedColorIndex);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.mPaint.setColor(Constants.getColor(SketchActivity.this.lastSelectedColorIndex));
                SketchActivity.this.brushView.setColor(Constants.getColor(SketchActivity.this.lastSelectedColorIndex));
            }
            SketchActivity.this.updateThickness();
        }
    }

    class C04694 implements OnSeekBarChangeListener {
        C04694() {
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            SketchActivity.this.setThickness(TextTrackStyle.DEFAULT_FONT_SCALE + (((float) progress) * 0.2f));
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
        }
    }

    class C04705 implements OnItemClickListener {
        C04705() {
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            SketchActivity.this.eraser.setChecked(false);
            SketchActivity.this.colorAdapter.setSelected(i);
            SketchActivity.this.colorAdapter.notifyDataSetChanged();
            SketchActivity.this.lastSelectedColorIndex = i;
            SketchActivity.this.mPaint.setColor(Constants.getColor(i));
            SketchActivity.this.brushView.setColor(Constants.getColor(i));
            SketchActivity.this.updateThickness();
        }
    }

    class C04728 implements OnClickListener {
        C04728() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
            SketchActivity.this.mDrawingPanel.reset();
        }
    }

    class C04739 implements OnClickListener {
        C04739() {
        }

        public void onClick(DialogInterface dialogInterface, int i) {
            dialogInterface.dismiss();
        }
    }

    class C07111 implements ImageLoadingListener {
        C07111() {
        }

        public void onLoadingStarted(String s, View view) {
        }

        public void onLoadingFailed(String s, View view, FailReason failReason) {
        }

        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
            SketchActivity.this.setLayoutParams(bitmap);
        }

        public void onLoadingCancelled(String s, View view) {
        }
    }

    class C07127 implements OnRequestedPermissionListener {
        C07127() {
        }

        public void onPermissionResult(int responsePermission, boolean isPermissionGranted) {
            if (isPermissionGranted) {
                SketchActivity.this.sketchViewContainer.resetScale();
                Bitmap bitmap = Constants.getBitmapFromView(SketchActivity.this.sketchViewContainer);
                Constants.saveBitmapToGallery(SketchActivity.this, bitmap);
                Constants.saveBitmapAtLocation(bitmap, CompressFormat.PNG, 100, new File(Constants.getMyArtworksDirectory(SketchActivity.this.context) + File.separator + Calendar.getInstance().getTimeInMillis() + ".png"));
                new Builder(SketchActivity.this).setMessage("Your drawing has been saved to the gallery.").setPositiveButton("Ok", null).show();
            }
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.sketch_phone2, this.parent);
        this.thicknessSlider = (SeekBar) findViewById(R.id.thicknessSlider);
        this.brushView = (BrushView) findViewById(R.id.brushView);
        this.sketchViewContainer = (ZoomLayout) findViewById(R.id.sketchContainer);
        this.horizontalListView = (HorizontalListView) findViewById(R.id.hl_color);
        this.mDrawingPanelLayout = (RelativeLayout) findViewById(R.id.drawingPanelLayout);
        this.eraser = (ToggleButton) findViewById(R.id.eraser);
        this.zoomToggle = (ToggleButton) findViewById(R.id.zoomToggle);
        this.zoomIntroLayout = (LinearLayout) findViewById(R.id.zoomIntroLayout);
        this.zoomChildLayout = (LinearLayout) findViewById(R.id.zoomChildLayout);
        this.zoomImage = (ImageView) findViewById(R.id.zoomImage);
        this.brushView.setSquareColor(0);
        RelativeLayout relativeLayout = this.mDrawingPanelLayout;
        View drawingPanel = new DrawingPanel(this);
        this.mDrawingPanel = (DrawingPanel) drawingPanel;
        relativeLayout.addView(drawingPanel);
        mAdView = (AdView) findViewById(R.id.adView);
        mAdView.loadAd(new AdRequest.Builder().build());

        img=findViewById(R.id.imageBack);
        img2=findViewById(R.id.imageBack2);
        img3=findViewById(R.id.imageBack3);
        img4=findViewById(R.id.imageBack4);
        img5=findViewById(R.id.imageBack5);
        img6=findViewById(R.id.imageBack6);
        img7=findViewById(R.id.imageBack7);
        img8=findViewById(R.id.imageBack8);
        img9=findViewById(R.id.imageBack9);
        img10=findViewById(R.id.imageBack10);
        img11=findViewById(R.id.imageBack11);
        img12=findViewById(R.id.imageBack12);
        img13=findViewById(R.id.imageBack13);
        img14=findViewById(R.id.imageBack14);
        img15=findViewById(R.id.imageBack15);
        img16=findViewById(R.id.imageBack16);
        img17=findViewById(R.id.imageBack17);
        img18=findViewById(R.id.imageBack18);
        img19=findViewById(R.id.imageBack19);
        img20=findViewById(R.id.imageBack20);
        img21=findViewById(R.id.imageBack21);
        img22=findViewById(R.id.imageBack22);
        img23=findViewById(R.id.imageBack23);
        img24=findViewById(R.id.imageBack24);
        img25=findViewById(R.id.imageBack25);
        img26=findViewById(R.id.imageBack26);

        img.setColorFilter(Constants.getColor(1));
        img2.setColorFilter(Constants.getColor(2));
        img3.setColorFilter(Constants.getColor(3));
        img4.setColorFilter(Constants.getColor(4));
        img5.setColorFilter(Constants.getColor(5));
        img6.setColorFilter(Constants.getColor(6));
        img7.setColorFilter(Constants.getColor(7));
        img8.setColorFilter(Constants.getColor(8));
        img9.setColorFilter(Constants.getColor(9));
        img10.setColorFilter(Constants.getColor(10));
        img11.setColorFilter(Constants.getColor(11));
        img12.setColorFilter(Constants.getColor(12));
        img13.setColorFilter(Constants.getColor(13));
        img14.setColorFilter(Constants.getColor(14));
        img15.setColorFilter(Constants.getColor(15));
        img16.setColorFilter(Constants.getColor(16));
        img17.setColorFilter(Constants.getColor(17));
        img18.setColorFilter(Constants.getColor(18));
        img19.setColorFilter(Constants.getColor(19));
        img20.setColorFilter(Constants.getColor(20));
        img21.setColorFilter(Constants.getColor(21));
        img22.setColorFilter(Constants.getColor(22));
        img23.setColorFilter(Constants.getColor(23));
        img24.setColorFilter(Constants.getColor(24));
        img25.setColorFilter(Constants.getColor(25));
        img26.setColorFilter(Constants.getColor(26));


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(1);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 1;
                SketchActivity.this.mPaint.setColor(Constants.getColor(1));
                SketchActivity.this.brushView.setColor(Constants.getColor(1));
                SketchActivity.this.updateThickness();
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(2);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 2;
                SketchActivity.this.mPaint.setColor(Constants.getColor(2));
                SketchActivity.this.brushView.setColor(Constants.getColor(2));
                SketchActivity.this.updateThickness();
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(3);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 3;
                SketchActivity.this.mPaint.setColor(Constants.getColor(3));
                SketchActivity.this.brushView.setColor(Constants.getColor(3));
                SketchActivity.this.updateThickness();
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(4);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 4;
                SketchActivity.this.mPaint.setColor(Constants.getColor(4));
                SketchActivity.this.brushView.setColor(Constants.getColor(4));
                SketchActivity.this.updateThickness();
            }
        });img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(5);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 5;
                SketchActivity.this.mPaint.setColor(Constants.getColor(5));
                SketchActivity.this.brushView.setColor(Constants.getColor(5));
                SketchActivity.this.updateThickness();
            }
        });img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(6);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 6;
                SketchActivity.this.mPaint.setColor(Constants.getColor(6));
                SketchActivity.this.brushView.setColor(Constants.getColor(6));
                SketchActivity.this.updateThickness();
            }
        });img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(7);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 7;
                SketchActivity.this.mPaint.setColor(Constants.getColor(7));
                SketchActivity.this.brushView.setColor(Constants.getColor(7));
                SketchActivity.this.updateThickness();
            }
        });img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(8);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 8;
                SketchActivity.this.mPaint.setColor(Constants.getColor(8));
                SketchActivity.this.brushView.setColor(Constants.getColor(8));
                SketchActivity.this.updateThickness();
            }
        });img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(9);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 9;
                SketchActivity.this.mPaint.setColor(Constants.getColor(9));
                SketchActivity.this.brushView.setColor(Constants.getColor(9));
                SketchActivity.this.updateThickness();
            }
        });img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(10);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 10;
                SketchActivity.this.mPaint.setColor(Constants.getColor(10));
                SketchActivity.this.brushView.setColor(Constants.getColor(10));
                SketchActivity.this.updateThickness();
            }
        });img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(11);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 11;
                SketchActivity.this.mPaint.setColor(Constants.getColor(11));
                SketchActivity.this.brushView.setColor(Constants.getColor(11));
                SketchActivity.this.updateThickness();
            }
        });img12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(12);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 12;
                SketchActivity.this.mPaint.setColor(Constants.getColor(12));
                SketchActivity.this.brushView.setColor(Constants.getColor(12));
                SketchActivity.this.updateThickness();
            }
        });img13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(13);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 13;
                SketchActivity.this.mPaint.setColor(Constants.getColor(13));
                SketchActivity.this.brushView.setColor(Constants.getColor(13));
                SketchActivity.this.updateThickness();
            }
        });img14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(14);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 14;
                SketchActivity.this.mPaint.setColor(Constants.getColor(14));
                SketchActivity.this.brushView.setColor(Constants.getColor(14));
                SketchActivity.this.updateThickness();
            }
        });img15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(15);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 15;
                SketchActivity.this.mPaint.setColor(Constants.getColor(15));
                SketchActivity.this.brushView.setColor(Constants.getColor(15));
                SketchActivity.this.updateThickness();
            }
        });img16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(16);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 16;
                SketchActivity.this.mPaint.setColor(Constants.getColor(16));
                SketchActivity.this.brushView.setColor(Constants.getColor(16));
                SketchActivity.this.updateThickness();
            }
        });img17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(17);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 17;
                SketchActivity.this.mPaint.setColor(Constants.getColor(17));
                SketchActivity.this.brushView.setColor(Constants.getColor(17));
                SketchActivity.this.updateThickness();
            }
        });img18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(18);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 18;
                SketchActivity.this.mPaint.setColor(Constants.getColor(18));
                SketchActivity.this.brushView.setColor(Constants.getColor(18));
                SketchActivity.this.updateThickness();
            }
        });img19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(19);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 19;
                SketchActivity.this.mPaint.setColor(Constants.getColor(19));
                SketchActivity.this.brushView.setColor(Constants.getColor(19));
                SketchActivity.this.updateThickness();
            }
        });img20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(20);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 20;
                SketchActivity.this.mPaint.setColor(Constants.getColor(20));
                SketchActivity.this.brushView.setColor(Constants.getColor(20));
                SketchActivity.this.updateThickness();
            }
        });img21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(21);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 21;
                SketchActivity.this.mPaint.setColor(Constants.getColor(21));
                SketchActivity.this.brushView.setColor(Constants.getColor(21));
                SketchActivity.this.updateThickness();
            }
        });img22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(22);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 22;
                SketchActivity.this.mPaint.setColor(Constants.getColor(22));
                SketchActivity.this.brushView.setColor(Constants.getColor(22));
                SketchActivity.this.updateThickness();
            }
        });img23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(23);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 23;
                SketchActivity.this.mPaint.setColor(Constants.getColor(23));
                SketchActivity.this.brushView.setColor(Constants.getColor(23));
                SketchActivity.this.updateThickness();
            }
        });img24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(24);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 24;
                SketchActivity.this.mPaint.setColor(Constants.getColor(24));
                SketchActivity.this.brushView.setColor(Constants.getColor(24));
                SketchActivity.this.updateThickness();
            }
        });img25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(25);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 25;
                SketchActivity.this.mPaint.setColor(Constants.getColor(25));
                SketchActivity.this.brushView.setColor(Constants.getColor(25));
                SketchActivity.this.updateThickness();
            }
        });img26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SketchActivity.this.eraser.setChecked(false);
                SketchActivity.this.colorAdapter.setSelected(26);
                SketchActivity.this.colorAdapter.notifyDataSetChanged();
                SketchActivity.this.lastSelectedColorIndex = 26;
                SketchActivity.this.mPaint.setColor(Constants.getColor(26));
                SketchActivity.this.brushView.setColor(Constants.getColor(26));
                SketchActivity.this.updateThickness();
            }
        });



        this.context = this;
        this.drawingName = getIntent().getStringExtra(Constants.KEY_DRAWING_NAME);
        if (this.drawingName == null) {
            try {
                this.drawingName = getAssets().list(Constants.ASSETS_DRAWING_DIRECTORY)[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        initializeDrawingView();
        setColorListView();
        ImageLoader.getInstance().loadImage("assets://drawings/" + this.drawingName, new C07111());
        this.zoomToggle.setOnCheckedChangeListener(new C04672());
    }

    public void introOkClicked(View view) {
        this.zoomIntroLayout.setEnabled(true);
        this.zoomChildLayout.setVisibility(View.GONE);
    }

    @SuppressLint("ResourceType")
    private void initializeDrawingView() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Constants.getColor(1));
        this.brushView.setColor(Constants.getColor(1));
        this.mPaint.setStyle(Style.STROKE);
        this.mPaint.setAntiAlias(true);
        this.thicknessClear = new Paint();
        this.thicknessClear.setColor(ContextCompat.getColor(this, 17170443));
        this.thicknessClear.setAntiAlias(true);
        this.thicknessClear.setStyle(Style.FILL);
        setThickness(20.0f);
        this.eraser.setOnCheckedChangeListener(new C04683());
        this.thicknessSlider.setOnSeekBarChangeListener(new C04694());
    }

    private void setColorListView() {
        this.colorAdapter = new ColorAdapter(this);
        this.horizontalListView.setAdapter(this.colorAdapter);
        this.colorAdapter.setSelected(1);
        this.lastSelectedColorIndex = 1;
        this.colorAdapter.notifyDataSetChanged();
        this.horizontalListView.setOnItemClickListener(new C04705());
    }

    private void setLayoutParams(final Bitmap bitmap) {
        this.sketchViewContainer.post(new Runnable() {
            public void run() {
                ((ImageView) SketchActivity.this.findViewById(R.id.drawingImageView)).setImageBitmap(bitmap);
            }
        });
    }

    int dpToPxInt(int dp) {
        return (int) TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics());
    }

    void setThickness(float thickness) {
        this.currentThickness = TypedValue.applyDimension(1, thickness, getResources().getDisplayMetrics());
        updateThickness();
    }

    void updateThickness() {
        this.brushView.setRadius(((int) this.currentThickness) / 2);
        this.mPaint.setStrokeWidth(0.0f);
        this.mPaint.setStrokeWidth(this.currentThickness);
        this.mDrawingPanel.changePaint(this.mPaint);
    }

    public void onClick(View view) {
        Constants.clickAnimation(view);
        if (view.getId() == R.id.aSketch_btnSave) {
            PermissionUtils.requestPermission((Activity) this, 9, new C07127());
        } else if (view.getId() == R.id.aSketch_btnShare) {
            this.sketchViewContainer.resetScale();
            Bitmap bitmap = Constants.getBitmapFromView(this.sketchViewContainer);
            Constants.shareBitmap(this, bitmap, getString(R.string.message_to_share), getString(R.string.intent_message));
            Constants.saveBitmapAtLocation(bitmap, CompressFormat.PNG, 100, new File(Constants.getMyArtworksDirectory(this.context) + File.separator + Calendar.getInstance().getTimeInMillis() + ".png"));
        } else if (view.getId() == R.id.aSketch_btnBack) {
            onBackPressed();
        } else if (view.getId() == R.id.aSketch_btnReset) {
            this.sketchViewContainer.resetScale();
            new Builder(this).setTitle("Reset!!!").setMessage("Sure to reset drawing?").setPositiveButton("Reset", new C04728()).setNegativeButton("No", new C04739()).create().show();
        } else if (view.getId() == R.id.aSketch_btnUndo) {
            this.mDrawingPanel.undo();
        } else if (view.getId() == R.id.aSketch_btnRedo) {
            this.mDrawingPanel.redo();
        }
    }

    protected void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
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

    @Override
    public void onBackPressed() {
        this.mInterstitial = new InterstitialAd(context);
        this.mInterstitial.setAdUnitId(getString(R.string.INTERSTITIAL_UNIT_ID));
        this.mInterstitial.loadAd(new AdRequest.Builder().build());
        this.mInterstitial.setAdListener(new AdListener() {

            @Override
            public void onAdFailedToLoad(int i) {
              finish();
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLoaded() {


                displayInterstitial();

                super.onAdLoaded();
            }

            @Override
            public void onAdClosed() {
             finish();
                super.onAdClosed();
            }


        });
        super.onBackPressed();
    }
    public void displayInterstitial() {

        if (mInterstitial.isLoaded()) {
            mInterstitial.show();
        }
    }

}
