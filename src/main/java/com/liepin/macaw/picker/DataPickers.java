package com.liepin.macaw.picker;

import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.AudienceDefinition;
import com.liepin.macaw.model.EstimateParamSet;
import com.liepin.macaw.model.IHashAble;
import com.liepin.macaw.model.Policy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 策略选测器的构造工厂类
 * Created by zhangpeng on 2017/10/20.
 */
public class DataPickers {
    private static final Logger logger = LoggerFactory.getLogger(DataPickers.class);

    /**
     * 哈希策略选择器
     * @param capacity 选择器的总容量（桶的数量）
     * @param objects 所有备选的对象
     * @param shuffleCode 哈希算法shuffleCode
     * */
    public static <T extends IHashAble> HashPicker<T> newHashPicker(int capacity,
                                                                    String shuffleCode,
                                                                    List<T> objects,
                                                                    AudienceDefinition audienceDefinition) throws ConfigException {

        HashPicker<T> picker = new HashPicker<>(capacity, shuffleCode);
        if (audienceDefinition != null) {
            picker.setKeyFeatures(audienceDefinition.getKeyFeature());
        }
        if (objects != null && objects.size() > 0) {
            for (T obj : objects) {
                picker.addObject(obj);
             }
        }
        picker.init();
        if (!picker.fullyAllocated()) {
            throw new ConfigException("所有备选对象的流量总和小于总流量数且未配置默认对象! ");
        }
        return picker;
    }

    /**
     * Thompson sampling策略选择器
     * @param policies 所有备选策略
     * @param params 所有策略评估参数
     * */
    public static ThompsonPicker newThompsonPicker(List<Policy> policies, EstimateParamSet params) throws ConfigException {
        ThompsonPicker layerPicker = new ThompsonPicker(policies, params);
        return layerPicker;
    }

    /**
     * UCB策略选择器
     * @param policies 所有备选策略
     * @param params 所有策略评估参数
     * */
    public static UCBPicker newUCBPicker(List<Policy> policies, EstimateParamSet params) throws ConfigException {
        UCBPicker layerPicker = new UCBPicker(policies, params);
        return layerPicker;
    }

    /**
     * 固定策略的策略选择器
     * @param policies 备选策略，只能有一个策略
     * */
    public static FixPolicyPicker newFixExperimentPicker(List<Policy> policies) throws ConfigException {

        if (policies == null || policies.size() == 0) {
            throw new ConfigException("没有配置实验！");
        } else if (policies.size() > 1 && !policies.get(1).isDefault()) {
            throw new ConfigException("指定流量（固定ID）域配置了多条实验！");
        }
        Policy policy = policies.get(0);
        if (policy.getSize() > 0) {
            logger.warn("指定流量（固定ID）域的实验配置了trafficShare，将不起作用！");
        }
        return new FixPolicyPicker(policies.get(0));
    }

    public static EpsilonGreedyPicker newEpsilonGreedyPicker(List<Policy> policies, EstimateParamSet params, double epsilon) throws ConfigException {
        EpsilonGreedyPicker picker = new EpsilonGreedyPicker(policies, epsilon, params);
        return picker;
    }
}
