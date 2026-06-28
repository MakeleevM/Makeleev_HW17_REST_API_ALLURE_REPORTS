package models.login;

import java.util.List;

public record BlankCredentialsLoginResponseModel(List<String> username, List<String> password) {
}
