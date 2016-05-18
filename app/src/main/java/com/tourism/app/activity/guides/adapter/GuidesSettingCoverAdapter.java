package com.tourism.app.activity.guides.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.tourism.app.R;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.base.ETSBaseAdapter;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.util.ViewHolderUtil;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

/**
 * Created by Jzy on 16/5/14.
 */
public class GuidesSettingCoverAdapter extends ETSBaseAdapter {
    private GuidesVO guidesVO;

    public GuidesSettingCoverAdapter(BaseActivity context, AbsListView listView) {
        super(context, listView);
    }

    @Override
    public void loadData() {

    }

    public void setGuidesVO(GuidesVO guidesVO) {
        this.guidesVO = guidesVO;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = mInflater.inflate(R.layout.item_guides_cover, parent, false);
        }

        ImageView item_icon_iv = ViewHolderUtil.get(convertView, R.id.item_icon_iv);
        ImageView item_selected_iv = ViewHolderUtil.get(convertView, R.id.item_selected_iv);

        GuidesNotedVO vo = (GuidesNotedVO) getItem(position);

        if (vo != null){
            LoadLocalImageUtil.getInstance().displayFromSDCard(vo.getPath(), item_icon_iv, null);
            if (guidesVO.getPhotoVO() !=null && vo.getLocal_id() == guidesVO.getPhotoVO().getLocal_id()){
                item_selected_iv.setVisibility(View.VISIBLE);
            }else{
                item_selected_iv.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }
}
