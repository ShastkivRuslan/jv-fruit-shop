package core.basesyntax;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.FruitTransaction;
import model.Operation;
import service.*;
import service.impl.*;
import strategy.OperationHandler;
import strategy.OperationStrategy;
import strategy.impl.BalanceOperationImpl;
import strategy.impl.OperationStrategyImpl;
import strategy.impl.PurchaseOperationImpl;
import strategy.impl.ReturnOperationImpl;
import strategy.impl.SupplyOperationImpl;

public class Main {
    private static final String READ_FILE_PATH = "src/main/resources/ReportToRead.csv";
    private static final String WRITE_FILE_PATH = "src/main/resources/FinalReport.csv";

    public static void main(String[] args) {
        ReaderService fileReader = new ReaderServiceImpl();
        List<String> inputReport = fileReader.read(READ_FILE_PATH);

        Map<Operation, OperationHandler> operationHandlers = new HashMap<>();
        operationHandlers.put(Operation.BALANCE, new BalanceOperationImpl());
        operationHandlers.put(Operation.PURCHASE, new PurchaseOperationImpl());
        operationHandlers.put(Operation.RETURN, new ReturnOperationImpl());
        operationHandlers.put(Operation.SUPPLY, new SupplyOperationImpl());
        OperationStrategy operationStrategy = new OperationStrategyImpl(operationHandlers);

        DataConverter dataConverter = new DataConverterImpl();
        List<FruitTransaction> transactions = dataConverter.convertToTransaction(inputReport);

        ShopService shopService = new ShopServiceImpl(operationStrategy);
        shopService.process(transactions);

        ReportGenerator reportGenerator = new ReportGeneratorImpl();
        String finalReport = reportGenerator.getReport();

        WriterService fileWriter = new WriterServiceImpl();
        fileWriter.write(finalReport, WRITE_FILE_PATH);
    }
}
