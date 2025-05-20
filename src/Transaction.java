import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private TransactionType type;
    private LocalDate date;
    private String category;
    private double amount;
    private String description;

    public Transaction(TransactionType type, LocalDate date, String category, double amount, String description) {
        this.type = type;
        this.date = date;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public TransactionType getType() {
        return type;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s | %s | %.2f | %s | %s",
                type, date, amount, category, description);
    }

    public String toCsv() {
        return String.format("%s,%s,%s,%.2f,%s",
                type.toString().toLowerCase(),
                date.format(DateTimeFormatter.ISO_LOCAL_DATE),
                category,
                amount,
                description.replace(",", " ")); // Avoid commas in CSV
    }
}
