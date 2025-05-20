# ExpenseTracker

# Expense Tracker - Java Console Application

## Overview

This is a simple Java-based console application to help users track their incomes and expenses. The application allows users to:

- Add income and expense transactions manually
- Categorize transactions (e.g., salary, business, food, rent, travel, other)
- Load transactions from a CSV file
- Save transactions to a CSV file
- View monthly summaries with total income, expenses, net savings, and category-wise breakdowns

The application uses modern Java features like streams and the `java.nio.file` package for efficient file handling.

---

## Project Structure

- `ExpenseTracker.java`  
  Main driver class handling user interaction and menu system.

- `TransactionManager.java`  
  Handles storage of transactions, file loading/saving, and summary calculations.

- `Transaction.java`  
  Data model representing a single income or expense transaction.

- `TransactionType.java`  
  Enum defining two types of transactions: `INCOME` and `EXPENSE`.

---

## File Format for CSV Load/Save

Each transaction is represented as a single line in CSV format:




---

## Application Flow

1. **Startup:**  
   User is greeted with a menu offering options to add transactions, view summaries, load/save files, or exit.

2. **Add Transaction:**  
   User inputs type (`income` or `expense`), selects category from predefined lists, enters amount, date (defaults to today if blank), and optional description. The transaction is added to memory.

3. **View Monthly Summary:**  
   User inputs month and year (e.g., `2025-05`).  
   The program filters all transactions for that month and calculates:  
   - Total Income  
   - Total Expense  
   - Net Savings (Income - Expense)  
   - Breakdown of income and expenses by category

4. **Load from File:**  
   User provides a CSV file path.  
   Existing transactions in memory are cleared and replaced by the file contents.  
   The file is parsed line-by-line with validation.

5. **Save to File:**  
   User provides a file path.  
   All current transactions in memory are saved in CSV format to the specified file.

6. **Exit:**  
   Program terminates.

---

## Notes and Improvements

- Paths entered by users can be wrapped in quotes; the program automatically strips these to avoid errors.
- File loading clears previous transactions to avoid duplicates. You may extend functionality to allow merging with duplicates handling.
- Date input validation ensures only valid ISO dates are accepted.
- Amount must be positive and numeric.
- Categories are fixed but can be extended easily by modifying the category lists.

---

## How to Compile and Run

1. Compile all `.java` files:

```bash
javac ExpenseTracker.java TransactionManager.java Transaction.java TransactionType.java


## Project Snippets

![Screenshot 1](images/Screenshot%202025-05-20%20151115.png)

![Screenshot 2](images/Screenshot%202025-05-20%20151205.png)

![Screenshot 3](images/Screenshot%202025-05-20%20152810.png)

![Screenshot 4](images/Screenshot%202025-05-20%20152838.png)



