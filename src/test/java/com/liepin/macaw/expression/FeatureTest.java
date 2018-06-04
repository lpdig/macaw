package com.liepin.macaw.expression;

import com.liepin.macaw.model.AudienceFeature;
import com.liepin.macaw.model.Domain;
import org.junit.Test;

public class FeatureTest {
    @Test
    public void testFeature(){
        AudienceFeature domainIdFeature = new AudienceFeature("domainId", null);
        AudienceFeature sizeFeature = new AudienceFeature("size", null);
        AudienceFeature epsilonFeature = new AudienceFeature("epsilon", null);
        Domain domain = new Domain();
        domain.setSize(100);
        domain.setEpsilon(0.3);
        domain.setDomainId("domain1");
        System.out.println(sizeFeature.getValue(domain));

        System.out.println(sizeFeature.getValue(domain, String.class));
        System.out.println(epsilonFeature.getValue(domain, String.class));
        System.out.println(epsilonFeature.getValue(domain, Integer.class));
        assert domainIdFeature.getValue(domain).equals("domain1");
        assert sizeFeature.getValue(domain, Integer.class) == 100;
        assert epsilonFeature.getValue(domain, Double.class) == 0.3;

    }
}
