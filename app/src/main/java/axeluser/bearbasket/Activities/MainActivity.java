package axeluser.bearbasket.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import java.util.List;

import axeluser.bearbasket.R;
import axeluser.bearbasket.DbUtils.DbHelper;
import axeluser.bearbasket.DbUtils.DbManager;
import axeluser.bearbasket.DbUtils.Entities.BasketList;

public class MainActivity extends AppCompatActivity {

    private DbManager dbManager;
    private ListView listView;
    private ArrayAdapter<BasketList> basketListArrayAdapter;
    private Comparator<BasketList> listComparator = new Comparator<BasketList>() {
        @Override
        public int compare(BasketList lhs, BasketList rhs) {
            return lhs.getId()<=rhs.getId()?-1:1;
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

        dbManager = new DbManager(this);
        listView = (ListView) findViewById(R.id.mainBasketList);
        List<BasketList> list = dbManager.getAllLists();
        updateListFromDb(dbManager);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, BasketListActivity.class);
                long listId = basketListArrayAdapter.getItem(position).getId();
                intent.putExtra(DbHelper.TableBasketItems.KEY_LIST_ID, listId);
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

    private void updateListFromDb(DbManager db){
        List<BasketList> list = db.getAllLists();
        basketListArrayAdapter =
                new ArrayAdapter<BasketList>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(basketListArrayAdapter);
    }

    public void addList(BasketList list){
        dbManager.createList(list);
        basketListArrayAdapter.add(list);
        basketListArrayAdapter.sort(listComparator);
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
                        BasketList list = new BasketList();
                        String name = input.getText().toString();
                        if(!name.isEmpty()){
                            list.setName(name);
                            addList(list);
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
        updateListFromDb(dbManager);
    }
}
