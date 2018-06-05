package com.liepin.macaw.estimator;


import com.liepin.macaw.model.EstimateParam;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UCB算法实现的实验策略评估类
 * Created by zhangpeng on 2017/12/18.
 */
public class UCBEstimator extends AbstractPolicyEstimator {

    private static Logger logger = LoggerFactory.getLogger(UCBEstimator.class);

    public UCBEstimator(Policy policy, EstimateParamSet paramSet) {
        super(policy, paramSet);
    }

    @Override
    public double estimate() {
        EstimateParam param = getParam();
        if (param == null) {
            logger.warn("实验策略：" +  getPolicy().getName() + "没有配置评估参数。");
            return 1;
        }

        double mean = new Double(param.getSuccesses()) / param.getTotal();
        double variance = (mean - mean * mean) / param.getTotal();
        logger.debug("mean:" + mean + ", variance:" + variance);
        return mean + 1.96 * Math.sqrt(variance);
    }

}
