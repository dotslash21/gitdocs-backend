package dev.arunangshu.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto extends BaseDto {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The nickname is required")
    @Size(min = 4, max = 16, message = "The nickname has to be between 4 to 16 characters long")
    private String nickname;

    @NotBlank(message = "the email is required")
    @Email(message = "Please provide a valid email")
    private String email;

    @URL
    private String picture;

    @Builder
    public UserDto(
            UUID id,
            Long version,
            OffsetDateTime createdDate,
            OffsetDateTime lastModifiedDate,
            String name,
            String nickname,
            String email,
            String picture) {

        super(id, version, createdDate, lastModifiedDate);
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.picture = picture;
    }
}
