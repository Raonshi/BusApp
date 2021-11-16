package com.example.springboottest1.controller;


import com.example.springboottest1.data.DataCenter;
import com.example.springboottest1.utils.Ops;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.crypto.Data;

@RestController
public class TestController {

    @RequestMapping(method = RequestMethod.GET, path = "/test")
    public JSONArray test(@RequestParam String deptid, @RequestParam String destid) {
        String startNodeid1 = "CJB271000143";
        //코스모사원 아파트
        String startNodeid2 = "CJB283000207";
        //청주고등학교

        String endNodeid1 = "CJB283000028";
        //사직 사거리
        String endNodeid2 = "CJB283000037";
        //서원 대학교

        //코스모 사원 아파트 - 사직 사거리 : 직통 경로 존재
        //코스모 사원 아파트 - 서원 대학교 : 무조건 환승
        //청주고등학교 - 사직 사거리 : 직통 경로 존재
        //청주고등학교 - 서원 대학교 : 근접위치(꽃다리) 직통경로 존재

        Ops ops = new Ops();

        ops.testCall(deptid, destid);

        return DataCenter.Singleton().finaldirectPathList;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/testcall")
    public JSONArray testcall() {

        JSONArray result = new JSONArray();

        for(int i = 0; i < 10; i++) {
            JSONObject obj = new JSONObject();
            JSONArray tmp = new JSONArray();
            for(int j = 0; j < 5; j++) {
                JSONObject tmpObj = new JSONObject();
                tmpObj.put("tmpidx", String.valueOf(j) + " 번째 인덱스");

                tmp.add(tmpObj);
            }
            obj.put("idx", tmp);


            result.add(obj);
        }


        return result;
    }
}
