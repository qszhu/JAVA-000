package io.github.qszhu.transaction.tcc;

import java.util.ArrayList;
import java.util.List;

public class TCCTransactionManager {
    private final List<TCCOperation> operations = new ArrayList<>();

    public void addOperation(TCCOperation op) {
        operations.add(op);
    }

    public void commit() throws Exception {
        // try
        Exception ex = null;
        for (TCCOperation op : operations) {
            try {
                op.doTry();
            } catch (Exception e) {
                ex = e;
                break;
            }
        }

        if (ex == null) {
            // confirm
            for (TCCOperation op : operations) {
                op.doConfirm();
            }
        } else {
            // cancel
            for (TCCOperation op : operations) {
                op.doCancel();
            }
            throw ex;
        }
    }
}
