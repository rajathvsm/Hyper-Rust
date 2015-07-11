package com.moodilabs.hyperrust;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class ActivityEdit extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        final EditText editText = (EditText) findViewById(R.id.editMessage);
        Button buttonSave = (Button) findViewById(R.id.saveButton);
        Button buttonClear = (Button) findViewById(R.id.clearButton);
        Button buttonCancel = (Button) findViewById(R.id.cancelButton);

        String message = intent.getStringExtra("editString");
        final int position = intent.getIntExtra("editPosition", 0);

        editText.setText(message);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String saveMessage = editText.getText().toString();

                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", saveMessage);
                returnIntent.putExtra("position", position);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });

    }


}
