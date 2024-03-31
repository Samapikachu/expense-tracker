package com.example.expense;

import android.annotation.SuppressLint;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class ViewExpenseActivity extends AppCompatActivity {
    private ListView listViewExpenses;
    private ExpenseDBHelper expenseDBHelper;
    private TextView totalIncomeTextView;
    private TextView spentAmountTextView;
    private TextView balanceTextView;
    private double totalIncome;
    private int userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_expense);

        listViewExpenses = findViewById(R.id.listViewExpenses);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);
        spentAmountTextView = findViewById(R.id.spentAmountTextView);
        balanceTextView = findViewById(R.id.balanceTextView);
        Button btnDelete = findViewById(R.id.btnDelete);

        // Retrieve user ID and total income from intent extras
        userId = getIntent().getIntExtra("USER_ID", -1); // Replace -1 with default value if needed
        totalIncome = getIntent().getDoubleExtra("TOTAL_INCOME", 0.0);

        // Initialize ExpenseDBHelper
        expenseDBHelper = new ExpenseDBHelper(this);

        populateExpensesListView();

        // Display total income, spent amount, and balance
        displayFinancialSummary();

        btnDelete.setOnClickListener(v -> onDeleteSelectedExpenses());
    }

    private void populateExpensesListView() {
        Cursor cursor = expenseDBHelper.getAllExpenses();
        if (cursor != null && cursor.getCount() > 0) {
            String[] fromColumns = {"date", "category", "amount", "description"};
            int[] toViews = {R.id.textViewDate, R.id.textViewCategory, R.id.textViewAmount, R.id.textViewDescription};
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_item_expense, cursor, fromColumns, toViews, 0);
            listViewExpenses.setAdapter(adapter);
        } else {
            listViewExpenses.setVisibility(View.GONE);
            Toast.makeText(this, "No expenses to display", Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("DefaultLocale")
    private void displayFinancialSummary() {
        // Get total spent amount from the database
        double spentAmount = expenseDBHelper.getTotalSpentAmount(userId);

        // Calculate balance
        double balance = totalIncome - spentAmount;

        // Update TextViews with financial data
        totalIncomeTextView.setText(String.format("Total Income: Rs%.2f", totalIncome));
        spentAmountTextView.setText(String.format("Spent Amount: Rs%.2f", spentAmount));
        balanceTextView.setText(String.format("Balance: Rs%.2f", balance));
    }

    public void onDeleteSelectedExpenses() {
        SparseBooleanArray selectedItems = listViewExpenses.getCheckedItemPositions();
        if (selectedItems != null) {
            SQLiteDatabase db = expenseDBHelper.getWritableDatabase();
            for (int i = selectedItems.size() - 1; i >= 0; i--) {
                if (selectedItems.valueAt(i)) {
                    int position = selectedItems.keyAt(i);
                    Cursor cursor = (Cursor) listViewExpenses.getItemAtPosition(position);
                    long id = cursor.getLong(cursor.getColumnIndexOrThrow(ExpenseDBHelper.COLUMN_ID));
                    db.delete(ExpenseDBHelper.TABLE_NAME, ExpenseDBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
                }
            }
            db.close();
            // Refresh the ListView
            populateExpensesListView();
            // Clear selected items
            listViewExpenses.clearChoices();
        }
    }


}
