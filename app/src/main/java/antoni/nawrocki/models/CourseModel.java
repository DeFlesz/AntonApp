package antoni.nawrocki.models;

public class CourseModel {
    private String title;
    private String description;
    private String thumbnail;
    private int userID;
    private double price;

    public CourseModel(String title, String description, String thumbnail, int userID, double price) {
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
        this.userID = userID;
        this.price = price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public int getUserID() {
        return userID;
    }

    public double getPrice() {
        return price;
    }
}
