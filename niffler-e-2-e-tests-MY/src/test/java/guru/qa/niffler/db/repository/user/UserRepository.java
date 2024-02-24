package guru.qa.niffler.db.repository.user;

import guru.qa.niffler.db.model.UserAuthEntity;
import guru.qa.niffler.db.model.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    UserAuthEntity createInAuth(UserAuthEntity user);

    UserEntity createInUserdata(UserEntity user);

    List<UserAuthEntity> getUsersFromAuth();

    Optional<UserAuthEntity> findByIdInAuth(UUID id);
    Optional<UserEntity> findByIdInUserdata(UUID id);


    List<UserEntity> getUsersFromUserData();

    UserAuthEntity updateInAuth(UserAuthEntity user);

    UserEntity updateInUserdata(UserEntity user);

    void deleteInAuthById(UUID id);

    void deleteInUserdataById(UUID id);
}