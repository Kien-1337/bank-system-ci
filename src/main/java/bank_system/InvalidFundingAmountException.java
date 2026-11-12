package bank_system;

import java.util.Locale;

public class InvalidFundingAmountException extends BankException {

    public InvalidFundingAmountException(double amount) {
        super("Số tiền không hợp lệ: $" + String.format(Locale.US, "%.2f", amount));
    }
}