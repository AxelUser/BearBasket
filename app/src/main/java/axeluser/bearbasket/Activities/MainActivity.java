package axeluser.bearbasket.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.Comparator;
import java.util.Date;
import java.util.UUID;

import axeluser.bearbasket.R;
import axeluser.bearbasket.database.models.BasketList;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends BearBasketActivityBase {

    private ListView listView;
    private ArrayAdapter<BasketList> basketListArrayAdapter;
    private Comparator<BasketList> listComparator = new Comparator<BasketList>() {
        @Override
        public int compare(BasketList lhs, BasketList rhs) {
            return lhs.getDate().compareTo(rhs.getDate());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabList);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showAddListDialog();
            }
        });

        listView = (ListView) findViewById(R.id.mainBasketList);
        updateListFromDb();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, BasketListActivity.class);
                String listId = basketListArrayAdapter.getItem(position).getId();
                intent.putExtra("list_id", listId);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void updateListFromDb(){
        RealmResults<BasketList> list = realm.where(BasketList.class)
                .findAllSorted("date", Sort.DESCENDING);
        basketListArrayAdapter =
                new ArrayAdapter<BasketList>(this, android.R.layout.simple_list_item_1, list);
        //basketListArrayAdapter.sort(listComparator);
        listView.setAdapter(basketListArrayAdapter);

    }

    public void addList(BasketList list){
        basketListArrayAdapter.add(list);
    }

    public void showAddListDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        input.setHint("Название");
        alertDialog.setTitle("Новый список")
                .setView(input)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = input.getText().toString();
                        if(!name.isEmpty()){
                            realm.beginTransaction();
                            BasketList list = realm.createObject(BasketList.class);
                            list.setId(UUID.randomUUID().toString());
                            list.setDate(new Date());
                            list.setName(name);
                            realm.commitTransaction();
                            //addList(list);
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

    @Override
    protected void onResume() {
        super.onResume();
        updateListFromDb();
    }
}
