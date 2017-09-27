package com.noteitapp.dev.noteit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class EditCategoryActivity extends AppCompatActivity {

    Button colorSelectorButton,colorPickerButton;
    View colorView,pickerView,colorSelectorView,colorPickerView;
    AlertDialog alertDialog,colorDialog;
    SeekBar red,green,blue;
    TextView redText,greenText,blueText;
    EditText categoryName;
    Button pickerButton,selectorButton,saveButton;
    int r = 0,g = 0,b = 0;
    Intent i;
    String id,category,color,count;
    ArrayList<String> colors;

    String colorSelected = "#C67C67",tmpColor =  "#C67C67";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colors = MainActivity.helper.getAllColors();

        i = getIntent();
        id = i.getExtras().getString("id");
        category = i.getExtras().getString("category");
        color = i.getExtras().getString("color");
        count = i.getExtras().getString("count");



        colorPickerButton = (Button) findViewById(R.id.edit_category_color_pick_button);
        colorSelectorButton = (Button) findViewById(R.id.edit_category_color_select_button);
        colorView = findViewById(R.id.edit_category_color_view_button);
        saveButton = (Button) findViewById(R.id.edit_category_save_button);
        categoryName = (EditText) findViewById(R.id.edit_category_name_button);

        categoryName.setText(category);
        colorView.setBackgroundColor(Color.parseColor(color));

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (String.valueOf(categoryName.getText()).equalsIgnoreCase("")) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(EditCategoryActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setTitle("Empty Category");
                    builder.setMessage("Enter a valid category name");
                    builder.show();

                } else {

                    if(colors.contains(colorSelected)){

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditCategoryActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.color_alert, null);
                        Button ok = (Button) dialogView.findViewById(R.id.color_present);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                colorDialog.dismiss();
                            }
                        });
                        dialogBuilder.setView(dialogView);
                        colorDialog = dialogBuilder.create();
                        colorDialog.show();

                    }else{

                        Category categoryO = new Category();
                        categoryO.setCategory(String.valueOf(categoryName.getText()).trim());
                        categoryO.setColor(colorSelected);
                        categoryO.setCount(count);
                        categoryO.setId(id);
                        MainActivity.helper.updateCategory(categoryO,category);
                        finish();
                    }
                }
            }
        });

        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditCategoryActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.color_picker, null);
                pickerButton = (Button) dialogView.findViewById(R.id.picker_button);
                red = (SeekBar) dialogView.findViewById(R.id.red);
                green = (SeekBar) dialogView.findViewById(R.id.green);
                blue = (SeekBar) dialogView.findViewById(R.id.blue);
                pickerView = dialogView.findViewById(R.id.picker_view);
                redText = (TextView) dialogView.findViewById(R.id.red_text);
                blueText = (TextView) dialogView.findViewById(R.id.blue_text);
                greenText = (TextView) dialogView.findViewById(R.id.green_text);
                colorPickerView = dialogView.findViewById(R.id.color_picker_view);

                red.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        r = progress;
                        redText.setText(String.valueOf(progress));
                        pickerView.setBackgroundColor(Color.rgb(r, g, b));
                        colorPickerView.setBackgroundColor(Color.rgb(r, g, b));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                green.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        g = progress;
                        greenText.setText(String.valueOf(progress));
                        pickerView.setBackgroundColor(Color.rgb(r, g, b));
                        colorPickerView.setBackgroundColor(Color.rgb(r, g, b));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                blue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        b = progress;
                        blueText.setText(String.valueOf(progress));
                        pickerView.setBackgroundColor(Color.rgb(r, g, b));
                        colorPickerView.setBackgroundColor(Color.rgb(r, g, b));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });


                pickerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        colorSelected = String.format("#%02x%02x%02x", r,g,b);
                        if(colors.contains(colorSelected)){

                            alertDialog.dismiss();
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditCategoryActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.color_alert, null);
                            Button ok = (Button) dialogView.findViewById(R.id.color_present);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    colorDialog.dismiss();
                                }
                            });
                            dialogBuilder.setView(dialogView);
                            colorDialog = dialogBuilder.create();
                            colorDialog.show();

                        }else{

                            Log.d("Jeeva", "Color: " + colorSelected);
                            colorView.setBackgroundColor(Color.parseColor(colorSelected));
                            alertDialog.dismiss();
                        }
                    }
                });
                dialogBuilder.setView(dialogView);
                alertDialog = dialogBuilder.create();
                alertDialog.show();


            }
        });

        colorSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditCategoryActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.color_selector, null);
                colorSelectorView = dialogView.findViewById(R.id.color_selector_view);
                selectorButton = (Button) dialogView.findViewById(R.id.selector_button);

                GridView colorGrid = (GridView) dialogView.findViewById(R.id.color_grid);
                GridAdapter colorAdapter = new GridAdapter();
                colorGrid.setAdapter(colorAdapter);

                colorGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        colorSelectorView.setBackgroundColor(Color.parseColor(tmpColor));
                        tmpColor = (Utils.colors[position]);
                        colorSelectorView.setBackgroundColor(Color.parseColor(tmpColor));
                        Log.d("Jeeva", "Color: " + tmpColor);

                    }
                });

                selectorButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        colorSelected = tmpColor;

                        if (colors.contains(colorSelected)) {

                            alertDialog.dismiss();
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(EditCategoryActivity.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.color_alert, null);
                            Button ok = (Button) dialogView.findViewById(R.id.color_present);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    colorDialog.dismiss();
                                }
                            });
                            dialogBuilder.setView(dialogView);
                            colorDialog = dialogBuilder.create();
                            colorDialog.show();

                        } else {

                            colorView.setBackgroundColor(Color.parseColor(colorSelected));
                            alertDialog.dismiss();
                        }

                    }
                                                  }

                );

                dialogBuilder.setView(dialogView);
                alertDialog=dialogBuilder.create();
                alertDialog.show();
            }
        });

    }

    class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Utils.colors.length;
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
            view = inflater.inflate(R.layout.color_list_item,parent,false);
            View v = view.findViewById(R.id.color);
            v.setBackgroundColor(Color.parseColor(Utils.colors[position]));
            return view;
        }
    }

}
