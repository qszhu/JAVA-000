package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.api.RpcfxRequest;
import io.kimmking.rpcfx.api.RpcfxResolver;
import io.kimmking.rpcfx.api.RpcfxResponse;
import io.kimmking.rpcfx.demo.api.Order;
import io.kimmking.rpcfx.demo.api.OrderService;
import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import io.kimmking.rpcfx.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class RpcfxServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcfxServerApplication.class, args);
    }

    @Autowired
    RpcfxResolver resolver;

    @PostMapping("/")
    public RpcfxResponse<?> invoke(@RequestBody RpcfxRequest request) {
        String resultClassName = request.getResultClass();
        if ("io.kimmking.rpcfx.demo.api.User".equals(resultClassName))
            return new RpcfxInvoker<User>(resolver).invoke(request);
        if ("io.kimmking.rpcfx.demo.api.Order".equals(resultClassName))
            return new RpcfxInvoker<Order>(resolver).invoke(request);
        return null;
    }

    @Bean
    public RpcfxResolver createResolver() {
        return new DemoResolver();
    }

    @Bean(name = "io.kimmking.rpcfx.demo.api.UserService")
    public UserService createUserService() {
        return new UserServiceImpl();
    }

    @Bean(name = "io.kimmking.rpcfx.demo.api.OrderService")
    public OrderService createOrderService() {
        return new OrderServiceImpl();
    }
}
