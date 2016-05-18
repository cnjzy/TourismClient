package com.tourism.app.activity.guides;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.GridView;

import com.tourism.app.R;
import com.tourism.app.activity.guides.adapter.GuidesGalleryImageAdapter;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DateUtils;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.PhotoManager;
import com.tourism.app.util.image.ImageFolder;
import com.tourism.app.util.image.ImageUtils;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.widget.view.CustomRoundProgress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GuidesGalleryImageActivity extends BaseActivity{

	private GridView gridView;
	private GuidesGalleryImageAdapter adapter;
	private List<ImageFolder> list;
	private List<ImageFolder> selectedList = new ArrayList<ImageFolder>();

	private ImageFolder folderVO;
	private GuidesVO guidesVO;

	private GuidesDao guidesDao;
	private GuidesNotedDao guidesNotedDao;

	private View progress_ll;
	private CustomRoundProgress sync_progress_rg;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_guides_gallery_image);
	}

	@Override
	public void init() {
		guidesDao = new GuidesDao(context);
		guidesNotedDao = new GuidesNotedDao(context);
		folderVO = (ImageFolder) getIntent().getExtras().getSerializable("vo");
		guidesVO = (GuidesVO) getIntent().getExtras().getSerializable("guidesVO");
	}

	@Override
	public void initView() {
		gridView = (GridView) findViewById(R.id.gridView);
		progress_ll = findViewById(R.id.progress_ll);
		sync_progress_rg = (CustomRoundProgress) findViewById(R.id.sync_progress_rg);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void initValue() {
		setNavigationRightButton(View.VISIBLE, 0, R.drawable.ico_ok);

		list = ImageUtils.getAllImageByFolder(context, folderVO);
		adapter = new GuidesGalleryImageAdapter(context, gridView);
		gridView.setAdapter(adapter);
		adapter.addLast(list, false);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()){
			case R.id.navigation_right_btn:
				progress_ll.setVisibility(View.VISIBLE);
				sync_progress_rg.setMaxProgress(list.size());
				new ProgressImageThread().start();
				break;
		}
	}

	/**
	 * 压缩图片
	 * @param imageFolder
	 */
	private void qualityImage(ImageFolder imageFolder){
		// 压缩图片
		Bitmap b = PhotoManager.extractThumbNail(imageFolder.getPath(), 600, 600, false);
		Bitmap bm = null;
		if (b != null) {
			b = PhotoManager.rotateBitmap(b, imageFolder.getPath(), -1);
			bm = b.copy(Bitmap.Config.ARGB_8888, true);
			// 创建新文件
			try {
				File tempFile = new File(imageFolder.getPath());
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tempFile));
				bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
				bos.flush();
				bos.close();
				bm.recycle();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isQuality(File f){//取得文件大小
		try {
			if (f.exists()) {
				FileInputStream fis = null;
				fis = new FileInputStream(f);
				long s = fis.available();
				if (s < 1024 * 1024 * 1) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		}catch (Exception e){
			e.printStackTrace();
		}
		return false;
	}

	class ProgressImageThread extends Thread{
		@Override
		public void run() {
			super.run();

			int count = 0;
			long startTime = DateUtils.getLongByString(guidesVO.getStart_date(), DateUtils.parsePatterns[1]);
			long endTime = DateUtils.getLongByString(guidesVO.getEnd_date(), DateUtils.parsePatterns[1]);;

			for (int i = 0; i < list.size(); i++){
				if (list.get(i).isSelected()){

					ImageFolder imageFolder = list.get(i);
					File file = new File(imageFolder.getPath());

					// 如果文件不存在或者文件大小为0，那么返回
					if (!file.exists() || file.length() == 0){
						LogUtil.i("Add Image", "the image is empty for path " + file.getAbsolutePath());
						continue;
					}

					// 选中图片数量加1
					count ++;

					// 判断图片是否需要压缩
					if (isQuality(file)){
						qualityImage(imageFolder);
					}

					// 设置开始时间
					if (startTime == 0 || file.lastModified() < startTime){
						startTime = file.lastModified();
					}

					// 设置结束时间
					if (endTime == 0 || file.lastModified() > endTime){
						endTime = file.lastModified();
					}

					String imageDate = DateUtils.getDateByLong(file.lastModified(), DateUtils.parsePatterns[1]);

					GuidesNotedVO guidesNotedVO = new GuidesNotedVO();
					guidesNotedVO.setIs_image_upload(false);
					guidesNotedVO.setPath(file.getPath());
					guidesNotedVO.setFileId(imageFolder.getFileId());
					guidesNotedVO.setDate(imageDate);
					guidesNotedVO.setType("pic");
					int notedId = guidesNotedDao.addNoted(guidesNotedVO, guidesVO);
					if (count == 1 && guidesVO.getFront_cover_photo_id() == 0){
						// 设置封面ID
						guidesVO.setFront_cover_photo_id(notedId);
					}
				}

				final int progress = i;
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						sync_progress_rg.setProgress(progress);
					}
				});
			}

			// 设置开始时间
			guidesVO.setStart_date(DateUtils.getDateByLong(startTime, DateUtils.parsePatterns[1]));
			// 设置结束时间
			guidesVO.setEnd_date(DateUtils.getDateByLong(endTime, DateUtils.parsePatterns[1]));
			// 设置图片数量
			guidesVO.setPhotos_count(guidesVO.getPhotos_count() + count);
			// 更新游记对象
			guidesDao.update(guidesVO);

			// 处理结束，退出
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					setResult(RESULT_OK);
					finish();
				}
			});
		}
	}
}
