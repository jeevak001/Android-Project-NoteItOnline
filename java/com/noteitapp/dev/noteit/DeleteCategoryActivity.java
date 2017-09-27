package com.noteitapp.dev.noteit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class DeleteCategoryActivity extends AppCompatActivity {


    private ArrayList<Category> categories,categoriesTmp;
    private ListView deleteList;
    private DeleteCategoryAdapter deleteAdapter;
    android.app.AlertDialog alertDialog,alertDialog1;
    private TextView deleteSubstitute;
    ListView list;
    ImageView up,down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        deleteList = (ListView) findViewById(R.id.delete_list);
        deleteList.setVisibility(View.VISIBLE);
        deleteSubstitute = (TextView) findViewById(R.id.delete_substitute);
        deleteSubstitute.setVisibility(View.GONE);

        categories = new ArrayList<>();
        categories = MainActivity.helper.getAllCategory();
        listCategory(categories,"Start");
        categoriesTmp = (ArrayList<Category>) categories.clone();

        if(categories.size() == 0){
            deleteSubstitute.setVisibility(View.VISIBLE);
        }else{
            deleteSubstitute.setVisibility(View.GONE);
            deleteAdapter = new DeleteCategoryAdapter(categories);
            deleteList.setAdapter(deleteAdapter);
        }



    }

    class DeleteCategoryAdapter extends BaseAdapter{

        ArrayList<Category> cats;
        ArrayList<Category> categoriesTmp;

        DeleteCategoryAdapter(ArrayList<Category> c){
            cats = c;
        }

        @Override
        public int getCount() {
            return cats.size();
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
        public View getView(final int position, View convertView, ViewGroup parent) {

            final View view;
            LayoutInflater inflater = (LayoutInflater) getApplication().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (View) inflater.inflate(R.layout.delete_list_item, null);
            up = (ImageView) view.findViewById(R.id.up);
            down = (ImageView) view.findViewById(R.id.down);
            final ViewGroup categoryGroup = (ViewGroup) view.findViewById(R.id.category_group);
            TextView text = (TextView) view.findViewById(R.id.delete_list_name);
            View color = view.findViewById(R.id.delete_list_color);
            Button remove = (Button) view.findViewById(R.id.delete_button);
            Button edit = (Button) view.findViewById(R.id.edit_button);
            Button removeAll = (Button) view.findViewById(R.id.delete_all_button);
            final Category category = cats.get(position);
            text.setText(String.valueOf(category.getCategory()));
            color.setBackgroundColor(Color.parseColor(category.getColor()));

            Log.d("Jeeva","CATEGORY_ITEM: " + category.getCategory() + ", " + category.getCount());

            int tmpPos = position;

            if(position == 0){
                up.setVisibility(View.GONE);
            }

            if(position == cats.size() - 1){
                down.setVisibility(View.GONE);
            }

            final int h1 = deleteList.getHeight();
            final int h2 = view.getHeight();

            up.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    categoriesTmp = (ArrayList<Category>) cats.clone();
                    listCategory(cats,"Main");
                    Category category = cats.get(position);
                    Category tmpCategory = categoriesTmp.get(position - 1);
                    categoriesTmp.set(position - 1,category);
                    categoriesTmp.set(position, tmpCategory);
                    listCategory(categoriesTmp, "Tmp");
                    cats = (ArrayList<Category>) categoriesTmp.clone();
                    listCategory(cats,"Changed Main");
                    refreshList(cats, position,h1,h2);
                }
            });

            down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Log.d("JeevaD", "DOWN_PRESSED");
                    categoriesTmp = (ArrayList<Category>) cats.clone();
                    listCategory(cats,"Main");
                    Category category = cats.get(position);
                    Category tmpCategory = categoriesTmp.get(position + 1);
                    categoriesTmp.set(position + 1,category);
                    categoriesTmp.set(position, tmpCategory);
                    listCategory(categoriesTmp, "Tmp");
                    cats = (ArrayList<Category>) categoriesTmp.clone();
                    listCategory(cats,"Changed Main");
                    refreshList(cats, position,h1,h2);
                }
            });

            if(position < (cats.size()-1)){
                removeAll.setVisibility(View.GONE);
            }

            removeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                            final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(DeleteCategoryActivity.this);
                            LayoutInflater inflater = DeleteCategoryActivity.this.getLayoutInflater();
                            final View dialogView = inflater.inflate(R.layout.delete_all_alert, null);
                            Button deleteNote = (Button) dialogView.findViewById(R.id.delete_all_alert_button);

                            deleteNote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    alertDialog.dismiss();
                                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                                    deleteList.startAnimation(animation);

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            MainActivity.helper.deleteAllCategory();
                                            refreshList();
                                            MainActivity.tmpPos = 0;
                                            finish();
                                        }
                                    }, 300);
                                }
                            });

                    dialogBuilder.setView(dialogView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();



                }
            });

            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final android.app.AlertDialog.Builder dialogBuilder = new android.app.AlertDialog.Builder(DeleteCategoryActivity.this);
                    LayoutInflater inflater = DeleteCategoryActivity.this.getLayoutInflater();
                    final View dialogView = inflater.inflate(R.layout.delete_category_alert, null);
                    Button deleteNote = (Button) dialogView.findViewById(R.id.delete_category_alert_button);
                    TextView textView = (TextView) dialogView.findViewById(R.id.delete_category_alert_text);
                    textView.setText("This will delete the Category " + category.getCategory());

                    deleteNote.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            alertDialog.dismiss();
                            Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_right);
                            categoryGroup.startAnimation(animation);
                            MainActivity.helper.deleteCategory(category);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                        refreshList(position - 1,h1,h2);
                                }
                            }, 300);
                        }
                    });

                    dialogBuilder.setView(dialogView);
                    alertDialog = dialogBuilder.create();
                    alertDialog.show();

                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent i = new Intent(DeleteCategoryActivity.this,EditCategoryActivity.class);
                    i.putExtra("id",cats.get(position).getId());
                    i.putExtra("category",cats.get(position).getCategory());
                    i.putExtra("color",cats.get(position).getColor());
                    i.putExtra("count",cats.get(position).getCount());
                    startActivityForResult(i,900);

                }
            });


            return view;
        }
    }


    public void listCategory(ArrayList<Category> cat,String tmp){

        Log.d("JeevaCAt","-------------------" + tmp +  "-------------------");

        for (Category c:cat){
            Log.d("JeevaCAt","CAT: " + c.getCategory() + ", " + c.getCount());
        }


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
        }
        return false;
    }

    private void refreshList() {
        deleteList.setVisibility(View.VISIBLE);
        ArrayList<Category> tmpCats;
        tmpCats = new ArrayList<>();
        tmpCats = MainActivity.helper.getAllCategory();

        if(tmpCats.size() == 0){
            deleteSubstitute.setVisibility(View.VISIBLE);
            deleteList.setVisibility(View.GONE);
        }else{
            deleteSubstitute.setVisibility(View.GONE);
            listCategory(tmpCats, "From Database");
            deleteAdapter = new DeleteCategoryAdapter(tmpCats);
            deleteList.setAdapter(deleteAdapter);
        }
    }

    private void refreshList(int tmp,int h1,int h2) {
        deleteList.setVisibility(View.VISIBLE);
        ArrayList<Category> tmpCats;
        tmpCats = new ArrayList<>();
        tmpCats = MainActivity.helper.getAllCategory();

        if(tmpCats.size() == 0){
            deleteSubstitute.setVisibility(View.VISIBLE);
            deleteList.setVisibility(View.GONE);
        }else{
            deleteSubstitute.setVisibility(View.GONE);
            listCategory(tmpCats, "From Database");
            deleteAdapter = new DeleteCategoryAdapter(tmpCats);
            deleteList.setAdapter(deleteAdapter);
            deleteList.setSelectionFromTop(tmp, h1 / 2 - h2 / 2);
        }
    }

    private void refreshList(ArrayList<Category> cat,int pos,int h1,int h2) {
        categories = cat;

        if(categories.size() == 0){
            deleteSubstitute.setVisibility(View.VISIBLE);
        } else{
            deleteSubstitute.setVisibility(View.GONE);
            MainActivity.helper.deleteCategoryOnly();
            MainActivity.helper.addCategories(cat);
            categories = MainActivity.helper.getAllCategory();
            listCategory(categories, "From Database");
            deleteAdapter = new DeleteCategoryAdapter(categories);
            deleteList.setAdapter(deleteAdapter);

            deleteList.setSelectionFromTop(pos, h1 / 2 - h2 / 2);        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        refreshList();
    }


}
