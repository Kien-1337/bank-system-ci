package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SavingsAccount extends Account {
    public static final double MAX_WITHDRAW_AMOUNT = 1000.0;
    public static final double MIN_REQUIRED_BALANCE = 5000.0;

    private static final Logger LOGGER = LoggerFactory.getLogger(SavingsAccount.class);

    public SavingsAccount(long accountNumber, double balance) {
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
                            Transaction.TYPE_DEPOSIT_SAVINGS, amount, initialBalance, finalBalance);
            addTransaction(transaction);
            LOGGER.info(
                    "Savings deposit succeeded: accountId={}, amount={}, newBalance={}",
                    getAccountNumber(),
                    amount,
                    finalBalance);
        } catch (InvalidFundingAmountException e) {
            LOGGER.warn(
                    "Savings deposit failed: accountId={}, amount={}, reason={}",
                    getAccountNumber(),
                    amount,
                    e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            if (amount > MAX_WITHDRAW_AMOUNT) {
                throw new InvalidFundingAmountException(amount);
            }
            if (initialBalance - amount < MIN_REQUIRED_BALANCE) {
                throw new InsufficientFundsException(amount);
            }

            doWithdrawing(amount);
            double finalBalance = getBalance();
            Transaction transaction =
                    new Transaction(
                            Transaction.TYPE_WITHDRAW_SAVINGS, amount, initialBalance, finalBalance);
            addTransaction(transaction);
            LOGGER.info(
                    "Savings withdrawal succeeded: accountId={}, amount={}, newBalance={}",
                    getAccountNumber(),
                    amount,
                    finalBalance);
        } catch (BankException e) {
            LOGGER.warn(
                    "Savings withdrawal failed: accountId={}, amount={}, reason={}",
                    getAccountNumber(),
                    amount,
                    e.getMessage());
        }
    }
}