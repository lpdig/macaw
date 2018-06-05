package com.liepin.macaw.estimator;

import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;

/**
 * 实验策略评估器抽象类
 *
 * 根据策略以往的表现，给出此策略的一个评估分值
 *
 * Created by zhangpeng on 2017/12/16.
 */
public abstract class AbstractPolicyEstimator {

    private Policy policy;
    protected EstimateParamSet paramSet;

    public AbstractPolicyEstimator(Policy policy, EstimateParamSet paramSet) {
        this.policy = policy;
        this.paramSet = paramSet;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public EstimateParam getParam() {
        return paramSet.getParam(policy.getName());
    }


    public abstract double estimate();
}
