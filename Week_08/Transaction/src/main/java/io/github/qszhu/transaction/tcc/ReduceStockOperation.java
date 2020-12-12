package io.github.qszhu.transaction.tcc;

import io.github.qszhu.transaction.service.ItemService;
import lombok.Builder;

@Builder
public class ReduceStockOperation extends BaseTCCOperation {
    private ItemService itemService;
    private int itemId;
    private int quantity;

    @Override
    public void trial() throws Exception {
    }

    @Override
    protected void confirm() throws Exception {
        itemService.reduce(itemId, quantity);
    }

    @Override
    protected void cancel() throws Exception {
        itemService.reduce(itemId, -quantity);
    }
}
