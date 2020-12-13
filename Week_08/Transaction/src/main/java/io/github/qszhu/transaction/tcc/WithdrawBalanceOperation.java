package io.github.qszhu.transaction.tcc;

import io.github.qszhu.transaction.service.UserService;
import lombok.Builder;

@Builder
public class WithdrawBalanceOperation extends BaseTCCOperation {
    private UserService userService;
    private int userId;
    private int amount;

    @Override
    public void trial() throws Exception {
        userService.freeze(userId, amount);
    }

    @Override
    protected void confirm() throws Exception {
        userService.withdraw(userId, amount);
    }

    @Override
    protected void cancel() throws Exception {
        userService.unfreeze(userId, amount);
    }
}
