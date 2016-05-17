package axeluser.bearbasket.DbUtils.Entities;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alexey on 27.02.2016.
 */
public class BasketList {
    private long id;
    private String name;
    private ArrayList<BasketItem> items;

    public ArrayList<BasketItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<BasketItem> items) {
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }
}
