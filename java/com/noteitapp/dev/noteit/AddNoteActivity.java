package com.noteitapp.dev.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class AddNoteActivity extends AppCompatActivity {

    Spinner categorySpinner;
    ArrayList<Category> categories;
    TextView noCatText;
    RatingBar ratingBar;
    EditText titleEdit,contentEdit;
    Button noCatButton,addNoteButton,alarmButton;
    static Toolbar toolbar;
    String color = "#BCDBCD";
    String addNoteCategory = "";
    Intent i;

    String titleText =  "",contentText = "",categoryText = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categorySpinner = (Spinner) findViewById(R.id.category);
        noCatText = (TextView) findViewById(R.id.no_category_text);
        noCatButton = (Button) findViewById(R.id.no_category_button);
        addNoteButton = (Button) findViewById(R.id.save_note);
        titleEdit = (EditText) findViewById(R.id.title);
        contentEdit = (EditText) findViewById(R.id.content);
        ratingBar = (RatingBar) findViewById(R.id.rating);


        i = getIntent();
        addNoteCategory = i.getExtras().getString("category");
        Log.d("Jeeva","CAT_INTENT: " + String.valueOf(addNoteCategory));

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("") && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")){

                    Log.d("Jeeva","ADD_NOTE: " + "Both not present");
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setTitle("Invalid Note");
                    builder.setMessage("Please enter Title or Content.");
                    builder.show();
                }

                if( !(String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("")) && String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase("")){

                    Log.d("Jeeva","ADD_NOTE: " + "Title only present");
                    if(!(categoryText.equalsIgnoreCase(""))){

                        Log.d("Jeeva", "ADD_NOTE_CATEGORY: " + categoryText);
                        Note note = new Note();
                        note.setTitle(String.valueOf(titleEdit.getText()).trim());
                        note.setCategory(categoryText);
                        Log.d("Jeeva", "SETTING_COLOR_WHILE_ADD: " + color);
                        note.setColor(color);
                        note.setContent("");
                        note.setPriority(String.valueOf(ratingBar.getRating()));
                        MainActivity.helper.addNote(note);
                        finish();


                    }else{
                        Log.d("Jeeva","ADD_NOTE_CATEGORY: " + "No Category");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.setTitle("No Category");
                        builder.setMessage("Please create a Category.");
                        builder.show();
                    }

                }

                if(!(String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("")) && !(String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase(""))){

                    Log.d("Jeeva","ADD_NOTE: " + "Both present");
                    if(!(categoryText.equalsIgnoreCase(""))){

                        Log.d("Jeeva", "ADD_NOTE_CATEGORY: " + categoryText);
                        Note note = new Note();
                        note.setTitle(String.valueOf(titleEdit.getText()).trim());
                        note.setCategory(categoryText);
                        Log.d("Jeeva", "SETTING_COLOR_WHILE_ADD: " + color);
                        note.setColor(color);
                        note.setPriority(String.valueOf(ratingBar.getRating()));
                        note.setContent(String.valueOf(contentEdit.getText()).trim());
                        MainActivity.helper.addNote(note);
                        finish();

                    }else{
                        Log.d("Jeeva","ADD_NOTE_CATEGORY: " + "No Category");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.setTitle("No Category");
                        builder.setMessage("Please create a Category.");
                        builder.show();
                    }

                }

                if((String.valueOf(titleEdit.getText()).trim().equalsIgnoreCase("")) && !(String.valueOf(contentEdit.getText()).trim().equalsIgnoreCase(""))){

                    Log.d("Jeeva","ADD_NOTE: " + "Content only present");
                    if(!(categoryText.equalsIgnoreCase(""))) {

                        Log.d("Jeeva","ADD_NOTE_CATEGORY: " + categoryText);
                        Note note = new Note();
                        note.setTitle("");
                        note.setPriority(String.valueOf(ratingBar.getRating()));
                        note.setCategory(categoryText);
                        note.setContent(String.valueOf(contentEdit.getText()).trim());
                        Log.d("Jeeva", "SETTING_COLOR_WHILE_ADD: " + color);
                        note.setColor(color);
                        MainActivity.helper.addNote(note);
                        finish();

                    }else{
                        Log.d("Jeeva","ADD_NOTE_CATEGORY: " + "No Category");
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddNoteActivity.this);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                        builder.setTitle("No Category");
                        builder.setMessage("Please create a Category.");
                        builder.show();
                    }

                }



            }
        });

        categories = new ArrayList<>();
        categories = MainActivity.helper.getAllCategory();
        int tmp = 0;

        for(int i=0;i<categories.size();i++){
            if(categories.get(i).getCategory().trim().equalsIgnoreCase(addNoteCategory)){
                tmp = i;
                Log.d("Jeeva","CAT_POS: " + String.valueOf(tmp));
                break;
            }
        }


        if(categories.size() == 0){
            categorySpinner.setVisibility(View.GONE);
        }else{
            categorySpinner.setVisibility(View.VISIBLE);
            noCatText.setVisibility(View.GONE);
            SpinnerAdapter adapter = new SpinnerAdapter();
            categorySpinner.setAdapter(adapter);
            categorySpinner.setSelection(tmp);


        }


        noCatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddNoteActivity.this,CreateCategoryActivity.class);
                startActivity(i);
            }
        });


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryText = categories.get(position).getCategory();
                color = categories.get(position).getColor();
                Log.d("Jeeva","CATEGORY_COLOR: " + color);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        categories = new ArrayList<>();
        categories = MainActivity.helper.getAllCategory();

        if(categories.size() == 0){
            categorySpinner.setVisibility(View.GONE);
        }else{
            categorySpinner.setVisibility(View.VISIBLE);
            noCatText.setVisibility(View.GONE);
            SpinnerAdapter adapter = new SpinnerAdapter();
            categorySpinner.setAdapter(adapter);
        }
    }

    class SpinnerAdapter extends BaseAdapter{

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.add_alarm_note) {
            Intent i = new Intent(AddNoteActivity.this,AlarmNoteActivity.class);
            finish();
            startActivityForResult(i, 100);

        }


        return super.onOptionsItemSelected(item);
    }

}
