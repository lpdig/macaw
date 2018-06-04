package com.liepin.macaw.expression;


import com.google.common.collect.Lists;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.expression.operation.*;
import com.liepin.macaw.model.AudienceFeature;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ExpressionParser {
    public static Expression parse(String expression) throws ConfigException {
        Stack stack = new Stack();
        List<String> exps = splitExpression(expression);
        Object obj;
        for (String w : exps) {
//        for (int i = 0; i < exps.length; i++) {
            if (w.equals(")")) {
                List list = new ArrayList(5);
                obj = stack.pop();
                while (!"(".equals(obj.toString())) {
                    list.add(obj);
                    obj = stack.pop();
                }
                Expression exp = createExpression(list);
                if (exp != null) {
                    stack.push(exp);
                }
            } else {
                stack.push(w);
            }
        }
        List list = new ArrayList(5);
        while (!stack.isEmpty()) {
            list.add(stack.pop());
        }
        return createExpression(list);
    }

    private static List<String> splitExpression(String expression) {
        char[] chars = expression.toCharArray();
        List<String> output = Lists.newArrayList();
        StringBuilder cache = new StringBuilder();
        for (char c : chars) {
            if (c == ' ') {
                if (cache.length() > 0) {
                    output.add(cache.toString());
                    cache = new StringBuilder();
                }
            } else if (c == '(' || c == ')' || c == '>' || c == '<' || c == '=') {
                if (cache.length() > 0) {
                    output.add(cache.toString());
                    cache = new StringBuilder();
                }
                output.add(String.valueOf(c));
            } else {
                cache.append(c);
            }
        }
        if (cache.length() > 0) {
            output.add(cache.toString());
        }
        return output;
    }

    protected static Expression createExpression(List list) throws ConfigException {
        if (list == null || list.isEmpty()) return null;
        if (list.size() == 1) {
            if (list.get(0) instanceof Expression) return (Expression) list.get(0);
            else throw new ConfigException("解析表达式异常！");
        }
        String oper = ((String)list.get(list.size() - 2)).toLowerCase();
        switch (oper) {
            case ">":
                return new Expression(
                        new FeatureObjAdaptor(
                                new AudienceFeature((String)list.get(list.size() - 1), null)),
                        new GT(Double.valueOf(list.get(0).toString())));
            case "<":
                return new Expression(
                        new FeatureObjAdaptor(
                                new AudienceFeature((String)list.get(list.size() - 1), null)),
                        new LT(Double.valueOf(list.get(0).toString())));
            case "=":
                return new Expression(
                        new FeatureObjAdaptor(
                                new AudienceFeature((String)list.get(list.size() - 1), null)),
                        new Equal(list.get(0).toString()));
            case "in":
                return new Expression(
                        new FeatureObjAdaptor(
                                new AudienceFeature((String)list.get(list.size() - 1), null)),
                        new IN(list.get(0).toString()));
            case "between":
                return new Expression(
                        new FeatureObjAdaptor(
                                new AudienceFeature((String)list.get(list.size() - 1), null)),
                        new Between(Double.valueOf(list.get(2).toString()), Double.valueOf(list.get(0).toString())));
            case "and":
                return new Expression(
                        new SelfAdaptor(),
                        new And((Expression) list.get(2), (Expression) list.get(0)));
            case "or":
                return new Expression(
                        new SelfAdaptor(),
                        new OR((Expression) list.get(2), (Expression) list.get(0)));
            default:
                throw new ConfigException("存在不能识别的运算符：" + oper);
        }
    }


    public static void main(String[] args) {
        String str = "size>";
        System.out.println(str.indexOf("[>|\\(|\\)]"));
    }
}
