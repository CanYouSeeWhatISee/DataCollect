package com.turingcat.datacollect.Service.impl;

import com.alibaba.fastjson.JSONObject;
import com.turingcat.datacollect.Service.DataCollectService;
import com.turingcat.datacollect.Util.JedisUtil;
import com.turingcat.datacollect.Util.Profile;
import com.turingcat.datacollect.Util.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DataCollectServiceImpl implements DataCollectService {
    /**
     * 亮度
     */
    private HashMap<String, Map> luminousHashMap = new HashMap();
    /**
     * 湿度
     */
    private HashMap<String, Map> moistureHashMap = new HashMap();
    /**
     * 分贝
     */

    private HashMap<String, Map> noiseHashMap = new HashMap();
    /**
     * 温度
     */
    private HashMap<String, Map> tempratureHashMap = new HashMap();
    /**
     * 五日平均数
     */
    private static HashMap<String, Object> matchHashMap = new HashMap<>();

    /**
     * 房间当前情景模式
     */
    public final static String CURRENT_PROFILE = "currentProfile:";

    private static String oneDay = null;

    private static int dayStep = 1;

    @Autowired
    private RedisConfig redisConfig;

    @Override
    public void collect() {
        String data;
        Integer i;
        Integer step = getStep();
        getday(step);
        for (i = 19; i <= 28; i++) {
            JedisUtil jedisUtil = new JedisUtil(redisConfig.getRedisIp(), redisConfig.getRedisPort(), i, redisConfig.getRedisPassword());
            Set<String> group = jedisUtil.keys("*");
            if (group != null && group.size() > 0) {
                for (String str : group) {
                    if (!str.isEmpty()) {
                        HashMap<String, Object> temMap = new HashMap<>();
                        HashMap<Integer, Object> dayMap = new HashMap<>();
                        HashMap<Integer, Object> stepMap = new HashMap<>();
                        data = jedisUtil.get(str);
                        if (data != null && !data.equals("0")) {
                            String[] json = data.split(",");
                            String ctrId = json[2];
                            Integer roomId = Integer.valueOf(json[5]);
                            String templateId = json[9];
                            Integer factoryId = Integer.valueOf(json[0]);
                            switch (factoryId) {
                                case 2501:
                                    if (luminousHashMap.containsKey(ctrId)) {
                                        temMap = (HashMap<String, Object>) luminousHashMap.get(ctrId);
                                    }
                                    if (temMap.containsKey(templateId + "_" + roomId)) {
                                        dayMap = (HashMap<Integer, Object>) temMap.get(templateId + "_" + roomId);
                                    }
                                    if (dayMap.containsKey(dayStep)) {
                                        stepMap = (HashMap<Integer, Object>) dayMap.get(dayStep);
                                    }
                                    stepMap.put(step, data);
                                    dayMap.put(dayStep, stepMap);
                                    temMap.put(templateId + "_" + roomId, dayMap);
                                    luminousHashMap.put(ctrId, temMap);

                                    System.out.println("2501---------------" + luminousHashMap);
                                    break;
                                case 2504:
                                    if (moistureHashMap.containsKey(ctrId)) {
                                        temMap = (HashMap<String, Object>) moistureHashMap.get(ctrId);
                                    }
                                    if (temMap.containsKey(templateId + "_" + roomId)) {
                                        dayMap = (HashMap<Integer, Object>) temMap.get(templateId + "_" + roomId);
                                    }
                                    if (dayMap.containsKey(dayStep)) {
                                        stepMap = (HashMap<Integer, Object>) dayMap.get(dayStep);
                                    }
                                    stepMap.put(step, data);
                                    dayMap.put(dayStep, stepMap);
                                    temMap.put(templateId + "_" + roomId, dayMap);
                                    moistureHashMap.put(ctrId, temMap);

                                    System.out.println("2504---------------" + moistureHashMap);
                                    break;
                                case 2506:
                                    if (noiseHashMap.containsKey(ctrId)) {
                                        temMap = (HashMap<String, Object>) noiseHashMap.get(ctrId);
                                    }
                                    if (temMap.containsKey(templateId + "_" + roomId)) {
                                        dayMap = (HashMap<Integer, Object>) temMap.get(templateId + "_" + roomId);
                                    }
                                    if (dayMap.containsKey(dayStep)) {
                                        stepMap = (HashMap<Integer, Object>) dayMap.get(dayStep);
                                    }
                                    stepMap.put(step, data);
                                    dayMap.put(dayStep, stepMap);
                                    temMap.put(templateId + "_" + roomId, dayMap);
                                    noiseHashMap.put(ctrId, temMap);

                                    System.out.println("2506---------------" + noiseHashMap);
                                    break;
                                case 2505:
                                    if (tempratureHashMap.containsKey(ctrId)) {
                                        temMap = (HashMap<String, Object>) tempratureHashMap.get(ctrId);
                                    }
                                    if (temMap.containsKey(templateId + "_" + roomId)) {
                                        dayMap = (HashMap<Integer, Object>) temMap.get(templateId + "_" + roomId);
                                    }
                                    if (dayMap.containsKey(dayStep)) {
                                        stepMap = (HashMap<Integer, Object>) dayMap.get(dayStep);
                                    }
                                    stepMap.put(step, data);
                                    dayMap.put(dayStep, stepMap);
                                    temMap.put(templateId + "_" + roomId, dayMap);
                                    tempratureHashMap.put(ctrId, temMap);

                                    System.out.println("2505---------------" + tempratureHashMap);
                                    break;
                                default:
                                    break;
                            }
                            deleteData(ctrId,step,templateId + "_" + roomId,factoryId);
                        }
                    }
                }
            }
        }
    }


    public void getday(Integer step) {
        if (step == 1) {
            dayStep++;
        }
    }

    @Override
    public void matchData(/*String ctrlId,String modeRoom,Date time*/) {
        ArrayList<Integer> matchList = new ArrayList<>();
        Integer total = 0;
        HashMap<String, Object> cmap = new HashMap<>();
        HashMap<String, Object> dmap = new HashMap<>();
        Integer step = 37;
        cmap = (HashMap<String, Object>) tempratureHashMap.get("10433").get("4_1000");
        Integer daystep = cmap.size();
        if (daystep >= 5) {
            for (int i = daystep; i > 0; i--) {
                dmap = (HashMap<String, Object>) cmap.get(i);
                String[] call = dmap.get(37).toString().split(",");
                if (call != null) {
                    matchList.add(Integer.valueOf(call[7]));
                }
            }
            for (int i = 0; i < matchList.size(); i++) {
                total += matchList.get(i);
            }
            Integer avg = total / matchList.size();
        }
    }

    public void deleteData(String ctrlId, Integer step, String templateId,Integer factoryId) {
        ArrayList<Integer> delList = new ArrayList<>();
        HashMap<String, Map> cmap = new HashMap<>();
        HashMap<String, Object> dmap = new HashMap<>();
        if(factoryId == 2501) {
            cmap = (HashMap<String, Map>) luminousHashMap.get(ctrlId).get(templateId);
        }else if(factoryId == 2504){
            cmap = (HashMap<String, Map>) moistureHashMap.get(ctrlId).get(templateId);
        }else if(factoryId == 2505){
            cmap = (HashMap<String, Map>) tempratureHashMap.get(ctrlId).get(templateId);
        }else if(factoryId ==2506){
            cmap = (HashMap<String, Map>) noiseHashMap.get(ctrlId).get(templateId);
        }
        Integer daystep = cmap.size();
        if (daystep > 5) {
            for (int i = daystep; i > 0; i--) {
                dmap = (HashMap<String, Object>) cmap.get(i);
                if (dmap.get(step) != null) {
                    {
                        delList.add(i);
                    }
                }
                if (delList.size() > 5) {
                    Integer minDay = Collections.min(delList);
                    System.out.println("删除"+cmap.get(minDay).get(step));
                    cmap.get(minDay).remove(step);
                    System.out.println();
                    if (dmap == null) {
                        cmap.remove(minDay);
                    }
                }
            }
        }
    }

    public Integer getStep() {
        Calendar cal = Calendar.getInstance();
        Integer hour = cal.get(Calendar.HOUR_OF_DAY);
        Integer minute = cal.get(Calendar.MINUTE);
        Integer step = 0;
        if (minute > 30) {
            switch (hour) {
                case 0:
                    step = 2;
                    break;
                case 1:
                    step = 4;
                    break;
                case 2:
                    step = 6;
                    break;
                case 3:
                    step = 8;
                    break;
                case 4:
                    step = 10;
                    break;
                case 5:
                    step = 12;
                    break;
                case 6:
                    step = 14;
                    break;
                case 7:
                    step = 16;
                    break;
                case 8:
                    step = 18;
                    break;
                case 9:
                    step = 20;
                    break;
                case 10:
                    step = 22;
                    break;
                case 11:
                    step = 24;
                    break;
                case 12:
                    step = 26;
                    break;
                case 13:
                    step = 28;
                    break;
                case 14:
                    step = 30;
                    break;
                case 15:
                    step = 32;
                    break;
                case 16:
                    step = 34;
                    break;
                case 17:
                    step = 36;
                    break;
                case 18:
                    step = 38;
                    break;
                case 19:
                    step = 40;
                    break;
                case 20:
                    step = 42;
                    break;
                case 21:
                    step = 44;
                    break;
                case 22:
                    step = 46;
                    break;
                case 23:
                    step = 48;
                    break;
            }
        } else {
            switch (hour) {
                case 0:
                    step = 1;
                    break;
                case 1:
                    step = 3;
                    break;
                case 2:
                    step = 5;
                    break;
                case 3:
                    step = 7;
                    break;
                case 4:
                    step = 9;
                    break;
                case 5:
                    step = 11;
                    break;
                case 6:
                    step = 13;
                    break;
                case 7:
                    step = 15;
                    break;
                case 8:
                    step = 17;
                    break;
                case 9:
                    step = 19;
                    break;
                case 10:
                    step = 21;
                    break;
                case 11:
                    step = 23;
                    break;
                case 12:
                    step = 25;
                    break;
                case 13:
                    step = 27;
                    break;
                case 14:
                    step = 29;
                    break;
                case 15:
                    step = 31;
                    break;
                case 16:
                    step = 33;
                    break;
                case 17:
                    step = 35;
                    break;
                case 18:
                    step = 37;
                    break;
                case 19:
                    step = 39;
                    break;
                case 20:
                    step = 41;
                    break;
                case 21:
                    step = 43;
                    break;
                case 22:
                    step = 45;
                    break;
                case 23:
                    step = 47;
                    break;
            }
        }
        return step;
    }

}
