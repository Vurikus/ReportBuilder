package example;

public class Transport {
    private String locationName = "";
    private String licenseNumber = "";
    private String model = "";

    public Transport(String locationName, String licenseNumber, String model) {
        this.locationName = locationName;
        this.licenseNumber = licenseNumber;
        this.model = model;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getModel() {
        return model;
    }
}
