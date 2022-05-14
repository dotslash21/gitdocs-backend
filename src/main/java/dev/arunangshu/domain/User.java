package dev.arunangshu.domain;

import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
public class User extends BaseEntity {

    @NotBlank(message = "The name is required")
    private String name;

    @NotBlank(message = "The nickname is required")
    @Size(min = 4, max = 16, message = "The nickname has to be between 4 to 16 characters long")
    @Column(unique = true, nullable = false)
    private String nickname;

    @NotBlank(message = "the email is required")
    @Email(message = "Please provide a valid email")
    @Column(unique = true, nullable = false)
    private String email;

    @URL
    private String picture;

    @Builder
    public User(
            UUID id,
            Long version,
            Timestamp createdDate,
            Timestamp lastModifiedDate,
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
