package com.tourism.app.widget.dialog;




import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.util.LogUtil;
import com.tourism.app.util.StringUtil;

/**
 * 
* @ClassName: CustomLoadingDialog
* @Description: TODO(自定义loading对话框)
* @author jzy
* @date 2012-7-20 下午03:41:39
*
 */
public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener{
	ProgressBar loading_progress_bar;
	TextView loading_text;
	TextView loading_del_textview;
	static int theme = R.style.custom_dialog;
	String title = "";
	String content = "";
	private boolean isShowProgress;
	private boolean  isHideCloseBtn;
	public CustomAlertDialog(Context context,String title,String content) {
	    super(context,theme);
	    this.title = title;
	    this.content = content;
		LogUtil.d("TAG","****************************");
	}
	public CustomAlertDialog(Context context,String title,String content,boolean isShowProgress,boolean isHideCloseBtn) {
		this(context,title,content);
	    this.isShowProgress = isShowProgress;
	    this.isHideCloseBtn = isHideCloseBtn;
		LogUtil.d("TAG","****************************");
	}

	 protected void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 //去掉标题
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		 //设置view样式
		 setContentView(R.layout.custom_alert_dialog);	
		 loading_text= (TextView) findViewById(R.id.loading_text);
		 loading_del_textview = (TextView) findViewById(R.id.loading_del_textview);
		 if(isHideCloseBtn){
			 loading_del_textview.setVisibility(View.GONE);
		 }else{
			 loading_del_textview.setVisibility(View.VISIBLE);
		 }
		 loading_del_textview.setOnClickListener(this);
		 loading_progress_bar = (ProgressBar) findViewById(R.id.loading_progress_bar);
		 if(isShowProgress){
			 loading_progress_bar.setVisibility(View.VISIBLE);
		 }else{
			 loading_progress_bar.setVisibility(View.GONE);
		 }
		 if(StringUtil.isNotEmpty(content)){
			 loading_text.setText(content);
			}
	 }
	
	 

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.loading_del_textview) {
			LogUtil.d("onClick","this dialog is force dismissed");
			dismiss();
		} else {
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK ) { 
			 if(isHideCloseBtn){
				 return true;
			 }
		 }
		return super.onKeyDown(keyCode, event);
	}
	 
}
