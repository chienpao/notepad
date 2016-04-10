package com.chienpao.notepad.notepad;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.chienpao.notepad.notepad.model.Patient;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;

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
    private DatePickerFragment dateTimeFragment;
    private int mDatePickerFlag = 0;

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

        mDOBEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerFlag = 0;
                showDatePickerDialog(v);
            }
        });

        mExpectDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerFlag = 1;
                showDatePickerDialog(v);
            }
        });

        mActualDateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePickerFlag = 2;
                showDatePickerDialog(v);
            }
        });

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

        setupDateTimeFragment();
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

    private void showDatePickerDialog(View v) {
        dateTimeFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

    private void setupDateTimeFragment() {

        /*if (mApp.getUserBirthdate() != null) {
            userBirthdate = mApp.getUserBirthdate();

        } else {
            userBirthdate = DateTime.now(); //default
        }*/

        dateTimeFragment = new DatePickerFragment();
    }

    /*
     * =================================================================================
     * NESTED CLASSES
     * =================================================================================
     */
    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = 0;
            int month = 0;
            int day = 0;
            /*if (userBirthdate != null) {
                year = userBirthdate.getYear();
                month = userBirthdate.getMonthOfYear() - 1;
                day = userBirthdate.getDayOfMonth();
            } else {*/
            LocalDateTime now = LocalDateTime.now();
            year = now.getYear();
            month = now.getMonthOfYear();
            day = now.getDayOfMonth();
            //}

            DatePickerDialog dateDlg = new DatePickerDialog(getActivity(), this, year, month, day);
            dateDlg.setMessage(getString(R.string.add_date_dialog_title));
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MONTH, calendar.getActualMaximum(Calendar.MONTH));
            calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
            dateDlg.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return dateDlg;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            DateTime dateTime = new DateTime(year, month + 1, day, 0, 0); // +1 for the month sice datePicker using range 0-11
            //userBirthdate = dateTime;

            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy");


            switch (mDatePickerFlag){
                case 0:
                    mDOBEditText.setText(fmt.print(dateTime));
                    break;
                case 1:
                    mExpectDateEditText.setText(fmt.print(dateTime));
                    break;
                case 2:
                    mActualDateEditText.setText(fmt.print(dateTime));
                    break;
            }

        }
    }
}
