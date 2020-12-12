package io.github.qszhu.transaction.tcc;

public interface TCCOperation {
    void doTry() throws Exception;
    void doConfirm() throws Exception;
    void doCancel() throws Exception;
}
