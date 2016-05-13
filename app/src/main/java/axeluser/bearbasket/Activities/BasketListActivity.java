package axeluser.bearbasket.Activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import axeluser.bearbasket.BasketItemListAdapter;
import axeluser.bearbasket.R;
import axeluser.bearbasket.DbUtils.DbHelper;
import axeluser.bearbasket.DbUtils.DbManager;
import axeluser.bearbasket.DbUtils.Entities.BasketItem;
import axeluser.bearbasket.DbUtils.Entities.BasketList;

public class BasketListActivity extends AppCompatActivity {

    private BasketList basketList;
    private BasketItem selectedItem = null;
    private DbManager dbManager;
    private ListView listViewBasket;
    private BasketItemListAdapter basketItemListAdapter;
    private ActionMode mActionMode = null;
    private Comparator<BasketItem> itemsComparator = new Comparator<BasketItem>() {
        @Override
        public int compare(BasketItem lhs, BasketItem rhs) {
            if(lhs.isChecked()){
                if(rhs.isChecked()){
                    return lhs.getCheckingDate().compareTo(rhs.getCreationDate());
                } else {
                    return 1;
                }
            } else {
                if(rhs.isChecked()){
                    return -1;
                } else {
                    return -lhs.getCreationDate().compareTo(rhs.getCreationDate());
                }
            }
        }
    };

    private void searchForSell(){
        for (int i=0; i<basketItemListAdapter.getCount(); i++){
            BasketItem item = basketItemListAdapter.getItem(i);
            String name = item.getName();
            if(name.toLowerCase().contains("макароны")){
                item.setPromotion(true);
            }
        }
        basketItemListAdapter.notifyDataSetChanged();
    }

    public void sortAdapter(){
        basketItemListAdapter.sort(itemsComparator);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_basket, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_time:
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                return true;
            case R.id.menu_item_del:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Удалить список?")
                        .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbManager.deleteList(basketList);
                                finish();
                            }
                        })
                        .create();
                alertDialog.show();
                return true;
            default: return false;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabBasket);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddItemDialog();
            }
        });
        dbManager = new DbManager(this);

        final long listId = getIntent().getLongExtra(DbHelper.TableBasketItems.KEY_LIST_ID, 0);
        basketList = dbManager.getListById(listId);
        setTitle(basketList.getName());

        listViewBasket = (ListView) findViewById(R.id.listViewBasket);
        ArrayList<BasketItem> basketItems = dbManager.getAllItemsForList(basketList);
        basketItemListAdapter = new BasketItemListAdapter(this, basketItems);
        listViewBasket.setAdapter(basketItemListAdapter);
        searchForSell();
    }

    public void addItem(BasketItem item){
        dbManager.createListItem(item);
        basketItemListAdapter.add(item);
        searchForSell();
        sortAdapter();
    }

    public void showAddItemDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(BasketListActivity.this);

        final EditText input = new EditText(BasketListActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Наименование : мера");
        alertDialog.setTitle("Купи...")
                .setView(input)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BasketItem list = new BasketItem();
                        String[] data = input.getText().toString().split(":");
                        String count = data.length==2?data[1].trim():"";
                        String name = data[0].trim();
                        if(!name.isEmpty()){
                            list.setName(name);
                            list.setCount(count);
                            list.setListId(basketList.getId());
                            list.setCreationDate(new Date());
                            list.setCheckingDate(new Date());
                            addItem(list);
                        }
                    }
                })
                .setNegativeButton("Назад", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }



        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }

}
