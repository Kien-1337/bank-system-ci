package bank_system;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Transaction {
    public static final int TYPE_DEPOSIT_CHECKING = 1;
    public static final int TYPE_WITHDRAW_CHECKING = 2;
    public static final int TYPE_DEPOSIT_SAVINGS = 3;
    public static final int TYPE_WITHDRAW_SAVINGS = 4;

    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);

    private int type;
    private double amount;
    private double initialBalance;
    private double finalBalance;

    public Transaction(int type, double amount, double initialBalance, double finalBalance) {
        this.type = type;
        this.amount = amount;
        this.initialBalance = initialBalance;
        this.finalBalance = finalBalance;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(double initialBalance) {
        this.initialBalance = initialBalance;
    }

    public double getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(double finalBalance) {
        this.finalBalance = finalBalance;
    }

    public static String getTransactionTypeString(int transactionType) {
        switch (transactionType) {
            case TYPE_DEPOSIT_CHECKING:
                return "Nạp tiền vãng lai";
            case TYPE_WITHDRAW_CHECKING:
                return "Rút tiền vãng lai";
            case TYPE_DEPOSIT_SAVINGS:
                return "Nạp tiền tiết kiệm";
            case TYPE_WITHDRAW_SAVINGS:
                return "Rút tiền tiết kiệm";
            default:
                return "Không rõ";
        }
    }

    public String getTransactionSummary() {
        LOGGER.debug("Generating transaction summary for type={}", type);
        String typeText = getTransactionTypeString(type);
        String initStr = String.format(Locale.US, "%.2f", initialBalance);
        String amountStr = String.format(Locale.US, "%.2f", amount);
        String finalStr = String.format(Locale.US, "%.2f", finalBalance);

        return "- Kiểu giao dịch: "
                + typeText
                + ". Số dư ban đầu: $"
                + initStr
                + ". Số tiền: $"
                + amountStr
                + ". Số dư cuối: $"
                + finalStr
                + ".";
    }
}