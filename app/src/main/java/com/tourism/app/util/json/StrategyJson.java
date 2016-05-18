package com.tourism.app.util.json;

import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesDeleteVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;


/**
 * Created by Jzy on 16/5/11.
 */
public class StrategyJson {

    /**
     * 获取json
     * @param vo
     * @return
     */
    public static JSONObject getGuiesJson(GuidesVO vo){
        JSONObject root = new JSONObject();
        try {
            root.put("local_id", vo.getLocal_id());
            root.put("server_id", vo.getServer_id());
            root.put("name", vo.getName());
            root.put("photos_count", vo.getPhotos_count());
            root.put("start_date", vo.getStart_date());
            root.put("end_date", vo.getEnd_date());
            root.put("privacy", vo.getPrivacy());
            root.put("front_cover_photo_id", vo.getFront_cover_photo_id());
            root.put("friend_category_id", vo.getFriend_category_id());

            JSONArray dayArray = new JSONArray();
            for (int i = 0; vo.getTrip_days() != null && i < vo.getTrip_days().size(); i++){
                GuidesDayVO dayVo = vo.getTrip_days().get(i);
                JSONObject dayItem = new JSONObject();
                dayItem.put("trip_date", dayVo.getTrip_date());
                dayItem.put("local_id", dayVo.getLocal_id());
                dayItem.put("server_id", dayVo.getServer_id());
                dayArray.put(dayItem);

                JSONArray locationArray = new JSONArray();
                for (int j = 0; dayVo.getLocations() != null && j < dayVo.getLocations().size(); j++){
                    GuidesLocationVO locationVO = dayVo.getLocations().get(j);
                    JSONObject locationItem = new JSONObject();
                    locationItem.put("local_id", locationVO.getLocal_id());
                    locationItem.put("server_id", locationVO.getServer_id());
                    locationItem.put("location_name", locationVO.getLocation_name());
                    locationItem.put("rank", j);
                    locationArray.put(locationItem);

                    JSONArray notedArray = new JSONArray();
                    for (int m = 0; m < locationVO.getNotes().size(); m++){
                        GuidesNotedVO notedVO = locationVO.getNotes().get(m);
                        JSONObject notedItem = new JSONObject();
                        notedItem.put("local_id", notedVO.getLocal_id());
                        notedItem.put("server_id", notedVO.getServer_id());
                        notedItem.put("rank", m);
                        notedItem.put("type", notedVO.getType());
                        notedItem.put("description", notedVO.getDescription());
                        notedArray.put(notedItem);
                    }
                    locationItem.put("notes", notedArray);
                }
                dayItem.put("locations", locationArray);
            }

            root.put("trip_days", dayArray);

        }catch (Exception e){
            e.printStackTrace();
        }

        return root;
    }


    /**
     * 获取json
     * @param vo
     * @return
     */
    public static JSONArray getDeleteJson(List<GuidesDeleteVO> deleteVO){
        JSONArray root = new JSONArray();
        try {
            // 添加token
            for (int i = 0 ; deleteVO != null && i < deleteVO.size(); i++) {
                JSONObject deleteItem = new JSONObject();
                deleteItem.put("type", deleteVO.get(i).getType());
                deleteItem.put("server_id", deleteVO.get(i).getServer_id());
                root.put(deleteItem);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return root;
    }


}
