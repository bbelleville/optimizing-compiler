package support;

import ir.Immediate;
import ir.BasicBlock;
import ir.Kill;
import ir.Adda;
import ir.Load;
import ir.Store;
import ir.Move;
import ir.Value;

public class VarType extends Type {
    private boolean global;
    private boolean used;
    private MemoryRegion globals;
    public VarType(boolean global, MemoryRegion globals) {
        this.globals = globals;
        this.global = global;
        this.used = false;
    }

    @Override
    public int getSize() {
        return getWordSize();
    }

    @Override
    public void assignValue(Designator d, BasicBlock cur, BasicBlock join, Environment env, Value newVal)
        throws Exception {
        if(global) {
            used = true;
            Adda ad = new Adda(getGBP(), getAddr(d));
            cur.addInstruction(ad);
            Store st = new Store(d.getVarName(), ad, newVal);
            cur.addInstruction(st);
            if(join != null) {
                join.addAtEndBeforeBranch(new Kill(d.getVarName()));
            }
        } else {
            Identifier var = d.getVarName();
            Value old = env.get(var);
            // generate a move instruction to indicate the value assignment
            Move move = new Move(newVal);
            cur.addInstruction(move);
            env.put(var, move);
            // get the variable reference to replace old references with
            Value replaceWith = env.get(var);
            if(join != null) {
                join.addPhi(var, old, replaceWith);
            }
        }
    }

    @Override
    public Value getValue(Designator d, BasicBlock cur, Environment env)
        throws Exception {
        if(global) {
            used = true;
            Adda ad = new Adda(getGBP(), getAddr(d));
            cur.addInstruction(ad);
            Load ld = new Load(d.getVarName(), global, ad); // global = true
            cur.addInstruction(ld);
            return ld;
        } else {
            return env.get(d.getVarName());
        }
    }

    // if a global variable hasn't been used when freeze is called,
    // convert it to a local variable
    @Override
    public void freeze() {
        if(!used && global) {
            global = false;
        }
    }

    private Value getAddr(Designator d) throws Exception{
        if(global) {
            return new Immediate(globals.getAddress(d.getVarName(), getSize()));
        }
        throw new Exception("attempt to get address for a local variable");
    }
}
