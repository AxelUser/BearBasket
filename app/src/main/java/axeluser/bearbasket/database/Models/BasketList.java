package axeluser.bearbasket.database.models;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Alexey on 27.02.2016.
 */
public class BasketList extends RealmObject {
    private String id;
    private String name;
    private RealmList<BasketItem> items;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public RealmList<BasketItem> getItems() {
        return items;
    }

    public void setItems(RealmList<BasketItem> items) {
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
