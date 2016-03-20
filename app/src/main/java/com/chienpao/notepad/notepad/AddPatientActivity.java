package com.chienpao.notepad.notepad;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chienpao.notepad.notepad.model.Patient;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class AddPatientActivity extends BasicActivity {
    private static final String TAG = "AddPatientActivity";
    private Button mSaveButton;
    private EditText mLastNameEditText;
    private EditText mFirstNameEditText;
    private EditText mDOBEditText;
    private EditText mFirstClinicHosptialEditText;
    private EditText mFirstDoctorNameEditText;
    private EditText mExpectDateEditText;
    private EditText mSecondClinicHosptialEditText;
    private EditText mSecondDoctorNameEditText;
    private EditText mActualDateEditText;
    private Realm mRealm;
    private ArrayList<Patient> mPatientArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);

        // Open the default Realm for the UI thread.
        mRealm = Realm.getInstance(this);

        mSaveButton = (Button) findViewById(R.id.save_button);
        mLastNameEditText = (EditText) findViewById(R.id.last_name_editText);
        mFirstNameEditText = (EditText) findViewById(R.id.first_name_editText);
        mDOBEditText = (EditText) findViewById(R.id.dob_editText);
        mFirstClinicHosptialEditText = (EditText) findViewById(R.id.first_clinic_hospital_editText);
        mFirstDoctorNameEditText = (EditText) findViewById(R.id.first_doctor_name_editText);
        mExpectDateEditText = (EditText) findViewById(R.id.expect_date_editText);
        mSecondClinicHosptialEditText = (EditText) findViewById(R.id.second_clinic_hospital_editText);
        mSecondDoctorNameEditText = (EditText) findViewById(R.id.second_doctor_name_editText);
        mActualDateEditText = (EditText) findViewById(R.id.actual_date_editText);

        mPatientArrayList = new ArrayList<>();
        RealmResults<Patient> PatientResult = mRealm.where(Patient.class).findAll();
        for (Patient Patient : PatientResult)
            mPatientArrayList.add(Patient);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validate()) {
                    Toast.makeText(AddPatientActivity.this, "First Name, Last Name and Expect Date can't empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                // All writes must be wrapped in a transaction to facilitate safe multi threading
                mRealm.beginTransaction();

                // Add items in realm
                Patient realPatient = mRealm.createObject(Patient.class);
                if (mPatientArrayList.isEmpty())
                    realPatient.setId(1);
                else {
                    realPatient.setId(mPatientArrayList.size() + 1);
                }
                realPatient.setPatientLastName(mLastNameEditText.getText().toString());
                realPatient.setPatientFirstName(mFirstNameEditText.getText().toString());
                realPatient.setPatientDateOfBirth(mDOBEditText.getText().toString());
                realPatient.setFirstClinicHospital(mFirstClinicHosptialEditText.getText().toString());
                realPatient.setFirstDoctorName(mFirstDoctorNameEditText.getText().toString());
                realPatient.setExpectDate(mExpectDateEditText.getText().toString());
                realPatient.setSecondClinicHospital(mSecondClinicHosptialEditText.getText().toString());
                realPatient.setSecondDoctorName(mSecondDoctorNameEditText.getText().toString());
                realPatient.setActualDate(mActualDateEditText.getText().toString());

                // When the transaction is committed, all changes a synced to disk.
                mRealm.commitTransaction();

                Toast.makeText(AddPatientActivity.this, "New patient successfully", Toast.LENGTH_LONG).show();

                // Finish activity
                finish();
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

    @Override
    public boolean isBackable() {
        return true;
    }

    private boolean validate() {
        String lastName = mLastNameEditText.getText().toString();
        String firstName = mFirstNameEditText.getText().toString();
        String expectDate = mExpectDateEditText.getText().toString();
        if (lastName.isEmpty() || firstName.isEmpty() || expectDate.isEmpty())
            return false;

        return true;
    }
}
