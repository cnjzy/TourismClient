package com.tourism.app.db.dao;

import android.content.Context;
import android.text.TextUtils;

import com.tourism.app.util.DateUtils;
import com.tourism.app.vo.GuidesDayVO;
import com.tourism.app.vo.GuidesLocationVO;
import com.tourism.app.vo.GuidesNotedVO;
import com.tourism.app.vo.GuidesVO;

import java.util.List;
import java.util.Map;

/**
 * Created by Jzy on 16/5/10.
 */
public class GuidesNotedDao extends BaseDao<GuidesNotedVO> {
    private GuidesDao guidesDao;
    private GuidesDayDao guidesDayDao;
    private GuidesLocationDao guidesLocationDao;

    public GuidesNotedDao(Context context) {
        super(context, new GuidesNotedVO());

        guidesDao = new GuidesDao(context);
        guidesDayDao = new GuidesDayDao(context);
        guidesLocationDao = new GuidesLocationDao(context);
    }

    /**
     * 添加信息
     *
     * @param vo
     * @return 封面ID
     */
    public int addNoted(GuidesNotedVO vo, GuidesVO guidesVO) {
        if (guidesVO != null) {
            // 获取对应天对象
            GuidesDayVO guidesDayVO = null;
            for (int i = 0; i < guidesVO.getTrip_days().size(); i++) {
                if (guidesVO.getTrip_days().get(i).getTrip_date().equals(vo.getDate())) {
                    guidesDayVO = guidesVO.getTrip_days().get(i);
                    break;
                }
            }
            if (guidesDayVO == null) {
                guidesDayVO = new GuidesDayVO();
                guidesDayVO.setParent_id(guidesVO.getLocal_id());
                guidesDayVO.setTrip_date(vo.getDate());
                int dayId = guidesDayDao.add(guidesDayVO);
                guidesDayVO.setLocal_id(dayId);
                guidesVO.getTrip_days().add(guidesDayVO);
            }

            // 获取地理位置对象
            GuidesLocationVO guidesLocationVO = null;
            for (int i = 0; i < guidesDayVO.getLocations().size(); i++) {
                if (guidesDayVO.getLocations().get(i).getParent_id() == guidesDayVO.getLocal_id()) {
                    guidesLocationVO = guidesDayVO.getLocations().get(i);
                    break;
                }
            }
            if (guidesLocationVO == null) {
                guidesLocationVO = new GuidesLocationVO();
                guidesLocationVO.setParent_id(guidesDayVO.getLocal_id());
                guidesLocationVO.setRank(guidesDayVO.getLocations().size());
                int locationId = guidesLocationDao.add(guidesLocationVO);

                guidesLocationVO.setLocal_id(locationId);
                guidesDayVO.getLocations().add(guidesLocationVO);

                vo.setParent_id(locationId);
            } else {
                vo.setParent_id(guidesLocationVO.getLocal_id());
            }

            // 添加noted
            int notedId = add(vo);
            vo.setLocal_id(notedId);
            guidesLocationVO.getNotes().add(vo);
            return notedId;
        }

        return 0;
    }

    public void addNotedByDate(String dateStr, GuidesVO guidesVO, GuidesNotedVO notedVO) {
        // 查询天节点是否有，没有创建
        GuidesDayVO guidesDayVO = guidesDayDao.getDayByGuidesIdAndDate(guidesVO.getLocal_id(), dateStr);
        if (guidesDayVO == null) {
            guidesDayVO = new GuidesDayVO();
            guidesDayVO.setTrip_date(dateStr);
            guidesDayVO.setParent_id(guidesVO.getLocal_id());
            guidesDayVO.setLocal_id(guidesDayDao.add(guidesDayVO));
        }

        // 创建新的地点节点
        GuidesLocationVO locationVO = new GuidesLocationVO();
        locationVO.setParent_id(guidesDayVO.getLocal_id());
        locationVO.setRank(0);
        locationVO.setLocation_name("");
        locationVO.setLocal_id(guidesLocationDao.add(locationVO));

        // 添加
        notedVO.setParent_id(locationVO.getLocal_id());
        add(notedVO);

        // 修改开始时间和结束时间
        if (TextUtils.isEmpty(guidesVO.getStart_date()) ||
                DateUtils.getLongByString(guidesVO.getStart_date(), DateUtils.parsePatterns[1]) > DateUtils.getLongByString(notedVO.getDate(), DateUtils.parsePatterns[1])) {
            guidesVO.setStart_date(notedVO.getDate());
        }
        if (TextUtils.isEmpty(guidesVO.getEnd_date()) ||
                DateUtils.getLongByString(guidesVO.getEnd_date(), DateUtils.parsePatterns[1]) < DateUtils.getLongByString(notedVO.getDate(), DateUtils.parsePatterns[1])) {
            guidesVO.setEnd_date(notedVO.getDate());
        }
        guidesDao.update(guidesVO);
    }

    public void delete(GuidesNotedVO notedVO) {

        GuidesLocationVO preLocationVO = guidesLocationDao.getById(notedVO.getParent_id());
        if (preLocationVO.getNotes().size() == 0) {
            guidesLocationDao.deleteById(preLocationVO.getLocal_id());
        }

        GuidesDayVO preDayVO = guidesDayDao.getById(preLocationVO.getParent_id());
        if (preDayVO.getLocations().size() == 0) {
            guidesDayDao.deleteById(preDayVO.getLocal_id());
        }

        deleteById(notedVO.getLocal_id(), notedVO.getServer_id(), "note");
    }

    public List<GuidesNotedVO> getByParams(Map<String, Object> params) {
        return super.getByParams(params, "rank", true);
    }

    public void deleteById(int id) {
        super.deleteById(id, getById(id).getServer_id(), "note");
    }
}
