package com.liepin.macaw.estimator;

import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;

public class EpsilonGreedyEstimator extends AbstractPolicyEstimator {

    public EpsilonGreedyEstimator(Policy policy, EstimateParamSet paramSet) {
        super(policy, paramSet);
    }

    @Override
    public double estimate() {
        EstimateParam param = getParam();
        if (param == null) return 0.0;
        return 1.0 * param.getSuccesses() / param.getTotal();
    }
}
