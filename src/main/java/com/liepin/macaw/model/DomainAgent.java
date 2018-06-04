package com.liepin.macaw.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.liepin.macaw.enums.RuleStrategyEnum;
import com.liepin.macaw.enums.DomainTypeEnum;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.expression.Expression;
import com.liepin.macaw.expression.ExpressionParser;
import com.liepin.macaw.picker.IDataPicker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 域（Domain）里包含分配到此域的所有实验策略，以及选择某一层实验的方法。
 * 根据域的类型（domainType）不同, 有不同的选择实验策略的方法
 * Created by zhangpeng on 2017/5/16.
 */
public class DomainAgent implements IHashAble, IExpressionBase {
    private static final Logger logger = LoggerFactory.getLogger(DomainAgent.class);
    private Domain domain;
    private AudienceDefinition audienceDefinition;
    private Map<String, IDataPicker<Policy>> layerDataPicker = Maps.newHashMap();
    private Map<String, List<Policy>> layerPolicyMap = Maps.newHashMap();
    private EstimateParamSet estimateParamSet;
    private Expression expression;

    public DomainAgent(Domain domain, AudienceDefinition audienceDefinition, EstimateParamSet estimateParamSet) throws ConfigException {
        this.domain = domain;
        this.audienceDefinition = audienceDefinition;
        this.estimateParamSet = estimateParamSet;
        if (StringUtils.isNotEmpty(domain.getRuleExpression())) {
            expression = ExpressionParser.parse(domain.getRuleExpression());
        }
    }

    /**
     * 初始化方法
     *
     * 初始化每一层的实验选择器，根据不同的域类型（domainType）生成不同的实验选择器
     * */
    public void init() throws ConfigException {
        for (String layerId : layerPolicyMap.keySet()) {
            layerDataPicker.put(layerId, domain.getDomainType().newPolicyPicker(this, layerId));
        }
    }

    public EstimateParamSet getEstimateParamSet() {
        return estimateParamSet;
    }

    public Domain getDomain() {
        return domain;
    }

    /**
     * 选择实验策略方法
     * */
    public Policy pickPolicy(Object key, String layerId) {
        return layerDataPicker.get(layerId).pick(key);
    }

    /**
     * 添加层
     *
     * 将此层中指定为当前域的实验策略添加到当前域
     * */
    public void addLayer(LayerAgent layer) throws ConfigException {
        List<Policy> policies = layer.getPoliciesByDomain(this);
        if (policies == null || policies.isEmpty()){
            throw new ConfigException(new StringBuilder("层：").append(layer.getLayerId())
                .append(" ，域：").append(domain.getDomainId()).append(" 没有配置实验策略！").toString()
            );
        }
        layerPolicyMap.put(layer.getLayerId(), layer.getPoliciesByDomain(this));
    }


    /**
     * 获取此域中某一层的实验策略
     * */
    public List<Policy> getPoliciesByLayerId(String layerId) {
        return layerPolicyMap.get(layerId);
    }

    /**
     * 获取此域中的层ID
     * */
    public Set<String> getLayers() {
        return layerDataPicker.keySet();
    }


    @Override
    public int getSize() {
        return domain.getSize();
    }

    @Override
    public void setSize(int size) {
        domain.setSize(size);
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean isDefault() {
        return domain.isDefault;
    }

    public DomainTypeEnum getDomainType() {
        return domain.domainType;
    }

    public String getDomainId() {
        return domain.domainId;
    }

    @Override
    public String toString() {
        return domain.domainId + (isDefault() ? ", default" : "") + ", class=" + getClass().getSimpleName();
    }

    @Override
    public int hashCode() {
        return domain.domainId.hashCode();
    }

    public List<RuleStrategyEnum> getRuleStrategy(){
        if (domain.getRuleStrategy() != null && domain.getRuleStrategy().size() > 0){
            return domain.getRuleStrategy();
        } else if(StringUtils.isNotEmpty(domain.getRuleExpression())){
            return Lists.newArrayList(RuleStrategyEnum.ExpressionBase);
        }
        return Lists.newArrayList(RuleStrategyEnum.Hash);
    }

    public AudienceDefinition getAudienceDefinition() {
        return audienceDefinition;
    }

}
