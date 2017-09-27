package com.noteitapp.dev.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class OnlineUpdateActivity extends AppCompatActivity {

    ProgressBar progress,progressTwo,progressRot;
    android.app.AlertDialog alertDialog;
    EditText url;
    Button update;
    TextView t1,t2,status;
    ArrayList<Category> categories;
    ArrayList<Note> notes;
    boolean insertOk = false;
    int catPos = 0,notePos = 0;
    int databaseCleared = 0;
    int catMax = 0;
    int noteMax = 0;
    int alreadyRunning = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_update);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        categories = MainActivity.helper.getAllCategory();
        notes = MainActivity.helper.getAllNotesByCategoryAll();
        progress.setMax(categories.size());
        progressTwo.setMax(notes.size());
        try {
            noteMax = 100 / notes.size();
            catMax = 100 / categories.size();
        }catch (Exception e){

        }


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(isOnline()){

                    if(alreadyRunning == 0){

                        if(categories.size() > 0){
                            notePos = 0;
                            catPos = 0;
                            databaseCleared = 0;
                            alreadyRunning = 1;
                            clearDatabase();
                        }else{

                            android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(OnlineUpdateActivity.this);
                            LayoutInflater inflater = OnlineUpdateActivity.this.getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.zero_error, null);
                            Button deleteNote = (Button) dialogView.findViewById(R.id.zero_error);
                            progressRot.setVisibility(View.GONE);

                            deleteNote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alreadyRunning = 0;
                                    status.setText("Click to Sync");
                                    alertDialog.dismiss();
                                    finish();
                                }
                            });

                            dialogBuilder.setView(dialogView);
                            alertDialog = dialogBuilder.create();
                            alertDialog.show();
                        }

                    }else{


                    }


                }else{

                    android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(OnlineUpdateActivity.this);
                    LayoutInflater inflater = OnlineUpdateActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.net_error, null);
                    Button deleteNote = (Button) dialogView.findViewById(R.id.net_error);
                    progressRot.setVisibility(View.GONE);

                    deleteNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alreadyRunning = 0;
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

    private void clearDatabase() {

        if(databaseCleared == 0){


            progressRot.setVisibility(View.VISIBLE);
            status.setText("Removing Old Entries");
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setUri(String.valueOf(url.getText()));
            requestPackage.setMethod("GET");
            requestPackage.setParam("clear", "true");
            new DatabaseClearTask().execute(requestPackage);
        }else{
            dataInsertCategory();
        }
    }

    private void dataInsertCategory() {

        if(catPos >= categories.size()){

            t1.setText("100 %");
            dataInsertNote();

        }else{

            progressRot.setVisibility(View.VISIBLE);
            status.setText("Adding New Categories");
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setUri(String.valueOf(url.getText()));
            requestPackage.setMethod("GET");
            requestPackage.setParam("cat","true");
            requestPackage.setParam("category", categories.get(catPos).getCategory().trim());
            requestPackage.setParam("color", categories.get(catPos).getColor().trim());
            requestPackage.setParam("count", categories.get(catPos).getCount().trim());
            new CategoryDataInsertTask().execute(requestPackage);
        }
    }

    private void dataInsertNote() {

        if(notePos >= notes.size()){

            t2.setText("100 %");
            progressRot.setVisibility(View.GONE);
            status.setText("All Data Synced Correctly");
            update.setVisibility(View.GONE);
            alreadyRunning = 0;

        }else{
            progressRot.setVisibility(View.VISIBLE);
            status.setText("Adding New Notes");
            RequestPackage requestPackage = new RequestPackage();
            requestPackage.setUri(String.valueOf(url.getText()));
            requestPackage.setMethod("GET");
            requestPackage.setParam("note", "true");
            requestPackage.setParam("title", notes.get(notePos).getTitle().trim());
            requestPackage.setParam("content", notes.get(notePos).getContent().trim().replace("'","").replace("\n","lss"));
            requestPackage.setParam("category", notes.get(notePos).getCategory().trim());
            requestPackage.setParam("color", notes.get(notePos).getColor().trim());
            requestPackage.setParam("priority", notes.get(notePos).getPriority().trim());
            new NoteDataInsertTask().execute(requestPackage);
        }
    }

    class CategoryDataInsertTask extends AsyncTask<RequestPackage,String,String>{


        @Override
        protected String doInBackground(RequestPackage... params) {


            String result = HttpManager.getData(params[0]);
            if(result == null){

            }else{

                if(result.trim().equalsIgnoreCase("ok")){
                    Log.d("JeevaUpdate","UPD: " + params[0].getParams().get("category") + "," + params[0].getParams().get("color") + "," + params[0].getParams().get("count"));
                    return "ok";
                }
            }

            return "no";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid.equalsIgnoreCase("ok")){

                Log.d("JeevaUpdate","Success");
                catPos++;
                progress.setProgress(catPos);
                t1.setText(String.valueOf(catPos * catMax) + " %");
                dataInsertCategory();
            }else{
                Log.d("JeevaUpdate","Failure");
                catPos++;
                progress.setProgress(catPos);
                t1.setText(String.valueOf(catPos * catMax) + " %");
                dataInsertCategory();
            }

        }
    }

    class NoteDataInsertTask extends AsyncTask<RequestPackage,String,String>{


        @Override
        protected String doInBackground(RequestPackage... params) {


            String result = HttpManager.getData(params[0]);
            if(result == null){



            }else{

                if(result.trim().equalsIgnoreCase("ok")){
                    Log.d("JeevaUpdate","UPD: " + params[0].getParams().get("title") + "," + params[0].getParams().get("content") + "," + params[0].getParams().get("category") + "," + params[0].getParams().get("color") + "," + params[0].getParams().get("priority"));
                    return "ok";
                }
            }

            return "no";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid.equalsIgnoreCase("ok")){

                Log.d("JeevaUpdate","Note Success");
                notePos++;
                progressTwo.setProgress(notePos);
                t2.setText(String.valueOf(notePos * noteMax) + " %");
                dataInsertNote();
            }
            if(aVoid.equalsIgnoreCase("no")){
                Log.d("JeevaUpdate","Note Failure");
                progressTwo.setProgress(notePos);
                t2.setText(String.valueOf(notePos * noteMax) + " %");
                dataInsertNote();
            }


        }
    }

    class DatabaseClearTask extends AsyncTask<RequestPackage,String,String>{


        @Override
        protected String doInBackground(RequestPackage... params) {


            String result = HttpManager.getData(params[0]);
            if(result == null){

                return "error";

            }else{

                if(result.trim().equalsIgnoreCase("ok")){
                    Log.d("JeevaUpdate","CLEARED: ");
                    return "ok";
                }
            }

            return "no";
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            if(aVoid.equalsIgnoreCase("ok")){

                Log.d("JeevaUpdate","Clear Success");
                databaseCleared = 1;
                clearDatabase();
            }
            if(aVoid.equalsIgnoreCase("no")){
                Log.d("JeevaUpdate","Clear Failure");
                clearDatabase();
            }
            if(aVoid.equalsIgnoreCase("error")){
                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(OnlineUpdateActivity.this);
                LayoutInflater inflater = OnlineUpdateActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.server_error, null);
                Button deleteNote = (Button) dialogView.findViewById(R.id.server_error);
                progressRot.setVisibility(View.GONE);

                deleteNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alreadyRunning = 0;
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



}
