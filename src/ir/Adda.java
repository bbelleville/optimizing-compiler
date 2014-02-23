package ir;

import ir.base.Opcode;
import ir.base.OrderIndependentArithmeticInstruction;
import ir.base.Value;

public class Adda extends OrderIndependentArithmeticInstruction {

    public Adda(Value a1, Value a2) {
        super(Opcode.adda, a1, a2);
    }

    @Override
    public boolean isComputable() {
        // adda instructinos are order independant, but can't really
        // be computed at compile time
        return false;           
    }
}
