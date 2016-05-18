package com.tourism.app.db.dao;

import android.content.Context;

import com.tourism.app.vo.GuidesDayVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/10.
 */
public class GuidesDayDao extends BaseDao<GuidesDayVO> {
    public GuidesDayDao(Context context) {
        super(context, new GuidesDayVO());
    }


    public GuidesDayVO getDayByGuidesIdAndDate(int id, String dateStr) {
        GuidesDayVO vo = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("parent_id", id);
            params.put("trip_date", dateStr);
            List<GuidesDayVO> list = getByParams(params);
            if (list != null && list.size() > 0){
                vo = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return vo;
    }

    public List<GuidesDayVO> getByParams(Map<String, Object> params) {
        return super.getByParams(params, "trip_date", true);
    }

    public void deleteById(int id) {
        super.deleteById(id, getById(id).getServer_id(), "day");
    }
}
