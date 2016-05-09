package com.tourism.app.activity.guides;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesGalleryAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.image.ImageFolder;

public class GuidesGalleryActivity extends BaseActivity {

    private ListView listView;
    private GuidesGalleryAdapter adapter;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_gallery);
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub

    }

    @Override
    public void initView() {
        listView = (ListView) findViewById(R.id.listView);
    }

    @Override
    public void initListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageFolder vo = (ImageFolder) listView.getItemAtPosition(position);
                if(vo != null) {
                    vo.recyleBitmap();
                    Bundle data = new Bundle();
                    data.putSerializable("vo", vo);
                    showActivity(context, GuidesGalleryImageActivity.class, data);
                }
            }
        });
    }

    @Override
    public void initValue() {
        adapter = new GuidesGalleryAdapter(context, listView);
        listView.setAdapter(adapter);
    }

}
