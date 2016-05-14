package axeluser.bearbasket.database.models;

/**
 * Created by Alexey on 28.02.2016.
 */
public class SaleInfo {
    private String title;
    private String description;
    private String price;

    public String getTitle() {
        return title;
    }

    public SaleInfo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public SaleInfo setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public SaleInfo setPrice(String price) {
        this.price = price;
        return this;
    }

    public static SaleInfo create(){
        return new SaleInfo();
    }
}
