package com.liepin.macaw;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.liepin.macaw.model.ABPolicyModel;
import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.Policy;
import com.liepin.macaw.utils.PolicyCounter;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 主要方法的测试类
 *
 */
public class ExperimentAppTest {

    @Test
    public void testHashApp(){
        try {
            ExperimentApp app = loadExperimentApp("test_hash_domain.json", null);
            ABPolicyModel model = app.getPolicy("1");
            Assert.assertTrue("key ：1，获取到的为E1", "E1".equals(model.getPolicy("L1").getName()));
            Map<String, Integer> policyCounter = Maps.newHashMap();
            Map<String, Integer> groupCounter = Maps.newHashMap();
            for (int i = 0; i < 10000; i++) {
                model = app.getPolicy(String.valueOf(i));
                Policy l1Policy = model.getPolicy("L1");
                Policy l2Policy = model.getPolicy("L2");
                if (policyCounter.containsKey(l1Policy.getName())) {
                    policyCounter.put(l1Policy.getName(), policyCounter.get(l1Policy.getName()) + 1);
                } else {
                    policyCounter.put(l1Policy.getName(), 1);
                }
                String groupKey = l1Policy.getName() + "_" + l2Policy.getName();
                if (groupCounter.containsKey(groupKey)) {
                    groupCounter.put(groupKey, groupCounter.get(groupKey) + 1);
                } else {
                    groupCounter.put(groupKey, 1);
                }
            }
            System.out.println(JSONObject.toJSONString(policyCounter));
            System.out.println(JSONObject.toJSONString(groupCounter));
            Assert.assertTrue("E1策略超出范围！", policyCounter.get("E1") == 3000);
            Assert.assertTrue("E2策略超出范围！", compareValue(policyCounter.get("E2"), 2100, 0.05));
            Assert.assertTrue("E3策略超出范围！", compareValue(policyCounter.get("E3"), 3500, 0.05));
            double f2_e2_e3 = 1.0 * groupCounter.get("E2_F2") / groupCounter.get("E3_F2");
            double f3_e2_e3 = 1.0 * groupCounter.get("E2_F3") / groupCounter.get("E3_F3");
            System.out.println(f2_e2_e3);
            System.out.println(f3_e2_e3);
            Assert.assertFalse("E1不应该存在E1_F1以外的组合", groupCounter.containsKey("E1_F2") || groupCounter.containsKey("E1_F3"));
            Assert.assertTrue(compareValue(f2_e2_e3, 0.6, 0.05));
            Assert.assertTrue(compareValue(f3_e2_e3, 0.6, 0.05));


        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue(e.getMessage(), false);

        }
    }

    @Test
    public void testThompsonApp(){
        try {
            ExperimentApp app = loadExperimentApp("test_thompson_domain.json", Lists.<String>newArrayList("L1", "L2"));
            PolicyCounter policyCounter = countPolicies(app);
            Assert.assertFalse("L1层不应该分配到策略E3", policyCounter.getPolicyCount("E3") > 0);
            double e1_e2 = 1.0 * policyCounter.getPolicyCount("E1") / policyCounter.getPolicyCount("E2");
            Assert.assertTrue("L1层E1和E2策略应该分配近似的次数", compareValue(e1_e2, 1.0, 0.05));
            Assert.assertTrue("L2层次数 F2  > F1",
                    policyCounter.getPolicyCount("F2") > policyCounter.getPolicyCount("F1"));
            Assert.assertTrue("L2层次数 F2  近似 F3",
                    compareValue(policyCounter.getPolicyCount("F3") , policyCounter.getPolicyCount("F2"), 0.05));

            // 实验策略更新测试
            EstimateParam param = new EstimateParam();
            param.setSuccesses(100);
            param.setPolicyName("F3");
            app.increaseParam(param);
            policyCounter = countPolicies(app);
            Assert.assertTrue("修改参数后 L2层次数 F3大于 F2",
                    policyCounter.getPolicyCount("F3") > policyCounter.getPolicyCount("F2"));

        } catch (Exception e) {
//            Assert.assertTrue(e.getMessage(), false);
            e.printStackTrace();
        }
    }


    @Test
    public void testUCBApp(){
        try {
            ExperimentApp app = loadExperimentApp("test_ucb_domain.json", Lists.<String>newArrayList("L1", "L2"));
            PolicyCounter policyCounter = countPolicies(app);
            Assert.assertEquals(Integer.valueOf(policyCounter.getPolicyCount("F1")), Integer.valueOf(10000));
            Assert.assertEquals(Integer.valueOf(policyCounter.getPolicyCount("E1")), Integer.valueOf(10000));
            app.putEstimateParams(getParams("L1_update"));
            app.putEstimateParams(getParams("L2_update"));
            policyCounter = countPolicies(app);
            Assert.assertEquals(Integer.valueOf(policyCounter.getPolicyCount("F2")), Integer.valueOf(10000));
            Assert.assertEquals(Integer.valueOf(policyCounter.getPolicyCount("E3")), Integer.valueOf(10000));
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
    }

    @Test
    public void testUpdateConfig(){
        try {
            ExperimentApp app = loadExperimentApp("test_hash_domain.json", Lists.<String>newArrayList("L1", "L2"));
            File file = new File(getClass().getClassLoader().getResource("test_hash_domain_update.json").toURI());
            String configStr = Files.toString(file, Charsets.UTF_8);
            ExperimentApp.Builder builder = JSONObject.parseObject(configStr, ExperimentApp.Builder.class);
            app.putDomains(builder.domains);
            app.putLayers(builder.layers);
            app.setTotalSize(builder.totalSize);
            app.init();
            PolicyCounter counter = countPolicies(app);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
    }

    private PolicyCounter countPolicies(ExperimentApp app){

        PolicyCounter policyCounter = new PolicyCounter();
        for (int i = 0; i < 10000; i++) {
            ABPolicyModel model = app.getPolicy(String.valueOf(i));
            Policy l1Policy = model.getPolicy("L1");
            Policy l2Policy = model.getPolicy("L2");
            policyCounter.addPolicy(l1Policy);
            policyCounter.addPolicy(l2Policy);
        }
        policyCounter.print();
        return policyCounter;
    }

    private ExperimentApp loadExperimentApp(String configFileName, List<String> layers) throws Exception {
        File file = new File(getClass().getClassLoader().getResource(configFileName).toURI());
        String configStr = Files.toString(file, Charsets.UTF_8);

        ExperimentApp.Builder builder = JSONObject.parseObject(configStr, ExperimentApp.Builder.class);

        ExperimentApp experimentApp = builder.build();
        if (layers != null && !layers.isEmpty()) {
            for (String config : layers) {
                experimentApp.putEstimateParams(getParams(config));
            }
        }
        return experimentApp;
    }

    private List<EstimateParam> getParams(String fileName) throws Exception {
        File l1File = new File(getClass().getClassLoader().getResource("layers/" + fileName + ".json").toURI());
        String l1String = Files.toString(l1File, Charsets.UTF_8);
        return JSONObject.parseArray(l1String, EstimateParam.class);
    }

    private boolean compareValue(int value , int baseValue, double relativeError) {
        double relativeValue = baseValue * relativeError;
        return Math.abs(value - baseValue) < relativeValue;
    }

    private boolean compareValue(double value , double baseValue, double relativeError) {
        double relativeValue = baseValue * relativeError;
        return Math.abs(value - baseValue) < relativeValue;
    }

    private void print(String str) {
        System.out.println(str);
    }
}