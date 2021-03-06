package com.chienpao.notepad.notepad;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
    private int mDoctorFlag = 0;
    private DateTimeFormatter fmt = DateTimeFormat.forPattern("dd/MM/yyyy");

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
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                realPatient.setPatientLastName(mLastNameEditText.getText().toString());
                realPatient.setPatientFirstName(mFirstNameEditText.getText().toString());
                try {
                    realPatient.setPatientDateOfBirth(new Date(sdf.parse(mDOBEditText.getText().toString()).getTime()));
                    realPatient.setExpectDate(new Date(sdf.parse(mExpectDateEditText.getText().toString()).getTime()));
                    realPatient.setActualDate(new Date(sdf.parse(mActualDateEditText.getText().toString()).getTime()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                realPatient.setFirstClinicHospital(mFirstClinicHosptialEditText.getText().toString());
                realPatient.setFirstDoctorName(mFirstDoctorNameEditText.getText().toString());
                realPatient.setSecondClinicHospital(mSecondClinicHosptialEditText.getText().toString());
                realPatient.setSecondDoctorName(mSecondDoctorNameEditText.getText().toString());


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
            //dateDlg.getDatePicker().setMaxDate(calendar.getTimeInMillis());

            // Create a new instance of DatePickerDialog and return it
            return dateDlg;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            DateTime dateTime = new DateTime(year, month + 1, day, 0, 0); // +1 for the month sice datePicker using range 0-11
            //userBirthdate = dateTime;

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
}
