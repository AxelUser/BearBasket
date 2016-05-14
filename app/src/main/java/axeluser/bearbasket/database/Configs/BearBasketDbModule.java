package axeluser.bearbasket.database.configs;

import axeluser.bearbasket.database.models.BasketItem;
import axeluser.bearbasket.database.models.BasketList;
import io.realm.annotations.RealmModule;

/**
 * Created by Alexey on 13.05.2016.
 */
@RealmModule(classes = {BasketItem.class, BasketList.class})
public class BearBasketDbModule {
}
