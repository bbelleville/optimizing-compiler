package ir;

import java.util.ArrayList;

public abstract class Instruction {
    public BasicBlock bb;
    private int number;
    private Instruction deleted;
    private Instruction dominating;

    private Opcode opcode;
    public ArrayList<Instruction> uses;

    abstract public ArrayList<Instruction> getArguments();

    public Instruction(int num, BasicBlock b, Opcode o) {
        number = num;
        bb = b;
        opcode = o;
    }

    public final void setDominating(Instruction i) {
        dominating = i;
    }

    public final int getNumber() {
        return number;
    }

    // returns the last instruction this was deleted for.
    public final Instruction getDeleted() {
	if(deleted == null)
	    return null;
	Instruction d = deleted;
        while(d.deleted != null) {
            d = d.deleted;
        }
        // can optimize by setting this.deleted = d, probably not necessary
        return d;
    }

    public final boolean isDeleted() {
        return deleted != null;
    }

    public final Opcode getOpcode() {
        return opcode;
    }

    public final void addUse(Instruction i) {
        uses.add(i);
    }

    // delete this instruction in favor of instruction i, in other
    // words perform common subexpression elimination.
    public void delete(Instruction i) throws Exception {
        deleted = i;

        // this may not be necessary, depending on how I generate my
        // SSA, and when I perform CSE, also may not be necessary if I
        // just make sure to not emit deleted instructions, and if an
        // argument is marked as deleted, use that instead.
        for(Instruction use : uses) {
            use.replaceArgument(this, i);
        }
    }

    public boolean isCommonSubexpression(Instruction i) {
        return this.opcode == i.opcode && this.getArguments().equals(i.getArguments());
    }

    public void performCSE() {
        Instruction i = dominating;
        while(i != null) {
            if(this.isCommonSubexpression(i)) {
                this.deleted = i.getDeleted();
                return;
            }
            i = i.dominating;
        }
    }

    // replace the argument that is eq to replace with i, this will
    // need to be done if replace has been deleted for i.
    abstract public void replaceArgument(Instruction replace, Instruction i) throws Exception;
}

