package com.liepin.macaw.picker;

import cern.jet.random.engine.RandomEngine;
import com.liepin.macaw.estimator.AbstractPolicyEstimator;
import com.liepin.macaw.estimator.ThompsonEstimator;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Thompson Sampling算法实现的实验选择器
 *
 * 基于以往的实验记录,选取的beta随机数最大的实验策略
 * Created by zhangpeng on 2017/9/26.
 */
public class ThompsonPicker extends AbstractBanditPicker{
    private static final Logger logger = LoggerFactory.getLogger(ThompsonPicker.class);

    private RandomEngine randomEngine;

    public ThompsonPicker(List<Policy> policies, EstimateParamSet paramSet) throws ConfigException {
        super(policies, paramSet);
    }

    private RandomEngine getRandomEngine(){
        if (randomEngine == null)
            randomEngine = RandomEngine.makeDefault();
        return randomEngine;
    }

    @Override
    AbstractPolicyEstimator newEstimator(Policy policy) {
        return new ThompsonEstimator(getRandomEngine(), policy, paramSet);
    }

}
