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

import io.realm.Realm;
import io.realm.RealmResults;

public class ViewPatientActivity extends BasicActivity {
    public static final String TAG = "ViewPatientActivity";

    private LinearLayout rootLayout = null;
    private TextView mPatientSumTextView;
    private ListView mPatientListView;
    private PatientAdapter mPatientAdapter;
    private ArrayList<Patient> mPatientArrayList;
    private RealmResults<Patient> mPatientResult;
    private String mFirstDoctorName;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_view);
        rootLayout = ((LinearLayout) findViewById(R.id.container));
        rootLayout.removeAllViews();

        // Open the default Realm for the UI thread.
        realm = Realm.getInstance(this);
        mPatientArrayList = new ArrayList<>();

        setDataFromIntent(getIntent());

        queryRealm();

        mPatientSumTextView = (TextView) findViewById(R.id.patient_sum_textView);
        mPatientAdapter = new PatientAdapter(this, mPatientArrayList);
        mPatientListView = (ListView) findViewById(R.id.patient_list_view);
        mPatientListView.setAdapter(mPatientAdapter);

        mPatientListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Patient selectedPatient = (Patient)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.setClass(ViewPatientActivity.this, ViewPatientDetailActivity.class);
                intent.putExtra("selectedPatientId", selectedPatient.getId());
                startActivity(intent);
            }
        });

        updateSumTextView();
    }

    private void setDataFromIntent(Intent intent) {
        mFirstDoctorName = intent.getStringExtra("first_doctor");
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

    private void queryRealm(){
        mPatientResult = realm.where(Patient.class).equalTo("firstDoctorName", mFirstDoctorName).findAll();
        for (Patient Patient : mPatientResult)
            mPatientArrayList.add(Patient);
    }

    private void updateSumTextView() {

        /*realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // begin & end transcation calls are done for you
                RealmResults<Patient> PatientResult = realm.where(Patient.class).equalTo("firstDoctorName", mFirstDoctorName).findAll();
                Patient pa = realm.where(Dog.class).equals("age", 1). ();
                theDog.setAge(3);
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                // Original Queries and Realm objects are automatically updated.
                puppies.size(); // => 0 because there are no more puppies (less than 2 years old)
                dog.getAge();   // => 3 the dogs age is updated
            }
        });*/

        mPatientSumTextView.setText(ViewPatientActivity.this.getString(R.string.main_activity_notes, mPatientResult.size()));
    }
}
