package com.liepin.macaw.picker;

import com.google.common.collect.Lists;
import com.liepin.macaw.exception.ConfigException;
import com.liepin.macaw.model.Policy;
import com.liepin.macaw.utils.ModelUtils;
import com.liepin.macaw.utils.PolicyCounter;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.util.List;

public class HashPickerTest {


    private static final Logger logger = Logger.getLogger(EpsilonPickerTest.class);

    @Test
    public void testPicker(){
        List<Policy> policies = Lists.newArrayList();
        policies.add(ModelUtils.newPolicy("P1", 30, "Policy1", true));
        policies.add(ModelUtils.newPolicy("P2", 20, "Policy2", false));
        policies.add(ModelUtils.newPolicy("P3", 10, "Policy3", false));
        policies.add(ModelUtils.newPolicy("P4", 30, "Policy4", false));
        try {
            HashPicker<Policy> picker = DataPickers.newHashPicker(100, "a", policies, null);
            PolicyCounter counter = new PolicyCounter();
            for (int i = 0; i < 1000; i++) {
                counter.addPolicy(picker.pick(String.valueOf(i)));
            }
            counter.print();
        } catch (ConfigException e) {
            e.printStackTrace();
        }
    }
}
