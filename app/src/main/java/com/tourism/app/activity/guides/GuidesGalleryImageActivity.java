package com.tourism.app.activity.guides;

import android.view.View;
import android.widget.GridView;

import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesGalleryImageAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.util.image.ImageFolder;
import com.tourism.app.util.image.ImageUtils;

import java.util.List;

public class GuidesGalleryImageActivity extends BaseActivity{

	private GridView gridView;
	private GuidesGalleryImageAdapter adapter;

	private ImageFolder folderVO;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_guides_gallery_image);
	}

	@Override
	public void init() {
		folderVO = (ImageFolder) getIntent().getExtras().getSerializable("vo");
	}

	@Override
	public void initView() {
		gridView = (GridView) findViewById(R.id.gridView);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initValue() {
		setNavigationRightButton(View.VISIBLE, 0, R.drawable.ico_ok);

		List<ImageFolder> list = ImageUtils.getAllImageByFolder(context, folderVO);
		adapter = new GuidesGalleryImageAdapter(context, gridView);
		gridView.setAdapter(adapter);
		adapter.addLast(list);
	}

}
