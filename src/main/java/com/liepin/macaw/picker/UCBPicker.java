package com.liepin.macaw.picker;

import com.liepin.macaw.estimator.AbstractPolicyEstimator;
import com.liepin.macaw.estimator.UCBEstimator;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;

import java.util.List;

/**
 * UCB算法实现的实验策略选择器
 * 根据以往的实验记录，选择置信区间上限最大的实验策略
 * Created by zhangpeng on 2017/12/18.
 */
public class UCBPicker extends AbstractBanditPicker{

    public UCBPicker(List<Policy> policies, EstimateParamSet paramSet) throws ConfigException {
        super(policies, paramSet);
    }

    @Override
    AbstractPolicyEstimator newEstimator(Policy policy) {
        return new UCBEstimator(policy, paramSet);
    }
}
