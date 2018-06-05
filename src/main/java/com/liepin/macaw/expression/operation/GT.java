package com.liepin.macaw.expression.operation;

import com.liepin.macaw.utils.CastUtils;

import javax.activation.UnsupportedDataTypeException;

public class GT implements Operation {

    private Double number2;

    public GT(Object number) {
        try {
            this.number2 = CastUtils.cast(number, Double.class);
        } catch (UnsupportedDataTypeException e) {
            this.number2 = 0.0;
        }
    }

    @Override
    public boolean match(Object number) {
        try {
            return CastUtils.cast(number, Double.class).compareTo(number2) > 0;
        } catch (UnsupportedDataTypeException e) {
            return false;
        }
    }
}
