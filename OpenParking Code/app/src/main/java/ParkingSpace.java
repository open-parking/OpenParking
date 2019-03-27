

public class ParkingSpace {

    private String address;
    private int zipcode;
    private Double latitude;
    private Double longitude;
    private Double cost;
    private String openTime;
    private String closeTime;



    private int price;

    //CSULB Latitude and Longitude
    //private final double latitude = 33.782896;
    //private final double longitude = -118.110230;

    public ParkingSpace( )
    {

    }

    public ParkingSpace( String address, int zipcode, Double latitude, Double longitude, Double cost, String openTime, String closeTime)
    {
        this.address = address;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.cost = cost;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getZipcode() {
        return zipcode;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

}
