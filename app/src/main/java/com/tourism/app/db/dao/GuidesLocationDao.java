package com.tourism.app.db.dao;

import android.content.Context;

import com.tourism.app.vo.GuidesLocationVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/10.
 */
public class GuidesLocationDao extends BaseDao<GuidesLocationVO>{
    public GuidesLocationDao(Context context) {
        super(context, new GuidesLocationVO());
    }

    public List<GuidesLocationVO> getByParams(Map<String, Object> params) {
        return super.getByParams(params, "rank", true);
    }

    public void deleteById(int id) {
        super.deleteById(id, getById(id).getServer_id(), "location");
    }
}
