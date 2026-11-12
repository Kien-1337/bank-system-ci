package bank_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

public class BankSystemTest {

    @Test
    void testCheckingAndSavingsTransactions() {
        CheckingAccount checking = new CheckingAccount(1001L, 500.0);
        checking.deposit(200.0);
        checking.withdraw(100.0);
        assertEquals(600.0, checking.getBalance());
        assertEquals(2, checking.getTransactionList().size());

        SavingsAccount savings = new SavingsAccount(2001L, 6000.0);
        savings.withdraw(500.0);
        savings.withdraw(1500.0);
        assertEquals(5500.0, savings.getBalance());
    }

    @Test
    void testBankImportAndSorting() {
        String data =
                "Nguyen Van A 123456789\n"
                        + "1001 CHECKING 1500.0\n"
                        + "2001 SAVINGS 7000.0\n";
        Bank bank = new Bank();
        bank.readCustomerList(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));

        assertEquals(1, bank.getCustomerList().size());
        assertTrue(bank.getCustomersInfoByNameOrder().contains("Nguyen Van A"));
    }

    @Test
    void testCrossPlatformFilePath() {
        File reportFile = new File("reports", "daily-summary.txt");
        String hardcodedLinuxPath = "reports/daily-summary.txt";
        assertEquals(hardcodedLinuxPath, reportFile.getPath());
    }
}