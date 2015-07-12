package com.moodilabs.hyperrust;
/**
 * Type first convention used to simplify autocomplete.
 * Using Shared Preferences instead of DB to code quickly.
 * */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

/*
*
* */
public class ActivityMain extends Activity {

    private Button buttonSave;
    private EditText editTextMessage;
    private ListView listViewMessage;

    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter<String> arrayListAdapter;

    private SharedPreferences sharedPreferences;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        arrayListAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,arrayList);


        buttonSave = (Button)findViewById(R.id.buttonSave);
        editTextMessage = (EditText)findViewById(R.id.editText);
        listViewMessage = (ListView)findViewById(R.id.listView);


        initialize();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editTextMessage.getText().toString();
                if(message.trim().equals(""))
                    return;
                else{
                    arrayList.add(message);
                    arrayListAdapter.notifyDataSetChanged();
                    editTextMessage.setText("");
                }
            }
        });


        listViewMessage.setAdapter(arrayListAdapter);
        listViewMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ClipData clip = ClipData.newPlainText("Copied text via Hyper Rust", (String)listViewMessage.getItemAtPosition(position));
                clipboard.setPrimaryClip(clip);
                showToast("Copied onto the clipboard successfully");
            }
        });
        listViewMessage.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ActivityMain.this);
                alert.setTitle("What you want to do?");
                alert.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent startEditActivity = new Intent(context, ActivityEdit.class);
                        startEditActivity.putExtra("editString", (String) listViewMessage.getItemAtPosition(position));
                        startEditActivity.putExtra("editPosition", position);
                        startActivityForResult(startEditActivity, 1);
                    }
                });

                alert.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(position);
                        arrayListAdapter.notifyDataSetChanged();
                    }
                });

                alert.setNeutralButton("Share", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String message = (String) listViewMessage.getItemAtPosition(position);
                        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);
                        startActivity(Intent.createChooser(shareIntent, "Share text via"));


                    }
                });

                alert.show();

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                String result=data.getStringExtra("result");
                int position = data.getIntExtra("position",0);
                arrayList.set(position,result);
                arrayListAdapter.notifyDataSetChanged();
            }
            if (resultCode == RESULT_CANCELED) {
                //Empty
            }
        }
    }

    /**
     * */
    private void initialize() {

        Set set = sharedPreferences.getStringSet("set", null);
        if(set!=null)
            arrayList.addAll(set);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(arrayList!=null) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            //Using LinkedHashSet to preserve order
            Set set = new LinkedHashSet(arrayList);
            edit.putStringSet("set", set);
            edit.apply();
        }
    }
    /**
     *
     * */
    private void showToast(String message){
        Toast toast =Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.show();

    }
}
