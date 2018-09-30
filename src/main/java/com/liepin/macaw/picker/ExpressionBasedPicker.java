package com.liepin.macaw.picker;

import com.liepin.macaw.model.IExpressionBase;

import java.util.ArrayList;
import java.util.List;

public class ExpressionBasedPicker<T extends IExpressionBase> implements IDataPicker {

    private T defaultRuleBase;
    private List<T> ruleBases;

    public ExpressionBasedPicker(List<T> ruleBases) {
        this.ruleBases = new ArrayList<>();
        for (T ruleBase : ruleBases) {
            if (ruleBase.isDefault()) {
                defaultRuleBase = ruleBase;
            } else if (ruleBase.getExpression() != null) {
                this.ruleBases.add(ruleBase);
            }

        }
    }

    @Override
    public T pick(Object code) {
        for (T ruleBase : ruleBases) {
            if (ruleBase.getExpression().match(code)) {
                return ruleBase;
            }
        }
        return defaultRuleBase;
    }

    public boolean hasDefault(){
        return defaultRuleBase != null;
    }

}
