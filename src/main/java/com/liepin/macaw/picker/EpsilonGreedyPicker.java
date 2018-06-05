package com.liepin.macaw.picker;

import com.liepin.macaw.estimator.AbstractPolicyEstimator;
import com.liepin.macaw.estimator.EpsilonGreedyEstimator;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;

import java.util.List;
import java.util.Random;

public class EpsilonGreedyPicker extends AbstractBanditPicker {


    double epsilon;

    Random random = new Random();
    List<Policy> policies;
    public EpsilonGreedyPicker(List<Policy> policies, double epsilon, EstimateParamSet paramSet) throws ConfigException {
        super(policies, paramSet);
        this.epsilon = epsilon;
        this.policies = policies;
    }



    @Override
    public Policy pick(Object code) {
        double randVal = random.nextDouble();
        if (randVal < epsilon) {
            int index = Math.abs(random.nextInt()) % policies.size();
            return policies.get(index);
        }
        return super.pick(code);
    }

    @Override
    AbstractPolicyEstimator newEstimator(Policy policy) {
        return new EpsilonGreedyEstimator(policy, paramSet);
    }
}
