package com.liepin.macaw.picker;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.AudienceFeature;
import com.liepin.macaw.model.IHashAble;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 流量划分工具类，基于桶做流量划分
 * <p>
 * 1. 初始化: 可指定bucket总数，调用addObject将某Object配置给指定数量的bucket，调用setDefaultObject设置默认Object
 * 2. 使用: 对给定key获取Object，可配置不同的hash策略，将key hash到指定的bucket，并获取对应的Object
 * 3. 校验: 可以用fullyAllocated校验目前的流量划分是否合法
 * Created by zhangpeng on 2017/5/12.
 */
public class HashPicker<T extends IHashAble> implements IDataPicker{


    private final int capacity;

    private HashFunction hashFunction = Hashing.sipHash24();

    private final String shuffleCode;

    private List<AudienceFeature> keyFeatures;

    /**
     * 保存所有目前splitter中已经分配的空间段落，使用Range数组表示
     */
    private List<Range<Integer>> bucketRanges = Lists.newArrayList();

    /**
     * 保存所有目前splitter中已经分配的对象，与bucketRanges中的段落相对应
     */
    private List<T> objects = Lists.newArrayList();

    /**
     * 目前剩余空间的初始位置
     */
    private int blankIndex = 0;

    private T defaultObject;

    public HashPicker(int capacity, String shuffleCode) {
        this.capacity = capacity;
        this.shuffleCode = shuffleCode;
    }

    public void init() {
        if (defaultObject != null) {
            int trafficShare = capacity - blankIndex;
            if (trafficShare > 0) {
                defaultObject.setSize(trafficShare);
                Range<Integer> buckets = Range.closed(blankIndex, capacity);
                objects.add(defaultObject);
                bucketRanges.add(buckets);
                blankIndex = capacity;
            }
        }
    }

    @Override
    public T pick(Object obj) {
        String key = getHashKey(obj);
        int bucket = hash(key, shuffleCode, capacity);
        for (int i = 0; i < bucketRanges.size(); i++) {
            if (bucketRanges.get(i).contains(bucket)) {
                return objects.get(i);
            }
        }
        return defaultObject;
    }

    /**
     * 增加一个策略，指定其占用的桶大小
     */
    public HashPicker<T> addObject(T object) throws ConfigException {
        int bucketNum = object.getSize();
        //
        if (bucketNum == -1 || object.isDefault()) {
            defaultObject = object;
            return this;
        }
        if (bucketNum == 0) return this;
        Range<Integer> buckets = Range.closed(blankIndex, blankIndex + bucketNum - 1);
        if (buckets.upperEndpoint() >= capacity) {
            throw new ConfigException(new StringBuilder("添加的桶数已经超过总量！object:").append(object.toString()).append(", 总桶数:").append(capacity)
                .append(", 当前数量:").append(buckets.upperEndpoint()).toString());
        }
        objects.add(object);
        bucketRanges.add(buckets);
        blankIndex += bucketNum;
        return this;
    }

    public int hash(String key, String shuffleCode, int baseSize) {
        long hashValue = hashFunction.hashUnencodedChars(key + shuffleCode).asLong();
        if (hashValue < 0)
            hashValue = hashValue * -1;
        return (int)(hashValue % baseSize);
    }

    public T getDefaultObject() {
        return defaultObject;
    }

    public boolean fullyAllocated() {
        return blankIndex >= capacity || defaultObject != null;
    }

    public int getRemainSize() {
        return capacity - blankIndex;
    }

    public int getCapacity() {
        return capacity;
    }

    private String getHashKey(Object object) {
        if (object == null) return "";
        if (keyFeatures != null && !keyFeatures.isEmpty()) {
            List<String> keys = Lists.newArrayList();
            for (AudienceFeature feature : keyFeatures) {
                keys.add(feature.getValue(object, String.class));
            }
            return StringUtils.join(keys, "_");
        }
        return object.toString();
    }

    @Override
    public String toString() {
        return "capacity=" + capacity + ", obj cnt=" + (objects.size() + (defaultObject == null ? 0 : 1))
                + ", shuffleCode=" + shuffleCode;
    }

    public void setKeyFeatures(List<AudienceFeature> keyFeatures) {
        this.keyFeatures = keyFeatures;
    }
}
