package com.liepin.macaw.utils;

import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.Policy;

public class ModelUtils {

    public static Policy newPolicy(String name, int size, Object param, boolean isDefault){
        Policy policy = new Policy();
        policy.setName(name);
        policy.setSize(size);
        policy.setParam(param);
        policy.setDefault(isDefault);
        return policy;
    }

    public static EstimateParam newParam(String policyName, long success, long total) {
        EstimateParam param = new EstimateParam();
        param.setPolicyName(policyName);
        param.setSuccesses(success);
        param.setTotal(total);
        return param;
    }
}
