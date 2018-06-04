package com.liepin.macaw.enums;

import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.DomainAgent;
import com.liepin.macaw.model.Policy;
import com.liepin.macaw.picker.DataPickers;
import com.liepin.macaw.picker.IDataPicker;

/**
 * Created by zhangpeng on 2017/10/20.
 */
public enum DomainTypeEnum {

    FixedGroup("固定组合域", false) {
        @Override
        public IDataPicker<Policy> newPolicyPicker(DomainAgent domain, String layerId) throws ConfigException {
            return DataPickers.newFixExperimentPicker(domain.getPoliciesByLayerId(layerId));
        }
    },
    MultiLayerHash("多层正交域", false) {
        @Override
        public IDataPicker<Policy> newPolicyPicker(DomainAgent domain, String layerId) throws ConfigException {
            return DataPickers.newHashPicker(domain.getSize(), layerId, domain.getPoliciesByLayerId(layerId), domain.getAudienceDefinition());
        }
    },
    MultiLayerUCB("多层UCB域", true) {
        @Override
        public IDataPicker<Policy> newPolicyPicker(DomainAgent domain, String layerId) throws ConfigException {
            return DataPickers.newUCBPicker(domain.getPoliciesByLayerId(layerId), domain.getEstimateParamSet());
        }
    },
    MultiLayerThompson("多层Thompson域", true) {
        @Override
        public IDataPicker<Policy> newPolicyPicker(DomainAgent domain, String layerId) throws ConfigException {
            return DataPickers.newThompsonPicker(domain.getPoliciesByLayerId(layerId), domain.getEstimateParamSet());
        }
    },
    MultiLayerEpsilonGreedy("多层EpsilonGreedy域", true) {
        @Override
        public IDataPicker<Policy> newPolicyPicker(DomainAgent domain, String layerId) throws ConfigException {
            return DataPickers.newEpsilonGreedyPicker(domain.getPoliciesByLayerId(layerId), domain.getEstimateParamSet()
                    , domain.getDomain().getEpsilon());
        }
    }
    ;

    DomainTypeEnum(String description, boolean bandit){
        this.description = description;
        this.bandit = bandit;
    }
    String description;
    boolean bandit;
    public String getDescription(){
        return description;
    }
    public boolean isBandit(){
        return bandit;
    }

    public abstract IDataPicker<Policy> newPolicyPicker(DomainAgent domain, String layerId) throws ConfigException;
}
