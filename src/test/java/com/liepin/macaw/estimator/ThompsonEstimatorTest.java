package com.liepin.macaw.estimator;

import cern.jet.random.engine.RandomEngine;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;
import org.junit.Test;

import java.util.List;

public class ThompsonEstimatorTest {

    @Test
    public void testThompsonEstimator(){
        try {
            String pname = "p1";
            Policy policy = new Policy();
            policy.setName(pname);
            RandomEngine engine = RandomEngine.makeDefault();
            EstimateParam param1 = new EstimateParam();
            param1.setSuccesses(500);
            param1.setTotal(1000);
            param1.setPolicyName(pname);
            ThompsonEstimator estimator1 = new ThompsonEstimator(engine, policy, new EstimateParamSet(param1));
            EstimateParam param2 = new EstimateParam();
            param2.setSuccesses(6000);
            param2.setTotal(10000);
            param2.setPolicyName(pname);
            ThompsonEstimator estimator2 = new ThompsonEstimator(engine, policy, new EstimateParamSet(param2));
            EstimateParam param3 = new EstimateParam();
            param3.setSuccesses(600);
            param3.setTotal(1000);
            param3.setPolicyName(pname);
            ThompsonEstimator estimator3 = new ThompsonEstimator(engine, policy, new EstimateParamSet(param3));
            List<Double> results1 = Lists.newArrayList();
            List<Double> results2 = Lists.newArrayList();
            List<Double> results3 = Lists.newArrayList();
            for (int i = 0; i < 1000; i++) {
                results1.add(estimator1.estimate());
                results2.add(estimator2.estimate());
                results3.add(estimator3.estimate());
            }
            System.out.println(JSONObject.toJSONString(results1));
            System.out.println(JSONObject.toJSONString(results2));
            System.out.println(JSONObject.toJSONString(results3));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
