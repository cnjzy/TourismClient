package com.tourism.app.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.tourism.app.R;
import com.tourism.app.common.Constants;
import com.tourism.app.util.DeviceUtil;
import com.tourism.app.util.preference.Preferences;
import com.tourism.app.util.preference.PreferencesUtils;
import com.tourism.app.widget.dialog.CustomLoadingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

public abstract class ETSBaseAdapter extends BaseAdapter {
	protected BaseActivity context;
	protected LayoutInflater mInflater;
	protected AbsListView listView;
	protected final String RELEASE_IMAGE_TAG = "release";
	protected PreferencesUtils preferencesUtils;

	// 图片第一次加载动画
	protected ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	/**
	 * 是否正在加载
	 */
	private boolean isLoading = true;

	// 主线程Handler
	protected Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_ADAPTER:
				// 修改数据集的时候， 不要调用这个
				notifyDataSetChanged();
				dismissLoadingDialog();
				break;
			}
		};
	};

	/**
	 * 刷新adapter
	 */
	public static final int UPDATE_ADAPTER = 1;

	/**
	 * 图片加载参数选项
	 */
	public DisplayImageOptions options;

	/**
	 * 加载提示框
	 */
	public static CustomLoadingDialog loadingDialog;

	/**
	 * 数据集
	 */
	protected List dataList = new ArrayList();

	/**
	 * 是否正在加载数据
	 */
	protected boolean isLoadingData = false;
	protected int loadRow = 0; // 加载行
	protected int CURRENT_TYPE = TYPE_LOADING;
	public static final int TYPE_LOADING = 1;
	public static final int TYPE_SHOW_DATA = 2;
	public static final int TYPE_IS_NULL = 3;

	public ETSBaseAdapter(BaseActivity context, AbsListView listView) {
		this.context = context;
		this.listView = listView;
		preferencesUtils = new PreferencesUtils(context, Preferences.CONFIG_FILE);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initLoadingDialog();
		isLoading = true;

		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.img_default_horizon).showImageForEmptyUri(R.drawable.img_default_horizon)
				.showImageOnFail(R.drawable.img_default_horizon).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).build();

	}

	private void initLoadingDialog() {
		loadingDialog = new CustomLoadingDialog(context, Constants.LOADING_CONTENTS, true);
	}

	public void showLoadingDialog() {
		if (loadingDialog != null && !loadingDialog.isShowing())
			loadingDialog.show();
	}

	public void dismissLoadingDialog() {
		if (loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}

	public void addFirst(final List list) {
		isLoadingData = false;
		isLoading = false;
		dismissLoadingDialog();
		if (list != null) {
			getDataList().addAll(0, list);
			list.clear();
		}
		notifyDataSetChanged();
	}

	public void addItem(Object o) {
		isLoadingData = false;
		isLoading = false;
		if (o != null) {
			getDataList().add(o);
			notifyDataSetChanged();
		}
	}

	public boolean remove(int i) {
		if (dataList != null && dataList.size() > i) {
			dataList.remove(i);
		}
		return true;
	}

	public boolean remove(Object o) {
		if (dataList != null && o != null) {
			return dataList.remove(o);
		}
		return false;
	}

	public void addLast(List list) {
		addLast(list, true);
	}

	public void addLast(List list, boolean isClear) {
		isLoadingData = false;
		isLoading = false;
		dismissLoadingDialog();
		if (list != null) {
			getDataList().addAll(list);
			if (isClear)
				list.clear();
		}
		notifyDataSetChanged();
	}

	public void clear() {
		isLoading = true;
		isLoadingData = false;
		getDataList().clear();
		AnimateFirstDisplayListener.displayedImages.clear();
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if (getDataList() != null && position >= 0 && position < dataList.size())
			return dataList.get(position);
		else
			return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (getDataList().size() > 0)
			loadRow = 0;
		return getDataList().size() + loadRow;
	}

	public List getDataList() {
		if (dataList == null)
			dataList = new ArrayList();
		return dataList;
	}

	public void releaseCacheImage() {
		for (int i = 0; listView != null && i < listView.getCount(); i++) {
			View view = listView.getChildAt(i);
			if (view != null) {
				ImageView imgView = (ImageView) view.findViewWithTag(RELEASE_IMAGE_TAG);
				if (imgView != null)
					imgView.setImageResource(R.drawable.ic_launcher);
			}
		}
	}

	public int updateType() {
		if (isLoading) {
			CURRENT_TYPE = TYPE_LOADING;
			loadRow = 0;
		} else if (getDataList().size() == 0) {
			CURRENT_TYPE = TYPE_IS_NULL;
			if (listView == null)
				loadRow = 0;
			else
				loadRow = 1;
		} else {
			CURRENT_TYPE = TYPE_SHOW_DATA;
			loadRow = 0;
		}
		return CURRENT_TYPE;
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		updateType();
		super.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (CURRENT_TYPE == TYPE_IS_NULL) {
			convertView = mInflater.inflate(R.layout.list_is_null, null);
			convertView.setTag(TYPE_IS_NULL);

			int itemCount = 0;
			if (listView instanceof ListView) {
				itemCount = ((ListView) listView).getHeaderViewsCount();
			}

			AbsListView.LayoutParams params = new AbsListView.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			// params.height = listView.getHeight() < DeviceUtil.dip2px(context,
			// 200) ? DeviceUtil.dip2px(context, 200)
			// : listView.getHeight();
			params.height = itemCount > 0 ? DeviceUtil.dip2px(context, 300) : listView.getHeight() - DeviceUtil.dip2px(context, 40);
			params.width = listView.getWidth();
			convertView.setLayoutParams(params);

			return convertView;
		}
		return null;
	}

	public void onDestroy() {
		getDataList().clear();
		AnimateFirstDisplayListener.displayedImages.clear();
	}

	public boolean checkTag(String tag) {
		if ("1".equals(tag)) {
			return true;
		} else {
			return false;
		}
	}

	public void showToast(String content) {
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}

	private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				}
			}
		}
	}

	public abstract void loadData();
}
