package com.meghna.project.notes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String>notes=new ArrayList<String>();
    static ArrayAdapter arrayAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.addnotemenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        super.onOptionsItemSelected(menuItem);
        if(menuItem.getItemId()==R.id.add)
        {
            Intent intent=new Intent(getApplicationContext(),NoteEditorActivity.class);
            startActivity(intent);
            Log.i("Msg1","Menu selected");
            return true;
        }
        return false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.meghna.project.notes", Context.MODE_PRIVATE);
        HashSet<String> set2= (HashSet<String>)sharedPreferences.getStringSet("notes",null);
        if(set2==null) {
            notes.add("Note 1");

        }
        else
        {
            notes=new ArrayList(set2);
            Log.i("Message","Saved notes loaded");
        }
        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,notes);

        ListView listView=(ListView)findViewById(R.id.mylist);
        listView.setAdapter(arrayAdapter);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),NoteEditorActivity.class);
                intent.putExtra("noteid",i);
                Log.i("info","item clicked");
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemtodelete=i;
                Log.i("info","long click");
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Delet Note")
                        .setMessage("Do you want to delete note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                notes.remove(itemtodelete);
                                arrayAdapter.notifyDataSetChanged();
                                Log.i("info","item deleted");

                                SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("com.meghna.project.notes", Context.MODE_PRIVATE);
                                HashSet<String> set=new HashSet<String>(MainActivity.notes);
                                sharedPreferences.edit().putStringSet("Notes",set).apply();
                                Log.i("info","shared pref updated");


                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
            }
        });
    }
}
