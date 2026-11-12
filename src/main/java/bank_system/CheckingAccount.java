package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CheckingAccount extends Account {
    private static final Logger LOGGER = LoggerFactory.getLogger(CheckingAccount.class);

    public CheckingAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();
            Transaction transaction =
                    new Transaction(
                            Transaction.TYPE_DEPOSIT_CHECKING, amount, initialBalance, finalBalance);
            addTransaction(transaction);
            LOGGER.info(
                    "Checking deposit succeeded: accountId={}, amount={}, newBalance={}",
                    getAccountNumber(),
                    amount,
                    finalBalance);
        } catch (InvalidFundingAmountException e) {
            LOGGER.warn(
                    "Checking deposit failed: accountId={}, amount={}, reason={}",
                    getAccountNumber(),
                    amount,
                    e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            doWithdrawing(amount);
            double finalBalance = getBalance();
            Transaction transaction =
                    new Transaction(
                            Transaction.TYPE_WITHDRAW_CHECKING, amount, initialBalance, finalBalance);
            addTransaction(transaction);
            LOGGER.info(
                    "Checking withdrawal succeeded: accountId={}, amount={}, newBalance={}",
                    getAccountNumber(),
                    amount,
                    finalBalance);
        } catch (BankException e) {
            LOGGER.warn(
                    "Checking withdrawal failed: accountId={}, amount={}, reason={}",
                    getAccountNumber(),
                    amount,
                    e.getMessage());
        }
    }
}