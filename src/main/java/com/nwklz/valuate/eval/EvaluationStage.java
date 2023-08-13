package com.nwklz.valuate.eval;

import com.nwklz.valuate.check.CombinedTypeCheck;
import com.nwklz.valuate.check.TypeCheck;
import com.nwklz.valuate.enums.ErrorFormat;
import com.nwklz.valuate.enums.OperatorSymbol;
import com.nwklz.valuate.operator.Operator;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EvaluationStage {
    private OperatorSymbol symbol;

    private EvaluationStage leftStage;

    private EvaluationStage rightStage;

    private Operator operator;

    private TypeCheck leftTypeCheck;

    private TypeCheck rightTypeCheck;

    private CombinedTypeCheck combinedTypeCheck;

    private ErrorFormat typeErrorFormat;


    public void swapWith(EvaluationStage other) {
        EvaluationStage temp = EvaluationStage.builder().build();
        temp.setToNonStage(other);
        other.setToNonStage(this);
        this.setToNonStage(temp);
    }

    public void setToNonStage(EvaluationStage other) {

        this.symbol = other.symbol;
        this.operator = other.operator;
        this.leftTypeCheck = other.leftTypeCheck;
        this.rightTypeCheck = other.rightTypeCheck;
        this.combinedTypeCheck = other.combinedTypeCheck;
        this.typeErrorFormat = other.typeErrorFormat;
    }

    public boolean isShortCircuitable() {

        switch (this.symbol) {
            case AND:
            case OR:
            case TERNARY_TRUE:
            case TERNARY_FALSE:
            case COALESCE:
                return true;
        }

        return false;
    }
}
