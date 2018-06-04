package com.liepin.macaw.expression.operation;

import com.google.common.base.Objects;

public class Equal implements Operation {
    private String obj;

    public Equal(String obj) {
        this.obj = obj;
    }

    @Override
    public boolean match(Object obj1) {
        if (obj1 != null) {
            return Objects.equal(obj, obj1.toString());
        }
        return false;
    }
}
