package ru.bidone.hint.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class dbWrapper {

    private static final String TAG = "dbWrapper";

    private static final String DB_NAME = "SAT.db";
    private static final int DB_VERSION = 1;

    private final Context context;
    private Cursor cursor;
    private SQLiteDatabase database;

    private final SharedPreferences sp;

    public dbWrapper(Context context) {
        this.context = context;
        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public ArrayList<MSArray> getSubjects() {
        return select("SELECT subjectID, subjectName FROM subjects;");
    }

    public ArrayList<MSArray> getArticles(int subID) {
        return select("SELECT articleID, articleName FROM articles WHERE subjectDfk = " + subID + ";");
    }

    public String getArticle(int artID){
        return select("SELECT articleText FROM articles WHERE articleID = " + artID + " LIMIT 1;").get(0).get(0);
    }
    public void openDB() {
        dbOpenHelper dbOpenHelper = new dbOpenHelper(context, DB_NAME, null, DB_VERSION);

        try {
            database = dbOpenHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(TAG, "Error while getting database");
            throw new Error("The end");
        }
    }

    private static class dbOpenHelper extends SQLiteOpenHelper {

        dbOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {}

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
    }

    private ArrayList<MSArray> select(String sqlQuery) {
        if(database != null){
            this.cursor = database.rawQuery(sqlQuery, null);
            return fetchRow();
        }else{
            Log.e(TAG, "The database is not open!");
            return null;
        }
    }

    private boolean insert(String sqlQuery) {
        if(database != null){
            try{
                this.cursor = database.rawQuery(sqlQuery, null);
                return cursor.getCount() >= 1;

            }catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }else{
            Log.e(TAG, "The database is not open!");
            return false;
        }
    }

    private ArrayList<MSArray> fetchRow() {

        ArrayList<MSArray> arrayList = new ArrayList<>();

        if (cursor.moveToFirst() && cursor.getCount() >= 1) {
            do {
                try {
                    MSArray record = new MSArray();
                    List<String> values = record.getValues();

                    for (int i = 0; i < cursor.getColumnCount(); i++){
                        String s = cursor.getString(i);
                        values.add(s == null ? "" : s);
                    }

                    arrayList.add(record);

                } catch (Exception h) {
                    Log.e(TAG, "Fetch Row Error: " + h.toString());
                }
            } while (cursor.moveToNext());
        }

        return arrayList;
    }

    public void copyDB() {
        File fileDB = context.getDatabasePath(DB_NAME);

        if (sp.getInt("DB_VERSION", 0) != DB_VERSION) {
            if (!fileDB.exists()) {
                dbOpenHelper copyDB = new dbOpenHelper(context, DB_NAME, null, DB_VERSION);
                copyDB.close();

                if (!fileDB.getParentFile().exists())
                    fileDB.getParentFile().mkdirs();
            }

            Log.i(TAG, "Copy DB : New database is being copied to device!");

            try {
                InputStream is = context.getAssets().open(DB_NAME);
                OutputStream os = new FileOutputStream(context.getDatabasePath(DB_NAME).getPath());

                CopyStream(is, os);

                os.close();
                os.flush();
                is.close();

                Log.i(TAG, "Copy DB : New database has been copied to device!");
                sp.edit().putInt("DB_VERSION", DB_VERSION).apply();
            } catch (IOException e) {
                Log.i(TAG, "Copy DB : Error in process!");
                e.printStackTrace();
            }
        } else
            Log.i(TAG, "Copy DB : The database already copied to device!");
    }

    private static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for(;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex) {
            Log.e("CopyStream", "Error : "+ex.toString());
        }
    }
}
