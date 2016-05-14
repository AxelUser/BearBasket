package axeluser.bearbasket.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import java.util.ArrayList;

import axeluser.bearbasket.R;
import axeluser.bearbasket.views.adapters.SalesListAdapter;
import axeluser.bearbasket.database.models.SaleInfo;

public class SalesInfoActivity extends BearBasketActivityBase {
    private SalesListAdapter salesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ListView listViewSales = (ListView) findViewById(R.id.listViewSales);

        salesListAdapter = new SalesListAdapter(this, initRecords());
        listViewSales.setAdapter(salesListAdapter);
    }

    private ArrayList<SaleInfo> initRecords(){
        ArrayList<SaleInfo> saleInfos = new ArrayList<>();
        saleInfos.add(SaleInfo
                .create()
                .setTitle("Макароны Шебекенские")
                .setDescription("в магазинах СемьЯ")
                .setPrice("45р"));
        return saleInfos;
    }
}
