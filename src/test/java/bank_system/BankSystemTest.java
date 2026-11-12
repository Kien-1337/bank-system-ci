package bank_system;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;

public class BankSystemTest {

    @Test
    void testCheckingAndSavingsTransactions() {
        CheckingAccount checking = new CheckingAccount(1001L, 500.0);
        checking.deposit(200.0);
        checking.withdraw(100.0);
        checking.deposit(-20.0);
        checking.withdraw(5000.0);
        assertEquals(600.0, checking.getBalance());
        assertEquals(2, checking.getTransactionList().size());
        assertTrue(checking.getTransactionHistory().contains("1001"));

        SavingsAccount savings = new SavingsAccount(2001L, 6000.0);
        savings.deposit(500.0);
        savings.withdraw(500.0);
        savings.withdraw(1500.0);
        savings.deposit(-50.0);
        assertEquals(6000.0, savings.getBalance());
        assertEquals(2001L, savings.getAccountNumber());
    }

    @Test
    void testAccountExceptionsAndTransactions() throws BankException {
        CheckingAccount checking = new CheckingAccount(1002L, 300.0);
        assertThrows(InvalidFundingAmountException.class, () -> checking.doDepositing(-50.0));
        assertThrows(InvalidFundingAmountException.class, () -> checking.doWithdrawing(-10.0));
        assertThrows(InsufficientFundsException.class, () -> checking.doWithdrawing(1000.0));

        SavingsAccount savings = new SavingsAccount(2002L, 4000.0);
        assertThrows(InsufficientFundsException.class, () -> savings.doWithdrawing(4500.0));
        assertThrows(InvalidFundingAmountException.class, () -> savings.doWithdrawing(-10.0));

        Transaction tx1 = new Transaction(Transaction.TYPE_DEPOSIT_CHECKING, 100.0, 200.0, 300.0);
        Transaction tx2 = new Transaction(Transaction.TYPE_WITHDRAW_CHECKING, 50.0, 300.0, 250.0);
        Transaction tx3 = new Transaction(Transaction.TYPE_DEPOSIT_SAVINGS, 500.0, 6000.0, 6500.0);
        Transaction tx4 = new Transaction(Transaction.TYPE_WITHDRAW_SAVINGS, 500.0, 6500.0, 6000.0);
        assertNotNull(tx1.getTransactionSummary());
        assertNotNull(tx2.getTransactionSummary());
        assertNotNull(tx3.getTransactionSummary());
        assertNotNull(tx4.getTransactionSummary());
    }

    @Test
    void testCustomerAndAccountEquality() {
        Customer c1 = new Customer(111222333L, "Tran Van C");
        Customer c2 = new Customer();
        c2.setFullName("Le Thi D");
        c2.setIdNumber(444555666L);
        assertEquals("Tran Van C", c1.getFullName());
        assertEquals(111222333L, c1.getIdNumber());
        assertEquals("Le Thi D", c2.getFullName());
        assertEquals(444555666L, c2.getIdNumber());
        assertTrue(c1.getCustomerInfo().contains("Tran Van C"));

        Account a1 = new CheckingAccount(3001L, 1000.0);
        Account a2 = new CheckingAccount(3001L, 2000.0);
        Account a3 = new SavingsAccount(3002L, 8000.0);
        assertEquals(a1, a2);
        assertNotEquals(a1, a3);
        assertFalse(a1.equals("not-an-account"));
        c1.addAccount(a1);
        c1.addAccount(a3);
        assertEquals(2, c1.getAccountList().size());
        c1.removeAccount(a1);
        assertEquals(1, c1.getAccountList().size());
    }

    @Test
    void testBankImportAndSorting() {
        String data =
                "Nguyen Van B 987654321\n"
                        + "1003 CHECKING 1500.0\n"
                        + "Nguyen Van A 123456789\n"
                        + "2003 SAVINGS 7000.0\n";
        Bank bank = new Bank();
        bank.readCustomerList(new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8)));

        assertEquals(2, bank.getCustomerList().size());
        String byName = bank.getCustomersInfoByNameOrder();
        String byId = bank.getCustomersInfoByIdOrder();
        assertTrue(byName.indexOf("Nguyen Van A") < byName.indexOf("Nguyen Van B"));
        assertTrue(byId.indexOf("123456789") < byId.indexOf("987654321"));
    }

    @Test
    void testCrossPlatformFilePath() {
        File reportFile = new File("reports", "daily-summary.txt");
        Path expectedPath = Paths.get("reports", "daily-summary.txt");
        assertEquals(expectedPath.toString(), reportFile.getPath());
        assertEquals("reports" + File.separator + "daily-summary.txt", reportFile.getPath());
    }
}