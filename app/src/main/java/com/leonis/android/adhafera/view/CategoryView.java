package com.leonis.android.adhafera.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.leonis.android.adhafera.R;

/**
 * Created by leonis on 2018/02/11.
 *
 */

public class CategoryView extends InputView implements View.OnClickListener {
    private String[] categories;
    private boolean[] selected;
    private final TextView fieldView;

    public CategoryView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        View.inflate(context, R.layout.category_view, this);

        fieldView = findViewById(R.id.field_category);
        errorChecker = findViewById(R.id.check_category);

        Button selectButton = findViewById(R.id.select_category);
        selectButton.setOnClickListener(this);
    }

    public void setInputText(String text) {
        fieldView.setText(text);
    }

    public String getInputText() {
        return fieldView.getText().toString();
    }

    public void setCategories(String[] names) {
        categories = names;
        selected = new boolean[categories.length];
        for(int i=0;i<selected.length;i++) {
            selected[i] = false;
        }
    }

    @Override
    public void onClick(View v) {
        new AlertDialog.Builder(context)
                .setIcon(android.R.drawable.ic_menu_help)
                .setTitle("カテゴリを選択")
                .setMultiChoiceItems(categories, selected,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton, boolean isChecked) {
                                selected[whichButton] = isChecked;
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        StringBuilder category = new StringBuilder();
                        for(int i=0;i<selected.length;i++) {
                            if(selected[i]) {
                                category.append(category.toString().equals("") ? categories[i] : "," + categories[i]);
                            }
                        }
                        fieldView.setText(category.toString());
                    }
                })
                .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                })
                .show();
    }
}
