package models.updateuser;

import java.util.List;

public record PutUpdateUserMissingFieldsResponseModel(List<String> username,
                                                      List<String> lastName,
                                                      List<String> email) {
}
