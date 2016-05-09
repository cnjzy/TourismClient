package com.tourism.app.procotol;

import java.util.List;
import java.util.Map;

import org.json.simple.BaseJson;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.gson.reflect.TypeToken;
import com.tourism.app.vo.BaseVO;

public class BaseResponseMessage extends ResponseMessage {
    private List resultList;
    private BaseVO result;

    public List getResultList() {
        return resultList;
    }

    public void setResultList(List resultList) {
        this.resultList = resultList;
    }

    public BaseVO getResult() {
        return result;
    }

    public void setResult(BaseVO result) {
        this.result = result;
    }
    
    protected void parseBody(JSONObject obj, TypeToken<?> type, TypeToken<?> typeList)
            throws ParseException {
        if (obj.containsKey("data")) {
            String dataStr = obj.get("data").toString();
            String strFirst = dataStr.substring(0, 1);
            if (strFirst.equals("[")) {
                resultList = (List) BaseJson.parser(typeList, dataStr);
            }else{
                result = (BaseVO) BaseJson.parser(type, dataStr);
            }
        }

    }

    // 当map中有数据时，如果为未知类型实现此方法
    protected Map<String, ?> parseMap(String resultMap, TypeToken<?> typeMap) {
        return (Map<String, ?>) BaseJson.parser(typeMap, resultMap);
    }
}
