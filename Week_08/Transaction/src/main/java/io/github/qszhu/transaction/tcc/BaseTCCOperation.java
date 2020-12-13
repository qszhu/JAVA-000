package io.github.qszhu.transaction.tcc;

import lombok.Data;

@Data
public abstract class BaseTCCOperation implements TCCOperation {
    private boolean confirmed = false;
    private boolean canceled = false;
    private boolean shouldFail = false;

    @Override
    public void doTry() throws Exception {
        if (shouldFail) throw new Exception("fail");
        trial();
    }

    protected abstract void trial() throws Exception;

    @Override
    public void doConfirm() throws Exception {
        if (confirmed) return;
        confirm();
        confirmed = true;
    }

    protected abstract void confirm() throws Exception;

    @Override
    public void doCancel() throws Exception {
        if (canceled) return;
        cancel();
        canceled = true;
    }

    protected abstract void cancel() throws Exception;
}
