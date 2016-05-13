package axeluser.bearbasket;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import axeluser.bearbasket.DbUtils.Entities.SaleInfo;

/**
 * Created by Alexey on 28.02.2016.
 */
public class SalesListAdapter extends ArrayAdapter<SaleInfo> {

    private Activity activity;

    public SalesListAdapter(Context context, List<SaleInfo> objects) {
        super(context, R.layout.list_view_row_sales, objects);
        activity = (Activity) context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_view_row_sales, parent, false);
        TextView salesListViewRowTitle
                = (TextView) rowView.findViewById(R.id.salesListViewRowTitle);
        TextView salesListViewRowInfo
                = (TextView) rowView.findViewById(R.id.salesListViewRowInfo);
        TextView salesListViewRowPrice
                = (TextView) rowView.findViewById(R.id.salesListViewRowPrice);
        SaleInfo saleInfo = getItem(position);
        salesListViewRowTitle.setText(saleInfo.getTitle());
        salesListViewRowInfo.setText(saleInfo.getDescription());
        salesListViewRowPrice.setText(saleInfo.getPrice());
        return rowView;
    }
}
