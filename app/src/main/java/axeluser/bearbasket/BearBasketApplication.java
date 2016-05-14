package axeluser.bearbasket;

import android.app.Application;

import axeluser.bearbasket.database.configs.BearBasketDbModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Alexey on 13.05.2016.
 */

public class BearBasketApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        configRealm();
    }

    /**
     * Настройка базы данных Realm
     */
    private void configRealm(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("bearbasket.realm")
                .schemaVersion(1)
                .modules(new BearBasketDbModule())
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}
