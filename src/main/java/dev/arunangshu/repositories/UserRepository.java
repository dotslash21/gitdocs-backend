package dev.arunangshu.repositories;

import dev.arunangshu.domain.User;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import lombok.NonNull;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Transactional
public class UserRepository implements PanacheRepositoryBase<User, UUID> {

    public Optional<User> findByNicknameOptional(@NonNull String nickname) {
        return find("nickname", nickname).firstResultOptional();
    }

    public Optional<User> findByEmailOptional(@NonNull String email) {
        return find("email", email).firstResultOptional();
    }
}
