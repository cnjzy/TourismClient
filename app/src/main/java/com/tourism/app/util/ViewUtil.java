package com.tourism.app.util;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.TextView;

public class ViewUtil {

    public static void alignGalleryToLeft(View parentView, Gallery gallery,
                                          int itemWidth, int spacing) {
        int galleryWidth = parentView.getWidth();// 得到Parent控件的宽度
        // 在这边我们必须先从资源尺寸中得到子控件的宽度跟间距，因为:
        // 1. 在运行时，我们无法得到间距(因为Gallery这个类，没有这样的权限)
        // 2.有可能在运行得宽度的时候，item资源还没有准备好。
        int offset = 0;
        if (galleryWidth <= itemWidth) {
            offset = galleryWidth / 2 - itemWidth / 2 - spacing;
        } else {
            offset = galleryWidth - itemWidth - 2 * spacing;
        }

        // 现在就可以根据更新的布局参数设置做对其了。
        MarginLayoutParams mlp = (MarginLayoutParams) gallery.getLayoutParams();
        mlp.setMargins(-offset, mlp.topMargin, mlp.rightMargin,
                mlp.bottomMargin);
    }

    /**
     * 设置TextView右侧图标和字体颜色
     *
     * @param context
     * @param view
     * @param right
     * @param color
     */
    public static void setViewRightDrawableAndTextColor(Context context,
                                                        TextView view, int right, int color) {
        view.setTextColor(context.getResources().getColor(color));
        setViewDrawable(context, view, 0, 0, right, 0);
    }

    /**
     * 设置TextView左侧图标和字体颜色
     *
     * @param context
     * @param view
     * @param left
     * @param color
     */
    public static void setViewLeftDrawableAndTextColor(Context context, TextView view, int left, int color) {
        view.setTextColor(context.getResources().getColor(color));
        setViewDrawable(context, view, left, 0, 0, 0);
    }

    /**
     * 设置TextView图标
     *
     * @param context
     * @param view
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public static void setViewDrawable(Context context, TextView view, int left,
                                       int top, int right, int bottom) {
        Drawable leftDrawable = null, topDrawable = null, rightDrawable = null, bottomDrawable = null;
        if (left > 0) {
            leftDrawable = context.getResources().getDrawable(left);
            leftDrawable.setBounds(0, 0, leftDrawable.getMinimumWidth(),
                    leftDrawable.getMinimumHeight());
        }
        if (top > 0) {
            topDrawable = context.getResources().getDrawable(top);
            topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(),
                    topDrawable.getMinimumHeight());
        }
        if (right > 0) {
            rightDrawable = context.getResources().getDrawable(right);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(),
                    rightDrawable.getMinimumHeight());
        }
        if (bottom > 0) {
            bottomDrawable = context.getResources().getDrawable(bottom);
            bottomDrawable.setBounds(0, 0, bottomDrawable.getMinimumWidth(),
                    bottomDrawable.getMinimumHeight());
        }
        view.setCompoundDrawables(leftDrawable, topDrawable, rightDrawable,
                bottomDrawable);
    }

    /**
     * 执行动画
     *
     * @param view
     */
    public static void startBackgroundAnimation(View view) {
        AnimationDrawable animationDrawable = (AnimationDrawable) view.getBackground();
        animationDrawable.start();
    }

    /**
     * 执行动画
     *
     * @param view
     */
    public static void startBackgroundAnimation(View view, int animo) {
        view.setBackgroundResource(animo);
        startBackgroundAnimation(view);
    }

    /**
     * 设置字体
     *
     * @param context
     * @param v
     * @param dis
     */
    public static void setSpecialModeTextSize(Context context, View v, int dis) {
        if ("SCH-N719".equals(DeviceUtil.getModel(context))) {
            if (v instanceof TextView) {
                ((TextView) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, dis);
            } else if (v instanceof Button) {
                ((Button) v).setTextSize(TypedValue.COMPLEX_UNIT_SP, dis);
            }
        }
    }

    /**
     * 是否是特殊机型
     *
     * @param context
     * @return
     */
    public static boolean isSpecialMode(Context context) {
        if ("SCH-N719".equals(DeviceUtil.getModel(context)))
            return true;
        else
            return false;
    }

    /**
     * @param root
     *            最外层布局，需要调整的布局
     * @param scrollToView
     *            被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
     */
    public static void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                // 获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                // 获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                // 若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    // 获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    // 计算root滚动高度，使scrollToView在可见区域
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    // 键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }
}
