package com.chienpao.notepad.notepad;

public class HistoryActivity extends BasicActivity {
    /*private static final String TAG = "HistoryActivity";
    private Button mDeleteHistoryButton;
    private ListView mHistoryListView;
    private HistoryItemAdapter mHistoryItemAdapter;
    private Realm mRealm;
    private ArrayList<Item> mHistoryItemArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mRealm = Realm.getInstance(this);
        mHistoryItemArrayList = new ArrayList<>();

        // Load all items from realm
        RealmResults<Item> itemResult = mRealm.where(Item.class).findAll();
        for (Item item : itemResult)
            mHistoryItemArrayList.add(item);

        mDeleteHistoryButton = (Button) findViewById(R.id.delete_history_button);
        mHistoryItemAdapter = new HistoryItemAdapter(this, mHistoryItemArrayList);
        mHistoryListView = (ListView) findViewById(R.id.history_list_view);
        mHistoryListView.setAdapter(mHistoryItemAdapter);

        // If no items in realm, delete history button must disable.
        if (mHistoryItemArrayList.isEmpty())
            mDeleteHistoryButton.setEnabled(false);
        else
            mDeleteHistoryButton.setEnabled(true);

        mDeleteHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRealm.beginTransaction();
                mRealm.allObjects(Item.class).clear();
                mRealm.commitTransaction();
                mRealm.close();
                mHistoryItemArrayList.clear();
                mHistoryItemAdapter.refreshAdapter();

                Toast.makeText(HistoryActivity.this, getString(R.string.history_activity_delete_history_toast), Toast.LENGTH_LONG).show();
                mDeleteHistoryButton.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history, menu);
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
    }*/
}
