package com.liepin.macaw.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 分层的装饰类
 */
public class LayerAgent {

    private Layer layer;
    private Policy defaultPolicy;

    protected Map<String, List<Policy>> domainPolicyMap = Maps.newHashMap();

    public LayerAgent(Layer layer) {
        this.layer = layer;
        init();
    }

    /**
     * 初始化操作，将本层的所有实验按照domain分组
     *
     * */
    private void init(){
        domainPolicyMap = Maps.newHashMap();
        for (Policy policy : layer.policies) {
            if (policy.isDefault()) {
                defaultPolicy = policy;
                continue;
            }
            Set<String> domainIds = policy.getDomainIds();
            if (domainIds != null && !domainIds.isEmpty()) {
                for (String domainId : domainIds) {
                    if (domainPolicyMap.containsKey(domainId)) {
                        domainPolicyMap.get(domainId).add(policy);
                    } else {
                        domainPolicyMap.put(domainId, Lists.newArrayList(policy));
                    }
                }
            } else {
                if (!domainPolicyMap.containsKey(null)) {
                    domainPolicyMap.put(null, Lists.<Policy>newArrayList(policy));
                } else {
                    domainPolicyMap.get(null).add(policy);
                }
            }
        }
    }

    protected List<Policy> getPoliciesByDomain(DomainAgent domain) {
        List<Policy> policies = Lists.newArrayList();
        List<Policy> dividedPolicies = domainPolicyMap.get(domain.getDomainId());
        if (dividedPolicies != null) {
            policies.addAll(dividedPolicies);
        }
        if (domain.isDefault() && domainPolicyMap.get(null) != null) {
            policies.addAll(domainPolicyMap.get(null));
        }
        if (defaultPolicy != null) {
            policies.add(defaultPolicy);
        }
        return policies;
    }

    public String getLayerId() {
        return layer.getLayerId();
    }

}
