package com.liepin.macaw.expression;

import com.liepin.macaw.model.AudienceFeature;

public class FeatureObjAdaptor implements ObjectAdaptor {
    private AudienceFeature feature;

    public FeatureObjAdaptor(AudienceFeature feature) {
        this.feature = feature;
    }

    @Override
    public Object getValue(Object obj) {
        return feature.getValue(obj);
    }
}
