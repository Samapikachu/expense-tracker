package com.example.expense;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.Calendar;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import androidx.appcompat.app.AppCompatActivity;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText editTextDate, editTextAmount, editTextDescription;
    private Spinner spinnerCategory;
    private ExpenseDBHelper expenseDBHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        editTextDate = findViewById(R.id.editTextDate);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);

        expenseDBHelper = new ExpenseDBHelper(this);
        userId = getIntent().getIntExtra("userId", -1);
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }
    private void showDatePickerDialog() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                        editTextDate.setText(date);
                    }
                }, year, month, dayOfMonth);

        // Show DatePickerDialog
        datePickerDialog.show();
    }

    public void addExpense(View view) {
        String date = editTextDate.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();
        double amount = Double.parseDouble(editTextAmount.getText().toString());
        String description = editTextDescription.getText().toString();


        long result = expenseDBHelper.insertExpense(userId,date, category, amount, description);
        if (result != -1) {
            // Successfully added expense
            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            // Failed to add expense
            Toast.makeText(this, "Failed to add expense", Toast.LENGTH_SHORT).show();

        }
    }
}
