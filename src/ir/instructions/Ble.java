package ir.instructions;

import ir.base.BasicBlock;
import ir.base.BranchInstruction;
import ir.base.Opcode;

public class Ble extends BranchInstruction {

    public Ble( BasicBlock target) {
        super(Opcode.ble, target);
    }
}
