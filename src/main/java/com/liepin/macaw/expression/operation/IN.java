package com.liepin.macaw.expression.operation;

import com.google.common.collect.Sets;

import java.util.Set;

public class IN implements Operation {
    private Set<String> set;

    public IN(String str) {
        set = Sets.newHashSet(str.split(","));
    }

    @Override
    public boolean match(Object obj) {
        if (obj == null) return false;
        return set.contains(obj.toString());
    }
}
