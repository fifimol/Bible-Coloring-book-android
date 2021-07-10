package com.biblestory.color;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.biblestory.color.category.CustomAdapter;
import com.biblestory.color.category.DataModel;
import com.biblestory.color.category.MyData;
import com.utils.Constants;

import java.util.ArrayList;

/**
 * Created by anonymous on 03/05/19.
 */

public class CategoriAsset extends AppBaseActivity {


    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<DataModel> data;
    public static View.OnClickListener myOnClickListener;
    private static ArrayList<Integer> removedItems;
    private ImageView btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater.from(this).inflate(R.layout.category_activity, this.parent);
        btn_back=(ImageView) findViewById(R.id.aSelect_btnBack);
btn_back.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        finish();
    }
});
        myOnClickListener = new MyOnClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(new GridLayoutManager(CategoriAsset.this,2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<DataModel>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            data.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.catFolder[i],
                    MyData.id_[i],
                    MyData.drawableArray[i]
            ));
        }

        removedItems = new ArrayList<Integer>();

        adapter = new CustomAdapter(data);
        recyclerView.setAdapter(adapter);
        try {
            ((TextView) findViewById(R.id.tvTitle)).setTypeface(Typeface.createFromAsset(getAssets(), Constants.FONT_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public class MyOnClickListener implements View.OnClickListener {

        private final Context context;

        private MyOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            removeItem(v);
        }

        private void removeItem(View v) {
            int selectedItemPosition = recyclerView.getChildPosition(v);
            RecyclerView.ViewHolder viewHolder
                    = recyclerView.findViewHolderForPosition(selectedItemPosition);
            TextView textViewName
                    = (TextView) viewHolder.itemView.findViewById(R.id.textViewVersion);
            String selectedName = (String) textViewName.getText();
            int selectedItemId = -1;
            for (int i = 0; i < MyData.catFolder.length; i++) {
                if (selectedName.equals(MyData.catFolder[i])) {
                    selectedItemId = MyData.id_[i];
                }
            }
        Intent intent=new Intent(CategoriAsset.this,SelectDrawingActivity.class);
           intent.putExtra("constans",selectedName);
            startActivity(intent);
        }
    }





}
