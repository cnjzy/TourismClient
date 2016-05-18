package com.tourism.app.db.dao;

import android.content.Context;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;
import com.tourism.app.db.DatabaseHelper;
import com.tourism.app.vo.BaseDBVO;
import com.tourism.app.vo.BaseVO;
import com.tourism.app.vo.GuidesDeleteVO;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/9.
 */
public class BaseDao<T extends BaseVO> {
    protected Context context;
    private Dao<T, Integer> dao;
    private DatabaseHelper helper;
    private T t;

    /**
     * 删除表对象
     */
    private static GuidesDeleteDao guidesDeleteDao;

    public BaseDao(Context context, T t) {
        try {
            this.t = t;
            this.context = context;
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
                BaseDBVO vo = (BaseDBVO) list.get(0);
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
    public void deleteById(int id, int server_id, String type) {
        try {

            if (guidesDeleteDao == null) {
                guidesDeleteDao = new GuidesDeleteDao(context);
            }

            if (server_id > 0 && !TextUtils.isEmpty(type)) {
                GuidesDeleteVO deleteVO = new GuidesDeleteVO();
                deleteVO.setServer_id(server_id);
                deleteVO.setType(type);
                guidesDeleteDao.add(deleteVO);
            }

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
    public void deleteByParams(Map<String, Object> params) {
        try {
            DeleteBuilder<T, Integer> builder = dao.deleteBuilder();
            if (params != null && params.size() > 0) {
                Where<T, Integer> where = builder.where();
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

    public T getById(int id) {
        try {
            return dao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 按照条件查询
     *
     * @param params
     */
    protected List<T> getByParams(Map<String, Object> params, String colName, boolean isAsc) {
        try {
            QueryBuilder<T, Integer> builder = dao.queryBuilder();
            if (params != null && params.size() > 0) {
                Where<T, Integer> where = builder.where();
                Iterator<String> it = params.keySet().iterator();
                int m = 0;
                while (it.hasNext()) {
                    if (m != 0) {
                        where.and();
                    }
                    String key = it.next();
                    where.eq(key, params.get(key));
                    m++;
                }
            }

            if (!TextUtils.isEmpty(colName)) {
                builder.orderBy(colName, isAsc);
            }

            return builder.query();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
