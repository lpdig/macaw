package com.liepin.macaw.model;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.liepin.macaw.enums.DomainTypeEnum;

import java.util.Map;

/**
 * AB策略详情
 * Created by lixuancheng(formid) on 2017/5/23.
 */
public class ABPolicyModel {

    static Joiner.MapJoiner joiner = Joiner.on(" & ").withKeyValueSeparator("~");

    private String domainId;
    private DomainTypeEnum domainType;
    private Map<String, Policy> layerPolicy = Maps.newHashMap();

    public Policy getPolicy(String layerId){
        return layerPolicy.get(layerId);
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public DomainTypeEnum getDomainType() {
        return domainType;
    }

    public void setDomainType(DomainTypeEnum domainType) {
        this.domainType = domainType;
    }

    public Map<String, Policy> getLayerPolicy() {
        return layerPolicy;
    }

    public void setLayerPolicy(Map<String, Policy> layerPolicy) {
        this.layerPolicy = layerPolicy;
    }

    @Override
    public String toString() {
        return domainId + ", type=" + domainType.name() + joiner.join(layerPolicy);
    }
}
