package com.noteitapp.dev.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetOnlineActivity extends AppCompatActivity {

    ProgressBar progress,progressTwo,progressRot;
    EditText url;
    Button update;
    TextView t1,t2,status;
    android.app.AlertDialog alertDialog;
    ArrayList<Category> categories;
    ArrayList<Note> notes;
    int categoryOk = 0,notesOk = 0,completeOk = 0;
    int catMax = 0,noteMax = 0;
    int timeCount = 0;
    int timeTmp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_online);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        categories = new ArrayList<>();
        notes = new ArrayList<>();

        progress = (ProgressBar) findViewById(R.id.progress);
        update = (Button) findViewById(R.id.sync_button);
        progressTwo = (ProgressBar) findViewById(R.id.progressTwo);
        progressRot = (ProgressBar) findViewById(R.id.progress_rot);
        progressRot.setVisibility(View.GONE);
        t1 = (TextView) findViewById(R.id.progressText);
        status = (TextView) findViewById(R.id.status);
        t2 = (TextView) findViewById(R.id.progressTwoText);
        t1.setText("0 %");
        t2.setText("0 %");
        url = (EditText) findViewById(R.id.edit_url);
        url.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        progress.setIndeterminate(false);
        progress.setProgress(0);
        progressTwo.setIndeterminate(false);
        progressTwo.setProgress(0);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isOnline()){

                    if(completeOk == 0){

                        categoryOk = 0;
                        notesOk = 0;
                        status.setText("Fetching Categories");
                        fetchCategories();
                        completeOk = 1;
                    }

                }else{

                    android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(GetOnlineActivity.this);
                    LayoutInflater inflater = GetOnlineActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.net_error, null);
                    Button deleteNote = (Button) dialogView.findViewById(R.id.net_error);
                    progressRot.setVisibility(View.GONE);

                    deleteNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            status.setText("Click to Sync");
                            alertDialog.dismiss();
                        }
                    });

                    dialogBuilder.setView(dialogView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();



                }

            }
        });


    }

    public boolean isOnline(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info != null && info.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }

    public void fetchCategories(){

        if(categoryOk == 1){


            status.setText("Fetching Notes");
            fetchNotes();

        }else{

            progressRot.setVisibility(View.VISIBLE);

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setParam("cat", "true");
            requestPackage.setUri(String.valueOf(url.getText()).trim());
            new FetchCategories().execute(requestPackage);

        }
    }

    private void insertIntoDatabase() {

        progress.setMax(100);
        progressTwo.setMax(100);

        if(categories.size() > 0){

            catMax = 100 / categories.size();
            noteMax = 100 / notes.size();

            timeTmp = 1000/categories.size();

            timeCount = 0;
            status.setText("Adding New Categories");
            for(int i=0;i<categories.size();i++){



                Handler handler = new Handler();
                final int finalI = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progress.setProgress(catMax * finalI);
                        t1.setText(String.valueOf(catMax * finalI));
                        MainActivity.helper.addCategory(categories.get(finalI));
                        if(finalI == (categories.size()-1)){
                            t1.setText("100 %");
                            progress.setProgress(100);
                        }

                    }
                }, timeCount);
                timeCount += timeTmp;

            }

            timeTmp = 1000/notes.size();

            status.setText("Adding New Notes");
            for (int i=0;i<notes.size();i++){



                Handler handler = new Handler();
                final int finalI = i;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        progressTwo.setProgress(noteMax * finalI);
                        t2.setText(String.valueOf(noteMax * finalI));
                        MainActivity.helper.addNote(notes.get(finalI));
                        if(finalI == (notes.size()-1)){
                            t2.setText("100 %");
                            progressTwo.setProgress(100);
                        }


                    }
                },timeCount);
                timeCount += timeTmp;

            }

            status.setText("All Notes Added");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(GetOnlineActivity.this);
                    LayoutInflater inflater = GetOnlineActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.online_okay, null);
                    Button deleteNote = (Button) dialogView.findViewById(R.id.online_okay);
                    progressRot.setVisibility(View.GONE);

                    deleteNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            status.setText("Click to Sync");
                            progressRot.setVisibility(View.GONE);
                            finish();

                        }
                    });

                    dialogBuilder.setView(dialogView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();
                }
            },timeCount + 500);

            update.setVisibility(View.GONE);


        }else{

            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(GetOnlineActivity.this);
            LayoutInflater inflater = GetOnlineActivity.this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.zero_error, null);
            Button deleteNote = (Button) dialogView.findViewById(R.id.zero_error);
            progressRot.setVisibility(View.GONE);

            deleteNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    status.setText("Click to Sync");
                    alertDialog.dismiss();
                    finish();
                }
            });

            dialogBuilder.setView(dialogView);
            alertDialog = dialogBuilder.create();
            alertDialog.show();
        }



    }

    public void fetchNotes(){

        if(notesOk == 1){

            Log.d("JeevaUpdate","All Okay");
            insertIntoDatabase();

        }else{

            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setParam("note", "true");
            requestPackage.setUri(String.valueOf(url.getText()).trim());
            new FetchNote().execute(requestPackage);
        }

    }


    class FetchCategories extends AsyncTask<RequestPackage,String,String> {


        @Override
        protected String doInBackground(RequestPackage... params) {


            String result = HttpManager.getData(params[0]);
            if(result == null){

                return "error";

            }else{



                    try {
                        JSONArray array = new JSONArray(result.trim());
                        categories.clear();

                        for(int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            Category category = new Category();
                            category.setCategory(object.getString("category").trim());
                            category.setColor(object.getString("color").trim());
                            category.setCount(object.getString("count").trim());
                            categories.add(category);
                        }

                        for (int i=0;i<categories.size();i++) {
                            Log.d("JeevaUpdate","Category: " + categories.get(i).getCategory());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "ok";


            }

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid.equalsIgnoreCase("ok")){

                categoryOk = 1;
                Log.d("JeevaUpdate","Got Categories");
                fetchCategories();
            }

            if(aVoid.equalsIgnoreCase("no")){

                Log.d("JeevaUpdate","No Code");
                fetchCategories();
            }


            if(aVoid.equalsIgnoreCase("error")){

                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(GetOnlineActivity.this);
                LayoutInflater inflater = GetOnlineActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.server_error, null);
                Button deleteNote = (Button) dialogView.findViewById(R.id.server_error);
                progressRot.setVisibility(View.GONE);

                deleteNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        completeOk = 0;
                        status.setText("Click to Sync");
                        alertDialog.dismiss();
                    }
                });

                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();
            }

        }
    }

    class FetchNote extends AsyncTask<RequestPackage,String,String> {


        @Override
        protected String doInBackground(RequestPackage... params) {


            String result = HttpManager.getData(params[0]);
            if(result == null){

                return "error";

            }else{


                    try {
                        JSONArray array = new JSONArray(result.trim());
                        notes.clear();

                        for(int i=0;i<array.length();i++){
                            JSONObject object = array.getJSONObject(i);
                            Note note = new Note();
                            note.setCategory(object.getString("category").trim());
                            note.setColor(object.getString("color").trim());
                            note.setPriority(object.getString("priority").trim());
                            note.setTitle(object.getString("title").trim());
                            note.setContent(object.getString("content").trim().replace("lss","\n"));
                            notes.add(note);
                        }

                        for (int i=0;i<notes.size();i++) {
                            Log.d("JeevaUpdate","Note: " + notes.get(i).getTitle());
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    return "ok";


            }

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid.equalsIgnoreCase("ok")){

                notesOk = 1;
                Log.d("JeevaUpdate","Got Notes");
                fetchNotes();
            }

            if(aVoid.equalsIgnoreCase("no")){

                fetchNotes();
                Log.d("JeevaUpdate", "No Code");
            }


        }
    }


}
