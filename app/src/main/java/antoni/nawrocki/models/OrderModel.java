package antoni.nawrocki.models;

import java.sql.Date;

public class OrderModel {
    private int amount;
    private Date date;
    private double price;

    public OrderModel(int amount, Date date, double price) {
        this.amount = amount;
        this.date = date;
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
