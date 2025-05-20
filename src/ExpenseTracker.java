import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExpenseTracker {

    private static final Scanner scanner = new Scanner(System.in);
    private static final TransactionManager manager = new TransactionManager();

    private static final Map<TransactionType, List<String>> categories = new HashMap<>();

    static {
        categories.put(TransactionType.INCOME, Arrays.asList("salary", "business", "other"));
        categories.put(TransactionType.EXPENSE, Arrays.asList("food", "rent", "travel", "other"));
    }

    public static void main(String[] args) {
        System.out.println("Welcome to Expense Tracker!");

        while (true) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Add Income/Expense");
            System.out.println("2. View Monthly Summary");
            System.out.println("3. Load transactions from file");
            System.out.println("4. Save transactions to file");
            System.out.println("5. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTransaction();
                    break;
                case "2":
                    viewMonthlySummary();
                    break;
                case "3":
                    loadFromFile();
                    break;
                case "4":
                    saveToFile();
                    break;
                case "5":
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private static void addTransaction() {
        TransactionType type = null;
        while (type == null) {
            System.out.print("Enter type (income/expense): ");
            String t = scanner.nextLine().trim().toLowerCase();
            if (t.equals("income")) type = TransactionType.INCOME;
            else if (t.equals("expense")) type = TransactionType.EXPENSE;
            else System.out.println("Invalid type, please enter 'income' or 'expense'.");
        }

        List<String> subCategories = categories.get(type);
        System.out.println("Choose category:");
        for (int i = 0; i < subCategories.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, subCategories.get(i));
        }

        int catChoice = -1;
        while (catChoice < 1 || catChoice > subCategories.size()) {
            System.out.print("Category number: ");
            try {
                catChoice = Integer.parseInt(scanner.nextLine().trim());
                if (catChoice < 1 || catChoice > subCategories.size()) {
                    System.out.println("Invalid number, try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, enter a number.");
            }
        }
        String category = subCategories.get(catChoice - 1);

        double amount = -1;
        while (amount <= 0) {
            System.out.print("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0) {
                    System.out.println("Amount must be positive.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount, enter a number.");
            }
        }

        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (yyyy-MM-dd) or leave blank for today: ");
            String dateInput = scanner.nextLine().trim();
            if (dateInput.isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(dateInput, DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (Exception e) {
                    System.out.println("Invalid date format. Please try again.");
                }
            }
        }

        System.out.print("Enter description (optional): ");
        String description = scanner.nextLine().trim();

        Transaction transaction = new Transaction(type, date, category, amount, description);
        manager.addTransaction(transaction);
        System.out.println("Transaction added: " + transaction);
    }

    private static void viewMonthlySummary() {
        System.out.print("Enter month and year to view summary (yyyy-MM), leave blank for current month: ");
        String input = scanner.nextLine().trim();
        YearMonth ym;
        if (input.isEmpty()) {
            ym = YearMonth.now();
        } else {
            try {
                ym = YearMonth.parse(input);
            } catch (Exception e) {
                System.out.println("Invalid format. Using current month.");
                ym = YearMonth.now();
            }
        }

        manager.printMonthlySummary(ym);
    }

    private static void loadFromFile() {
        System.out.print("Enter file path to load transactions: ");
        String inputPath = scanner.nextLine().trim();

        // Remove surrounding quotes if present
        if (inputPath.startsWith("\"") && inputPath.endsWith("\"") && inputPath.length() > 1) {
            inputPath = inputPath.substring(1, inputPath.length() - 1);
        }

        Path filePath;
        try {
            filePath = Path.of(inputPath);
        } catch (InvalidPathException e) {
            System.out.println("Invalid file path format. Please try again.");
            return;
        }

        if (!Files.exists(filePath)) {
            System.out.println("File does not exist.");
            return;
        }

        try {
            manager.loadFromFile(filePath);
        } catch (IOException e) {
            System.out.println("Error loading from file: " + e.getMessage());
        }
    }


    private static void saveToFile() {
        System.out.print("Enter file path to save transactions: ");
        String inputPath = scanner.nextLine().trim();

        // Remove surrounding quotes if present
        if (inputPath.startsWith("\"") && inputPath.endsWith("\"") && inputPath.length() > 1) {
            inputPath = inputPath.substring(1, inputPath.length() - 1);
        }

        Path filePath;
        try {
            filePath = Path.of(inputPath);
        } catch (InvalidPathException e) {
            System.out.println("Invalid file path format. Please try again.");
            return;
        }

        try {
            manager.saveToFile(filePath);
        } catch (Exception e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }

}
