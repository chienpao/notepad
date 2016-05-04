package com.chienpao.notepad.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chienpao.notepad.notepad.adapter.DoctorListAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class DoctorActivity extends BasicActivity {
    public static final String TAG = "DoctorActivity";

    private ListView mDoctorListView;
    private DoctorListAdapter mDoctorListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        final String[] doctorArray = getResources().getStringArray(R.array.doctorArray);
        ArrayList<String> doctorArrayList = new ArrayList<>(Arrays.asList(doctorArray));
        mDoctorListView = (ListView) findViewById(R.id.doctor_list_view);
        mDoctorListAdapter = new DoctorListAdapter(this, doctorArrayList);
        mDoctorListView.setAdapter(mDoctorListAdapter);
        mDoctorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("first_doctor", doctorArray[position]);
                intent.setClass(DoctorActivity.this, ViewPatientActivity.class);
                startActivity(intent);
            }
        });
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
}
