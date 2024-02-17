package guru.qa.niffler.db.repository;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    static UserRepository getRepository() {
        String repository = System.getProperty("repository", "jdbc");

        if ("jdbc".equals(repository)) {
            return new UserRepositoryJdbc();
        } else if ("sjdbc".equals(repository)) {
            return new UserRepositorySJdbc();
        } else {
            throw new IllegalArgumentException("Not supported repository argument. " +
                    "Can be \"jdbc\", \"sjdbc\" or empty(default: \"jdbc\")");
        }
    }

    UserAuthEntity createInAuth(UserAuthEntity user);

    Optional<UserAuthEntity> findByIdInAuth(UUID id);

    UserAuthEntity updateInAuth(UserAuthEntity user);

    void deleteInAuthById(UUID id);

    UserEntity createInUserdata(UserEntity user);

    Optional<UserEntity> findByIdInUserdata(UUID id);

    UserEntity updateInUserdata(UserEntity user);

    void deleteInUserdataById(UUID id);
}
