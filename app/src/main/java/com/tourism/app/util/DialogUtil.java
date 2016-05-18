package com.tourism.app.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tourism.app.MyApp;
import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.widget.view.CustomRoundProgress;
import com.tourism.app.widget.wheel.OnWheelChangedListener;
import com.tourism.app.widget.wheel.WheelView;
import com.tourism.app.widget.wheel.adapter.ArrayWheelAdapter;
import com.tourism.app.widget.wheel.adapter.NumericWheelAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogUtil {

	public interface OnCallbackListener {
		void onClick(int whichButton, Object o);
	}

	/**
	 * 拨打电话Dialog
	 * 
	 * @param context
	 * @param phone
	 * @return
	 */
	public static Dialog showCallPhone(final Activity context, final String phone) {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("提示").setMessage("是否拨打电话：" + phone).setNegativeButton("确定", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				DeviceUtil.openCallPhone(context, phone.replaceAll("-", ""));
				dialog.cancel();
			}
		}).setPositiveButton("取消", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create();

		dialog.show();

		return dialog;
	}

	/**
	 * 提示dialog
	 */
	public static void showTipDialog(final BaseActivity act, String title, String content, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_tip, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		final TextView dialog_title_tv = (TextView) view.findViewById(R.id.dialog_title_tv);
		final TextView dialog_content_tv = (TextView) view.findViewById(R.id.dialog_content_tv);
		final Button dialog_left_btn = (Button) view.findViewById(R.id.dialog_left_btn);
		final Button dialog_right_btn = (Button) view.findViewById(R.id.dialog_right_btn);

		dialog_title_tv.setText(title);
		dialog_content_tv.setText(content);
		dialog_left_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});
		dialog_right_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null)
					onCallbackListener.onClick(0, null);
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 获取预约dialog
	 */
	public static void showCategoryMakeDialog(final BaseActivity act, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_category_make, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		ImageButton close_btn = (ImageButton) view.findViewById(R.id.close_btn);
		final EditText user_name_et = (EditText) view.findViewById(R.id.user_name_et);
		final EditText user_phone_et = (EditText) view.findViewById(R.id.user_phone_et);
		final EditText user_remarks_et = (EditText) view.findViewById(R.id.user_remarks_et);
		Button submit_btn = (Button) view.findViewById(R.id.submit_btn);

		close_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		submit_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userName = user_name_et.getText().toString();
				String userPhone = user_phone_et.getText().toString();
				String remarks = user_remarks_et.getText().toString();
				if (TextUtils.isEmpty(userName)) {
					MyApp.showToast("用户名不能为空");
				} else if (TextUtils.isEmpty(userPhone)) {
					MyApp.showToast("手机号不能为空");
				} else {
					dialog.cancel();
					if (onCallbackListener != null) {
						Map<String, String> map = new HashMap<String, String>();
						map.put("userName", userName);
						map.put("userPhone", userPhone);
						map.put("remarks", remarks);
						onCallbackListener.onClick(0, map);
					}
				}
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 获取报名dialog
	 */
	public static void showCategorySignedUpDialog(final BaseActivity act, String price, String amount, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_category_signed_up, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		ImageButton close_btn = (ImageButton) view.findViewById(R.id.close_btn);
		final EditText user_name_et = (EditText) view.findViewById(R.id.user_name_et);
		final EditText user_phone_et = (EditText) view.findViewById(R.id.user_phone_et);
		TextView user_signed_up_price_tv = (TextView) view.findViewById(R.id.user_signed_up_price_tv);
		TextView user_signed_up_amount_tv = (TextView) view.findViewById(R.id.user_signed_up_amount_tv);
		final RadioButton pay_zfb_rb = (RadioButton) view.findViewById(R.id.pay_zfb_rb);
		Button submit_btn = (Button) view.findViewById(R.id.submit_btn);

		user_signed_up_price_tv.setText("定金：￥" + price);
		user_signed_up_amount_tv.setText("提示：本次活动共收取" + amount + "元");

		close_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		submit_btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String userName = user_name_et.getText().toString();
				String userPhone = user_phone_et.getText().toString();
				int payType = pay_zfb_rb.isChecked() ? 2 : 1;
				if (TextUtils.isEmpty(userName)) {
					MyApp.showToast("用户名不能为空");
				} else if (TextUtils.isEmpty(userPhone)) {
					MyApp.showToast("手机号不能为空");
				} else {
					if (onCallbackListener != null) {
						dialog.cancel();
						Map<String, String> map = new HashMap<String, String>();
						map.put("userName", userName);
						map.put("userPhone", userPhone);
						map.put("payType", payType + "");
						onCallbackListener.onClick(0, map);
					}
				}
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 获取游记Listdialog
	 */
	public static void showGuidesListDialog(final Activity act, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_guides_list, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		Button btn1 = (Button) view.findViewById(R.id.btn1);
		Button btn2 = (Button) view.findViewById(R.id.btn2);
		Button btn3 = (Button) view.findViewById(R.id.btn3);
		Button btn4 = (Button) view.findViewById(R.id.btn4);
		Button btn5 = (Button) view.findViewById(R.id.btn5);

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(0, null);
				}
			}
		});
		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(1, null);
				}
			}
		});
		btn3.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(2, null);
				}
			}
		});
		btn4.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(3, null);
				}
			}
		});
		btn5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 游记评论dialog
	 */
	public static void showGuidesReplyDialog(final BaseActivity act, Handler handler, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.view_guides_add_reply, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		final EditText guides_reply_et = (EditText) view.findViewById(R.id.guides_reply_et);
		TextView guides_reply_count_tv = (TextView) view.findViewById(R.id.guides_reply_count_tv);

		guides_reply_et.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					System.err.println("11111111");
					dialog.cancel();
				}
				return false;
			}
		});
		
		handler.postDelayed(new Runnable() {
			public void run() {
				DeviceUtil.showIMM(act, guides_reply_et);
			}
		}, 500);
		

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 游记评论dialog
	 */
	public static void showGuidesCameraDialog(final BaseActivity act, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.select_pic_layout, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		Button btn_take_photo = (Button) view.findViewById(R.id.btn_take_photo);
        Button btn_pick_photo = (Button) view.findViewById(R.id.btn_pick_photo);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

        btn_take_photo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.cancel();
                if(onCallbackListener != null){
                    onCallbackListener.onClick(0, null);
                }
            }
        });

        btn_pick_photo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(1, null);
				}
			}
		});

        btn_cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 用户更多Dialog
	 */
	public static void showUserMoreDialog(final Activity act, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_user_more, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		Button btn1 = (Button) view.findViewById(R.id.btn1);
		Button btn5 = (Button) view.findViewById(R.id.btn5);

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(0, null);
				}
			}
		});

		btn5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 游记文本操作Dialog
	 */
	public static void showGuidesNotedDialog(final Activity act, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_guides_noted_action, null);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		Button btn1 = (Button) view.findViewById(R.id.btn1);
		Button btn2 = (Button) view.findViewById(R.id.btn1);
		Button btn5 = (Button) view.findViewById(R.id.btn5);

		btn1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(0, null);
				}
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
				if (onCallbackListener != null) {
					onCallbackListener.onClick(1, null);
				}
			}
		});

		btn5.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	/**
	 * 游记文本操作Dialog
	 */
	public static void showReplyDialog(final BaseActivity act, final OnCallbackListener onCallbackListener) {
		View view = act.getLayoutInflater().inflate(R.layout.view_guides_add_reply, null);
		view.setVisibility(View.VISIBLE);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		View reply_bottom_ll = view.findViewById(R.id.reply_bottom_ll);
		final EditText guides_reply_et = (EditText) view.findViewById(R.id.guides_reply_et);
		final TextView guides_reply_count_tv = (TextView) view.findViewById(R.id.guides_reply_count_tv);

		view.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		guides_reply_et.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				guides_reply_count_tv.setText(guides_reply_et.getText().length() + "/150");
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		guides_reply_et.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
					if (!TextUtils.isEmpty(guides_reply_et.getText())) {
						if (onCallbackListener != null) {
							onCallbackListener.onClick(0, guides_reply_et.getText().toString());
						}
						dialog.cancel();
						return true;
					} else {
						MyApp.showToast("评论内容不能为空");
					}
				}
				return false;
			}
		});

		// set a large value put it in bottom
		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		ViewUtil.controlKeyboardLayout(view, reply_bottom_ll);
		DeviceUtil.showIMM(act, guides_reply_et);

		dialog.show();
	}

	/**
	 * 游记文本操作Dialog
	 */
	public static void showCustomProgressDialog(final BaseActivity act, final OnCallbackListener onCallbackListener, Handler handler) {
		View view = act.getLayoutInflater().inflate(R.layout.dialog_image_sync, null);
		view.setVisibility(View.VISIBLE);
		final Dialog dialog = new Dialog(act, R.style.custom_dialog);

		CustomRoundProgress sync_progress_rg = (CustomRoundProgress) view.findViewById(R.id.sync_progress_rg);



		// set a large value put it in bottom
//		setDilaogBottomStyle(dialog);
		dialog.setContentView(view);

		dialog.show();
	}

	private static void setDilaogBottomStyle(Dialog dialog) {
		// set a large value put it in bottom
		Window w = dialog.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		w.setWindowAnimations(R.style.DataSheetAnimation);
		dialog.onWindowAttributesChanged(lp);
		dialog.setCanceledOnTouchOutside(true);
	}

	// ============================================================================================
	public static Dialog showDateAlert(final Context context, final OnDateSelectedListener mOnDateSelectedListener, String title) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_date, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);

		Calendar calendar = Calendar.getInstance();

		final TextView dateTitle = (TextView) layout.findViewById(R.id.data_title);
		final Button submit = (Button) layout.findViewById(R.id.date_submit);
		final Button cancel = (Button) layout.findViewById(R.id.date_cancel);

		final WheelView month = (WheelView) layout.findViewById(R.id.month);
		final WheelView year = (WheelView) layout.findViewById(R.id.year);
		final WheelView day = (WheelView) layout.findViewById(R.id.day);

		dateTitle.setText(title);

		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day, context);
			}
		};

		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mOnDateSelectedListener.onSelected(year.getCurrentItem() + 1, month.getCurrentItem() + 1, day.getCurrentItem() + 1);
				dlg.dismiss();
			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dlg.dismiss();
			}
		});

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = context.getResources().getStringArray(R.array.month_item);
		month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// year
		int curYear = calendar.get(Calendar.YEAR) - 1;
		year.setViewAdapter(new DateNumericAdapter(context, 1, 3000, curYear));
		year.setCurrentItem(curYear);
		year.addChangingListener(listener);

		// day
		updateDays(year, month, day, context);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

		// set a large value put it in bottom
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		dlg.setContentView(layout);

		if (context instanceof Activity) {
			if (!((Activity) context).isFinishing()) {
				dlg.show();
			}
		}
		return dlg;
	}

	/**
	 * 提示框(列表样式)
	 * 
	 * @param context
	 * @param title
	 * @param exit
	 * @param alertDo
	 * @param cancelListener
	 * @param array
	 * @return
	 */
	public static Dialog showListAlert(final Context context, final String title, String exit, String cancel, final OnAlertSelectId alertDo,
			OnCancelListener cancelListener, int array) {
		final Dialog dlg = new Dialog(context, R.style.MMTheme_DataSheet);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.alert_dialog_menu_layout, null);
		final int cFullFillWidth = 10000;
		layout.setMinimumWidth(cFullFillWidth);
		final ListView list = (ListView) layout.findViewById(R.id.content_list);

		AlertAdapter adapter = new AlertAdapter(context, title, context.getResources().getStringArray(array), exit, cancel);

		list.setAdapter(adapter);
		list.setDividerHeight(0);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!(title == null || title.equals("")) && position - 1 >= 0) {
					alertDo.onClick(position - 1, null);
					dlg.dismiss();
					list.requestFocus();
				} else {
					alertDo.onClick(position, null);
					dlg.dismiss();
					list.requestFocus();
				}

			}
		});
		// set a large value put it in bottom
		Window w = dlg.getWindow();
		WindowManager.LayoutParams lp = w.getAttributes();
		lp.x = 0;
		final int cMakeBottom = -1000;
		lp.y = cMakeBottom;
		lp.gravity = Gravity.BOTTOM;
		dlg.onWindowAttributesChanged(lp);
		dlg.setCanceledOnTouchOutside(true);
		if (cancelListener != null) {
			dlg.setOnCancelListener(cancelListener);
		}
		dlg.setContentView(layout);
		if (context instanceof Activity) {
			if (!((Activity) context).isFinishing()) {
				dlg.show();
			}
		}
		return dlg;
	}

	public interface OnAlertSelectId {
		void onClick(int whichButton, Object o);
	}

	static class AlertAdapter extends BaseAdapter {
		// private static final String TAG = "AlertAdapter";
		public static final int TYPE_BUTTON = 0;
		public static final int TYPE_TITLE = 1;
		public static final int TYPE_EXIT = 2;
		public static final int TYPE_CANCEL = 3;
		private List<String> items;
		private int[] types;
		// private boolean isSpecial = false;
		private boolean isTitle = false;
		// private boolean isExit = false;
		private Context context;

		public AlertAdapter(Context context, String title, String[] items, String exit, String cancel) {
			this.items = new ArrayList<String>();
			for (int i = 0; i < items.length; i++) {
				this.items.add(items[i]);
			}
			this.types = new int[this.items.size() + 3];
			this.context = context;
			if (title != null && !title.equals("")) {
				types[0] = TYPE_TITLE;
				this.isTitle = true;
				this.items.add(0, title);
			}

			if (exit != null && !exit.equals("")) {
				// this.isExit = true;
				types[this.items.size()] = TYPE_EXIT;
				this.items.add(exit);
			}

			if (cancel != null && !cancel.equals("")) {
				// this.isSpecial = true;
				types[this.items.size()] = TYPE_CANCEL;
				this.items.add(cancel);
			}
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Object getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public boolean isEnabled(int position) {
			if (position == 0 && isTitle) {
				return false;
			} else {
				return super.isEnabled(position);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final String textString = (String) getItem(position);
			ViewHolder holder;
			int type = types[position];
			if (convertView == null || ((ViewHolder) convertView.getTag()).type != type) {
				holder = new ViewHolder();
				if (type == TYPE_CANCEL) {
					convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_cancel, null);
				} else if (type == TYPE_BUTTON) {
					convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout, null);
				} else if (type == TYPE_TITLE) {
					convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_title, null);
				} else if (type == TYPE_EXIT) {
					convertView = View.inflate(context, R.layout.alert_dialog_menu_list_layout_special, null);
				}

				// holder.view = (LinearLayout)
				// convertView.findViewById(R.id.popup_layout);
				holder.text = (TextView) convertView.findViewById(R.id.popup_text);
				holder.type = type;

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.text.setText(textString);
			return convertView;
		}
	}

	static class ViewHolder {
		// LinearLayout view;
		TextView text;
		int type;
	}

	/**
	 * Updates day wheel. Sets max days according to selected month and year
	 */
	static void updateDays(WheelView year, WheelView month, WheelView day, Context context) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + year.getCurrentItem());
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DateNumericAdapter(context, 1, maxDays, calendar.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
	}

	/**
	 * Adapter for numeric wheels. Highlights the current value.
	 */
	private static class DateNumericAdapter extends NumericWheelAdapter {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateNumericAdapter(Context context, int minValue, int maxValue, int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	/**
	 * Adapter for string based wheel. Highlights the current value.
	 */
	private static class DateArrayAdapter extends ArrayWheelAdapter<String> {
		// Index of current item
		int currentItem;
		// Index of item to be highlighted
		int currentValue;

		/**
		 * Constructor
		 */
		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(Typeface.SANS_SERIF);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	public interface OnDateSelectedListener {
		public void onSelected(int year, int month, int day);
	}
}
