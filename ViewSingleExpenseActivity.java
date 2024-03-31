package com.example.expense;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class ViewSingleExpenseActivity extends AppCompatActivity {

    private TextView textViewDate;
    private TextView textViewCategory;
    private TextView textViewAmount;
    private TextView textViewDescription;

    private ExpenseDBHelper expenseDBHelper;
    private long expenseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_single_expense);

        textViewDate = findViewById(R.id.textViewDate);
        textViewCategory = findViewById(R.id.textViewCategory);
        textViewAmount = findViewById(R.id.textViewAmount);
        textViewDescription = findViewById(R.id.textViewDescription);
        Button btnDeleteExpense = findViewById(R.id.btnDeleteExpense);

        // Initialize ExpenseDBHelper
        expenseDBHelper = new ExpenseDBHelper(this);

        // Retrieve expense ID passed from previous activity
        expenseId = getIntent().getLongExtra("EXPENSE_ID", -1);

        // Display expense details
        displayExpenseDetails();

        // Set up click listener for delete button
        btnDeleteExpense.setOnClickListener(v -> onDeleteExpense());
    }

    private void displayExpenseDetails() {
        // Retrieve expense details from database using expense ID
        Expense expense = expenseDBHelper.getExpenseById(expenseId);

        // Display expense details in TextViews
        textViewDate.setText(expense.getDate());
        textViewCategory.setText(expense.getCategory());
        textViewAmount.setText(String.valueOf(expense.getAmount()));
        textViewDescription.setText(expense.getDescription());
    }

    private void onDeleteExpense() {
        // Delete the expense from the database
        ExpenseDBHelper.deleteExpenseById(expenseId);

        // Display a toast message indicating successful deletion
        Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();

        // Close this activity and return to the previous one (ViewExpenseActivity)
        finish();
    }
}
