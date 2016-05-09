package com.tourism.app.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tourism.app.db.DatabaseHelper;
import com.tourism.app.vo.GuidesVO;

/**
 * Created by Jzy on 16/5/9.
 */
public class GuidesDao extends BaseDao<GuidesVO>{


    public GuidesDao(Context context) {
        super(context, new GuidesVO());
    }
}
