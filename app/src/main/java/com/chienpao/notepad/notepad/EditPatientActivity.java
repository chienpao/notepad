package com.chienpao.notepad.notepad;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.chienpao.notepad.notepad.adapter.DoctorListAdapter;
import com.chienpao.notepad.notepad.model.Patient;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class EditPatientActivity extends BasicActivity {
    private static final String TAG = "EditPatientActivity";
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
    private int mDoctorFlag = 0;
    private Patient mSelectedPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_patient);

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

        if (getIntent() != null)
            setDataFromIntent(getIntent());

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

        mFirstDoctorNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoctorFlag = 0;
                showDoctorListDialogFragment();
            }
        });

        mSecondDoctorNameEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDoctorFlag = 1;
                showDoctorListDialogFragment();
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
                    Toast.makeText(EditPatientActivity.this, "First Name, Last Name and Expect Date can't empty.", Toast.LENGTH_LONG).show();
                    return;
                }

                // All writes must be wrapped in a transaction to facilitate safe multi threading
                mRealm.beginTransaction();

                mSelectedPatient.setPatientLastName(mLastNameEditText.getText().toString());
                mSelectedPatient.setPatientFirstName(mFirstNameEditText.getText().toString());
                mSelectedPatient.setPatientDateOfBirth(new Date(mDOBEditText.getText().toString()));
                mSelectedPatient.setFirstClinicHospital(mFirstClinicHosptialEditText.getText().toString());
                mSelectedPatient.setFirstDoctorName(mFirstDoctorNameEditText.getText().toString());
                mSelectedPatient.setExpectDate(new Date(mExpectDateEditText.getText().toString()));
                mSelectedPatient.setSecondClinicHospital(mSecondClinicHosptialEditText.getText().toString());
                mSelectedPatient.setSecondDoctorName(mSecondDoctorNameEditText.getText().toString());
                mSelectedPatient.setActualDate(new Date(mActualDateEditText.getText().toString()));

                // When the transaction is committed, all changes a synced to disk.
                mRealm.commitTransaction();

                Toast.makeText(EditPatientActivity.this, "Edit patient successfully", Toast.LENGTH_LONG).show();

                // Finish activity
                finish();
            }
        });

        setupDateTimeFragment();
    }

    private void setDataFromIntent(Intent intent) {
        long selectedPatientId = intent.getLongExtra("selectedPatientId", 1L);

        mSelectedPatient = mRealm.where(Patient.class).equalTo("id", selectedPatientId).findFirst();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = null;

        if (validate(mSelectedPatient.getPatientLastName()))
            mLastNameEditText.setText(mSelectedPatient.getPatientLastName());

        if (validate(mSelectedPatient.getPatientFirstName()))
            mFirstNameEditText.setText(mSelectedPatient.getPatientFirstName());

        if (validateDate(mSelectedPatient.getPatientDateOfBirth())) {
            dateString = sdf.format(mSelectedPatient.getPatientDateOfBirth());
            mDOBEditText.setText(dateString);
        }

        if (validate(mSelectedPatient.getFirstClinicHospital()))
            mFirstClinicHosptialEditText.setText(mSelectedPatient.getFirstClinicHospital());

        if (validate(mSelectedPatient.getFirstDoctorName()))
            mFirstDoctorNameEditText.setText(mSelectedPatient.getFirstDoctorName());

        if (validateDate(mSelectedPatient.getExpectDate())) {
            mExpectDateEditText.setText(mSelectedPatient.getExpectDate().toString());
            dateString = sdf.format(mSelectedPatient.getPatientDateOfBirth());
            mExpectDateEditText.setText(dateString);
        }

        if (validate(mSelectedPatient.getSecondClinicHospital()))
            mSecondClinicHosptialEditText.setText(mSelectedPatient.getSecondClinicHospital());

        if (validate(mSelectedPatient.getSecondDoctorName()))
            mSecondDoctorNameEditText.setText(mSelectedPatient.getSecondDoctorName());

        if (validateDate(mSelectedPatient.getActualDate())) {
            dateString = sdf.format(mSelectedPatient.getActualDate());
            mActualDateEditText.setText(dateString);
        }

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
            //dateDlg.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return dateDlg;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            DateTime dateTime = new DateTime(year, month + 1, day, 0, 0); // +1 for the month sice datePicker using range 0-11
            //userBirthdate = dateTime;

            DateTimeFormatter fmt = DateTimeFormat.forPattern("dd MMM yyyy");


            switch (mDatePickerFlag) {
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

    // Inner class
    public static class DoctorListDialogFragment extends DialogFragment {

        private ArrayList<String> mStringArrayList;

        static DoctorListDialogFragment newInstance(ArrayList<String> stringArrayList) {
            DoctorListDialogFragment f = new DoctorListDialogFragment();

            // Supply stringArrayList input as an argument.
            Bundle args = new Bundle();
            args.putStringArrayList("stringArrayList", stringArrayList);
            f.setArguments(args);

            return f;
        }


        public interface DialogClickListener {
            void onClick(int position);
        }

        private DialogClickListener mDialogClickListener;

        public void setDialogClickListener(DialogClickListener dialogClickListener) {
            mDialogClickListener = dialogClickListener;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            mStringArrayList = getArguments().getStringArrayList("stringArrayList");
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

            View view = inflater.inflate(R.layout.fragment_dialog_doctor_list, container);

            ListView listView = (ListView) view.findViewById(R.id.list_view);
            DoctorListAdapter doctorListAdapter = new DoctorListAdapter(getActivity(), mStringArrayList);
            listView.setAdapter(doctorListAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mDialogClickListener.onClick(position);
                    dismiss();
                }
            });
            return view;
        }

    }

    private void showDoctorListDialogFragment() {
        String[] doctorArray = getResources().getStringArray(R.array.doctorArray);
        final ArrayList<String> doctorArrayList = new ArrayList<>(Arrays.asList(doctorArray));

        DoctorListDialogFragment doctorListDialogFragment = DoctorListDialogFragment.newInstance(doctorArrayList);
        doctorListDialogFragment.setDialogClickListener(new DoctorListDialogFragment.DialogClickListener() {
            @Override
            public void onClick(int position) {
                switch (mDoctorFlag) {
                    case 0:
                        mFirstDoctorNameEditText.setText(doctorArrayList.get(position));
                        break;
                    case 1:
                        mSecondDoctorNameEditText.setText(doctorArrayList.get(position));
                        break;
                }
            }
        });

        doctorListDialogFragment.show(getSupportFragmentManager(), "doctorListDialogFragment");
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
}
