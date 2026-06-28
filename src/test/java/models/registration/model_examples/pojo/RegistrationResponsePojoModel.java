package models.registration.model_examples.pojo;

import static java.lang.String.format;

public class RegistrationResponsePojoModel {
    Integer id;
    String username;
    String firstName;
    String lastName;
    String email;
    String rempteAddr;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRempteAddr() {
        return rempteAddr;
    }

    public void setRempteAddr(String rempteAddr) {
        this.rempteAddr = rempteAddr;
    }

    @Override
    public String toString() {
        return format("{\"id\": \"%s\", {\"username\": \"%s\", {\"firstName\": \"%s\", " +
                        "\"lastName\": \"%s\", {\"email\": \"%s\", {\"rempteAddr\": \"%s\"}",
                this.id, this.username, this.firstName, this.lastName, this.email, this.rempteAddr);
    }
}
