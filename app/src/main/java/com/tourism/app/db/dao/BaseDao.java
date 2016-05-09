package com.tourism.app.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.sina.weibo.sdk.api.share.Base;
import com.tourism.app.db.DatabaseHelper;
import com.tourism.app.vo.BaseVO;
import com.tourism.app.vo.GuidesVO;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/9.
 */
public class BaseDao<T extends BaseVO> {
    private Dao<T, Integer> dao;
    private DatabaseHelper helper;
    private T t;

    public BaseDao(Context context, T t) {
        try {
            this.t = t;
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(t.getClass());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取最后一条数据ID
     *
     * @return
     */
    private int getLastId() {
        try {
            List list = dao.queryBuilder().selectColumns("local_id").orderBy("local_id", false).offset(0l).limit(1l).query();
            if (list != null && list.size() > 0) {
                BaseVO vo = (BaseVO) list.get(0);
                return vo.getLocal_id();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 添加
     *
     * @param vo
     */
    public synchronized int add(T vo) {
        try {
            if (dao.create(vo) == 1) {
                return getLastId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 修改
     *
     * @param vo
     */
    public void update(T vo) {
        try {
            dao.update(vo);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(int id) {
        try {
            dao.deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 条件删除
     *
     * @param params
     */
    public void deleteByParams(Map<String, String> params) {
        try {
            DeleteBuilder<T, Integer> builder = dao.deleteBuilder();
            Where<T, Integer> where = builder.where();
            where.eq("1", "1");
            if (params != null && params.size() > 0) {
                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()) {
                    where.and();
                    String key = it.next();
                    where.eq(key, params.get(key));
                }
            }
            builder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public T getById(int id){
        try{
            return dao.queryForId(id);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照条件查询
     *
     * @param params
     */
    public List<T> getByParams(Map<String, String> params) {
        try {
            QueryBuilder<T, Integer> builder = dao.queryBuilder();
            Where<T, Integer> where = builder.where();
            where.eq("1", "1");
            if (params != null && params.size() > 0) {
                Iterator<String> it = params.keySet().iterator();
                while (it.hasNext()) {
                    where.and();
                    String key = it.next();
                    where.eq(key, params.get(key));
                }
            }
            return builder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
