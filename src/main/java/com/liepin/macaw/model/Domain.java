package com.liepin.macaw.model;

import com.liepin.macaw.enums.RuleStrategyEnum;
import com.liepin.macaw.enums.DomainTypeEnum;

import java.util.List;

public class Domain {

    protected String domainId;
    protected DomainTypeEnum domainType;
    protected List<RuleStrategyEnum> ruleStrategy;
    protected String description;
    protected boolean isDefault;
    protected int size;
    protected double epsilon;
    protected String ruleExpression;

    public String getRuleExpression() {
        return ruleExpression;
    }

    public void setRuleExpression(String ruleExpression) {
        this.ruleExpression = ruleExpression;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public List<RuleStrategyEnum> getRuleStrategy() {
        return ruleStrategy;
    }

    public void setRuleStrategy(List<RuleStrategyEnum> ruleStrategy) {
        this.ruleStrategy = ruleStrategy;
    }
}
