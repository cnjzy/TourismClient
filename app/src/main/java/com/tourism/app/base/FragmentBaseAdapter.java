package com.tourism.app.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import android.widget.Toast;

import com.tourism.app.R;
import com.tourism.app.common.Constants;
import com.tourism.app.util.preference.Preferences;
import com.tourism.app.util.preference.PreferencesUtils;
import com.tourism.app.widget.dialog.CustomLoadingDialog;
public abstract class FragmentBaseAdapter extends BaseAdapter{
	protected FragmentBaseAdapter adapter = this;
	protected BaseActivity context;
	protected BaseFragment ft;
	protected LayoutInflater mInflater;
	protected AbsListView listView;
	protected final String RELEASE_IMAGE_TAG = "release";
	protected PreferencesUtils preferencesUtils;
	
	//主线程Handler
	protected Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case UPDATE_ADAPTER:
				//修改数据集的时候， 不要调用这个
				adapter.notifyDataSetChanged();
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
	 * 加载提示框
	 */
	public static CustomLoadingDialog loadingDialog;
	
	/**
	 * 数据集
	 */
	protected List dataList = new ArrayList();
	
	/**
	 * 图片缓存路径
	 */
	private static final String IMAGE_CACHE_DIR = "thumbs";
	
	/**
	 * 是否正在加载数据
	 */
	protected boolean isLoadingData = false;
	protected int loadRow = 0;	//加载行
	protected int CURRENT_TYPE = TYPE_LOADING;
	protected static final int TYPE_LOADING = 1;
	protected static final int TYPE_SHOW_DATA = 2;
	protected static final int TYPE_IS_NULL = 3;
	
	public FragmentBaseAdapter(BaseActivity context, AbsListView listView, BaseFragment ft){
		dataList = new ArrayList();
		this.ft = ft;
		this.context = context;
		this.listView = listView;
		this.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
	            @Override
	            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
	                // Pause fetcher to ensure smoother scrolling when flinging
	                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
	                } else {
	                }
	            }

	            @Override
	            public void onScroll(AbsListView absListView, int firstVisibleItem,
	                    int visibleItemCount, int totalItemCount) {
	            	if(firstVisibleItem + visibleItemCount >= totalItemCount-4){
	            		loadData();
	            	}
	            }
	        });
		
		preferencesUtils = new PreferencesUtils(context, Preferences.CONFIG_FILE);
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		initLoadingDialog();
	}
	
	private void initLoadingDialog(){
		loadingDialog = new  CustomLoadingDialog(context, Constants.LOADING_CONTENTS, true);
	}
	
	public void showLoadingDialog(){
		if(loadingDialog != null && !loadingDialog.isShowing())
			loadingDialog.show();
	}
	
	public void dismissLoadingDialog(){
		if(loadingDialog != null && loadingDialog.isShowing())
			loadingDialog.dismiss();
	}
	
	public void addFirst(final List list){
		isLoadingData = false;
		dismissLoadingDialog();
		if(list != null){
			dataList.addAll(0, list);
			list.clear();
		}
		notifyDataSetChanged();
	}
	
	public void addItem(Object o){
		isLoadingData = false;
		if(o != null){
			dataList.add(o);
			notifyDataSetChanged();
		}
	}
	
	public boolean remove(int i){
		if(dataList != null && dataList.size() > i){
			dataList.remove(i);
		}
		return true;
	}
	
	public boolean remove(Object o){
		if(dataList != null && o != null){
			return dataList.remove(o);
		}
		return false;
	}
	
	public void addLast(List list){
		addLast(list, true);
	}
	
	public void addLast(List list, boolean isClear){
		isLoadingData = false;
		dismissLoadingDialog();
		if(list != null){
			this.dataList.addAll(list);
			if(isClear)
				list.clear();
		}
		notifyDataSetChanged();
	}
	
	public void clear(){
		isLoadingData = false;
		dataList.clear();
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		if(position >= 0 && position < dataList.size())
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
		if(dataList == null)
			dataList = new ArrayList();
		// TODO Auto-generated method stub
		return dataList.size() + loadRow;
	}
	
	public void releaseCacheImage(){
		for (int i = 0; i < listView.getCount(); i++) {
			View view = listView.getChildAt(i);
			if(view != null){
				ImageView imgView = (ImageView)view.findViewWithTag(RELEASE_IMAGE_TAG);
				if(imgView != null)
					imgView.setImageResource(R.drawable.ic_launcher);
			}
		}
	}
	
	public int updateType() {
		if (isLoadingData) {
			CURRENT_TYPE = TYPE_LOADING;
			loadRow = 0;
		} else if (dataList.size() == 0) {
			CURRENT_TYPE = TYPE_IS_NULL;
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
	
	public void onDestroy(){
		dataList.clear();
		dataList = null;
		context = null;
		listView = null;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (CURRENT_TYPE == TYPE_IS_NULL) {
			convertView = mInflater.inflate(R.layout.list_is_null, null);

			AbsListView.LayoutParams params = new AbsListView.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
			;
			params.height = listView.getHeight();
			convertView.setLayoutParams(params);

			return convertView;
		}
		return null;
	}
	
	public boolean checkTag(String tag){
		if("1".equals(tag)){
			return true;
		}else{
			return false;
		}
	}
	
	public void showToast(String content){
		Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
	}
	
	public int getSize(){
		return dataList.size();
	}
	
	public abstract void loadData();
}
