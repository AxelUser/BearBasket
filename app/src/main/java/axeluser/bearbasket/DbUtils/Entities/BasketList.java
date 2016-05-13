package axeluser.bearbasket.DbUtils.Entities;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Alexey on 27.02.2016.
 */
public class BasketList extends RealmObject {
    private long id;
    private String name;
    private RealmList<BasketItem> items;

    public RealmList<BasketItem> getItems() {
        return items;
    }

    public void setItems(RealmList<BasketItem> items) {
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
