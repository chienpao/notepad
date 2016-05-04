package com.chienpao.notepad.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.chienpao.notepad.notepad.model.Patient;

import java.util.ArrayList;
import java.util.Date;

import io.realm.Realm;

public class ViewPatientDetailActivity extends BasicActivity {
    private static final String TAG = "ViewPatientDetailActivity";
    private TextView mLastNameTextView;
    private TextView mFirstNameTextView;
    private TextView mDOBTextView;
    private TextView mFirstClinicHosptialTextView;
    private TextView mFirstDoctorNameTextView;
    private TextView mExpectDateTextView;
    private TextView mSecondClinicHosptialTextView;
    private TextView mSecondDoctorNameTextView;
    private TextView mActualDateTextView;
    private ArrayList<Patient> mPatientArrayList;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_patient_view);

        // Open the default Realm for the UI thread.
        realm = Realm.getInstance(this);

        mLastNameTextView = (TextView) findViewById(R.id.last_name_textView);
        mFirstNameTextView = (TextView) findViewById(R.id.first_name_textView);
        mDOBTextView = (TextView) findViewById(R.id.dob_textView);
        mFirstClinicHosptialTextView = (TextView) findViewById(R.id.first_clinic_hospital_textView);
        mFirstDoctorNameTextView = (TextView) findViewById(R.id.first_doctor_name_textView);
        mExpectDateTextView = (TextView) findViewById(R.id.expect_date_textView);
        mSecondClinicHosptialTextView = (TextView) findViewById(R.id.second_clinic_hospital_textView);
        mSecondDoctorNameTextView = (TextView) findViewById(R.id.second_doctor_name_textView);
        mActualDateTextView = (TextView) findViewById(R.id.actual_date_textView);

        if (getIntent() != null)
            setDataFromIntent(getIntent());

    }

    private void setDataFromIntent(Intent intent) {
        long selectedPatientId = intent.getLongExtra("selectedPatientId", 1L);

        Patient selectedPatient = realm.where(Patient.class).equalTo("id", selectedPatientId).findFirst();

        if (validate(selectedPatient.getPatientLastName()))
            mLastNameTextView.setText(selectedPatient.getPatientLastName());

        if (validate(selectedPatient.getPatientFirstName()))
            mFirstNameTextView.setText(selectedPatient.getPatientFirstName());

        if (validateDate(selectedPatient.getPatientDateOfBirth()))
            mDOBTextView.setText(selectedPatient.getPatientDateOfBirth().toString());

        if (validate(selectedPatient.getFirstClinicHospital()))
            mFirstClinicHosptialTextView.setText(selectedPatient.getFirstClinicHospital());

        if (validate(selectedPatient.getFirstDoctorName()))
            mFirstDoctorNameTextView.setText(selectedPatient.getFirstDoctorName());

        if (validateDate(selectedPatient.getExpectDate()))
            mExpectDateTextView.setText(selectedPatient.getExpectDate().toString());

        if (validate(selectedPatient.getSecondClinicHospital()))
            mSecondClinicHosptialTextView.setText(selectedPatient.getSecondClinicHospital());

        if (validate(selectedPatient.getSecondDoctorName()))
            mSecondDoctorNameTextView.setText(selectedPatient.getSecondDoctorName());

        if (validateDate(selectedPatient.getActualDate()))
            mActualDateTextView.setText(selectedPatient.getActualDate().toString());
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

    private boolean validate(String validateString) {
        if (validateString == null)
            return false;
        else
            return true;
    }

    private boolean validateDate(Date validateString) {
        if (validateString == null)
            return false;
        else
            return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close(); // Remember to close Realm when done.
    }

}
