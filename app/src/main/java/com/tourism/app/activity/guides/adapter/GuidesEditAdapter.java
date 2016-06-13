/*
 * Copyright (C) 2015 Tomás Ruiz-López.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tourism.app.activity.guides.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tourism.app.R;
import com.tourism.app.activity.guides.GuidesAddNotedActivity;
import com.tourism.app.activity.guides.GuidesEditActivity;
import com.tourism.app.activity.guides.GuidesImageActivity;
import com.tourism.app.activity.guides.touchhelper.ItemTouchHelperAdapter;
import com.tourism.app.activity.guides.touchhelper.OnStartDragListener;
import com.tourism.app.activity.guides.viewholders.CountFooterViewHolder;
import com.tourism.app.activity.guides.viewholders.CountHeaderViewHolder;
import com.tourism.app.activity.guides.viewholders.CountItemViewHolder;
import com.tourism.app.base.BaseActivity;
import com.tourism.app.db.dao.GuidesDayDao;
import com.tourism.app.db.dao.GuidesLocationDao;
import com.tourism.app.db.dao.GuidesNotedDao;
import com.tourism.app.util.DateUtils;
import com.tourism.app.util.DialogUtil;
import com.tourism.app.util.LoadLocalImageUtil;
import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;
import com.tourism.app.widget.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuidesEditAdapter extends SectionedRecyclerViewAdapter<CountHeaderViewHolder,
        CountItemViewHolder,
        CountFooterViewHolder> implements ItemTouchHelperAdapter {

    protected BaseActivity context = null;

    private final OnStartDragListener mDragStartListener;

    private List<GuidesLocationVO> groupList = new ArrayList<GuidesLocationVO>();
    private List<List<GuidesNotedVO>> itemList = new ArrayList<List<GuidesNotedVO>>();

    public boolean isUpload = false;
    private GuidesVO guidesVO;

    GuidesDayDao dayDao;
    GuidesLocationDao locationDao;
    GuidesNotedDao guidesNotedDao;

    public GuidesEditAdapter(BaseActivity context, OnStartDragListener dragStartListener, GuidesVO vo) {
        this.context = context;
        this.guidesVO = vo;
        this.mDragStartListener = dragStartListener;

        dayDao = new GuidesDayDao(context);
        locationDao = new GuidesLocationDao(context);
        guidesNotedDao = new GuidesNotedDao(context);

        for (int i = 0; i < vo.getTrip_days().size(); i++) {
            GuidesDayVO dayVO = vo.getTrip_days().get(i);
            if (dayVO.getLocations().size() > 0) {
                for (int j = 0; j < dayVO.getLocations().size(); j++) {
                    GuidesLocationVO locationVO = dayVO.getLocations().get(j);
                    if (j == 0) {
                        locationVO.setIsDate(true);
                        locationVO.setDateStr(dayVO.getTrip_date());
                    }
                    groupList.add(locationVO);
                    itemList.add(locationVO.getNotes());
                }
            } else {
                GuidesLocationVO locationVO = new GuidesLocationVO();
                locationVO.setIsDate(true);
                locationVO.setDateStr(dayVO.getTrip_date());
                groupList.add(locationVO);
                itemList.add(new ArrayList<GuidesNotedVO>());
            }
        }
    }

    @Override
    protected int getItemCountForSection(int section) {
        return itemList.get(section).size();
    }

    @Override
    protected int getSectionCount() {
        return groupList.size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }

    @Override
    protected CountHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_guides_list_header, parent, false);
        return new CountHeaderViewHolder(view);
    }

    @Override
    protected CountFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_guides_list_content, parent, false);
        return new CountFooterViewHolder(view);
    }

    @Override
    protected CountItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_guides_list_content, parent, false);
        return new CountItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(CountHeaderViewHolder holder, int section) {
        final GuidesLocationVO locationVO = groupList.get(section);
        if (locationVO != null) {
            if (locationVO.isDate()) {
                holder.item_title_ll.setVisibility(View.VISIBLE);
                holder.item_title_date_name_tv.setText("DAY " + DateUtils.getIntervalDays(guidesVO.getStart_date(), locationVO.getDateStr(), DateUtils.parsePatterns[1]));
                holder.item_title_date_tv.setText(locationVO.getDateStr());
            } else {
                holder.item_title_ll.setVisibility(View.GONE);
            }
//            if (TextUtils.isEmpty(locationVO.getLocation_name())){
//                holder.contenet_ll.setVisibility(View.GONE);
//            }else {
//                holder.contenet_ll.setVisibility(View.VISIBLE);
//                holder.item_name_tv.setText(locationVO.getLocation_name());
//            }
            holder.item_name_tv.setText(locationVO.getLocation_name());

            if (!TextUtils.isEmpty(locationVO.getLocation_name())){
                holder.item_name_tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DialogUtil.showGuidesRemoveLocationDialog(context, new DialogUtil.OnCallbackListener() {
                            public void onClick(int whichButton, Object o) {
                                // 删除当前位置节点
                                locationDao.deleteById(locationVO.getLocal_id());
                                // 如果当前位置节点下有数据，那么放到新的节点内
                                if (locationVO.getNotes().size() > 0){
                                    // 获取天数据操作类
                                    GuidesDayVO dayVO = dayDao.getById(locationVO.getParent_id());
                                    Map<String, Object> params = new HashMap<String, Object>();
                                    params.put("parent_id", dayVO.getLocal_id());
                                    params.put("location_name", "");
                                    dayVO.setLocations(locationDao.getByParams(params));

                                    GuidesLocationVO newLocationVO = null;
                                    for(int i = 0; i < dayVO.getLocations().size(); i++){
                                        if (TextUtils.isEmpty(dayVO.getLocations().get(i).getLocation_name())){
                                            newLocationVO = dayVO.getLocations().get(i);
                                            break;
                                        }
                                    }

                                    if (newLocationVO == null){
                                        newLocationVO = new GuidesLocationVO();
                                        newLocationVO.setDateStr(dayVO.getTrip_date());
                                        newLocationVO.setLocation_name("");
                                        newLocationVO.setRank(0);
                                        newLocationVO.setParent_id(dayVO.getLocal_id());
                                        newLocationVO.setLocal_id(locationDao.add(newLocationVO));
                                    }

                                    for (int i = 0; i < locationVO.getNotes().size(); i++){
                                        locationVO.getNotes().get(i).setParent_id(newLocationVO.getLocal_id());
                                        guidesNotedDao.update(locationVO.getNotes().get(i));
                                    }

                                    ((GuidesEditActivity)context).updateData();
                                }
                            }
                        });
                    }
                });
            }else{
                holder.item_name_tv.setOnClickListener(null);
            }
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(CountFooterViewHolder holder, int section) {
        holder.render("Footer " + (section + 1));
    }

    @Override
    protected void onBindItemViewHolder(final CountItemViewHolder holder, int section, int position) {

        final GuidesNotedVO notedVO = itemList.get(section).get(position);
        if (notedVO != null) {
            if (notedVO.getType().equals("txt")){
                holder.item_content_icon_iv.setVisibility(View.GONE);
                holder.item_content_text_tv.setVisibility(View.VISIBLE);
                holder.item_content_text_tv.setText(notedVO.getDescription());
            } else {
                holder.item_content_icon_iv.setVisibility(View.VISIBLE);
                holder.item_content_text_tv.setVisibility(View.GONE);
                LoadLocalImageUtil.getInstance().displayFromSDCard(notedVO.getPath(), holder.item_content_icon_iv, null);
            }

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mDragStartListener.onStartDrag(holder);
                    return false;
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogUtil.showGuidesImageOrNotedEditDialog(context, notedVO.getType(), new DialogUtil.OnCallbackListener() {
                        @Override
                        public void onClick(int whichButton, Object o) {
                            switch (whichButton) {
                                case 0:
                                    Bundle data = new Bundle();
                                    data.putSerializable("notedVO", notedVO);
                                    data.putSerializable("vo", guidesVO);
                                    if (notedVO.getType().equals("txt")) {
                                        BaseActivity.showActivityForResult(context, GuidesAddNotedActivity.class, 301, data);
                                    } else {
                                        BaseActivity.showActivityForResult(context, GuidesImageActivity.class, 302, data);
                                    }
                                    break;
                                case 1:
                                    List<GuidesLocationVO> locationVOList = new ArrayList<GuidesLocationVO>();
                                    for (int i = 0; i < guidesVO.getTrip_days().size(); i++) {
                                        GuidesLocationVO dayVO = new GuidesLocationVO();
                                        dayVO.setIsDate(true);
                                        dayVO.setDateStr(guidesVO.getTrip_days().get(i).getTrip_date());
                                        dayVO.setLocal_id(guidesVO.getTrip_days().get(i).getLocal_id());
                                        locationVOList.add(dayVO);
                                        for (int j = 0; j < guidesVO.getTrip_days().get(i).getLocations().size(); j++) {
                                            // 如果当天有一个没有地理位置的节点，那么删除天节点
                                            if (TextUtils.isEmpty(guidesVO.getTrip_days().get(i).getLocations().get(j).getLocation_name())) {
                                                locationVOList.remove(locationVOList.size()-1);
                                            }
                                            guidesVO.getTrip_days().get(i).getLocations().get(j).setIsDate(false);
                                            guidesVO.getTrip_days().get(i).getLocations().get(j).setDateStr(guidesVO.getTrip_days().get(i).getTrip_date());
                                            locationVOList.add(guidesVO.getTrip_days().get(i).getLocations().get(j));
                                        }
                                    }

                                    DialogUtil.showGuidesLocationListDialog(context, locationVOList, notedVO, new DialogUtil.OnCallbackListener() {
                                        public void onClick(int whichButton, Object o) {
                                            ((GuidesEditActivity)context).updateData();
                                        }
                                    });
                                    break;
                            }
                        }
                    });
                }
            });
        }
    }

    @Override
    public void onItemDismiss(int position) {
//        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        int fromSection = getPositionAtSection(fromPosition);
        int toSection = getPositionAtSection(toPosition);

        if (fromSection == toSection) {

            int fromIndex = getPositionAtSectionIndex(fromPosition);
            int toIndex = getPositionAtSectionIndex(toPosition);

            Log.i("swap", "toSection:" + toSection + " fromIndex:" + fromIndex + " toIndex:" + toIndex + " size:" + itemList.get(toSection).size());

            Collections.swap(itemList.get(toSection), fromIndex, toIndex);
            notifyItemMoved(fromPosition, toPosition);
        } else {
            return false;
        }
        return true;
    }

    private int getPositionAtSection(int position) {
        int section = 0;
        int count = 0;
        for (int i = 0; i < itemList.size(); i++) {
            count += 1 + itemList.get(i).size();
            if (position < count) {
                break;
            } else {
                section++;
            }
        }
        return section;
    }

    private int getPositionAtSectionIndex(int position) {
        int index = 0;
        int count = 1;
        for (int i = 0; i < itemList.size(); i++) {
            if (position < count + itemList.get(i).size()) {
                index = position - count;
                break;
            }
            count += 1 + itemList.get(i).size();
        }
        return index;
    }
}
