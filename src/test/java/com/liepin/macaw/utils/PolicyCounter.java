package com.liepin.macaw.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.liepin.macaw.model.Policy;

import java.util.Map;

public class PolicyCounter {
    Map<String, Integer> policyCounter = Maps.newHashMap();

    public void addPolicy(Policy policy) {
        if (policyCounter.containsKey(policy.getName())) {
            policyCounter.put(policy.getName(), policyCounter.get(policy.getName()) + 1);
        } else {
            policyCounter.put(policy.getName(), 1);
        }
    }

    public void print(){
        System.out.println(JSONObject.toJSONString(policyCounter));
    }

    public int getPolicyCount(String policyName) {
        if (policyCounter.containsKey(policyName))
            return policyCounter.get(policyName);
        return 0;
    }
}
