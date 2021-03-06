package io.kimmking.rpcfx.client;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.ParserConfig;
import io.kimmking.rpcfx.api.RpcfxException;
import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResponse;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URISyntaxException;

public final class Rpcfx {

    static {
        ParserConfig.getGlobalInstance().addAccept("io.kimmking");
    }

    public static <T, U> T create(final Class<T> serviceClass, final Class<U> resultClass, final String url) {

        // 0. 替换动态代理 -> AOP
        return (T) Proxy.newProxyInstance(
                Rpcfx.class.getClassLoader(),
                new Class[]{serviceClass},
                new RpcfxInvocationHandler<T, U>(serviceClass, resultClass, url)
        );

    }

    public static class RpcfxInvocationHandler<T, U> implements InvocationHandler {

        private final Class<T> serviceClass;
        private final String url;
        private final Class<U> resultClass;

        public RpcfxInvocationHandler(Class<T> serviceClass, Class<U> resultClass, String url) {
            this.serviceClass = serviceClass;
            this.url = url;
            this.resultClass = resultClass;
        }

        // 可以尝试，自己去写对象序列化，二进制还是文本的，，，rpcfx是xml自定义序列化、反序列化，json: code.google.com/p/rpcfx
        // int byte char float double long bool
        // [], data class

        @Override
        public Object invoke(Object proxy, Method method, Object[] params) throws RpcfxException {
            RpcfxRequest request = new RpcfxRequest();
            request.setServiceClass(this.serviceClass.getName());
            request.setMethod(method.getName());
            request.setParams(params);
            request.setResultClass(this.resultClass.getName());

            try {
                RpcfxResponse<U> response = post(request, url);
                if (response.isStatus()) return response.getResult();

                throw response.getException();
            } catch (Exception e) {
                throw new RpcfxException(e);
            }
        }

        private RpcfxResponse<U> post(RpcfxRequest req, String url) throws IOException, URISyntaxException {
            String reqJson = JSON.toJSONString(req);
            System.out.println("req json: " + reqJson);

            String respJson = NettyRequester.getInstance().request(url, reqJson);
            System.out.println("resp json: " + respJson);
            return JSON.parseObject(respJson, new TypeReference<RpcfxResponse<U>>(resultClass) {
            });
        }
    }

}
