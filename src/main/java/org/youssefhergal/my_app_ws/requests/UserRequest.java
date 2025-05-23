package org.youssefhergal.my_app_ws.requests;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRequest {

    @NotBlank(message = "First name cannot be null")
    private String firstname;
    @NotBlank(message = "Last name cannot be null")
    private String lastname;
    @Email(message = "Email must be valid")
    private String email;
    @NotBlank(message = "Password cannot be null")
    @Size(min = 5, message = "Password must be at least 8 characters")
    private String password;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
