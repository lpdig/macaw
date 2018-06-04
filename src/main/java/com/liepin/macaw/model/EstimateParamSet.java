package com.liepin.macaw.model;

import com.google.common.collect.Maps;

import java.util.Map;

public class EstimateParamSet {
    private Map<String, EstimateParam> params = Maps.newHashMap();

    public EstimateParamSet(){}

    public EstimateParamSet(EstimateParam... params) {
        for (EstimateParam param : params) {
            putParam(param);
        }
    }

    public void putParam(EstimateParam param) {
        params.put(param.getPolicyName(), param);
    }

    public synchronized void paramIncrease(EstimateParam paramIncr) {
        if (params.containsKey(paramIncr.getPolicyName())) {
            EstimateParam param = params.get(paramIncr.getPolicyName());
            param.setSuccesses(param.getSuccesses() + paramIncr.getSuccesses());
            param.setTotal(param.getTotal() + paramIncr.getTotal());
        } else {
            putParam(paramIncr);
        }
    }

    public EstimateParam getParam(String policyName) {
        return params.get(policyName);
    }
}
