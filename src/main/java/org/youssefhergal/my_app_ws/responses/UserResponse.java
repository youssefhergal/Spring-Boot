package org.youssefhergal.my_app_ws.responses;

import java.util.List;

public class UserResponse {

    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private List<AddressResponse> addresses;
    private ContactResponse contact;

    private boolean admin;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<AddressResponse> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressResponse> addresses) {
        this.addresses = addresses;
    }


    public ContactResponse getContact() {
        return contact;
    }

    public void setContact(ContactResponse contact) {
        this.contact = contact;
    }

    public Boolean isAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }
}
