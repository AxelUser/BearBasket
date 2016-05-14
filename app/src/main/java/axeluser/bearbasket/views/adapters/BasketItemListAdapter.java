package axeluser.bearbasket.views.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import axeluser.bearbasket.activities.BasketListActivity;
import axeluser.bearbasket.activities.SalesInfoActivity;
import axeluser.bearbasket.database.models.BasketItem;
import axeluser.bearbasket.R;
import axeluser.bearbasket.views.interfaces.IBearBasketSortableView;
import io.realm.Realm;

/**
 * Created by Alexey on 28.02.2016.
 */
public class BasketItemListAdapter extends ArrayAdapter<BasketItem> {
    private int selectedPosition = -1;
    private IBearBasketSortableView activity;
    public BasketItemListAdapter(Context context, List<BasketItem> objects) {
        super(context, R.layout.list_view_row_basket_item, objects);
        activity = (IBearBasketSortableView) context;
    }

    public void selectItem(int selectedPosition){
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public void clearSelection(){
        selectItem(-1);
    }

    private View getDefaultRow(final int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_view_row_basket_item, parent, false);
        final RelativeLayout listViewRowBase =
                (RelativeLayout) rowView.findViewById(R.id.listViewRowBase);
        TextView listViewRoBasketItemName =
                (TextView) rowView.findViewById(R.id.listViewRoBasketItemName);
        TextView listViewRoBasketItemCount =
                (TextView) rowView.findViewById(R.id.listViewRoBasketItemCount);
        ImageView listViewRoBasketItemImageButtonSell =
                (ImageView) rowView.findViewById(R.id.listViewRoBasketItemImageButtonSell);
        final BasketItem basketItem = getItem(position);

        listViewRowBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition<0) {
                    basketItem.setChecked(!basketItem.isChecked());
                    basketItem.setCheckingDate(new Date());
                    //activity.sortAdapter();
                } else {
                    clearSelection();
                    notifyDataSetChanged();
                }
            }
        });
        listViewRowBase.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                selectItem(position);
                return true;
            }
        });
        listViewRowBase.setLongClickable(true);

        listViewRoBasketItemImageButtonSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!basketItem.isPromotion()) {
                    Toast.makeText(getContext(), "Акций нет", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(getContext(), SalesInfoActivity.class);
                    getContext().startActivity(intent);
                }
            }
        });
        listViewRoBasketItemImageButtonSell.setLongClickable(true);

        if (!basketItem.isPromotion()){
            listViewRoBasketItemImageButtonSell.setImageAlpha(20);
        }
        if(basketItem.isChecked()){
            listViewRoBasketItemName.setTextColor(getContext()
                    .getResources().getColor(R.color.colorTextShadowed));
            listViewRoBasketItemName.setPaintFlags(listViewRoBasketItemName.getPaintFlags()
                    | Paint.STRIKE_THRU_TEXT_FLAG);
            listViewRoBasketItemCount.setTextColor(getContext()
                    .getResources().getColor(R.color.colorTextShadowed));
        }
        listViewRoBasketItemName.setText(basketItem.getName());
        if(!basketItem.getCount().isEmpty()){
            listViewRoBasketItemCount.setText(String.valueOf(basketItem.getCount()));
        } else {
            listViewRoBasketItemCount.setVisibility(View.GONE);
        }
        return rowView;
    }

    private View getSelectedRow(final int position, View convertView, ViewGroup parent){
        //rowView.setVisibility(View.GONE);
        LayoutInflater inflater = ((Activity)getContext()).getLayoutInflater();
        View editRowView =
                inflater.inflate(R.layout.list_view_row_basket_item_selected, parent, false);
        RelativeLayout layoutEdit =
                (RelativeLayout) editRowView
                        .findViewById(R.id.listViewRowEditBase);
        TextView listViewRowEditBasketItemName
                = (TextView) editRowView.findViewById(R.id.listViewRowEditBasketItemName);
        ImageView listViewRowEditBasketItemDeleteButton
                = (ImageView) editRowView.findViewById(R.id.listViewRowEditBasketItemDeleteButton);
        final BasketItem basketItem = getItem(position);
        listViewRowEditBasketItemName.setText(basketItem.getName());
        listViewRowEditBasketItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(basketItem);
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                basketItem.deleteFromRealm();
                realm.commitTransaction();
                //dbManager.deleteListItem(basketItem);
                clearSelection();
            }
        });
        layoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSelection();
            }
        });
        layoutEdit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                clearSelection();
                return true;
            }
        });
        return editRowView;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if(selectedPosition == position){
            return getSelectedRow(position, convertView, parent);
        } else {
            return getDefaultRow(position, convertView, parent);
        }
    }



}
