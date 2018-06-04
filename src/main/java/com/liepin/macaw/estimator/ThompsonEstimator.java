package com.liepin.macaw.estimator;

import cern.jet.random.Beta;
import cern.jet.random.engine.RandomEngine;
import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;

/**
 * Thompson Sampling算法实现的实验策略评估
 * Created by zhangpeng on 2017/12/18.
 */
public class ThompsonEstimator extends AbstractPolicyEstimator {
    private RandomEngine randomEngine;

    public ThompsonEstimator(RandomEngine randomEngine, Policy policy, EstimateParamSet paramSet) {
        super(policy, paramSet);
        this.randomEngine = randomEngine;
    }

    @Override
    public double estimate() {
        long alpha = 1;
        long beta = 1;
        EstimateParam param = getParam();
        if (param != null) {
            alpha += param.getSuccesses();
            beta += param.getTotal() - param.getSuccesses();
        }
        Beta betaRandom = new Beta(alpha, beta, randomEngine);
        return betaRandom.nextDouble();
    }
}
