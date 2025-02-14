package com.example.dairydelight.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.dairydelight.Models.CartModel;

import java.util.ArrayList;
import java.util.List;

public class CartDatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "cart.db";
    private static final int DATABASE_VERSION = 1;

    // Table and columns
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUANTITY = "quantity";
    private static final String COLUMN_IS_ADDED_TO_CART = "isAddedToCart";

    // Constructor
    public CartDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL query to create the cart table
        String CREATE_TABLE = "CREATE TABLE " + TABLE_CART + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_IMAGE + " TEXT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_DESCRIPTION + " TEXT, "
                + COLUMN_PRICE + " REAL, "
                + COLUMN_QUANTITY + " INTEGER, "
                + COLUMN_IS_ADDED_TO_CART + " INTEGER DEFAULT 0)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older table if exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        onCreate(db);
    }

    // Create (Insert) - Add an item to the cart
    public long addItemToCart(CartModel item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGE, item.getcImage());
        values.put(COLUMN_NAME, item.getcTitle());
        values.put(COLUMN_DESCRIPTION, item.getcDescription());
        values.put(COLUMN_PRICE, item.getcPrice());
        values.put(COLUMN_QUANTITY, item.getCquantity());
        values.put(COLUMN_IS_ADDED_TO_CART, 1);  // Mark item as added to cart

        long result = db.insert(TABLE_CART, null, values);  // Insert into database
        db.close();
        return result;
    }

    // Read (Get all items) - Get all items in the cart
    public List<CartModel> getAllCartItems() {
        List<CartModel> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COLUMN_IS_ADDED_TO_CART + " = 1", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                CartModel item = new CartModel();
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                item.setcImage(cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE)));
                item.setcTitle(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                item.setcDescription(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                item.setcPrice(cursor.getDouble(cursor.getColumnIndex(COLUMN_PRICE)));
                item.setCquantity(cursor.getInt(cursor.getColumnIndex(COLUMN_QUANTITY)));
                cartItems.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cartItems;
    }

    // Update - Update the quantity of a cart item
    public void updateCartItemQuantity(int id, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_QUANTITY, quantity);

        db.update(TABLE_CART, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Delete - Delete a cart item by ID
    public void deleteCartItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

}
