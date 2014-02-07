package ir;

import ir.base.BranchInstruction;
import ir.base.Opcode;

public class Bge extends BranchInstruction {

    public Bge(BasicBlock target) {
        super(Opcode.bge, target);
    }
}