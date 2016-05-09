package com.tourism.app.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.tourism.app.R;
import com.tourism.app.activity.adapter.SearchListAdapter;
import com.tourism.app.base.BaseActivity;

public class SearchActivity extends BaseActivity{
	private EditText navigation_title_et;
	private ListView listView;
	private SearchListAdapter adapter;
	
	
	@Override
	public void initLayout() {
		setContentView(R.layout.activity_search);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initView() {
		navigation_title_et = (EditText) findViewById(R.id.navigation_title_et);
		listView = (ListView) findViewById(R.id.listView);
	}

	@Override
	public void initListener() {
		
	}

	@Override
	public void initValue() {
		adapter = new SearchListAdapter(context, listView);
		listView.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		switch(v.getId()){
		case R.id.navigation_right_btn:
			goBack();
			break;
		}
	}
}
