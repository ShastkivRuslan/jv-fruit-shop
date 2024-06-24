package strategy.impl;

import db.Storage;
import model.FruitTransaction;
import strategy.OperationHandler;

public class PurchaseOperationImpl implements OperationHandler {
    int MIN_QUANTITY = 0;

    @Override
    public void applyOperation(FruitTransaction transaction) {
        if (transaction.getQuantity() <= MIN_QUANTITY) {
            throw new RuntimeException("Purchase must be 1 or more items. But was: "
                    + transaction.getQuantity());
        }
        int result = Storage.getQuantity(transaction.getFruitName()) - transaction.getQuantity();
        if (result < 0) {
            throw new RuntimeException("There is no required quantity in stock.Exist: "
                    + Storage.getQuantity(transaction.getFruitName()) + "Required: "
                    + transaction.getQuantity());
        }
        Storage.updateDb(transaction.getFruitName(),
                result);
    }
}
