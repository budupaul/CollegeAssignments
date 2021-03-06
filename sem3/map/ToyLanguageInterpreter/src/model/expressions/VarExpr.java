package model.expressions;

import utils.Heap;
import utils.SymbolTable;
import utils.exceptions.InterpreterException;

/**
 * Created by mirko on 12/10/2016.
 */
public class VarExpr implements Expression {
    private String name;

    public VarExpr(String name) {
        this.name = name;
    }

    @Override
    public int evaluate(SymbolTable<String, Integer> symTable, Heap<Integer, Integer> heap) throws InterpreterException {
        return symTable.getValue(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
