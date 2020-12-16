package io.kimmking.rpcfx.api;

public class RpcfxException extends Exception {
    public RpcfxException(Throwable e) {
        super(e);
    }

    public RpcfxException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public RpcfxException() {
    }

    public RpcfxException(String message) {
        super(message);
    }

    public RpcfxException(String message, Throwable cause) {
        super(message, cause);
    }
}
