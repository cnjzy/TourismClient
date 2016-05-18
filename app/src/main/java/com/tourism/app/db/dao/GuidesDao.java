package com.tourism.app.db.dao;

import android.content.Context;

import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/9.
 */
public class GuidesDao extends BaseDao<GuidesVO> {

    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;
    private GuidesNotedDao guidesNotedDao;

    public GuidesDao(Context context) {
        super(context, new GuidesVO());
    }

    public List<GuidesVO> getByParams(Map<String, Object> params) {
        return super.getByParams(params, "local_id", false);
    }

    public void deleteById(int id) {
        super.deleteById(id, 0, "");
    }

    public void delete(GuidesVO vo) {

        if (guidesDayDao == null)
            guidesDayDao = new GuidesDayDao(context);
        if (guidesLocationDao == null)
            guidesLocationDao = new GuidesLocationDao(context);
        if (guidesNotedDao == null)
            guidesNotedDao = new GuidesNotedDao(context);

        for (int i = 0; i < vo.getTrip_days().size(); i++) {
            GuidesDayVO dayVO = vo.getTrip_days().get(i);
            for (int j = 0; j < dayVO.getLocations().size(); j++) {
                GuidesLocationVO locationVO = dayVO.getLocations().get(j);
                for (int m = 0; m < locationVO.getNotes().size(); m++) {
                    GuidesNotedVO notedVO = locationVO.getNotes().get(m);
                    guidesNotedDao.deleteById(notedVO.getLocal_id());
                }
                guidesLocationDao.deleteById(locationVO.getLocal_id());
            }
            guidesDayDao.deleteById(dayVO.getLocal_id());
        }
        deleteById(vo.getLocal_id(), vo.getServer_id(), "guide");
    }
}
