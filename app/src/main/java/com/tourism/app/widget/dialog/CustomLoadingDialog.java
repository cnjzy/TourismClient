package com.tourism.app.widget.dialog;



import java.util.concurrent.TimeUnit;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.net.DefaultThreadPool;
import com.tourism.app.util.LogUtil;

/**
 * 
* @ClassName: CustomLoadingDialog
* @Description: TODO(自定义loading对话框)
* @author jzy
* @date 2012-7-20 下午03:41:39
*
 */
public class CustomLoadingDialog extends Dialog{
	TextView loading_del_textview;
	String content = "";
	boolean isGoBack = true;
	private Context context;
	
	public CustomLoadingDialog(Context context,String content,boolean isGoBack) {
		super(context,R.style.custom_dialog);
	    this.content = content;
	    this.isGoBack = isGoBack;
	}
	
	public CustomLoadingDialog(Context context,int content,boolean isGoBack) {
		super(context,R.style.custom_dialog);
		if(content > 0)
			this.content = context.getString(content);
	    this.isGoBack = isGoBack;
	}

	protected void onCreate(Bundle savedInstanceState){
		 super.onCreate(savedInstanceState);
		 //设置view样式
		 setContentView(R.layout.custom_loading_dialog);	
	 }
	 //called when this dialog is dismissed
	 protected void onStop() {
		 super.onStop();
	 }
	 @Override
	public void show() {
		// TODO Auto-generated method stub
		super.show();
		 
	}
	 @Override
	public void dismiss() {
		// TODO Auto-generated method stub
		super.dismiss();
		 try{
		     if(isGoBack){
		         DefaultThreadPool.pool.awaitTermination(1, TimeUnit.MICROSECONDS);
		     }
		 }catch (Exception e) {
			 LogUtil.d("awaitTermination","awaitTermination");
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
	 
	
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if (keyCode == KeyEvent.KEYCODE_BACK ) { 
			 if(isGoBack){
				 dismiss();
				 if(context instanceof BaseActivity){
					 ((BaseActivity)context).finish();
				 }
				 return super.onKeyDown(keyCode, event);
			 }else{
				 return super.onKeyDown(keyCode, event);
			 }
			 	
		 }
		 
		return super.onKeyDown(keyCode, event);
		
	}
}
