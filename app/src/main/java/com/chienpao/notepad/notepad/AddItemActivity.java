package com.chienpao.notepad.notepad;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chienpao.notepad.notepad.adapter.ItemAdapter;
import com.chienpao.notepad.notepad.model.Item;

import java.util.ArrayList;

import io.realm.Realm;

public class AddItemActivity extends BasicActivity {
    private static final String TAG = "AddItemActivity";
    private ArrayList<Item> mItemArrayList;
    private ListView mListView;
    private ItemAdapter mItemAdapter;
    private Button mSaveButton;
    private Button mNewButton;
    private Realm mRealm;
    private LinearLayout containerLinearLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // Open the default Realm for the UI thread.
        mRealm = Realm.getInstance(this);

        containerLinearLayout = ((LinearLayout) findViewById(R.id.container));
        containerLinearLayout.removeAllViews();

        mItemArrayList = new ArrayList<>();
        mItemArrayList.add(new Item("Steve", 30, "February 21, 1986"));
        mSaveButton = (Button) findViewById(R.id.save_button);
        mNewButton = (Button) findViewById(R.id.new_button);
        mItemAdapter = new ItemAdapter(this, mItemArrayList, mSaveButton);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.setAdapter(mItemAdapter);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // All writes must be wrapped in a transaction to facilitate safe multi threading
                mRealm.beginTransaction();

                // Add items in realm
                for (Item item : mItemArrayList) {
                    Item realItem = mRealm.createObject(Item.class);
                    realItem.setName(item.getName());
                    realItem.setCount(item.getCount());
                    realItem.setDate(item.getDate());
                }

                // When the transaction is committed, all changes a synced to disk.
                mRealm.commitTransaction();

                // Clear array list
                mItemArrayList.clear();
                mItemAdapter.refreshAdapter();

                // Set save button to disable
                mSaveButton.setEnabled(false);

                Toast.makeText(AddItemActivity.this, getString(R.string.add_item_activity_save_toast), Toast.LENGTH_LONG).show();

                // Find the All item (no query conditions) and read a field
                //RealmResults<Item> itemResult = mRealm.where(Item.class).findAll();
                //for(Item item : itemResult)
                //    showStatus(item.getName() + ":" + item.getCount() + ":" + item.getNote());
            }
        });

        mNewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // set save button enable
                mSaveButton.setEnabled(true);

                // Add new item to array list
                mItemArrayList.add(new Item());
                mItemAdapter.refreshAdapter();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        //containerLinearLayout.removeAllViews();
        containerLinearLayout.addView(tv);
    }

    @Override
    public boolean isBackable() {
        return true;
    }
}
