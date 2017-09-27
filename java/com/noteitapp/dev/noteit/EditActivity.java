package com.noteitapp.dev.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EditActivity extends AppCompatActivity {

    String id;
    String title;
    String category,categoryText = "",categoryCount = "";
    String content,count;
    String priority;
    String color;
    Intent i;
    int pos;

    Spinner categorySpinner;
    ArrayList<Category> categories;
    RatingBar ratingBar;
    EditText titleEdit,contentEdit;
    ImageView image_save;
    Button saveNoteButton,saveNoteButtonTmp;
    static Toolbar toolbar;
    static MyDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        helper = new MyDBHelper(this);

        i = getIntent();
        id = i.getExtras().getString("id");
        title = i.getExtras().getString("title");
        category = i.getExtras().getString("category");
        content = i.getExtras().getString("content");
        priority = i.getExtras().getString("priority");
        color = i.getExtras().getString("color");

        categorySpinner  = (Spinner) findViewById(R.id.edit_category);
        ratingBar = (RatingBar) findViewById(R.id.edit_rating);
        titleEdit = (EditText) findViewById(R.id.edit_title);
        contentEdit = (EditText) findViewById(R.id.edit_content);
        saveNoteButton = (Button) findViewById(R.id.edit_save_note);
        image_save = (ImageView) findViewById(R.id.tmp_save_note);
        saveNoteButtonTmp = (Button) findViewById(R.id.edit_save_note_tmp);


        categories = new ArrayList<>();
        categories = MainActivity.helper.getAllCategory();

        int tmp = 0;

        for(int i=0;i<categories.size();i++){
            if(categories.get(i).getCategory().trim().equalsIgnoreCase(category)){
                tmp = i;
                Log.d("Jeeva","CAT_POS: " + String.valueOf(tmp));
                break;
            }
        }

        SpinnerAdapter adapter = new SpinnerAdapter();
        categorySpinner.setAdapter(adapter);

        categorySpinner.setSelection(tmp);

        titleEdit.setText(title);
        contentEdit.setText(content);
        ratingBar.setRating(Float.parseFloat(priority));

        contentEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    if (String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("") && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")) {

                        Log.d("JeevaEdit", "EDIT_NOTE: " + "Both not present");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.setTitle("Invalid Note");
                        builder.setMessage("Please enter Title or Content.");
                        builder.show();

                    } else {

                        Log.d("JeevaEdit", "EDIT_NOTE: " + "Title only present");

                        Log.d("JeevaEdit", "EDIT_NOTE_CATEGORY: " + categoryText);
                        Note note = new Note();
                        note.setId(String.valueOf(id));
                        note.setTitle(String.valueOf(titleEdit.getText()).trim());
                        note.setCategory(categoryText);
                        Log.d("JeevaEdit", "SETTING_COLOR_WHILE_ADD: " + color);
                        note.setColor(color);
                        note.setContent(String.valueOf(contentEdit.getText()));
                        note.setPriority(String.valueOf(ratingBar.getRating()));
                        helper.editNote(note);

                        Category c1 = new Category();
                        c1.setCategory(category);
                        Category c2 = new Category();
                        c2.setCategory(categoryText);
                        if(String.valueOf(c1.getCategory()).equalsIgnoreCase(String.valueOf(c2.getCategory()))){

                        }else{

                            helper.addMinus(c1,c2);
                        }
                    }
                }
                return false;
            }
        });

        titleEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    if (String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("") && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")) {

                        Log.d("JeevaEdit", "EDIT_NOTE: " + "Both not present");
                        AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.setTitle("Invalid Note");
                        builder.setMessage("Please enter Title or Content.");
                        builder.show();

                    } else {

                        Log.d("JeevaEdit", "EDIT_NOTE: " + "Title only present");

                        Log.d("JeevaEdit", "EDIT_NOTE_CATEGORY: " + categoryText);
                        Note note = new Note();
                        note.setId(String.valueOf(id));
                        note.setTitle(String.valueOf(titleEdit.getText()).trim());
                        note.setCategory(categoryText);
                        Log.d("JeevaEdit", "SETTING_COLOR_WHILE_ADD: " + color);
                        note.setColor(color);
                        note.setContent(String.valueOf(contentEdit.getText()));
                        note.setPriority(String.valueOf(ratingBar.getRating()));
                        helper.editNote(note);

                        Category c1 = new Category();
                        c1.setCategory(category);
                        Category c2 = new Category();
                        c2.setCategory(categoryText);
                        if(String.valueOf(c1.getCategory()).equalsIgnoreCase(String.valueOf(c2.getCategory()))){

                        }else{

                            helper.addMinus(c1,c2);
                        }
                    }
                }
                return false;
            }
        });

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("") && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")) {

                    Log.d("JeevaEdit", "EDIT_NOTE: " + "Both not present");
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setTitle("Invalid Note");
                    builder.setMessage("Please enter Title or Content.");
                    builder.show();

                } else {

                    Log.d("JeevaEdit", "EDIT_NOTE: " + "Title only present");

                    Log.d("JeevaEdit", "EDIT_NOTE_CATEGORY: " + categoryText);
                    Note note = new Note();
                    note.setId(String.valueOf(id));
                    note.setTitle(String.valueOf(titleEdit.getText()).trim());
                    note.setCategory(categoryText);
                    Log.d("JeevaEdit", "SETTING_COLOR_WHILE_ADD: " + color);
                    note.setColor(color);
                    note.setContent(String.valueOf(contentEdit.getText()));
                    note.setPriority(String.valueOf(ratingBar.getRating()));
                    helper.editNote(note);

                    Category c1 = new Category();
                    c1.setCategory(category);
                    Category c2 = new Category();
                    c2.setCategory(categoryText);
                    if(String.valueOf(c1.getCategory()).equalsIgnoreCase(String.valueOf(c2.getCategory()))){

                    }else{

                        helper.addMinus(c1,c2);
                    }
                    finish();
                }



            }
        });

        saveNoteButtonTmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("") && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")) {

                    Log.d("JeevaEdit", "EDIT_NOTE: " + "Both not present");
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setTitle("Invalid Note");
                    builder.setMessage("Please enter Title or Content.");
                    builder.show();

                } else {

                    Log.d("JeevaEdit", "EDIT_NOTE: " + "Title only present");

                    Log.d("JeevaEdit", "EDIT_NOTE_CATEGORY: " + categoryText);
                    Note note = new Note();
                    note.setId(String.valueOf(id));
                    note.setTitle(String.valueOf(titleEdit.getText()).trim());
                    note.setCategory(categoryText);
                    Log.d("JeevaEdit", "SETTING_COLOR_WHILE_ADD: " + color);
                    note.setColor(color);
                    note.setContent(String.valueOf(contentEdit.getText()));
                    note.setPriority(String.valueOf(ratingBar.getRating()));
                    helper.editNote(note);

                    Category c1 = new Category();
                    c1.setCategory(category);
                    Category c2 = new Category();
                    c2.setCategory(categoryText);
                    if(String.valueOf(c1.getCategory()).equalsIgnoreCase(String.valueOf(c2.getCategory()))){

                    }else{

                        helper.addMinus(c1,c2);
                    }
                }



            }
        });

        image_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("") && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")) {

                    Log.d("JeevaEdit", "EDIT_NOTE: " + "Both not present");
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setTitle("Invalid Note");
                    builder.setMessage("Please enter Title or Content.");
                    builder.show();

                } else {

                    Log.d("JeevaEdit", "EDIT_NOTE: " + "Title only present");

                    Log.d("JeevaEdit", "EDIT_NOTE_CATEGORY: " + categoryText);
                    Note note = new Note();
                    note.setId(String.valueOf(id));
                    note.setTitle(String.valueOf(titleEdit.getText()).trim());
                    note.setCategory(categoryText);
                    Log.d("JeevaEdit", "SETTING_COLOR_WHILE_ADD: " + color);
                    note.setColor(color);
                    note.setContent(String.valueOf(contentEdit.getText()));
                    note.setPriority(String.valueOf(ratingBar.getRating()));
                    helper.editNote(note);

                    Category c1 = new Category();
                    c1.setCategory(category);
                    Category c2 = new Category();
                    c2.setCategory(categoryText);
                    if(String.valueOf(c1.getCategory()).equalsIgnoreCase(String.valueOf(c2.getCategory()))){

                    }else{

                        helper.addMinus(c1,c2);
                    }
                }



            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryText = categories.get(position).getCategory();
                MainActivity.tmpPos = position + 1;
                categoryCount = categories.get(position).getCount();
                color = categories.get(position).getColor();
                Log.d("Jeeva", "CATEGORY_COLOR: " + color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categorySpinner.setSelection(pos);
                categoryText = categories.get(pos).getCategory();
            }
        });

    }

    class SpinnerAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return categories.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (View) inflater.inflate(R.layout.category_list_item, null);
            Category category = categories.get(position);
            TextView text = (TextView) view.findViewById(R.id.list_text);
            TextView count = (TextView) view.findViewById(R.id.category_count);
            count.setText(category.getCount());
            count.setTextColor(Color.parseColor(category.getColor()));
            View categoryColor = view.findViewById(R.id.list_color);
            text.setText(String.valueOf(category.getCategory()));
            categoryColor.setBackgroundColor(Color.parseColor(category.getColor()));
            return view;
        }
    }



}
