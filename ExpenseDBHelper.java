package com.example.expense;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "expense.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "expenses";
    public static final String COLUMN_ID = "_id";
    private static final String COLUMN_DATE = "date";
    public static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_INCOME = "income";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USER_ID + " INTEGER, " +
            COLUMN_DATE + " TEXT, " +
            COLUMN_CATEGORY + " TEXT, " +
            COLUMN_AMOUNT + " REAL, " +
            COLUMN_DESCRIPTION + " TEXT," +
            COLUMN_INCOME + " REAL)";

    public ExpenseDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long insertExpense(int userId, String date, String category, double amount, String description ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_CATEGORY, category);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    @SuppressLint("Range")
    public double getTotalSpentAmount(int userId) {
        double totalSpentAmount = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + COLUMN_AMOUNT + ") AS total_spent FROM " + TABLE_NAME +
                " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.moveToFirst()) {
            totalSpentAmount = cursor.getDouble(cursor.getColumnIndex("total_spent"));
            cursor.close();
        }
        return totalSpentAmount;
    }
    @SuppressLint("Range")
    public Expense getExpenseById(long expenseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, COLUMN_ID + "=?", new String[]{String.valueOf(expenseId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            // Extract expense details from the cursor and return
            Expense expense = new Expense();
            expense.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
            expense.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            expense.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            expense.setCategory(cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)));
            expense.setAmount(cursor.getDouble(cursor.getColumnIndex(COLUMN_AMOUNT)));
            expense.setDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
            cursor.close();
            return expense;
        } else {
            return null;
        }
    }

    // New method to delete an expense by its ID
    public void deleteExpenseById(long expenseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(expenseId)});
        db.close();
    }
}



