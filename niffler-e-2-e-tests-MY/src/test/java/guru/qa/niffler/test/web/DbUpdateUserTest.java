package guru.qa.niffler.test.web;

import guru.qa.niffler.db.model.*;
import guru.qa.niffler.db.repository.user.UserRepository;
import guru.qa.niffler.helper.RandomData;
import guru.qa.niffler.jupiter.extension.UserRepositoryExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(UserRepositoryExtension.class)
public class DbUpdateUserTest {
    private UserRepository userRepository;

    private UserAuthEntity userAuth;
    private UserEntity user;


    private final String USERNAME = new RandomData().getUserName();

    @BeforeEach
    void createUser() {
        userAuth = new UserAuthEntity();
        userAuth.setUsername(USERNAME);
        userAuth.setPassword("12345");
        userAuth.setEnabled(true);
        userAuth.setAccountNonExpired(true);
        userAuth.setAccountNonLocked(true);
        userAuth.setCredentialsNonExpired(true);
        userAuth.setAuthorities(Arrays.stream(Authority.values())
                .map(e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setAuthority(e);
                    return ae;
                }).toList()
        );

        user = new UserEntity();
        user.setUsername(USERNAME);
        user.setCurrency(CurrencyValues.RUB);
        userRepository.createInAuth(userAuth);
        userRepository.createInUserdata(user);
    }

    @AfterEach
    void removeUser() {
        userRepository.deleteInAuthById(userAuth.getId());
        userRepository.deleteInUserdataById(user.getId());
    }

    @Test
    void updateUserAndLeaveOnlyWriteAuthority_updatedUserShouldHaveOnlyWriteAuthority() {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority(Authority.write);
        userAuth.setAuthorities(Collections.singletonList(authorityEntity));
        userRepository.updateInAuth(userAuth);
        Optional<UserAuthEntity> userFromDb = userRepository.findByIdInAuth(userAuth.getId());
        assertTrue(userFromDb.isPresent());
        assertEquals(userFromDb.get().getId(), userAuth.getId());
        assertEquals(userFromDb.get().getAuthorities().size(), 1);
        assertEquals(userFromDb.get().getAuthorities().get(0).getAuthority(), Authority.write);
    }

    @Test
    void updateUserAndLeaveOnlyReadAuthority_updatedUserShouldHaveOnlyReadAuthority() {
        AuthorityEntity authorityEntity = new AuthorityEntity();
        authorityEntity.setAuthority(Authority.read);
        userAuth.setAuthorities(Collections.singletonList(authorityEntity));
        userRepository.updateInAuth(userAuth);
        Optional<UserAuthEntity> userFromDb = userRepository.findByIdInAuth(userAuth.getId());
        assertTrue(userFromDb.isPresent());
        assertEquals(userFromDb.get().getId(), userAuth.getId());
        assertEquals(userFromDb.get().getAuthorities().size(), 1);
        assertEquals(userFromDb.get().getAuthorities().get(0).getAuthority(), Authority.read);
    }

//    @Test
//    void updateUserAndRemoveAllAuthorities_updatedUserShouldHaveNoAuthority() {
//        userAuth.setAuthorities(Collections.emptyList());
//        userRepository.updateInAuth(userAuth);
//        Optional<UserAuthEntity> userFromDb = userRepository.findByIdInAuth(userAuth.getId());
//        assertTrue(userFromDb.isPresent());
//        assertEquals(userFromDb.get().getId(), userAuth.getId());
//        assertEquals(userFromDb.get().getAuthorities().size(), 0);
//    }

    @Test
    void updateUserAndKeepAllAuthorities_updatedUserShouldHaveReadAndWriteAuthority() {
        List<AuthorityEntity> aeList = new ArrayList<>();
        AuthorityEntity readAe = new AuthorityEntity();
        readAe.setAuthority(Authority.read);
        AuthorityEntity writeAe = new AuthorityEntity();
        writeAe.setAuthority(Authority.write);
        aeList.add(readAe);
        aeList.add(writeAe);
        userAuth.setAuthorities(aeList);
        userRepository.updateInAuth(userAuth);
        Optional<UserAuthEntity> userFromDb = userRepository.findByIdInAuth(userAuth.getId());
        assertTrue(userFromDb.isPresent());
        assertEquals(userFromDb.get().getId(), userAuth.getId());
        assertEquals(userFromDb.get().getAuthorities().size(), 2);
    }

    @Test
    void updateUserDataTest() {
        String firstname = USERNAME + "_firstname";
        user.setFirstname(firstname);
        userRepository.updateInUserdata(user);
        Optional<UserEntity> userFromDb = userRepository.findByIdInUserdata(user.getId());
        assertTrue(userFromDb.isPresent());
        assertEquals(userFromDb.get().getId(), user.getId());
        assertEquals(userFromDb.get().getFirstname(), firstname);
    }
}
