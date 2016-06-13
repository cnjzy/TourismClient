package com.tourism.app.activity.guides;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDao;
import com.tourism.app.vo.GuidesVO;

public class GuidesAddActivity extends BaseActivity{
	private GuidesDao guidesDao;
	private GuidesVO vo = new GuidesVO();

	private EditText guides_name_et;

	@Override
	public void initLayout() {
		setContentView(R.layout.activity_guides_add);
	}

	@Override
	public void init() {
		guidesDao = new GuidesDao(context);
	}

	@Override
	public void initView() {
		guides_name_et = (EditText) findViewById(R.id.guides_name_et);
	}

	@Override
	public void initListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initValue() {
		setNavigationTitle("新建攻略");
		setNavigationRightButton(View.VISIBLE, 0, R.drawable.ico_ok);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch(v.getId()){
			case R.id.navigation_right_btn:
				String guidesName = guides_name_et.getText().toString();
				if(TextUtils.isEmpty(guidesName)){
					showToast("游记名不能为空");
				}else {
					vo.setName(guidesName);
					int id = guidesDao.add(vo);
					vo.setLocal_id(id);
					Bundle data = new Bundle();
					data.putSerializable("vo", vo);
					showActivity(context, GuidesEditActivity.class, data);
					finish();
				}
				break;
		}
	}
}
