import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TransactionManager {
    private final List<Transaction> transactions = new ArrayList<>();

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> getTransactions() {
        return Collections.unmodifiableList(transactions);
    }


    public void loadFromFile(Path filePath) throws IOException {
        transactions.clear(); // Clear existing transactions
        try (Stream<String> lines = Files.lines(filePath)) {
            int[] count = {0}; // Using array for mutable count inside lambda

            lines.forEach(line -> {
                if (line.trim().isEmpty() || line.startsWith("#")) return;

                String[] parts = line.split(",", 5);
                if (parts.length < 4) {
                    System.out.println("Invalid line (skipping): " + line);
                    return;
                }

                for (int i = 0; i < parts.length; i++) {
                    parts[i] = parts[i].trim();
                }

                TransactionType type;
                if (parts[0].equalsIgnoreCase("income")) type = TransactionType.INCOME;
                else if (parts[0].equalsIgnoreCase("expense")) type = TransactionType.EXPENSE;
                else {
                    System.out.println("Unknown transaction type in line (skipping): " + line);
                    return;
                }

                LocalDate date;
                try {
                    date = LocalDate.parse(parts[1], DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    System.out.println("Invalid date in line (skipping): " + line + " (" + e.getMessage() + ")");
                    return;
                }

                String category = parts[2].toLowerCase();

                double amount;
                try {
                    amount = Double.parseDouble(parts[3]);
                    if (amount < 0) {
                        System.out.println("Negative amount in line (skipping): " + line);
                        return;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid amount in line (skipping): " + line + " (" + e.getMessage() + ")");
                    return;
                }

                String description = parts.length == 5 ? parts[4].trim() : "";

                transactions.add(new Transaction(type, date, category, amount, description));
                count[0]++;
            });

            System.out.println(count[0] + " transactions loaded successfully.");
        }
    }


    public void saveToFile(Path filePath) throws IOException {
        List<String> lines = transactions.stream()
                .map(Transaction::toCsv)
                .collect(Collectors.toList());
        Files.write(filePath, lines);
        System.out.println("Transactions saved to file successfully.");
    }

    public void printMonthlySummary(YearMonth ym) {
        double totalIncome = 0;
        double totalExpense = 0;
        Map<String, Double> incomeByCategory = new HashMap<>();
        Map<String, Double> expenseByCategory = new HashMap<>();

        for (Transaction t : transactions) {
            if (YearMonth.from(t.getDate()).equals(ym)) {
                if (t.getType() == TransactionType.INCOME) {
                    totalIncome += t.getAmount();
                    incomeByCategory.put(t.getCategory(),
                            incomeByCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                } else {
                    totalExpense += t.getAmount();
                    expenseByCategory.put(t.getCategory(),
                            expenseByCategory.getOrDefault(t.getCategory(), 0.0) + t.getAmount());
                }
            }
        }

        System.out.println("\nSummary for " + ym);
        System.out.println("-------------------------");
        System.out.printf("Total Income: %.2f\n", totalIncome);
        System.out.printf("Total Expense: %.2f\n", totalExpense);
        System.out.printf("Net Savings: %.2f\n", totalIncome - totalExpense);

        System.out.println("\nIncome by Category:");
        if (incomeByCategory.isEmpty()) {
            System.out.println("  None");
        } else {
            incomeByCategory.forEach((cat, amt) -> System.out.printf("  %s: %.2f\n", cat, amt));
        }

        System.out.println("\nExpense by Category:");
        if (expenseByCategory.isEmpty()) {
            System.out.println("  None");
        } else {
            expenseByCategory.forEach((cat, amt) -> System.out.printf("  %s: %.2f\n", cat, amt));
        }
    }
}
