package com.liepin.macaw.estimator;

import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by zhangpeng on 2017/12/21.
 */
public class UCBEstimatorTest {
    private static final Logger logger = Logger.getLogger(UCBEstimatorTest.class);

    @Before
    public void setUp(){
    }

    @Test
    public void testLinUCBEstimator(){
        String pname = "P1";
        Policy policy = new Policy();
        policy.setName(pname);
        EstimateParam param = new EstimateParam();
        param.setPolicyName(pname);
        param.setTotal(100);
        param.setSuccesses(10);
        EstimateParamSet paramSet = new EstimateParamSet();
        paramSet.putParam(param);
        UCBEstimator estimator = new UCBEstimator(policy, paramSet);
        double est1 = estimator.estimate();
        logger.debug(est1);
        param.setSuccesses(80);
        double est2 = estimator.estimate();
        logger.debug(est2);
        assert est1 < est2;
        param.setSuccesses(85);
        double est3 = estimator.estimate();
        logger.debug(est3);
        assert est2 < est3;

        param.setTotal(10);
        param.setSuccesses(8);
        double est4 = estimator.estimate();
        logger.debug(est4);
        assert est2 < est4;
    }
}
