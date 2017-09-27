package com.noteitapp.dev.noteit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    ListView categoryList;
    Typeface helvetica;
    boolean isHtml = false;
    ArrayList<Category> categories;
    Button audioNotesButton,actionButton,addMainButton,createNote;
    ViewGroup createCategoryButtonI,alarmNotesButton,alarmNotesButtonI,audioNotesButtonI,actionButtonI,addMainButtonI;
    ViewGroup drawerSubstitute;
    android.app.AlertDialog alertDialog;
    EditText search;
    ViewGroup searchGroup;
    ImageView closeSearch;
    ViewGroup deleteCategoryButton,createCategoryButton,emptyAddNote,putButton,getButton,addButton;
    ViewGroup deleteCategoryButtonI,emptyAddNoteI,createNoteI,putButtonI,getButtonI,addButtonI;
    static MyDBHelper helper;
    DrawerLayout drawer;
    static Toolbar toolbar;
    static int tmpPos = 0;
    String addNoteCategory = "";
    int isAlarm = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        categoryList = (ListView) findViewById(R.id.category_list);
        createCategoryButtonI = (ViewGroup) findViewById(R.id.create_category);
        addMainButtonI = (ViewGroup) findViewById(R.id.add_note);
        deleteCategoryButtonI = (ViewGroup) findViewById(R.id.delete_category);
        alarmNotesButtonI = (ViewGroup) findViewById(R.id.alarm_notes);
        putButtonI = (ViewGroup) findViewById(R.id.put_button);
        getButtonI = (ViewGroup) findViewById(R.id.get_button);
        actionButton = (Button) findViewById(R.id.actions);
        drawerSubstitute = (ViewGroup) findViewById(R.id.substitute_categories);
        drawerSubstitute.setVisibility(View.GONE);

        Log.d("JeevaIsAlarm",String.valueOf(isAlarm));

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        helper = new MyDBHelper(this);


        Log.d("JeevaAlarm", "On Create Called");
        Intent i = new Intent(MainActivity.this,MyNoteService.class);
        i.putExtra("type", "first");
        //stopService(i);
        MainActivity.helper.setAllZero();
        //startService(i);

        addMainButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
                i.putExtra("category", addNoteCategory);
                startActivityForResult(i, 102);
            }
        });

        putButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, OnlineUpdateActivity.class);
                startActivity(i);
            }
        });

        getButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, GetOnlineActivity.class);
                startActivity(i);
            }
        });



        createCategoryButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Jeeva", "Create Category");
                Intent i = new Intent(MainActivity.this, CreateCategoryActivity.class);
                startActivityForResult(i, 3001);
            }
        });

        deleteCategoryButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Jeeva", "Delete Category");
                Intent i = new Intent(MainActivity.this, DeleteCategoryActivity.class);
                tmpPos = 0;
                startActivityForResult(i, 3000);
            }
        });

        alarmNotesButtonI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AlarmNoteActivity.class);
                isAlarm = 1;
                startActivityForResult(i, 9001);

            }
        });




        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.action_buttons, null);
                createCategoryButton = (ViewGroup) dialogView.findViewById(R.id.create_category);
                deleteCategoryButton = (ViewGroup) dialogView.findViewById(R.id.delete_category);
                alarmNotesButton = (ViewGroup) dialogView.findViewById(R.id.alarm_notes);
                putButton = (ViewGroup) dialogView.findViewById(R.id.put_button);
                getButton = (ViewGroup) dialogView.findViewById(R.id.get_button);
                addButton = (ViewGroup) dialogView.findViewById(R.id.add_note);




                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();

                addButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
                        alertDialog.dismiss();
                        i.putExtra("category", addNoteCategory);
                        startActivityForResult(i, 102);
                    }
                });

                putButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(MainActivity.this, OnlineUpdateActivity.class);
                        alertDialog.dismiss();
                        startActivity(i);
                    }
                });

                getButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(MainActivity.this, GetOnlineActivity.class);
                        alertDialog.dismiss();
                        startActivity(i);
                    }
                });



                createCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Jeeva", "Create Category");
                        Intent i = new Intent(MainActivity.this, CreateCategoryActivity.class);
                        alertDialog.dismiss();
                        startActivityForResult(i, 3001);
                    }
                });

                deleteCategoryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Jeeva", "Delete Category");
                        Intent i = new Intent(MainActivity.this, DeleteCategoryActivity.class);
                        alertDialog.dismiss();
                        startActivityForResult(i, 3000);
                    }
                });

                alarmNotesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isAlarm = 1;
                        Log.d("JeevaIsAlarm", String.valueOf(isAlarm));
                        alertDialog.dismiss();
                        Log.d("Jeeva", "Alarm Notes");
                        AlarmFragment fragment = new AlarmFragment(MainActivity.this);
                        getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
                        drawer.closeDrawer(GravityCompat.START);

                    }
                });


            }
        });




        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        resetDrawer();
    }

    public void resetDrawer(){

        if(isAlarm == 1){
            AlarmFragment fragment = new AlarmFragment(MainActivity.this);
            getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
            drawer.closeDrawer(GravityCompat.START);
        }else{

            categories = helper.getAllCategory();
            Category catAll = new Category();
            categories.add(0, catAll);

            if(categories.size() == 1){
                categoryList.setVisibility(View.GONE);
                drawerSubstitute.setVisibility(View.VISIBLE);
            }else{
                drawerSubstitute.setVisibility(View.GONE);
                categoryList.setVisibility(View.VISIBLE);
                categoryList.setAdapter(new CategoryListAdapter());
            }



            if(tmpPos == 0){
                NoteFragment fragment = new NoteFragment(MainActivity.this);
                toolbar.setTitle("All Notes");
                getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
            }else{

                NoteFragment fragment = new NoteFragment(MainActivity.this,categories.get(tmpPos));
                toolbar.setTitle(String.valueOf(categories.get(tmpPos).getCategory()));
                getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
            }

            categoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    if(position == 0){

                        drawer.closeDrawer(GravityCompat.START);

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                addNoteCategory = "";
                                NoteFragment fragment = new NoteFragment(MainActivity.this);
                                toolbar.setTitle("All Notes");
                                getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
                            }
                        }, 200);

                    }else{

                        drawer.closeDrawer(GravityCompat.START);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                addNoteCategory = categories.get(position).getCategory().trim();
                                tmpPos = position;
                                NoteFragment fragment = new NoteFragment(MainActivity.this, categories.get(tmpPos));
                                toolbar.setTitle(categories.get(tmpPos).getCategory());
                                getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
                            }
                        }, 200);

                    }
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_update) {
            Intent i = new Intent(MainActivity.this,OnlineUpdateActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.action_fetch) {
            Intent i = new Intent(MainActivity.this,GetOnlineActivity.class);
            startActivity(i);
            return true;
        }

        if (id == R.id.add_note) {

            Log.d("JeevaIsAlarm",String.valueOf(isAlarm));

            if(isAlarm == 1){

                Intent i = new Intent(MainActivity.this, AlarmNoteActivity.class);
                isAlarm = 1;
                startActivityForResult(i, 9001);

            }else{
                Intent i = new Intent(MainActivity.this,AddNoteActivity.class);
                i.putExtra("category",addNoteCategory);
                startActivityForResult(i, 100);
            }


        }

        if (id == R.id.action_search) {

            if(search != null){
                isHtml = true;
                searchGroup.setVisibility(View.VISIBLE);
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
                searchGroup.startAnimation(animation);

            }

        }

        return super.onOptionsItemSelected(item);
    }





    public class CategoryListAdapter extends BaseAdapter{

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
            TextView text = (TextView) view.findViewById(R.id.list_text);
            TextView count = (TextView) view.findViewById(R.id.category_count);
            View categoryColor = view.findViewById(R.id.list_color);


            if(position == 0){
                count.setText(String.valueOf(MainActivity.helper.getNotesCountAll()));
                count.setTextColor(Color.parseColor("#0b6257"));
                text.setText(String.valueOf("All"));
                categoryColor.setBackgroundColor(Color.parseColor("#0b6257"));
            }else{
                Category category = categories.get(position);
                count.setText(category.getCount());
                count.setTextColor(Color.parseColor(category.getColor()));
                text.setText(String.valueOf(category.getCategory()));
                categoryColor.setBackgroundColor(Color.parseColor(category.getColor()));
            }
            return view;
        }
    }


    ////////////////////////////////////////////////////////////////

    public class NoteFragment extends Fragment {

        private Context context;
        private Category category;
        private GridView notesList;
        private View noteColor;
        private ArrayList<Note> notes;
        private CardView notesSubstitute;
        private TextView notesSubstituteText;
        private boolean isAll = false;
        long duration = 100;

        public NoteFragment(Context ctx){
            context = ctx;
            isAll = true;
        }

        public NoteFragment(Context ctx,Category cat) {
            context = ctx;
            category = cat;
            MainActivity.toolbar.setTitle(category.getCategory());

            Category category = new Category();
            category.setCategory(cat.getCategory().trim());
            category.setColor(cat.getColor());

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_note, container, false);
            notesSubstitute = (CardView) view.findViewById(R.id.notes_substitute);
            notesSubstituteText = (TextView) view.findViewById(R.id.notes_substitute_text);
            notesSubstitute.setVisibility(View.GONE);
            notesList = (GridView) view.findViewById(R.id.notes_list);
            createNote = (Button) view.findViewById(R.id.create_note);
            closeSearch = (ImageView) view.findViewById(R.id.close_search);
            searchGroup = (ViewGroup) view.findViewById(R.id.search_group);
            search = (EditText) view.findViewById(R.id.search);
            searchGroup.setVisibility(View.GONE);


            closeSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isHtml = false;
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    searchGroup.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            searchGroup.setVisibility(View.GONE);
                            resetNotes();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            });

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("JeevaQ", "SEARCH_CHANGE: " + s);
                    findandChangeAdapter(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

            createNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
                    i.putExtra("category",addNoteCategory);
                    startActivityForResult(i, 102);
                }
            });


            if(isAll){
                notes =  MainActivity.helper.getAllNotesByCategoryAll();
            }else{
                notes =  MainActivity.helper.getAllNotesByCategory(category);
            }
            if(notes.size() != 0){
                NotesAdapter notesAdapter = new NotesAdapter(notes);
                notesList.setAdapter(notesAdapter);
            }else{
                notesSubstitute.setVisibility(View.VISIBLE);
                notesList.setVisibility(View.GONE);
            }

            return view;
        }



        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }

        class NotesAdapter extends BaseAdapter{

            ArrayList<Note> notesInAdapter;

            NotesAdapter(ArrayList<Note> tmp){
                notesInAdapter = tmp;
            }

            @Override
            public int getCount() {
                return notesInAdapter.size();
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
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                View view = convertView;

                LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.notes_list_item, parent, false);
                TextView title = (TextView) view.findViewById(R.id.note_title);
                TextView content = (TextView) view.findViewById(R.id.notes_content);
                TextView time = (TextView) view.findViewById(R.id.time);
                TextView rating = (TextView) view.findViewById(R.id.note_rating);
                View noteAlarm = view.findViewById(R.id.note_alarm);
                final ImageView deleteNote = (ImageView) view.findViewById(R.id.delete_note);
                ImageView editNote = (ImageView) view.findViewById(R.id.edit_note);

                noteColor = view.findViewById(R.id.note_color);
                final View finalView = view;


                time.setVisibility(View.GONE);
                noteAlarm.setVisibility(View.GONE);


                if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("1.0")){
                    rating.setText("R1");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("2.0")){
                    rating.setText("R2");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("3.0")){
                    rating.setText("R3");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("4.0")){
                    rating.setText("R4");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("5.0")){
                    rating.setText("R5");
                }else{
                    rating.setText("R0");
                }



                deleteNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.delete_alert, null);
                        Button deleteNote = (Button) dialogView.findViewById(R.id.delete_alert_button);

                        deleteNote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();
                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                                finalView.startAnimation(animation);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Note note = new Note();
                                        note.setId(notesInAdapter.get(position).getId());
                                        note.setCategory(notesInAdapter.get(position).getCategory());
                                        MainActivity.helper.deleteNote(note);
                                        resetNotes();
                                        resetDrawer();
                                    }
                                }, 300);
                            }
                        });

                        dialogBuilder.setView(dialogView);
                        alertDialog = dialogBuilder.create();
                        alertDialog.show();

                    }
                });

                editNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), EditActivity.class);
                        i.putExtra("id", notesInAdapter.get(position).getId());
                        i.putExtra("category", notesInAdapter.get(position).getCategory());
                        i.putExtra("priority", notesInAdapter.get(position).getPriority());
                        i.putExtra("title", notesInAdapter.get(position).getTitle());
                        i.putExtra("content", notesInAdapter.get(position).getContent());
                        i.putExtra("color", notesInAdapter.get(position).getColor());
                        startActivityForResult(i, 2300);
                    }
                });




                if(isAll){
                    title.setTextColor(Color.parseColor(notesInAdapter.get(position).getColor()));
                    noteColor.setBackgroundColor(Color.parseColor(notesInAdapter.get(position).getColor()));
                }else{
                    title.setTextColor(Color.parseColor(category.getColor()));
                    noteColor.setBackgroundColor(Color.parseColor(category.getColor()));
                }


                if(isHtml){
                    title.setText(Html.fromHtml(notesInAdapter.get(position).getTitle()));
                    String tmp = String.valueOf(notesInAdapter.get(position).getContent()).replaceAll("\n","<br />");
                    content.setText(Html.fromHtml(tmp));
                }else{
                    title.setText((notesInAdapter.get(position).getTitle()));
                    content.setText((notesInAdapter.get(position).getContent()));
                }

                if(String.valueOf(title.getText()).equalsIgnoreCase("")){
                    title.setVisibility(View.GONE);
                }
                if(String.valueOf(content.getText()).equalsIgnoreCase("")){
                    content.setVisibility(View.GONE);
                }

                return view;
            }
        }

        private void findandChangeAdapter(String search) {


            boolean present = false;

            String[] tags = search.split(" ");

            for(int j = 0;j < tags.length; j++){
                Log.d("JeevaQ","TAGS: " + tags[j]);
            }

            if(isAll){
                notes =  MainActivity.helper.getAllNotesByCategoryAll();
            }else{
                notes =  MainActivity.helper.getAllNotesByCategory(category);
            }

            ArrayList<Note> searchNotes = notes;
            ArrayList<Integer> index = new ArrayList<>();
            ArrayList<Note> newNotes = new ArrayList<>();

            for(int i = 0; i < searchNotes.size(); i++){


                for(int j = 0;j < tags.length; j++){

                    if(searchNotes.get(i).getTitle().toLowerCase().contains(tags[j].toLowerCase())){
                        present = true;
                        break;

                    }else if(searchNotes.get(i).getContent().toLowerCase().contains(tags[j].toLowerCase())){
                        present = true;
                        break;

                    }else{
                        present = false;
                    }


                }

                Log.d("JeevaQ","SEARCH_PRESENT: " + String.valueOf(i) +  "," +  searchNotes.get(i).getTitle() + "," + searchNotes.get(i).getContent() + "," + String.valueOf(present));

                if(present == false){
                    index.add(new Integer(i));
                }
            }

            for(int i = 0;i < searchNotes.size();i++){

                if(index.contains(i)){
                    Log.d("JeevaQ","NOT_INCLUDED: " + searchNotes.get(i).getTitle() + ", " + searchNotes.get(i).getContent());
                }else{


                    newNotes.add(searchNotes.get(i));

                    for(int k = 0; k < newNotes.size(); k++){

                        for(int l = 0;l < tags.length; l++){

                            if(newNotes.get(k).getTitle().toLowerCase().contains(tags[l].toLowerCase())){

                            }else if(newNotes.get(k).getContent().toLowerCase().contains(tags[l].toLowerCase())){
                                if(!tags[l].equalsIgnoreCase("")) {
                                    Log.d("JEEVA_TAGS", "Content  Present");
                                    Note note = newNotes.get(k);
                                    note.setContent(String.valueOf(newNotes.get(k).getContent().toLowerCase()).replaceAll(tags[l].toLowerCase(), "<font color='red'>" + tags[l].toLowerCase() + "</font>"));
                                    break;
                                }

                            }else{
                            }


                        }

                        Log.d("JEEVA_TAGS_TITLE",String.valueOf(newNotes.get(k).getTitle()));
                        Log.d("JEEVA_TAGS_CONTENT",String.valueOf(newNotes.get(k).getContent()));
                    }

                }
            }

            if(newNotes.size() != 0){
                NotesAdapter notesAdapter = new NotesAdapter(newNotes);
                notesList.setVisibility(View.VISIBLE);
                notesList.setAdapter(notesAdapter);
            }else{

                notesList.setVisibility(View.GONE);
            }
        }



        private void resetNotes() {

            if (isAll) {

                notes = MainActivity.helper.getAllNotesByCategoryAll();
            } else {

                notes = MainActivity.helper.getAllNotesByCategory(category);
            }

            for(int i=0;i<notes.size();i++){

                notes.get(i).getTitle().replaceAll("\n","\n<br/>");
                notes.get(i).getContent().replaceAll("\n","\n<br/>");
                Log.d("JEEVA_BREAK",notes.get(i).getTitle());
                Log.d("JEEVA_BREAK",notes.get(i).getContent());
            }
            if (notes.size() != 0) {
                NotesAdapter notesAdapter = new NotesAdapter(notes);
                notesList.setVisibility(View.VISIBLE);
                notesList.setAdapter(notesAdapter);
            } else {
                tmpPos = 0;
                notesSubstitute.setVisibility(View.VISIBLE);
                notesList.setVisibility(View.GONE);
            }
        }
    }

    ///////////////////////////////////////////////////////////


    public class AlarmFragment extends Fragment{


        private Context context;
        private Category category;
        private GridView notesList;
        private View noteColor;
        private ArrayList<AlarmNote> notes;
        private CardView notesSubstitute;
        private TextView notesSubstituteText;
        private boolean isAll = false;


        public AlarmFragment(Context ctx) {
            context = ctx;
            MainActivity.toolbar.setTitle("Alarm Notes");

        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_note, container, false);
            notesSubstitute = (CardView) view.findViewById(R.id.notes_substitute);
            notesSubstituteText = (TextView) view.findViewById(R.id.notes_substitute_text);
            notesSubstitute.setVisibility(View.GONE);
            notesList = (GridView) view.findViewById(R.id.notes_list);
            createNote = (Button) view.findViewById(R.id.create_note);
            closeSearch = (ImageView) view.findViewById(R.id.close_search);
            searchGroup = (ViewGroup) view.findViewById(R.id.search_group);
            search = (EditText) view.findViewById(R.id.search);
            searchGroup.setVisibility(View.GONE);

            notesSubstituteText.setText("No Alarm Notes Present");
            createNote.setText("Create Alarm Note");

            closeSearch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    searchGroup.startAnimation(animation);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            searchGroup.setVisibility(View.GONE);
                            resetNotes();
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                }
            });

            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("JeevaQ", "SEARCH_CHANGE: " + s);
                    findandChangeAdapter(String.valueOf(s));
                }

                @Override
                public void afterTextChanged(Editable s) {

                }

            });

            createNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, AlarmNoteActivity.class);
                    isAlarm = 1;
                    startActivityForResult(i, 9001);
                }
            });

            notes =  MainActivity.helper.getAlarmNotes();

            if(notes.size() != 0){
                AlarmAdapter notesAdapter = new AlarmAdapter(notes);
                notesList.setAdapter(notesAdapter);
            }else{
                notesSubstitute.setVisibility(View.VISIBLE);
                notesList.setVisibility(View.GONE);
            }

            return view;
        }



        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
        }

        @Override
        public void onDetach() {
            super.onDetach();
        }

        class AlarmAdapter extends BaseAdapter{

            ArrayList<AlarmNote> notesInAdapter;

            AlarmAdapter(ArrayList<AlarmNote> tmp){
                notesInAdapter = tmp;
            }

            @Override
            public int getCount() {
                return notesInAdapter.size();
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
            public boolean isEnabled(int position) {
                return false;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {

                View view = convertView;

                LayoutInflater inflater = (LayoutInflater)LayoutInflater.from(getActivity());
                view = inflater.inflate(R.layout.notes_list_item, parent, false);
                TextView title = (TextView) view.findViewById(R.id.note_title);
                TextView time = (TextView) view.findViewById(R.id.time);
                TextView content = (TextView) view.findViewById(R.id.notes_content);
                TextView rating = (TextView) view.findViewById(R.id.note_rating);
                View noteColor = view.findViewById(R.id.note_color);
                noteColor.setVisibility(View.GONE);


                if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("1.0")){
                    rating.setText("R1");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("2.0")){
                    rating.setText("R2");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("3.0")){
                    rating.setText("R3");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("4.0")){
                    rating.setText("R4");
                }else if(notesInAdapter.get(position).getPriority().equalsIgnoreCase("5.0")){
                    rating.setText("R5");
                }else{
                    rating.setText("R0");
                }



                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                Calendar calendar = Calendar.getInstance();

                Date dateT = new Date(Long.parseLong(notesInAdapter.get(position).getTime()));
                DateFormat formatter = new SimpleDateFormat("hh:mm a");
                String dateFormatted = formatter.format(dateT);

                calendar.setTimeInMillis(Long.parseLong(notesInAdapter.get(position).getTime()));

                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH) + 1;
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);
                String date = String.valueOf(mDay) + "/" + String.valueOf(mMonth) + "/" + String.valueOf(mYear) + " " + dateFormatted;

                time.setText(date);

                final ImageView deleteNote = (ImageView) view.findViewById(R.id.delete_note);
                ImageView editNote = (ImageView) view.findViewById(R.id.edit_note);
                editNote.setVisibility(View.GONE);

                final View finalView = view;

                deleteNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.delete_alert, null);
                        Button deleteNote = (Button) dialogView.findViewById(R.id.delete_alert_button);

                        deleteNote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                alertDialog.dismiss();
                                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                                finalView.startAnimation(animation);

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        AlarmNote note = new AlarmNote();
                                        note.setId(notesInAdapter.get(position).getId());
                                        MainActivity.helper.removeAlarm(note.getId());
                                        resetNotes();
                                        resetDrawer();
                                    }
                                }, 300);

                            }
                        });

                        dialogBuilder.setView(dialogView);
                        alertDialog = dialogBuilder.create();
                        alertDialog.show();

                    }
                });

                title.setText(notesInAdapter.get(position).getTitle());

                content.setText(notesInAdapter.get(position).getContent());


                if(String.valueOf(title.getText()).equalsIgnoreCase("")){
                    title.setVisibility(View.GONE);
                }
                if(String.valueOf(content.getText()).equalsIgnoreCase("")){
                    content.setVisibility(View.GONE);
                }

                return view;
            }
        }

        private void findandChangeAdapter(String search) {


            boolean present = false;

            String[] tags = search.split(" ");

            for(int j = 0;j < tags.length; j++){
                Log.d("JeevaQ","TAGS: " + tags[j]);
            }

            ArrayList<AlarmNote> notes;

            notes = MainActivity.helper.getAlarmNotes();

            ArrayList<AlarmNote> searchNotes = notes;
            ArrayList<Integer> index = new ArrayList<>();
            ArrayList<AlarmNote> newNotes = new ArrayList<>();

            for(int i = 0; i < searchNotes.size(); i++){

                for(int j = 0;j < tags.length; j++){

                    if(searchNotes.get(i).getTitle().toLowerCase().contains(tags[j].toLowerCase())){
                        present = true;
                        break;

                    }else if(searchNotes.get(i).getContent().toLowerCase().contains(tags[j].toLowerCase())){
                        present = true;
                        break;

                    }else{
                        present = false;
                    }


                }

                Log.d("JeevaQ","SEARCH_PRESENT: " + String.valueOf(i) +  "," +  searchNotes.get(i).getTitle() + "," + searchNotes.get(i).getContent() + "," + String.valueOf(present));

                if(present == false){
                    index.add(new Integer(i));
                }
            }

            for(int i = 0;i < searchNotes.size();i++){

                if(index.contains(i)){
                    Log.d("JeevaQ","NOT_INCLUDED: " + searchNotes.get(i).getTitle() + ", " + searchNotes.get(i).getContent());
                }else{
                    newNotes.add(searchNotes.get(i));
                }
            }

            if(newNotes.size() != 0){
                AlarmAdapter notesAdapter = new AlarmAdapter(newNotes);
                notesList.setVisibility(View.VISIBLE);
                notesList.setAdapter(notesAdapter);
            }else{

                notesList.setVisibility(View.GONE);
            }
        }



        private void resetNotes() {

            ArrayList<AlarmNote> notes;
            notes = MainActivity.helper.getAlarmNotes();

            if (notes.size() != 0) {
                AlarmAdapter notesAdapter = new AlarmAdapter(notes);
                notesList.setVisibility(View.VISIBLE);
                notesList.setAdapter(notesAdapter);
            } else {
                notesSubstitute.setVisibility(View.VISIBLE);
                notesList.setVisibility(View.GONE);
            }
        }


    }


    ///////////////////////////////////////////////////////////

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("JeevaIsAlarm", String.valueOf(isAlarm));
        drawer.closeDrawers();

        if(isAlarm == 1){

            Log.d("Jeeva", "Alarm Notes");
            AlarmFragment fragment = new AlarmFragment(MainActivity.this);
            getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();

        }else{
            resetDrawer();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("JeevaIsAlarm", String.valueOf(isAlarm));

        if(isAlarm == 1){

            toolbar.setTitle("Alarm Notes");
            Log.d("Jeeva", "Alarm Notes");
            AlarmFragment fragment = new AlarmFragment(MainActivity.this);
            getSupportFragmentManager().beginTransaction().replace(R.id.notes_fragment, fragment).commit();
            isAlarm = 0;

        }else{
            resetDrawer();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



}
