package io.kimmking.rpcfx.api;

import lombok.Data;

@Data
public class RpcfxResponse<U> {
    private U result;
    private boolean status;
    private RpcfxException exception;
}
