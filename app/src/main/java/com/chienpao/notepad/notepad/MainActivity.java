package com.chienpao.notepad.notepad;

import android.content.Intent;
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

import com.chienpao.notepad.notepad.adapter.HistoryItemAdapter;
import com.chienpao.notepad.notepad.model.Cat;
import com.chienpao.notepad.notepad.model.Dog;
import com.chienpao.notepad.notepad.model.Item;
import com.chienpao.notepad.notepad.model.Person;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends BasicActivity {
    public static final String TAG = "MainActivity";

    private LinearLayout rootLayout = null;
    private TextView mItemSumTextView;
    private Button mDeleteHistoryButton;
    private ListView mHistoryListView;
    private HistoryItemAdapter mHistoryItemAdapter;
    private ArrayList<Item> mHistoryItemArrayList;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        mItemSumTextView = (TextView) findViewById(R.id.item_sum_textView);
        mDeleteHistoryButton = (Button) findViewById(R.id.delete_history_button);
        mHistoryItemArrayList = new ArrayList<>();
        mHistoryItemAdapter = new HistoryItemAdapter(this, mHistoryItemArrayList);
        mHistoryListView = (ListView) findViewById(R.id.history_list_view);
        mHistoryListView.setAdapter(mHistoryItemAdapter);

        // Open the default Realm for the UI thread.
        realm = Realm.getInstance(this);

        mDeleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.beginTransaction();
                realm.allObjects(Item.class).clear();
                realm.commitTransaction();
                mHistoryItemArrayList.clear();
                mHistoryItemAdapter.refreshAdapter();

                Toast.makeText(MainActivity.this, getString(R.string.history_activity_delete_history_toast), Toast.LENGTH_LONG).show();
                mDeleteHistoryButton.setEnabled(false);
            }
        });

        // These operations are small enough that
        // we can generally safely run them on the UI thread.

        //basicCRUD(realm);
        //basicQuery(realm);
        //basicLinkQuery(realm);

        // More complex operations can be executed on another thread.
        /*new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String info;
                info = complexReadWrite();
                info += complexQuery();
                return info;
            }

            @Override
            protected void onPostExecute(String result) {
                showStatus(result);
            }
        }.execute();*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Clear history array list
        mHistoryItemArrayList.clear();

        // Load all items from realm
        RealmResults<Item> itemResult = realm.where(Item.class).findAll();
        for (Item item : itemResult)
            mHistoryItemArrayList.add(item);

        if (mHistoryItemArrayList.isEmpty()) {
            mItemSumTextView.setText(getString(R.string.main_activity_notes, "0"));
            mDeleteHistoryButton.setEnabled(false);
        } else {
            mDeleteHistoryButton.setEnabled(true);
            mItemSumTextView.setText(getString(R.string.main_activity_notes, Integer.toString(mHistoryItemArrayList.size())));
        }

        mHistoryItemAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    private void showStatus(String txt) {
        Log.i(TAG, txt);
        TextView tv = new TextView(this);
        tv.setText(txt);
        rootLayout.addView(tv);
    }

    private void basicCRUD(Realm realm) {
        showStatus("Perform basic Create/Read/Update/Delete (CRUD) operations...");

        // All writes must be wrapped in a transaction to facilitate safe multi threading
        realm.beginTransaction();

        // Add a person
        Person person = realm.createObject(Person.class);
        person.setId(1);
        person.setName("Young Person");
        person.setAge(14);

        // When the transaction is committed, all changes a synced to disk.
        realm.commitTransaction();

        // Find the first person (no query conditions) and read a field
        person = realm.where(Person.class).findFirst();
        showStatus(person.getName() + ":" + person.getAge());

        // Update person in a transaction
        realm.beginTransaction();
        person.setName("Senior Person");
        person.setAge(99);
        showStatus(person.getName() + " got older: " + person.getAge());
        realm.commitTransaction();

        // Delete all persons
        realm.beginTransaction();
        realm.allObjects(Person.class).clear();
        realm.commitTransaction();
    }

    private void basicQuery(Realm realm) {
        showStatus("\nPerforming basic Query operation...");
        showStatus("Number of persons: " + realm.allObjects(Person.class).size());

        RealmResults<Person> results = realm.where(Person.class).equalTo("age", 99).findAll();

        showStatus("Size of result set: " + results.size());
    }

    private void basicLinkQuery(Realm realm) {
        showStatus("\nPerforming basic Link Query operation...");
        showStatus("Number of persons: " + realm.allObjects(Person.class).size());

        RealmResults<Person> results = realm.where(Person.class).equalTo("cats.name", "Tiger").findAll();

        showStatus("Size of result set: " + results.size());
    }

    private String complexReadWrite() {
        String status = "\nPerforming complex Read/Write operation...";

        // Open the default realm. All threads must use it's own reference to the realm.
        // Those can not be transferred across threads.
        Realm realm = Realm.getInstance(this);

        // Add ten persons in one transaction
        realm.beginTransaction();
        Dog fido = realm.createObject(Dog.class);
        fido.setName("fido");
        for (int i = 0; i < 10; i++) {
            Person person = realm.createObject(Person.class);
            person.setId(i);
            person.setName("Person no. " + i);
            person.setAge(i);
            person.setDog(fido);

            // The field tempReference is annotated with @Ignore.
            // This means setTempReference sets the Person tempReference
            // field directly. The tempReference is NOT saved as part of
            // the RealmObject:
            person.setTempReference(42);

            for (int j = 0; j < i; j++) {
                Cat cat = realm.createObject(Cat.class);
                cat.setName("Cat_" + j);
                person.getCats().add(cat);
            }
        }
        realm.commitTransaction();

        // Implicit read transactions allow you to access your objects
        status += "\nNumber of persons: " + realm.allObjects(Person.class).size();

        // Iterate over all objects
        for (Person pers : realm.allObjects(Person.class)) {
            String dogName;
            if (pers.getDog() == null) {
                dogName = "None";
            } else {
                dogName = pers.getDog().getName();
            }
            status += "\n" + pers.getName() + ":" + pers.getAge() + " : " + dogName + " : " + pers.getCats().size();

            // The field tempReference is annotated with @Ignore
            // Though we initially set its value to 42, it has
            // not been saved as part of the Person RealmObject:
            assert (pers.getTempReference() == 0);
        }

        // Sorting
        RealmResults<Person> sortedPersons = realm.allObjects(Person.class);
        sortedPersons.sort("age", Sort.DESCENDING);
        assert (realm.allObjects(Person.class).last().getName() == sortedPersons.first().getName());
        status += "\nSorting " + sortedPersons.last().getName() + " == " + realm.allObjects(Person.class).first().getName();

        realm.close();
        return status;
    }

    private String complexQuery() {
        String status = "\n\nPerforming complex Query operation...";

        Realm realm = Realm.getInstance(this);
        status += "\nNumber of persons: " + realm.allObjects(Person.class).size();

        // Find all persons where age between 7 and 9 and name begins with "Person".
        RealmResults<Person> results = realm.where(Person.class)
                .between("age", 7, 9)       // Notice implicit "and" operation
                .beginsWith("name", "Person").findAll();
        status += "\nSize of result set: " + results.size();

        realm.close();
        return status;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onClickHandler(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.add_button:
                intent = new Intent(this, AddItemActivity.class);
                startActivity(intent);
                break;
        }
    }
}
