package org.youssefhergal.my_app_ws.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Entity(name = "contacts")
public class ContactEntity implements Serializable {


    @Id
    @GeneratedValue
    private long id;

    @NotBlank(message = "Contact Id cannot be null")
    @Column(length = 30)
    private String contactId;

    @NotBlank(message = "Number cannot be null")
    private String mobile;
    private String skype;


    @OneToOne
    @JoinColumn(name = "users_id")
    private UserEntity user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }
}
