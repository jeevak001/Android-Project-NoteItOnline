package com.noteitapp.dev.noteit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Jeeva on 4/7/2016.
 */
public class MyDBHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 103;
    private static final String DATABASE_NAME = "notes_database";
    private static final String TABLE_SIMPLE_NOTES = "simple_notes";
    private static final String TABLE_NOTES_CATEGORY = "notes_category";
    private static final String TABLE_NOTES_ALARM = "alarm_notes";

    private static final String KEY_ID = "_id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_PRIORITY = "priority";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_TIMESTAMP = "timestamp";
    private static final String KEY_COLOR = "color";
    private static final String KEY_COUNT = "count";
    private static final String KEY_SET = "isset";


    public MyDBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_ALARM_TABLE = "CREATE TABLE "
                + TABLE_NOTES_ALARM + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_PRIORITY + " TEXT DEFAULT '0',"
                + KEY_TIMESTAMP + " TEXT,"
                + KEY_SET + " DECIMAL"
                + ")";

        String CREATE_NOTES_TABLE = "CREATE TABLE "
                + TABLE_SIMPLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_TITLE + " TEXT,"
                + KEY_CONTENT + " TEXT,"
                + KEY_PRIORITY + " TEXT DEFAULT '0',"
                + KEY_CATEGORY + " TEXT,"
                + KEY_COLOR + " TEXT,"
                + KEY_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ")";


        String CREATE_CATEGORY_TABLE = "CREATE TABLE "
                + TABLE_NOTES_CATEGORY + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_CATEGORY + " TEXT,"
                + KEY_COLOR + " TEXT,"
                + KEY_COUNT + " TEXT DEFAULT '0'"
                + ")";


        Log.d("Jeeva", "NOTES_TABLE_CREATE: " + CREATE_NOTES_TABLE);
        Log.d("Jeeva", "CATEGORY_TABLE_CREATE: " + CREATE_CATEGORY_TABLE);
        Log.d("JeevaAlarm", "ALARM_TABLE_CREATE: " + CREATE_ALARM_TABLE);

        db.execSQL(CREATE_NOTES_TABLE);
        db.execSQL(CREATE_ALARM_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIMPLE_NOTES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES_ALARM);
        onCreate(db);
    }

    //////////////////////////////////////////////////


    public ArrayList<AlarmNote> getAlarmNotes(){
        ArrayList<AlarmNote> notes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES_ALARM + " ORDER BY timestamp;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("JeevaAlarm", "---------------------------{} ALARM NOTE GET ENTRIES-------------------------------------------------");

        if (cursor.moveToFirst()) {
            do {
                AlarmNote note = new AlarmNote();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setPriority(String.valueOf(cursor.getString(3)));
                note.setTime(cursor.getString(4));
                note.setSet(cursor.getInt(5));
                Log.d("JeevaAlarm", "ALARM_NOTE_ENTRY: " + note.getId() + "," + note.getTitle() + "," + note.getContent() + ", " + note.getPriority() +  ", " + note.getTime() + ", " + note.getSet());
                notes.add(note);
            } while (cursor.moveToNext());
        }

        return notes;
    }

    public void addAlarmNote(AlarmNote note){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_TIMESTAMP, note.getTime());
        values.put(KEY_SET, note.getSet());
        values.put(KEY_PRIORITY, note.getPriority());
        db.insert(TABLE_NOTES_ALARM, null, values);
        db.close();
    }

    public void setZero(int tmp){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SET,0);
        db.update(TABLE_NOTES_ALARM, values, "_id = ?", new String[]{String.valueOf(tmp)});
        db.close();
    }

    public void setAllZero(){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SET,0);
        db.update(TABLE_NOTES_ALARM, values, null, null);
        db.close();
    }

    public void removeAlarm(int id){
        Log.d("JeevaAlarm", "--------------------------------REMOVED ID " + String.valueOf(id) + "--------------------------------------------");
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES_ALARM, "_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }


    public void showAllAlarmNotes(String tmp){
        ArrayList<AlarmNote> notes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES_ALARM + " ORDER BY timestamp;";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("JeevaAlarm", "---------------------------{" + tmp + "} ALARM NOTE ENTRIES-------------------------------------------------");

        if (cursor.moveToFirst()) {
            do {
                AlarmNote note = new AlarmNote();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setPriority(String.valueOf(cursor.getString(3)));
                note.setTime(cursor.getString(4));
                note.setSet(cursor.getInt(5));
                Log.d("JeevaAlarm", "ALARM_NOTE_ENTRY: " + note.getId() + "," + note.getTitle() + "," + note.getContent() + ", " + note.getPriority() +  ", " + note.getTime() + ", " + note.getSet());
                notes.add(note);
            } while (cursor.moveToNext());
        }
    }

    public AlarmNote checkIfTopSet(){

        SQLiteDatabase dbTmp = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES_ALARM  + " ORDER BY timestamp LIMIT 1;";
        Cursor cursor = dbTmp.rawQuery(selectQuery, null);
        AlarmNote note = new AlarmNote();
        if (cursor.moveToFirst()) {

            note.setId(Integer.parseInt(cursor.getString(0)));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setPriority(cursor.getString(3));
            note.setTime(cursor.getString(4));
            note.setSet(cursor.getInt(5));

            Log.d("JeevaAlarm", "ALARM_NOTE_ENTRY_TOP_CHECK: " + note.getId() + "," + note.getTitle() + "," + note.getContent() + ", " + note.getPriority() + ", " + note.getTime() + ", " + note.getSet());
        }else{
            return null;
        }

        return note;

    }

    public void setTop(int i){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SET, 1);
        db.update(TABLE_NOTES_ALARM, values, "_id = ?", new String[]{String.valueOf(i)});
        db.close();


    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_CATEGORY, note.getCategory());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_PRIORITY, note.getPriority());

        Category category = new Category();
        category.setCategory(note.getCategory());
        int tmp = getNotesCount(category);
        Log.d("Jeeva", "GET_NOTES_COUNT_AFTER_ADD: " + String.valueOf(tmp + 1));
        category.setCount(String.valueOf(tmp + 1));
        increaseCount(category);

        db.insert(TABLE_SIMPLE_NOTES, null, values);
        db.close();

    }

    public void editNote(Note note) {

        Log.d("JeevaEdit", "Note Edit Called");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_CONTENT, note.getContent());
        values.put(KEY_CATEGORY, note.getCategory());
        values.put(KEY_COLOR, note.getColor());
        values.put(KEY_PRIORITY, note.getPriority());

        Log.d("JeevaEdit", "EDIT_NOTE: " + note.getId() + ", " + note.getTitle() + ", " + note.getContent() + ", " + note.getCategory() + ", " + note.getPriority());

        db.update(TABLE_SIMPLE_NOTES, values, "_id = ?", new String[]{note.getId()});
        db.close();

    }

    public Note getNote(int id){

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SIMPLE_NOTES, new String[]{KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_PRIORITY, KEY_CATEGORY, KEY_TIMESTAMP},
                KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Note note = new Note(
                String.valueOf(cursor.getString(0)),
                String.valueOf(cursor.getString(1)),
                String.valueOf(cursor.getString(2)),
                String.valueOf(cursor.getString(3)),
                String.valueOf(cursor.getString(4)),
                String.valueOf(cursor.getString(5))
        );

        return note;
    }

    public ArrayList<Note> getAllNotesByCategory(Category cat) {

        ArrayList<Note> notes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_SIMPLE_NOTES + " WHERE category = '" + cat.getCategory() + "' ORDER BY priority DESC,_id DESC;";
        Log.d("Jeeva","GET_NOTES_BY_CATEGORY: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId((cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setPriority(String.valueOf(cursor.getString(3)));
                note.setCategory(cursor.getString(4));
                note.setColor(cursor.getString(5));
                note.setTimestamp(cursor.getString(6));
                Log.d("Jeeva", "GET_NOTE_BY_CATEGORY: " + note.getTitle() + "," + note.getContent() + ", " + note.getColor() +  ", " + note.getCategory() + ", " + note.getPriority());
                notes.add(note);
            } while (cursor.moveToNext());
        }

        return notes;
    }



    public ArrayList<Note> getAllNotesByCategoryAll() {

        ArrayList<Note> notes = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_SIMPLE_NOTES  + " ORDER BY priority DESC,_id DESC;";
        Log.d("Jeeva","GET_NOTES_BY_CATEGORY_ALL: " + selectQuery);
        SQLiteDatabase dbTmp1 = this.getWritableDatabase();
        Cursor cursor = dbTmp1.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId((cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setPriority(cursor.getString(3));
                note.setCategory(cursor.getString(4));
                note.setColor(cursor.getString(5));
                Log.d("Jeeva", "GET_NOTE_BY_CATEGORY: " + note.getTitle() + "," + note.getContent() + ", " + note.getColor() +  ", " + note.getCategory() + ", " + note.getPriority());
                notes.add(note);
            } while (cursor.moveToNext());
        }

        return notes;
    }

    public ArrayList<String> getAllColors() {

        ArrayList<String> colors = new ArrayList<>();

        String selectQuery = "SELECT color FROM " + TABLE_NOTES_CATEGORY  + ";";
        Log.d("Jeeva","GET_ALL_COLORS: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String color = cursor.getString(0);
                Log.d("Jeeva","COLOR_DB: " + color);
                colors.add(color);
            } while (cursor.moveToNext());
        }

        return colors;
    }

    public int getNotesCountAll() {

        String selectQuery = "SELECT * FROM " + TABLE_SIMPLE_NOTES  + ";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getNotesCount(Category category) {

        String selectQuery = "SELECT * FROM " + TABLE_SIMPLE_NOTES + " WHERE category = '" + category.getCategory() +  "'; ";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }


    public void deleteNote(Note note){

        SQLiteDatabase db = this.getWritableDatabase();
        Category category = new Category();
        category.setCategory(note.getCategory());
        int tmp = getNotesCount(category);

        db.delete(TABLE_SIMPLE_NOTES, KEY_ID + " = ?", new String[]{String.valueOf(note.getId())});

        Log.d("Jeeva", "GET_NOTES_COUNT_AFTER_DELETE: " + String.valueOf(tmp - 1));
        category.setCount(String.valueOf(tmp - 1));
        reduceCount(category);
        db.close();
    }

    public void deleteNotesByCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SIMPLE_NOTES, "category = ?", new String[]{category.getCategory()});
        db.close();
    }

    private void updateNotesColorCategory(String newName, String color, String oldName) {

        Log.d("JeevaEdit", "Note Category Update Called");

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, newName);
        values.put(KEY_COLOR, color);

        Log.d("JeevaEdit", "CATEGORY_UPDATE_EDIT_NOTE: " + oldName + " , " + newName + " ," + color);

        db.update(TABLE_SIMPLE_NOTES, values, "category = ?", new String[]{oldName});
        db.close();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addCategory(Category category) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, Utils.caps(category.getCategory()));
        values.put(KEY_COLOR, Utils.caps(category.getColor()));
        values.put(KEY_COUNT, category.getCount());

        long id = db.insert(TABLE_NOTES_CATEGORY, null, values);

        Log.d("Jeeva","Category Add: " + String.valueOf(id) + " " + category.getCategory() + " " + category.getColor());

        db.close();

    }

    public int getCategoryCount(String category){

        String selectQuery = "SELECT count FROM " + TABLE_NOTES_CATEGORY + " WHERE category = '" + category + "';";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()){
            return Integer.parseInt(cursor.getString(0));        }

        return 0;
    }

    public ArrayList<Category> getAllCategory(){

        ArrayList<Category> categories = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NOTES_CATEGORY;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Category category = new Category();
                category.setId(String.valueOf(cursor.getString(0)).trim());
                category.setCategory(String.valueOf(cursor.getString(1)).trim());
                category.setColor(String.valueOf(cursor.getString(2)).trim());
                category.setCount(String.valueOf(cursor.getString(3)).trim());
                categories.add(category);
                Log.d("Jeeva","GET_ALL_CATEGORY: " + category.getId() + ", " + category.getCategory() + ", " + category.getCount() + ", " + category.getColor());
            } while (cursor.moveToNext());
        }

        return categories;
    }

    public int updateCategory(Category category,String oldName){

        SQLiteDatabase dbase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATEGORY, category.getCategory());
        values.put(KEY_COLOR, category.getColor());
        values.put(KEY_COUNT, category.getCount());
        Log.d("Jeeva", "CATEGORY_UPDATE: " + category.getCategory() + ", " + category.getColor() + ", " + category.getCount());
        updateNotesColorCategory(category.getCategory(), category.getColor(), oldName);
        dbase = this.getWritableDatabase();
        dbase.update(TABLE_NOTES_CATEGORY, values, KEY_ID + " = ?", new String[]{String.valueOf(category.getId())});
        dbase.close();
        return 1;


    }




    public void deleteCategory(Category category){

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES_CATEGORY, KEY_ID + " = ?", new String[]{String.valueOf(category.getId())});
        deleteNotesByCategory(category);
        db.close();
    }

    public void deleteAllCategory(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES_CATEGORY,null,null);
        db.delete(TABLE_SIMPLE_NOTES, null, null);
        db.close();
    }

    public void deleteCategoryOnly(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NOTES_CATEGORY,null,null);
        db.close();
    }

    public void addCategories(ArrayList<Category> categories){

        for(Category cat:categories){

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY, Utils.caps(cat.getCategory()));
            values.put(KEY_COLOR, Utils.caps(cat.getColor()));
            values.put(KEY_COUNT, cat.getCount());

            long id = db.insert(TABLE_NOTES_CATEGORY, null, values);

            Log.d("Jeeva","Category Add: " + String.valueOf(id) + " " + cat.getCategory() + " " + cat.getColor());

            db.close();
        }
    }

    public int reduceCount(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, category.getCount());
        Log.d("Jeeva", "CATEGORY_REDUCE_COUNT: " + category.getCategory() + ", " + category.getCount());
        return db.update(TABLE_NOTES_CATEGORY, values, KEY_CATEGORY + " = ?", new String[]{String.valueOf(category.getCategory())});
    }

    public int increaseCount(Category category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, category.getCount());
        Log.d("Jeeva", "CATEGORY_INCREASE_COUNT: " + category.getCategory() + ", " + category.getCount());
        return db.update(TABLE_NOTES_CATEGORY, values, KEY_CATEGORY + " = ?", new String[]{String.valueOf(category.getCategory())});
    }

    public void addMinus(Category category, Category categoryText) {

        SQLiteDatabase db = this.getWritableDatabase();

        int count1 = getCategoryCount(category.getCategory().trim());
        int count2 = getCategoryCount(categoryText.getCategory().trim());

        ContentValues values = new ContentValues();
        values.put(KEY_COUNT, String.valueOf(count1 - 1) );
        db.update(TABLE_NOTES_CATEGORY, values, KEY_CATEGORY + " = ?", new String[]{String.valueOf(category.getCategory())});

        ContentValues values2 = new ContentValues();
        values2.put(KEY_COUNT, String.valueOf(count2 + 1));
        db.update(TABLE_NOTES_CATEGORY, values2, KEY_CATEGORY + " = ?", new String[]{String.valueOf(categoryText.getCategory())});

    }
}
