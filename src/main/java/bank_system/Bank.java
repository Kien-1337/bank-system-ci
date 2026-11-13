package bank_system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bank {
    private static final String CUSTOMER_ID_REGEX = "\\d{9}";
    private static final int ACCOUNT_PARTS_MIN_LENGTH = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(Bank.class);

    private List<Customer> customerList;

    public Bank() {
        this.customerList = new ArrayList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<>();
        } else {
            this.customerList = customerList;
        }
    }

    public void readCustomerList(InputStream inputStream) {
        if (inputStream == null) {
            LOGGER.warn("Input stream is null; skipping customer import");
            return;
        }

        LOGGER.info("Starting customer list import from input stream");
        Customer currentCustomer = null;

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                if (!trimmedLine.isEmpty()) {
                    currentCustomer = processLine(trimmedLine, currentCustomer);
                }
            }
            LOGGER.info("Completed customer import: totalCustomers={}", customerList.size());
        } catch (IOException | NumberFormatException e) {
            LOGGER.error("Failed to parse customer list: error={}", e.getMessage(), e);
        }
    }

    private Customer processLine(String line, Customer currentCustomer) {
        int lastSpace = line.lastIndexOf(' ');
        if (lastSpace <= 0) {
            return currentCustomer;
        }

        String lastToken = line.substring(lastSpace + 1).trim();
        if (lastToken.matches(CUSTOMER_ID_REGEX)) {
            String fullName = line.substring(0, lastSpace).trim();
            long idNumber = Long.parseLong(lastToken);
            Customer newCustomer = new Customer(idNumber, fullName);
            customerList.add(newCustomer);
            LOGGER.debug("Loaded customer: idNumber={}, fullName={}", idNumber, fullName);
            return newCustomer;
        }

        if (currentCustomer != null) {
            parseAndAttachAccount(line, currentCustomer);
        }
        return currentCustomer;
    }

    private void parseAndAttachAccount(String line, Customer customer) {
        String[] parts = line.split("\\s+");
        if (parts.length < ACCOUNT_PARTS_MIN_LENGTH) {
            LOGGER.warn("Malformed account record skipped: line={}", line);
            return;
        }

        long accountNumber = Long.parseLong(parts[0]);
        String accountType = parts[1];
        double balance = Double.parseDouble(parts[2]);

        if (Account.CHECKING.equals(accountType)) {
            customer.addAccount(new CheckingAccount(accountNumber, balance));
        } else if (Account.SAVINGS.equals(accountType)) {
            customer.addAccount(new SavingsAccount(accountNumber, balance));
        } else {
            LOGGER.warn("Unknown account type skipped: type={}, accountId={}", accountType, accountNumber);
        }
    }

    public String getCustomersInfoByIdOrder() {
        List<Customer> sorted = new ArrayList<>(customerList);
        Collections.sort(
                sorted,
                new Comparator<Customer>() {
                    @Override
                    public int compare(Customer c1, Customer c2) {
                        return Long.compare(c1.getIdNumber(), c2.getIdNumber());
                    }
                });
        return formatCustomerList(sorted);
    }

    public String getCustomersInfoByNameOrder() {
        List<Customer> sorted = new ArrayList<>(customerList);
        Collections.sort(
                sorted,
                (c1, c2) -> {
                    int nameCmp = c1.getFullName().compareTo(c2.getFullName());
                    if (nameCmp != 0) {
                        return nameCmp;
                    }
                    return Long.compare(c1.getIdNumber(), c2.getIdNumber());
                });
        return formatCustomerList(sorted);
    }

    private String formatCustomerList(List<Customer> customers) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < customers.size(); i++) {
            sb.append(customers.get(i).getCustomerInfo());
            if (i < customers.size() - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}