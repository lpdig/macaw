package com.liepin.macaw.estimator;

import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;
import org.apache.log4j.Logger;
import org.junit.Test;

public class EpsilonEstimatorTest {
    private static final Logger logger = Logger.getLogger(EpsilonEstimatorTest.class);

    @Test
    public void testEpsilonEstimator(){
        String pname = "P1";
        Policy policy = new Policy();
        policy.setName(pname);
        EstimateParam param = new EstimateParam();
        param.setPolicyName(pname);
        param.setTotal(100);
        param.setSuccesses(10);
        EpsilonGreedyEstimator estimator = new EpsilonGreedyEstimator(policy, new EstimateParamSet(param));
        double est1 = estimator.estimate();
        param.setSuccesses(80);
        logger.debug(est1);
        double est2 = estimator.estimate();
        assert est1 < est2;
        param.setSuccesses(85);
        double est3 = estimator.estimate();
        logger.debug(est3);
        assert est2 < est3;

        param.setTotal(10);
        param.setSuccesses(8);
        double est4 = estimator.estimate();
        logger.debug(est4);
        assert est2 == est4;
    }
}
