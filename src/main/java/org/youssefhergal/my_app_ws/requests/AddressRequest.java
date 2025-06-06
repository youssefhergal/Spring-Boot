package org.youssefhergal.my_app_ws.requests;

import jakarta.validation.constraints.NotBlank;

public class AddressRequest {

    @NotBlank(message = "City cannot be null")
    private String city;

    @NotBlank(message = "Country cannot be null")
    private String country;

    @NotBlank(message = "Street cannot be null")
    private String streetName;

    @NotBlank(message = "Postal code cannot be null")
    private String postalCode;

    @NotBlank(message = "Type of address cannot be null")
    private String type;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}