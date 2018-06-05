package com.liepin.macaw.model;

import net.sf.cglib.beans.BeanMap;

import java.util.Map;
import java.util.Set;

/**
 * 实验策略
 * Created by zhangpeng on 2017/5/4.
 */
public class Policy implements IHashAble{

    private String name;
    private Object param;
    private int size;
    private Set<String> domainIds;
    private boolean isDefault;

    @Override
    public int getSize() {
        return size;
    }

    public String getName() {
        return name;
    }

    public Object getParam() {
        return param;
    }

    public Set<String> getDomainIds() {
        return domainIds;
    }

    @Override
    public String toString() {
        return name;
    }

    public void setDomainIds(Set<String> domainIds) {
        this.domainIds = domainIds;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    public void setParam(Object param) {
        this.param = param;
    }

    public void setName(String name) {
        this.name = name;
    }

    public <T> T getParam(Class<T> tClass) throws IllegalAccessException, InstantiationException {
        if (param == null) return null;
        if (tClass.isInstance(param)) {
            return (T) param;
        }
        Map fromMap;
        if (param instanceof Map) {
            fromMap = (Map) param;
        } else {
            fromMap = BeanMap.create(param);
        }
        BeanMap toMap = BeanMap.create(tClass.newInstance());
        toMap.putAll(fromMap);
        return (T) toMap.getBean();
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
