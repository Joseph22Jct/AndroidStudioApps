package edu.miami.cs.giuseppe.thephlogging;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//=============================================================================
public class PDB extends SQLiteOpenHelper {
    public static final String TAG = "Database Helper";

    public static final String TABLE_NAME = "MessageTable";
    public static final String COL1 = "id";
    public static final String COL2 = "title";
    public static final String COL3 = "imageID";
    public static final String COL4 = "description";
    public static final String COL5 = "time";



    public PDB(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME+ ("(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, "+ COL3 +" TEXT," + COL4 + " TEXT," + COL5+" TEXT)");
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int e){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }
    public boolean addData(String item1, String item2, String item3, String item4){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item1);
        contentValues.put(COL3, item2);
        contentValues.put(COL4, item3);
        contentValues.put(COL5, item4);
        Log.d(TAG, "addData: adding " + item1+ " at time: " + item2 + " to table "+ TABLE_NAME);
        long result= db.insert(TABLE_NAME,null, contentValues);
        if (result == -1){
            return false;
        }
        else{
            return true;
        }

    }

    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM "+ TABLE_NAME;
        Cursor data = db.rawQuery(query,null);
        return data;
    }

    public boolean compareImgIDs(String ID){ //Checks if the value exists
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT "+ COL3+" FROM " + TABLE_NAME +" WHERE "+COL3+ " = '" + ID+"'";
        Cursor data = db.rawQuery(query, null);
        if (data.moveToFirst()){
            return true;
        }
        else{
            return false;
        }
    }

    public void RemoveItem(String ID){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME+ " WHERE "
                + COL1+ " = '"+ ID+ "'";
        db.execSQL(query);


    }

    public void ChangeItem(int ID, String title, String desc, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME+ " SET " + COL2 + " = '"
                + title +
                "' WHERE " + COL1 + " = '" + ID+ "'";
        db.execSQL(query);
        query = "UPDATE "+ TABLE_NAME+ " SET " + COL4 + " = '"
                + desc +
                "' WHERE " + COL1 + " = '" + ID+ "'";
        db.execSQL(query);
        query = "UPDATE "+ TABLE_NAME+ " SET " + COL5 + " = '"
                + time +
                "' WHERE " + COL1 + " = '" + ID+ "'";
        db.execSQL(query);


    }





}