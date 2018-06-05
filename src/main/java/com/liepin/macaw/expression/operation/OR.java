package com.liepin.macaw.expression.operation;

import com.liepin.macaw.expression.Expression;

public class OR implements Operation {
    private Expression expression1;
    private Expression expression2;

    public OR(Expression expression1, Expression expression2) {
        this.expression1 = expression1;
        this.expression2 = expression2;
    }

    @Override
    public boolean match(Object obj) {
        if (expression1.match(obj)) return true;
        return expression2.match(obj);
    }
}
