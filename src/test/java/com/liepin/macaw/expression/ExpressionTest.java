package com.liepin.macaw.expression;

import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.Domain;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest {
    @Test
    public void testExpression(){
        Domain domain = new Domain();
        domain.setSize(100);
        domain.setEpsilon(0.3);
        domain.setDomainId("domain1");
        String exp = "((size>90)and ( epsilon > 0.3 ) ) or ( epsilon < 0.3 )";
        String exp1 = "((size>90)and ( epsilon > 0.3 ) ) or ( epsilon = 0.3 )";
        String exp2 = "size in 100,101,102";
        try {
            Expression expression = ExpressionParser.parse(exp);
            Assert.assertFalse(expression.match(domain));
            expression = ExpressionParser.parse(exp1);
            Assert.assertTrue(expression.match(domain));
            expression = ExpressionParser.parse(exp2);
            Assert.assertTrue(expression.match(domain));
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }
}
