package com.noteitapp.dev.noteit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;


public class CreateCategoryActivity extends AppCompatActivity {


    Button colorSelectorButton,colorPickerButton;
    View colorView,pickerView,colorSelectorView,colorPickerView;
    AlertDialog alertDialog,colorDialog,pickerDialog;
    SeekBar red,green,blue;
    TextView redText,greenText,blueText;
    EditText categoryName;
    Button pickerButton,selectorButton,saveButton;
    int r = 0,g = 0,b = 0;
    ArrayList<String> colors;

    String colorSelected = "#C67C67",tmpColor =  "#C67C67";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        colorPickerButton = (Button) findViewById(R.id.color_pick);
        colorSelectorButton = (Button) findViewById(R.id.color_select);colorView = findViewById(R.id.color_view);
        saveButton = (Button) findViewById(R.id.save_button);
        categoryName = (EditText) findViewById(R.id.category_name);

        colors = MainActivity.helper.getAllColors();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(String.valueOf(categoryName.getText()).equalsIgnoreCase("")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateCategoryActivity.this);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
                    builder.setTitle("Empty Category");
                    builder.setMessage("Enter a valid category name");
                    builder.show();

                }else{

                    if(colors.contains(colorSelected)){

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateCategoryActivity.this);
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

                        Category category = new Category();
                        category.setCategory(String.valueOf(categoryName.getText()).trim());
                        category.setColor(colorSelected);
                        category.setCount("0");
                        MainActivity.helper.addCategory(category);
                        finish();
                    }
                }
            }
        });





        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder dialogBuilderT = new AlertDialog.Builder(CreateCategoryActivity.this);
                LayoutInflater inflaterT = getLayoutInflater();
                final View dialogViewT = inflaterT.inflate(R.layout.color_picker, null);
                pickerButton = (Button) dialogViewT.findViewById(R.id.picker_button);
                red = (SeekBar) dialogViewT.findViewById(R.id.red);
                green = (SeekBar) dialogViewT.findViewById(R.id.green);
                blue = (SeekBar) dialogViewT.findViewById(R.id.blue);
                redText = (TextView) dialogViewT.findViewById(R.id.red_text);
                blueText = (TextView) dialogViewT.findViewById(R.id.blue_text);
                greenText = (TextView) dialogViewT.findViewById(R.id.green_text);
                red.setProgress(198);
                green.setProgress(124);
                blue.setProgress(103);
                redText.setText(String.valueOf(198));
                blueText.setText(String.valueOf(103));
                greenText.setText(String.valueOf(124));
                colorPickerView = dialogViewT.findViewById(R.id.color_picker_view);
                pickerView = dialogViewT.findViewById(R.id.picker_view);

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
                        colorSelected = String.format("#%02x%02x%02x", r, g, b);
                        if (colors.contains(colorSelected)) {

                            pickerDialog.dismiss();
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateCategoryActivity.this);
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

                            Log.d("Jeeva", "Color: " + colorSelected);
                            colorView.setBackgroundColor(Color.parseColor(colorSelected));
                            pickerDialog.dismiss();
                        }


                    }
                });

                dialogBuilderT.setView(dialogViewT);
                pickerDialog = dialogBuilderT.create();
                pickerDialog.show();



            }
        });

        colorSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateCategoryActivity.this);
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
                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CreateCategoryActivity.this);
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

    class GridAdapter extends BaseAdapter{

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
