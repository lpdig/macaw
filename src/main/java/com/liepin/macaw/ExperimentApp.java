package com.liepin.macaw;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liepin.macaw.enums.RuleStrategyEnum;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.*;
import com.liepin.macaw.picker.DataPickers;
import com.liepin.macaw.picker.ExpressionBasedPicker;
import com.liepin.macaw.picker.HashPicker;

import java.util.List;
import java.util.Map;

/**
 * 整个包的入口类，提供初始化配置、查询指定key策略等功能
 * Created by zhangpeng on 2017/5/16.
 */
public class ExperimentApp {

    private String applicationId;
    private Integer totalSize;
    private ExpressionBasedPicker<DomainAgent> ruleBasedPicker;
    private HashPicker<DomainAgent> domainHashPicker;
    private Map<String, LayerAgent> layerMap = Maps.newHashMap();
    private Map<String, DomainAgent> domainMap = Maps.newHashMap();
    private EstimateParamSet params = new EstimateParamSet();
    private AudienceDefinition audienceDefinition;

    protected ExperimentApp(Builder builder) throws ConfigException {
        setApplicationId(builder.applicationId);
        setTotalSize(builder.totalSize);
        setAudienceDefinition(builder.audienceFeatures);
        putDomains(builder.domains);
        putLayers(builder.layers);
        putEstimateParams(builder.estimateParams);
        init();
    }

    /**
     * 设置实验ID
     * @param applicationId
     * */
    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    /**
     * 批量更新实验的域配置
     * @param domains
     * */
    public void putDomains(List<Domain> domains) throws ConfigException {
        domainMap = Maps.newHashMap();
        for (Domain domain : domains) {
            putDomain(domain);
        }
    }

    /**
     * 更新实验的域配置
     * @param domain
     * */
    public void putDomain(Domain domain) throws ConfigException {
        if (domain.isDefault()) {
            domain.setSize(-1);
        }
        domainMap.put(domain.getDomainId(), new DomainAgent(domain, audienceDefinition, params));
    }

    /**
     * 批量更新实验的层配置
     * @param layers
     * */
    public void putLayers(List<Layer> layers) {
        layerMap = Maps.newHashMap();
        for (Layer layer : layers) {
            putLayer(layer);
        }
    }

    /**
     * 更新实验的层配置
     * @param layer
     * */
    public void putLayer(Layer layer) {
        LayerAgent decorator = new LayerAgent(layer);
        layerMap.put(layer.getLayerId(), decorator);
    }

    /**
     * 批量设置（更新）实验评估参数
     * @param estimateParams key 层ID， value 本层所有实验策略的评估参数
     * */
    public void putEstimateParams(List<EstimateParam> estimateParams) throws ConfigException {
        if (estimateParams == null) return;
        for (EstimateParam estimateParam : estimateParams) {
            params.putParam(estimateParam);
        }
    }

    public void putEstimateParam(EstimateParam param) {
        params.putParam(param);
    }

    public void increaseParam(EstimateParam paramIncr) {
        params.paramIncrease(paramIncr);
    }

    /**
     *  初始化层， 设置各个层的参数
     *  初始化domainHashPicker
     *  并将每层的实验策略按照domain分组
     *
     * */
    public void init() throws ConfigException {
        int size = totalSize == null ? 100 : totalSize <= 0 ? 100 : totalSize;

        List<DomainAgent> ruleDomains = Lists.newArrayList();
        List<DomainAgent> hashDomains = Lists.newArrayList();
        for (DomainAgent domain : domainMap.values()) {
            for (LayerAgent layer : layerMap.values()) {
                domain.addLayer(layer);
            }
            List<RuleStrategyEnum> strategyEnums = domain.getRuleStrategy();
            if (strategyEnums.contains(RuleStrategyEnum.Hash)) {
                hashDomains.add(domain);
            }
            if (strategyEnums.contains(RuleStrategyEnum.ExpressionBase)) {
                ruleDomains.add(domain);
            }
        }
        domainHashPicker = DataPickers.newHashPicker(size, applicationId, hashDomains, audienceDefinition);
        ruleBasedPicker = new ExpressionBasedPicker<>(ruleDomains);
        // 先初始化domainHashPicker 然后初始化各个domain
        for (DomainAgent domain : domainMap.values()) {
            domain.init();
        }
    }

    /**
     * 根据传入的key获得所有层的实验策略
     * @param audience
     * @return ABPolicyModel 包含所有层已选取的实验策略
     *
     * */
    public ABPolicyModel getPolicy(Object audience) {
        ABPolicyModel abPolicy = new ABPolicyModel();

        DomainAgent userDomain = ruleBasedPicker.pick(audience);
        if (userDomain == null){
            userDomain = domainHashPicker.pick(audience);
        }
        abPolicy.setDomainId(userDomain.getDomainId());
        abPolicy.setDomainType(userDomain.getDomainType());
        for (String layerId : userDomain.getLayers()) {
            abPolicy.getLayerPolicy().put(layerId, userDomain.pickPolicy(audience, layerId));
        }
        return abPolicy;
    }

    public Policy getPolicy(String layerId, Object audience) {
        DomainAgent userDomain = ruleBasedPicker.pick(audience);
        if (userDomain == null){
            userDomain = domainHashPicker.pick(audience);
        }
        return userDomain.pickPolicy(audience, layerId);
    }

    public String getApplicationId() {
        return applicationId;
    }


    public boolean hasLayer(String layerId) {
        return layerMap.containsKey(layerId);
    }

    public LayerAgent getLayer(String layerId) {
        return layerMap.get(layerId);
    }

    public Integer getTotalSize() {
        return totalSize;
    }

    public ExperimentApp setTotalSize(Integer totalSize) {
        this.totalSize = totalSize;
        return this;
    }

    public void setAudienceDefinition(List<AudienceFeature> audienceFeatures) {
        this.audienceDefinition = new AudienceDefinition();
        audienceDefinition.setFeatures(audienceFeatures);
    }

    public static class Builder{
        String applicationId;
        Integer totalSize;
        List<Domain> domains;
        List<Layer> layers;
        List<EstimateParam> estimateParams;
        List<AudienceFeature> audienceFeatures;

        public Builder(){}

        public Builder setApplicationId(String applicationId) {
            this.applicationId = applicationId;
            return this;
        }

        public Builder setTotalSize(Integer totalSize) {
            this.totalSize = totalSize;
            return this;
        }

        public Builder setDomains(List<Domain> domains) {
            this.domains = domains;
            return this;
        }

        public Builder setLayers(List<Layer> layers) {
            this.layers = layers;
            return this;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public Integer getTotalSize() {
            return totalSize;
        }

        public List<Domain> getDomains() {
            return domains;
        }

        public List<Layer> getLayers() {
            return layers;
        }

        public List<EstimateParam> getEstimateParams() {
            return estimateParams;
        }

        public Builder setEstimateParams(List<EstimateParam> estimateParams) {
            this.estimateParams = estimateParams;
            return this;
        }

        public List<AudienceFeature> getAudienceFeatures() {
            return audienceFeatures;
        }

        public Builder setAudienceFeatures(List<AudienceFeature> audienceFeatures) {
            this.audienceFeatures = audienceFeatures;
            return this;
        }

        public ExperimentApp build() throws ConfigException {
            return new ExperimentApp(this);
        }

    }

}

