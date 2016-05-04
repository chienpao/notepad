package com.chienpao.notepad.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chienpao.notepad.notepad.adapter.PatientAdapter;
import com.chienpao.notepad.notepad.model.Patient;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class ViewOldPatientActivity extends BasicActivity {
    public static final String TAG = "ViewOldPatientActivity";

    private LinearLayout rootLayout = null;
    private TextView mPatientSumTextView;
    private ListView mPatientListView;
    private PatientAdapter mPatientAdapter;
    private ArrayList<Patient> mPatientArrayList;
    private RealmResults<Patient> mPatientResult;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_patient_view);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        // Open the default Realm for the UI thread.
        realm = Realm.getInstance(this);
        mPatientArrayList = new ArrayList<>();

        queryRealm();

        mPatientSumTextView = (TextView) findViewById(R.id.patient_sum_textView);
        mPatientAdapter = new PatientAdapter(this, mPatientArrayList);
        mPatientListView = (ListView) findViewById(R.id.patient_list_view);
        mPatientListView.setAdapter(mPatientAdapter);
        mPatientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient selectedPatient = (Patient) parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(ViewOldPatientActivity.this, ViewPatientDetailActivity.class);
                intent.putExtra("selectedPatientId", selectedPatient.getId());
                startActivity(intent);
            }
        });

        mPatientSumTextView.setText(ViewOldPatientActivity.this.getString(R.string.main_activity_notes, mPatientResult.size()));
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

    @Override
    public boolean isBackable() {
        return true;
    }

    private void queryRealm() {
        mPatientResult = realm.where(Patient.class).greaterThan("actualDate", new Date()).findAll();
        if (mPatientResult != null) {
            for (Patient Patient : mPatientResult)
                mPatientArrayList.add(Patient);
        }
    }
}
