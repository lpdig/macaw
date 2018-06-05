package com.liepin.macaw.expression;

import com.liepin.macaw.expression.operation.Operation;

public class Expression {
    private ObjectAdaptor parser;
    private Operation operation;

    public Expression(ObjectAdaptor parser, Operation operation) {
        this.parser = parser;
        this.operation = operation;
    }

    public boolean match(Object obj) {
        return operation.match(parser.getValue(obj));
    }
}
