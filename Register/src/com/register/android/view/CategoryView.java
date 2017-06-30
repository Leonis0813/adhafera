package com.register.android.view;

import com.register.android.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CategoryView extends InputView implements OnClickListener {
  private String[] categories;
  private boolean[] selected;
  private TextView fieldView;
  private Button selectButton;

  public CategoryView(Context context, AttributeSet attributeSet) {
    super(context, attributeSet);

    View.inflate(context, R.layout.category_view, this);

    fieldView = (TextView) findViewById(R.id.field_category);
    errorChecker = (TextView) findViewById(R.id.check_category);

    selectButton = (Button) findViewById(R.id.select_category);
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
        String category = "";
        for(int i=0;i<selected.length;i++) {
          if(selected[i]) {
            category += category.equals("") ? categories[i] : "," + categories[i];
          }
        }
        fieldView.setText(category);
      }
    })
    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int whichButton) {}
    })
    .show();    
  }
}
