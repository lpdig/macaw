package com.liepin.macaw.picker;

import com.google.common.collect.Maps;
import com.liepin.macaw.estimator.AbstractPolicyEstimator;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * bandit实验选择器的抽象类
 *
 * 基于以往的实验参数,选取的beta随机数最大的实验
 * Created by zhangpeng on 2017/9/26.
 */
public abstract class AbstractBanditPicker implements IDataPicker<Policy>{
    private static final Logger logger = LoggerFactory.getLogger(AbstractBanditPicker.class);
    Map<String, AbstractPolicyEstimator> estimatorMap = Maps.newHashMap();
    protected EstimateParamSet paramSet;

    public AbstractBanditPicker(List<Policy> policies, EstimateParamSet paramSet) throws ConfigException {
        this.paramSet = paramSet;
        for (Policy policy : policies) {
            addObject(policy);
        }
    }

    /**
     * 添加备选策略
     * @param obj 备选策略
     * */
    protected void addObject(Policy obj) throws ConfigException{
        if (!estimatorMap.containsKey(obj.getName())) {
            estimatorMap.put(obj.getName(), newEstimator(obj));
        } else {
            estimatorMap.get(obj.getName()).setPolicy(obj);
        }
    }

    @Override
    public Policy pick(Object code) {
        double currentValue = -1;
        Policy policy = null;
        for (AbstractPolicyEstimator estimator : estimatorMap.values()){
            double newVal = estimator.estimate();
            logger.debug("Policy:" + estimator.getPolicy().getName() + ", score:" + newVal);
            if (newVal > currentValue){
                currentValue = newVal;
                policy = estimator.getPolicy();
            }
        }
        return policy;
    }

    abstract AbstractPolicyEstimator newEstimator(Policy policy);

}
