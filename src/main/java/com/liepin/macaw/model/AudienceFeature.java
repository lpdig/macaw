package com.liepin.macaw.model;

import com.google.common.base.Objects;
import com.liepin.macaw.utils.CastUtils;
import net.sf.cglib.beans.BeanMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.UnsupportedDataTypeException;
import java.util.Map;

/**
 * 受众特征
 * */
public class AudienceFeature {

    private static final Logger logger = LoggerFactory.getLogger(AudienceFeature.class);
    private String property;
    private String description;
    private boolean keyProperty = false;

    public AudienceFeature(){
    }
    public AudienceFeature(String property, String description) {
        this.property = property;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public int hashCode() {
        if (property == null) return 0;
        return property.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (! (obj instanceof AudienceFeature)) return false;
        AudienceFeature f = (AudienceFeature) obj;
        return Objects.equal(property, f.getProperty());
    }

    public <T> T getValue(Object obj, Class<T> tClass) {
        Object value = getValue(obj);
        try {
            return CastUtils.cast(value, tClass);
        } catch (UnsupportedDataTypeException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public Object getValue(Object obj) {
        if (obj == null) return null;
        if ("_".equals(property)) return obj;
        if (obj instanceof Map) return ((Map) obj).get(property);
        BeanMap map = BeanMap.create(obj);
        return map.get(property);
    }

    public boolean isKeyProperty() {
        return keyProperty;
    }

    public void setKeyProperty(boolean keyProperty) {
        this.keyProperty = keyProperty;
    }
}
