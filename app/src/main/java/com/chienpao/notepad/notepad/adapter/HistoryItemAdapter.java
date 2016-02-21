package com.chienpao.notepad.notepad.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chienpao.notepad.notepad.R;
import com.chienpao.notepad.notepad.model.Item;

import java.util.ArrayList;


/**
 * Created by pao on 16/2/6.
 */
public class HistoryItemAdapter extends BaseAdapter {

    private static final String TAG = "HistoryItemAdapter";
    private Context mContext;
    private ArrayList<Item> mHistoryItemArrayList;

    public HistoryItemAdapter(Context context, ArrayList<Item> historyItemArrayList) {
        mContext = context;
        mHistoryItemArrayList = historyItemArrayList;
    }

    @Override
    public int getCount() {
        return mHistoryItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mHistoryItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ItemViewHolder itemViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_history_item, null);

            TextView itemName = (TextView) convertView.findViewById(R.id.item_name_textView);
            TextView itemCount = (TextView) convertView.findViewById(R.id.item_count_textView);
            TextView itemDate = (TextView) convertView.findViewById(R.id.item_date_textView);

            itemViewHolder = new ItemViewHolder();
            itemViewHolder.mItemName = itemName;
            itemViewHolder.mItemCount = itemCount;
            itemViewHolder.mItemDate = itemDate;
            convertView.setTag(itemViewHolder);

        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }

        final Item item = mHistoryItemArrayList.get(position);
        String name = item.getName();
        int count = item.getCount();
        String date = item.getDate();

        // Set content here
        //if (name != null)
        itemViewHolder.mItemName.setText(name);
        itemViewHolder.mItemCount.setText(Integer.toString(count));
        //if (note != null)
        itemViewHolder.mItemDate.setText(date);

        return convertView;
    }

    private class ItemViewHolder {
        public TextView mItemName;
        public TextView mItemCount;
        public TextView mItemDate;
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

}
