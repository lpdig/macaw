package com.liepin.macaw.picker;

import com.liepin.macaw.model.Policy;

/**
 * 固定实验策略的选择器
 * <p>
 * 此选择器每次调用都只会返回固定的实验策略
 * Created by zhangpeng on 2017/10/20.
 */
public class FixPolicyPicker implements IDataPicker<Policy>{

    private Policy policy;

    public FixPolicyPicker(Policy policy) {
        this.policy = policy;
    }

    @Override
    public Policy pick(Object code) {
        return policy;
    }

}
