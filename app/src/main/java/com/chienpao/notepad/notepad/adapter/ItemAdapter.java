package com.chienpao.notepad.notepad.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.chienpao.notepad.notepad.R;
import com.chienpao.notepad.notepad.model.Item;

import java.util.ArrayList;


/**
 * Created by pao on 16/2/4.
 */
public class ItemAdapter extends BaseAdapter {

    private static final String TAG = "ItemAdapter";
    private Context mContext;
    private ArrayList<Item> mItemArrayList;
    private Button mSaveButton;

    public ItemAdapter(Context context, ArrayList<Item> itemArrayList, Button saveButton) {
        mContext = context;
        mItemArrayList = itemArrayList;
        mSaveButton = saveButton;
    }

    @Override
    public int getCount() {
        return mItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        ItemViewHolder itemViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);

            EditText itemName = (EditText) convertView.findViewById(R.id.item_name_editText);
            EditText itemCount = (EditText) convertView.findViewById(R.id.item_count_editText);
            EditText itemDate = (EditText) convertView.findViewById(R.id.item_date_editText);
            Button deleteButton = (Button) convertView.findViewById(R.id.delete_button);

            itemViewHolder = new ItemViewHolder();
            itemViewHolder.mItemName = itemName;
            itemViewHolder.mItemCount = itemCount;
            itemViewHolder.mItemDate = itemDate;
            itemViewHolder.mDeleteButton = deleteButton;
            convertView.setTag(itemViewHolder);

        } else {
            itemViewHolder = (ItemViewHolder) convertView.getTag();
        }

        // Set Tag every time for position
        itemViewHolder.mItemName.setTag(position);
        itemViewHolder.mItemCount.setTag(position);
        itemViewHolder.mItemDate.setTag(position);

        final Item item = mItemArrayList.get(position);
        String name = item.getName();
        int count = item.getCount();
        String date = item.getDate();

        // Set content here
        itemViewHolder.mItemName.setText(name);
        itemViewHolder.mItemCount.setText(Integer.toString(count));
        itemViewHolder.mItemDate.setText(date);

        itemViewHolder.mItemName.addTextChangedListener(new NameTextWatcher(itemViewHolder));
        itemViewHolder.mItemCount.addTextChangedListener(new CountTextWatcher(itemViewHolder));
        itemViewHolder.mItemDate.addTextChangedListener(new NoteTextWatcher(itemViewHolder));

        itemViewHolder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mItemArrayList.isEmpty()) {
                    mItemArrayList.remove(position);
                    notifyDataSetChanged();

                    if(mItemArrayList.isEmpty())
                        mSaveButton.setEnabled(false);
                }
            }
        });

        return convertView;
    }

    private class ItemViewHolder {
        public EditText mItemName;
        public EditText mItemCount;
        public EditText mItemDate;
        public Button mDeleteButton;
    }

    public void refreshAdapter() {
        notifyDataSetChanged();
    }

    class NameTextWatcher implements TextWatcher {
        public NameTextWatcher(ItemViewHolder itemViewHolder) {
            mItemViewHolder = itemViewHolder;
        }

        private ItemViewHolder mItemViewHolder;

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString())) {
                int position = (Integer) mItemViewHolder.mItemName.getTag();
                mItemArrayList.get(position).setName(s.toString());
            }
        }
    }

    class CountTextWatcher implements TextWatcher {
        public CountTextWatcher(ItemViewHolder itemViewHolder) {
            mItemViewHolder = itemViewHolder;
        }

        private ItemViewHolder mItemViewHolder;

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString())) {
                int position = (Integer) mItemViewHolder.mItemCount.getTag();
                mItemArrayList.get(position).setCount(Integer.parseInt(s.toString()));
            }
        }
    }

    class NoteTextWatcher implements TextWatcher {
        public NoteTextWatcher(ItemViewHolder itemViewHolder) {
            mItemViewHolder = itemViewHolder;
        }

        private ItemViewHolder mItemViewHolder;

        @Override
        public void onTextChanged(CharSequence s, int start,
                                  int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start,
                                      int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s != null && !"".equals(s.toString())) {
                int position = (Integer) mItemViewHolder.mItemDate.getTag();
                mItemArrayList.get(position).setDate(s.toString());
            }
        }
    }

}
