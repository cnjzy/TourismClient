package com.tourism.app.db.dao;

import android.content.Context;

import com.tourism.app.vo.GuidesDeleteVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/12.
 */
public class GuidesDeleteDao extends BaseDao<GuidesDeleteVO> {

    public GuidesDeleteDao(Context context) {
        super(context, new GuidesDeleteVO());
    }


    public List<GuidesDeleteVO> getByParams(Map<String, Object> params) {
        return super.getByParams(params, "", true);
    }
}
