package com.example.expense;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {

    private double totalIncome = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        // Show dialog to get total income from the user
        showIncomeInputDialog();
    }

    private void showIncomeInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View promptView = inflater.inflate(R.layout.dialog_income_input, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(promptView);

        final EditText editTextIncome = promptView.findViewById(R.id.editTextIncome);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Get user's input and store it as totalIncome
                        String incomeStr = editTextIncome.getText().toString().trim();
                        if (!incomeStr.isEmpty()) {
                            totalIncome = Double.parseDouble(incomeStr);
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void addExpense(View view) {
        Intent intent = new Intent(this, AddExpenseActivity.class);
        startActivity(intent);
    }

    public void viewExpenses(View view) {
        Intent intent = new Intent(this, ViewExpenseActivity.class);
        // Pass total income to ViewExpenseActivity
        intent.putExtra("TOTAL_INCOME", totalIncome);
        startActivity(intent);
    }
}
