package com.liepin.macaw.expression;

public class SelfAdaptor implements ObjectAdaptor {

    @Override
    public Object getValue(Object obj) {
        return obj;
    }
}
