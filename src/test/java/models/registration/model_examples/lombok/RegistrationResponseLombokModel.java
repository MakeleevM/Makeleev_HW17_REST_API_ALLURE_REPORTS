package models.registration.model_examples.lombok;

import lombok.Data;

@Data
public class RegistrationResponseLombokModel {
    Integer id;
    String username;
    String firstName;
    String lastName;
    String email;
    String rempteAddr;
}
