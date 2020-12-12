package io.github.qszhu.transaction;

import io.github.qszhu.transaction.service.ItemService;
import io.github.qszhu.transaction.service.UserService;
import io.github.qszhu.transaction.tcc.ReduceStockOperation;
import io.github.qszhu.transaction.tcc.TCCOperation;
import io.github.qszhu.transaction.tcc.TCCTransactionManager;
import io.github.qszhu.transaction.tcc.WithdrawBalanceOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    private static UserService userService;
    private static ItemService itemService;

    private static void successTransaction() throws Exception {
        WithdrawBalanceOperation wop = WithdrawBalanceOperation.builder()
                .userService(userService)
                .userId(1).amount(100)
                .build();
        ReduceStockOperation rop = ReduceStockOperation.builder()
                .itemService(itemService)
                .itemId(1).quantity(1)
                .build();

        runTransaction(wop, rop);
    }

    private static void failedTransaction1() throws Exception {
        WithdrawBalanceOperation wop = WithdrawBalanceOperation.builder()
                .userService(userService)
                .userId(1).amount(100)
                .build();
        wop.setShouldFail(true);
        ReduceStockOperation rop = ReduceStockOperation.builder()
                .itemService(itemService)
                .itemId(1).quantity(1)
                .build();

        runTransaction(wop, rop);
    }

    private static void failedTransaction2() throws Exception {
        WithdrawBalanceOperation wop = WithdrawBalanceOperation.builder()
                .userService(userService)
                .userId(1).amount(100)
                .build();
        ReduceStockOperation rop = ReduceStockOperation.builder()
                .itemService(itemService)
                .itemId(1).quantity(1)
                .build();
        rop.setShouldFail(true);

        runTransaction(wop, rop);
    }

    private static void runTransaction(TCCOperation... ops) throws Exception {
        TCCTransactionManager manager = new TCCTransactionManager();
        for (TCCOperation op : ops) {
            manager.addOperation(op);
        }

        int balance = userService.getBalance(1);
        System.out.println("balance before: " + balance);

        int remain = itemService.getRemain(1);
        System.out.println("stock before: " + remain);

        try {
            manager.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        balance = userService.getBalance(1);
        System.out.println("balance after: " + balance);

        remain = itemService.getRemain(1);
        System.out.println("stock after: " + remain);
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        userService = (UserService) context.getBean("userService");
        itemService = (ItemService) context.getBean("itemService");

        System.out.println("Success transactions");
        successTransaction();

        System.out.println("Failed 1st operation");
        failedTransaction1();

        System.out.println("Failed 2nd operation");
        failedTransaction2();
    }
}
