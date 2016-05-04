package com.chienpao.notepad.notepad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chienpao.notepad.notepad.R;
import com.chienpao.notepad.notepad.model.Patient;

import java.util.ArrayList;

public class PatientAdapter extends BaseAdapter {

    private static final String TAG = "PatientAdapter";
    private Context mContext;
    private ArrayList<Patient> mPatientArrayList;
    private ArrayList<Boolean> mCheckedArrayList;

    public PatientAdapter(Context context, ArrayList<Patient> patientArrayList) {
        mContext = context;
        mPatientArrayList = patientArrayList;
        mCheckedArrayList = new ArrayList<>(patientArrayList.size());
    }

    @Override
    public int getCount() {
        return mPatientArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPatientArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ItemViewHolder itemViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_patient, null);

            TextView patientId = (TextView) convertView.findViewById(R.id.patient_id_textView);
            TextView patientName = (TextView) convertView.findViewById(R.id.patient_name_textView);
            TextView patientExpectDate = (TextView) convertView.findViewById(R.id.expect_date_textView);
            //CheckBox patientCheckbox = (CheckBox) convertView.findViewById(R.id.patient_checkbox);

            itemViewHolder = new ItemViewHolder();
            itemViewHolder.mPatientId = patientId;
            itemViewHolder.mPatientName = patientName;
            itemViewHolder.mPatientExpectDate = patientExpectDate;
            //itemViewHolder.mPatientCheckbox = patientCheckbox;
            //itemViewHolder.mPatientCheckbox.setTag(patientId);
            convertView.setTag(itemViewHolder);

        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }

        final Patient patient = mPatientArrayList.get(position);
        long id = patient.getId();
        String lastName = patient.getPatientLastName();
        String firstName = patient.getPatientFirstName();
        String expectDate = patient.getExpectDate();

        // Set content here
        itemViewHolder.mPatientId.setText(String.valueOf(id));
        itemViewHolder.mPatientName.setText(lastName + " " + firstName);
        itemViewHolder.mPatientExpectDate.setText(expectDate);
        /*itemViewHolder.mPatientCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: single delete...
                if (v.isSelected()) {
                    mCheckedArrayList.set(position, true);
                } else {
                    mCheckedArrayList.set(position, false);
                }
            }
        });*/

        return convertView;
    }

    private class ItemViewHolder {
        public TextView mPatientId;
        public TextView mPatientName;
        public TextView mPatientExpectDate;
        //public CheckBox mPatientCheckbox;
    }


    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    public ArrayList<Boolean> getCheckedArrayList() {
        return mCheckedArrayList;
    }

}
