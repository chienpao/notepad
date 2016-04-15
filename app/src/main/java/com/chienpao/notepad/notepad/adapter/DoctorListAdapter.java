package com.chienpao.notepad.notepad.adapter;

/**
 * Created by pao on 2016/04/15
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chienpao.notepad.notepad.R;

import java.util.ArrayList;

public class DoctorListAdapter extends BaseAdapter {
    private static final String TAG = "DoctorListAdapter";
    private Context mContext;
    private ArrayList<String> mStringArrayList;

    public DoctorListAdapter(Context context, ArrayList<String> arrayList) {
        mContext = context;
        mStringArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mStringArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mStringArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_doctor_view, null);

            TextView itemName = (TextView) convertView.findViewById(R.id.item_textView);

            viewHolder = new ViewHolder();
            viewHolder.mItem = itemName;

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mItem.setText(mStringArrayList.get(position));

        return convertView;
    }

    private class ViewHolder {
        public TextView mItem;
    }
}


