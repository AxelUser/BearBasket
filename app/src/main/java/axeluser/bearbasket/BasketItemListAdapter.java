package axeluser.bearbasket;

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
import axeluser.bearbasket.DbUtils.DbManager;
import axeluser.bearbasket.DbUtils.Entities.BasketItem;

/**
 * Created by Alexey on 28.02.2016.
 */
public class BasketItemListAdapter extends ArrayAdapter<BasketItem> {
    private int selectedPosition = -1;
    private BasketListActivity activity;
    public BasketItemListAdapter(Context context, List<BasketItem> objects) {
        super(context, R.layout.list_view_row_basket_item, objects);
        activity = (BasketListActivity) context;
    }

    public void selectItem(int selectedPosition){
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }

    public void clearSelection(){
        selectItem(-1);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        final DbManager dbManager = new DbManager(getContext());

        listViewRowBase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedPosition<0) {
                    basketItem.setChecked(!basketItem.isChecked());
                    basketItem.setCheckingDate(new Date());
                    dbManager.updateListItem(basketItem);
                    activity.sortAdapter();
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

        if(selectedPosition == position){
            rowView.setVisibility(View.GONE);
            View editRowView =
                    inflater.inflate(R.layout.list_view_row_basket_item_selected, parent, false);
            RelativeLayout layoutEdit =
                    (RelativeLayout) editRowView
                            .findViewById(R.id.listViewRowEditBase);
            TextView listViewRowEditBasketItemName
                    = (TextView) editRowView.findViewById(R.id.listViewRowEditBasketItemName);
            ImageView listViewRowEditBasketItemDeleteButton
                    = (ImageView) editRowView.findViewById(R.id.listViewRowEditBasketItemDeleteButton);
            listViewRowEditBasketItemName.setText(basketItem.getName());
            listViewRowEditBasketItemDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(basketItem);
                    dbManager.deleteListItem(basketItem);
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



}
