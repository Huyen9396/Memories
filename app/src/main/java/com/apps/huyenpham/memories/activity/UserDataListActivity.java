package com.apps.huyenpham.memories.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.apps.huyenpham.memories.R;
import com.apps.huyenpham.memories.adapter.UserDataListAdapter;
import com.apps.huyenpham.memories.model.UserData;

import java.util.ArrayList;

import static com.apps.huyenpham.memories.model.Utils.COL_ID;
import static com.apps.huyenpham.memories.model.Utils.TABLE_NAME;
import static com.apps.huyenpham.memories.model.Utils.database;
import static com.apps.huyenpham.memories.model.Utils.idData;

public class UserDataListActivity extends AppCompatActivity {
    private ListView userDataList;
    private ArrayList<UserData> userDataArrayList;
    private UserDataListAdapter adapter;
    private final String GET_ALL_DATA = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_ID + " DESC";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_list);

        userDataList = (ListView) findViewById(R.id.user_data_list);
        userDataArrayList = new ArrayList<>();

        adapter = new UserDataListAdapter(UserDataListActivity.this, userDataArrayList);
        userDataList.setAdapter(adapter);
        getData();

        userDataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                idData = userDataArrayList.get(position).getId();
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                startActivity(intent);
            }
        });

        userDataList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteDialog(userDataArrayList.get(position).getId());
                return true;
            }
        });

    }

    private void getData() {
        Cursor cursor = database.getData(GET_ALL_DATA);
        userDataArrayList.clear();
        while (cursor != null && cursor.moveToNext()) {
            userDataArrayList.add(new UserData(
                    cursor.getInt(0),
                    cursor.getBlob(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getDouble(6),
                    cursor.getDouble(7)
            ));
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteDialog(final int id) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete this data");
        dialog.setMessage("Are you sure?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.queryData("DELETE FROM " + TABLE_NAME + " WHERE id = '"+ id +"'");
                getData();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(R.anim.anim_activity_in, R.anim.anim_activity_out);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
