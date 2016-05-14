package axeluser.bearbasket.activities;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
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

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import axeluser.bearbasket.views.adapters.BasketItemListAdapter;
import axeluser.bearbasket.R;
import axeluser.bearbasket.database.models.BasketItem;
import axeluser.bearbasket.database.models.BasketList;
import axeluser.bearbasket.views.interfaces.IBearBasketSortableView;
import io.realm.Realm;
import io.realm.RealmResults;

public class BasketListActivity extends BearBasketActivityBase implements IBearBasketSortableView {

    private BasketList basketList;
    private BasketItem selectedItem = null;
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

    @Override
    public void sortAdapter(){
        //basketItemListAdapter.sort(itemsComparator);
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
                deleteBasketListDialog(this, realm, basketList);
                return true;
            default: return false;
        }

    }

    private void deleteBasketListDialog(Context context, final Realm realm, final BasketList basketList){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Удалить список?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        basketList.getItems().deleteAllFromRealm();
                        basketList.deleteFromRealm();
                        finish();
                    }
                })
                .create();
        alertDialog.show();
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


        final String listId = getIntent().getStringExtra("list_id");
        //RealmResults<BasketList> realmResults = realm.where(BasketList.class).findAll();
        basketList = realm.where(BasketList.class).equalTo("id", listId).findFirst();
        setTitle(basketList.getName());

        listViewBasket = (ListView) findViewById(R.id.listViewBasket);
        basketItemListAdapter = new BasketItemListAdapter(this, basketList.getItems());
        listViewBasket.setAdapter(basketItemListAdapter);
        searchForSell();
    }

    public void addItem(BasketItem item){
        //basketItemListAdapter.add(item);
        searchForSell();
        //sortAdapter();
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
                        String[] data = input.getText().toString().split(":");
                        String count = data.length==2?data[1].trim():"";
                        String name = data[0].trim();
                        if(!name.isEmpty()) {
                            realm.beginTransaction();
                            BasketItem item = realm.createObject(BasketItem.class);
                            item.setName(name);
                            item.setCount(count);
                            item.setList(basketList);
                            item.setCreationDate(new Date());
                            item.setCheckingDate(new Date());
                            basketList.getItems().add(item);
                            realm.commitTransaction();
                            addItem(item);
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
