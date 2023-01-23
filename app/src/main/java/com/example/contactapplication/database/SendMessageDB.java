package com.example.contactapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.contactapplication.model.SendMessageModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SendMessageDB extends SQLiteOpenHelper {
    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "details";
    private static final String SEND_MESSAGE_TABLE = "send_message";

    private static final String ID = "id";
    private static final String CONTACT_NAME = "name";
    private static final String SENT_MESSAGE = "message";
    private static final String MESSAGE_SENT_TIME = "datetime";

    public SendMessageDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String query = "CREATE TABLE " + SEND_MESSAGE_TABLE + " ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CONTACT_NAME + " TEXT, "
                + SENT_MESSAGE + " TEXT, "
                + MESSAGE_SENT_TIME + " TEXT)";

        database.execSQL(query);
    }

    public void updateSentMessage(String name, String message, String dateTime) {
        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, name);
        values.put(SENT_MESSAGE, message);
        values.put(MESSAGE_SENT_TIME, dateTime);

        SQLiteDatabase database = this.getWritableDatabase();
        database.insert(SEND_MESSAGE_TABLE, null, values);

        database.close();
    }

    public List<SendMessageModel> fetchSchedulePlant() {
        List<SendMessageModel> sendMessageModels = new ArrayList<>();

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + SEND_MESSAGE_TABLE, null);
        if (cursor.moveToFirst()) {
            do {
                sendMessageModels.add(new SendMessageModel(
                        cursor.getString(1),
                        cursor.getString(2),
                        LocalDateTime.parse(cursor.getString(3))));

            } while (cursor.moveToNext());
        }
        cursor.close();

        return sendMessageModels;
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int olderVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SEND_MESSAGE_TABLE);
        onCreate(sqLiteDatabase);
    }
}
