package pl.com.seremak.assetsmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private String preferredUsername;
    private String name;
    private String givenName;
    private String familyName;
}
