package com.chienpao.notepad.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chienpao.notepad.notepad.adapter.PatientAdapter;
import com.chienpao.notepad.notepad.model.Patient;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends BasicActivity {
    public static final String TAG = "MainActivity";

    private LinearLayout rootLayout = null;
    private TextView mPatientSumTextView;
    private Button mDeleteHistoryButton;
    private ListView mPatientListView;
    private PatientAdapter mPatientAdapter;
    private ArrayList<Patient> mPatientArrayList;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        mPatientSumTextView = (TextView) findViewById(R.id.patient_sum_textView);
        mDeleteHistoryButton = (Button) findViewById(R.id.delete_history_button);
        mPatientArrayList = new ArrayList<>();
        mPatientAdapter = new PatientAdapter(this, mPatientArrayList);
        mPatientListView = (ListView) findViewById(R.id.patient_list_view);
        mPatientListView.setAdapter(mPatientAdapter);
        mPatientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient selectedPatient = (Patient)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, EditPatientActivity.class);
                intent.putExtra("selectedPatientId", selectedPatient.getId());
                startActivity(intent);
            }
        });

        // Open the default Realm for the UI thread.
        realm = Realm.getInstance(this);

        mDeleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                realm.beginTransaction();
                realm.allObjects(Patient.class).clear();
                realm.commitTransaction();
                onRefresh();

                Toast.makeText(MainActivity.this, getString(R.string.history_activity_delete_history_toast), Toast.LENGTH_LONG).show();
                mDeleteHistoryButton.setEnabled(false);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        onRefresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds Patients to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem Patient) {
        // Handle action bar Patient clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = Patient.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(Patient);
    }

    public void onClickHandler(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.new_button:
                intent = new Intent(this, AddPatientActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public boolean isBackable() {
        return true;
    }

    private void onRefresh() {
        // Clear history array list
        mPatientArrayList.clear();

        // Load all Patients from realm
        RealmResults<Patient> PatientResult = realm.where(Patient.class).findAll();
        for (Patient Patient : PatientResult)
            mPatientArrayList.add(Patient);

        if (mPatientArrayList.isEmpty()) {
            mPatientSumTextView.setText(MainActivity.this.getString(R.string.main_activity_notes, "0"));
            mDeleteHistoryButton.setEnabled(false);
        } else {
            mDeleteHistoryButton.setEnabled(true);
            mPatientSumTextView.setText(MainActivity.this.getString(R.string.main_activity_notes, Integer.toString(mPatientArrayList.size())));
        }

        mPatientAdapter.notifyDataSetChanged();
    }
}
