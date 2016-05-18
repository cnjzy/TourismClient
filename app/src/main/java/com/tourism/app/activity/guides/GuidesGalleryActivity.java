package com.tourism.app.activity.guides;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesGalleryAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.image.ImageFolder;
import com.tourism.app.vo.GuidesVO;

public class GuidesGalleryActivity extends BaseActivity {

    private ListView listView;
    private GuidesGalleryAdapter adapter;

    private GuidesVO guidesVO;

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_guides_gallery);
    }

    @Override
    public void init() {
        guidesVO = (GuidesVO) getIntent().getExtras().getSerializable("vo");
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
                    data.putSerializable("guidesVO", guidesVO);
                    showActivityForResult(context, GuidesGalleryImageActivity.class, 99, data);
                }
            }
        });
    }

    @Override
    public void initValue() {
        adapter = new GuidesGalleryAdapter(context, listView);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 99){
            setResult(RESULT_OK);
            finish();
        }
    }
}
