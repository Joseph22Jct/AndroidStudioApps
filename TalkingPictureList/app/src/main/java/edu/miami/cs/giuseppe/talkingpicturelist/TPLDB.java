package edu.miami.cs.giuseppe.talkingpicturelist;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//=============================================================================
public class TPLDB extends SQLiteOpenHelper {
    public static final String TAG = "Database Helper";

    public static final String TABLE_NAME = "MessageTable";
    public static final String COL1 = "id";
    public static final String COL2 = "message";
    public static final String COL3 = "imageID";


    public TPLDB(Context context){
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTable = "CREATE TABLE " + TABLE_NAME+ ("(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + COL2 + " TEXT, "+ COL3 +" TEXT)");
        db.execSQL(createTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int e){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);

    }
    public boolean addData(String item1, String item2){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item1);
        contentValues.put(COL3, item2);
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
                + COL3+ " = '"+ ID+ "'";
        db.execSQL(query);


    }

    public void ChangeDescription(String ImageID, String value, String oldDesc){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE "+ TABLE_NAME+ " SET " + COL2 + " = '"
                + value + "' WHERE " + COL3 + " = '" + ImageID+ "'" +
                " AND " + COL2+ " = '" + oldDesc +"'";
        db.execSQL(query);

    }





}