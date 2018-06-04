package com.liepin.macaw.model;

/**
 * 实验评估配置类
 *
 * 包含某个实验策略以往的总次数和成功次数
 * Created by zhangpeng on 2017/9/26.
 */
public class EstimateParam {
    private String policyName;
    private long total;
    private long successes;


    public String getPolicyName() {
        return policyName;
    }

    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getSuccesses() {
        return successes;
    }

    public void setSuccesses(long successes) {
        this.successes = successes;
    }

    public static EstimateParam newDefaultParam(){
        EstimateParam param = new EstimateParam();
        param.setSuccesses(0);
        param.setTotal(1);
        return param;
    }
}
