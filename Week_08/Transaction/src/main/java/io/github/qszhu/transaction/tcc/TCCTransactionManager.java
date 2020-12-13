package io.github.qszhu.transaction.tcc;

import java.util.ArrayList;
import java.util.List;

public class TCCTransactionManager {
    private final List<TCCOperation> operations = new ArrayList<>();

    public void addOperation(TCCOperation op) {
        operations.add(op);
    }

    public void commit() throws Exception {
        System.out.println("try:");
        Exception ex = null;
        for (TCCOperation op : operations) {
            try {
                op.doTry();
            } catch (Exception e) {
                ex = e;
                System.out.println(e.getMessage());
                break;
            }
        }

        if (ex == null) {
            System.out.println("confirm:");
            for (TCCOperation op : operations) {
                op.doConfirm();
            }
        } else {
            System.out.println("cancel:");
            for (TCCOperation op : operations) {
                op.doCancel();
            }
            throw ex;
        }
    }
}
