package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;

public class UserServiceImpl implements UserService {

    @Override
    public void throwException() throws Exception {
        throw new Exception("test exception");
    }

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }
}
