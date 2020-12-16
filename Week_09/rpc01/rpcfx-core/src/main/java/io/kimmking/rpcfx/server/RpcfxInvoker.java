package io.kimmking.rpcfx.server;

import io.kimmking.rpcfx.api.RpcfxException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class RpcfxInvoker<U> {

    private RpcfxResolver resolver;

    public RpcfxInvoker(RpcfxResolver resolver) {
        this.resolver = resolver;
    }

    public RpcfxResponse<U> invoke(RpcfxRequest request) {
        RpcfxResponse<U> response = new RpcfxResponse<>();
        String serviceClass = request.getServiceClass();

        // 作业1：改成泛型和反射
        Object service = resolver.resolve(serviceClass);

        try {
            Method method = resolveMethodFromClass(service.getClass(), request.getMethod());
            Object result = method.invoke(service, request.getParams()); // dubbo, fastjson,
            response.setResult((U) result);
            response.setStatus(true);
            return response;
        } catch (IllegalAccessException | InvocationTargetException e) {

            // 3.Xstream

            response.setException(new RpcfxException(e));
            response.setStatus(false);
            return response;
        }
    }

    private Method resolveMethodFromClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods()).filter(m -> methodName.equals(m.getName())).findFirst().get();
    }

}
