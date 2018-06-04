package com.liepin.macaw.expression.operation;

import com.liepin.macaw.utils.CastUtils;

public class Between implements Operation {
    private Double start;
    private Double end;

    public Between(Object start, Object end) {
        try {

            this.start = CastUtils.cast(start, Double.class);
            this.end = CastUtils.cast(end, Double.class);
        } catch (Exception e) {
            this.start = 0.0;
            this.end = 0.0;
        }
    }

    @Override
    public boolean match(Object obj) {
        try {
            Double value = CastUtils.cast(obj, Double.class);
            return value >= start && value < end;
        } catch (Exception e) {
            return false;
        }
    }
}
