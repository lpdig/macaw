package com.liepin.macaw.model;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * 受众定义类， 包含测试受众的所有特征
 * */
public class AudienceDefinition {
    private List<AudienceFeature> features;

    public List<AudienceFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<AudienceFeature> features) {
        this.features = features;
    }

    public List<AudienceFeature> getKeyFeature(){
        if (features == null) return null;
        List<AudienceFeature> keyFeatures = Lists.newArrayList();

        for (AudienceFeature feature : features) {
            if (feature.isKeyProperty()) {
                keyFeatures.add(feature);
            }
        }
        return keyFeatures;
    }
}
