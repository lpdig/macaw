package com.liepin.macaw.picker;

import com.google.common.collect.Lists;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.expression.Expression;
import com.liepin.macaw.expression.ExpressionParser;
import com.liepin.macaw.model.IExpressionBase;
import com.liepin.macaw.model.Policy;
import com.liepin.macaw.utils.ModelUtils;
import org.junit.Test;

import java.util.List;

public class RuleBasedPickerTest {

    @Test
    public void testPicker(){
        List<Policy> policies = Lists.newArrayList();
        policies.add(ModelUtils.newPolicy("P1", 30, "Policy1", true));
        policies.add(ModelUtils.newPolicy("P2", 20, "Policy2", false));
        policies.add(ModelUtils.newPolicy("P3", 10, "Policy3", false));
        policies.add(ModelUtils.newPolicy("P4", 30, "Policy4", false));
        List<ExpressionObj> objs = Lists.newArrayList();
        objs.add(new ExpressionObj("Obj1", "value > 100", false));
        objs.add(new ExpressionObj("Obj2", "value < 10", false));
        objs.add(new ExpressionObj("Obj3", "(name in p1,p2,p3,p4) and (value < 80)", false));
        objs.add(new ExpressionObj("Obj4", " weight > 10", true));
        ExpressionBasedPicker<ExpressionObj> picker = new ExpressionBasedPicker<>(objs);
        assert picker.pick(new Param("p1", 101, 0.3)) == objs.get(0);
        assert picker.pick(new Param("p2", 9, 0.3)) == objs.get(1);
        assert picker.pick(new Param("p3", 50, 0.3)) == objs.get(2);
        assert picker.pick(new Param("p5", 50, 0.3)) == objs.get(3);
    }

    public static class Param {
        private String name;
        private Integer value;
        private Double weight;
        public Param(String name, Integer value, Double weight) {
            this.name = name;
            this.value = value;
            this.weight = weight;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }

        public Double getWeight() {
            return weight;
        }

        public void setWeight(Double weight) {
            this.weight = weight;
        }
    }

    private static class ExpressionObj implements IExpressionBase {

        public Expression expression;
        public String name;
        public boolean defaultFlag;
        public ExpressionObj(String name, String exp, boolean defaultFlag) {
            try {
                this.expression = ExpressionParser.parse(exp);
                this.name = name;
                this.defaultFlag = defaultFlag;
            } catch (ConfigException e) {
                e.printStackTrace();
            }
        }

        @Override
        public Expression getExpression() {
            return expression;
        }

        @Override
        public boolean isDefault() {
            return defaultFlag;
        }
    }
}
