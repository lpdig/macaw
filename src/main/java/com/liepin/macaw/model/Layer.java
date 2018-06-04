package com.liepin.macaw.model;

import com.google.common.collect.Lists;

import java.util.List;

public class Layer {
    private String layerId;
    private String description;
    protected List<Policy> policies = Lists.newArrayList();

    public String getLayerId() {
        return layerId;
    }

    public void setLayerId(String layerId) {
        this.layerId = layerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Policy> policies) {
        this.policies = policies;
    }
}
