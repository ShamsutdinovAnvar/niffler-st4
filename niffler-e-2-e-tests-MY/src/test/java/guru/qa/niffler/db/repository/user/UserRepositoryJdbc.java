package guru.qa.niffler.db.repository.user;

import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.model.*;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.db.Database.AUTH;
import static guru.qa.niffler.db.Database.USERDATA;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class UserRepositoryJdbc implements UserRepository {
    private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(AUTH);
    private final DataSource udDs = DataSourceProvider.INSTANCE.dataSource(USERDATA);
    private final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Override
    public UserAuthEntity createInAuth(UserAuthEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement userPs = conn.prepareStatement(
                    "INSERT INTO \"user\" " +
                            "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", RETURN_GENERATED_KEYS);
                 PreparedStatement authorityPs = conn.prepareStatement(
                         "INSERT INTO \"authority\" " +
                                 "(user_id, authority) " +
                                 "VALUES (?, ?)")
            ) {

                userPs.setString(1, user.getUsername());
                userPs.setString(2, pe.encode(user.getPassword()));
                userPs.setBoolean(3, user.getEnabled());
                userPs.setBoolean(4, user.getAccountNonExpired());
                userPs.setBoolean(5, user.getAccountNonLocked());
                userPs.setBoolean(6, user.getCredentialsNonExpired());

                userPs.executeUpdate();

                UUID authUserId;
                try (ResultSet keys = userPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        authUserId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }

                for (Authority authority : Authority.values()) {
                    authorityPs.setObject(1, authUserId);
                    authorityPs.setString(2, authority.name());
                    authorityPs.addBatch();
                    authorityPs.clearParameters();
                }

                authorityPs.executeBatch();
                conn.commit();
                user.setId(authUserId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public UserEntity createInUserdata(UserEntity user) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO \"user\" " +
                            "(username, currency) " +
                            "VALUES (?, ?)", RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().name());
                ps.executeUpdate();

                UUID userId;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        userId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }
                user.setId(userId);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public List<UserAuthEntity> getUsersFromAuth() {
        List<UserAuthEntity> userAuthEntityList = new ArrayList<>();
        try (Connection conn = authDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"user\"", RETURN_GENERATED_KEYS)) {
                try (ResultSet result = ps.executeQuery()) {
                    while (result.next()) {
                        UserAuthEntity userAuthEntity = new UserAuthEntity();
                        userAuthEntity.setUsername(result.getString("username"));
                        userAuthEntity.setPassword(result.getString("password"));
                        userAuthEntity.setEnabled(result.getBoolean("enabled"));
                        userAuthEntity.setAccountNonExpired(result.getBoolean("account_non_expired"));
                        userAuthEntity.setAccountNonLocked(result.getBoolean("account_non_locked"));
                        userAuthEntity.setCredentialsNonExpired(result.getBoolean("credentials_non_expired"));
                        userAuthEntityList.add(userAuthEntity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userAuthEntityList;
    }

    @Override
    public Optional<UserAuthEntity> findByIdInAuth(UUID id) {
        try (Connection conn = authDs.getConnection()) {
            UserAuthEntity userAuthEntity = new UserAuthEntity();
            try (
                    PreparedStatement userPs = conn.prepareStatement("SELECT * FROM \"user\" WHERE id = ?", RETURN_GENERATED_KEYS);
                    PreparedStatement authorityPs = conn.prepareStatement("SELECT * FROM \"authority\" WHERE user_id = ?")
            ) {
                userPs.setObject(1, id);
                try (ResultSet userResult = userPs.executeQuery()) {
                    if (userResult.next()) {
                        userAuthEntity.setUsername(userResult.getString("username"));
                        userAuthEntity.setPassword(userResult.getString("password"));
                        userAuthEntity.setEnabled(userResult.getBoolean("enabled"));
                        userAuthEntity.setAccountNonExpired(userResult.getBoolean("account_non_expired"));
                        userAuthEntity.setAccountNonLocked(userResult.getBoolean("account_non_locked"));
                        userAuthEntity.setCredentialsNonExpired(userResult.getBoolean("credentials_non_expired"));
                    }
                }
                authorityPs.setObject(1, id);
                try (ResultSet authorityResult = authorityPs.executeQuery()) {
                    List<AuthorityEntity> authorities = new ArrayList<>();
                    while (authorityResult.next()) {
                        AuthorityEntity authorityEntity = new AuthorityEntity();
                        authorityEntity.setAuthority(Authority.valueOf(authorityResult.getString("authority")));
                        authorityEntity.setUser(userAuthEntity);
                        authorityEntity.setId((UUID) authorityResult.getObject("id"));
                        authorities.add(authorityEntity);
                    }
                    userAuthEntity.setAuthorities(authorities);
                }
                return Optional.of(userAuthEntity);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findByIdInUserdata(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<UserEntity> getUsersFromUserData() {
        List<UserEntity> userEntityList = new ArrayList<>();
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT * FROM \"user\"", RETURN_GENERATED_KEYS)) {
                try (ResultSet result = ps.executeQuery()) {
                    while (result.next()) {
                        UserEntity userEntity = new UserEntity();
                        userEntity.setUsername(result.getString("username"));
                        userEntity.setCurrency(CurrencyValues
                                .valueOf(
                                        result.getString("currency")
                                )
                        );
                        userEntity.setFirstname(result.getString("firstname"));
                        userEntity.setSurname(result.getString("surname"));
                        userEntity.setPhoto(result.getBytes("photo"));
                        userEntityList.add(userEntity);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userEntityList;
    }

    @Override
    public UserAuthEntity updateInAuth(UserAuthEntity user) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement userPs = conn.prepareStatement("UPDATE \"user\" SET " +
                    "password = ?," +
                    "enabled = ?," +
                    "account_non_expired = ?," +
                    "account_non_locked = ?," +
                    "credentials_non_expired = ? " +
                    "WHERE id = ?"


            );
                 PreparedStatement authorityDeletePs = conn.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?");
                 PreparedStatement authorityInsertPs = conn.prepareStatement("INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")

            ) {
                userPs.setString(1, user.getPassword());
                userPs.setBoolean(2, user.getEnabled());
                userPs.setBoolean(3, user.getAccountNonExpired());
                userPs.setBoolean(4, user.getAccountNonLocked());
                userPs.setBoolean(5, user.getCredentialsNonExpired());
                userPs.setObject(6, user.getId());

                userPs.executeUpdate();

                authorityDeletePs.setObject(1, user.getId());
                authorityDeletePs.execute();

                List<Authority> userAuthorities = user
                        .getAuthorities()
                        .stream()
                        .map(AuthorityEntity::getAuthority)
                        .toList();

                for (Authority authority : userAuthorities) {
                    authorityInsertPs.setObject(1, user.getId());
                    authorityInsertPs.setString(2, authority.name());
                    authorityInsertPs.addBatch();
                    authorityInsertPs.clearParameters();
                }
                authorityInsertPs.executeBatch();
                conn.commit();
                return user;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserEntity updateInUserdata(UserEntity user) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("UPDATE \"user\" SET " +
                    "currency = ?," +
                    "firstname = ?," +
                    "surname = ?," +
                    "photo = ? " +
                    "WHERE id = ?")) {
                ps.setString(1, user.getCurrency().name());
                ps.setString(2, user.getFirstname());
                ps.setString(3, user.getSurname());
                ps.setBytes(4, user.getPhoto());
                ps.setObject(5, user.getId());
                ps.executeUpdate();
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteInAuthById(UUID id) {
        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);
            try (
                    PreparedStatement authorityPs = conn.prepareStatement("DELETE FROM \"authority\" WHERE user_id = ?");
                    PreparedStatement userPs = conn.prepareStatement("DELETE FROM \"user\" WHERE id = ?")
            ) {
                authorityPs.setObject(1, id);
                authorityPs.execute();
                userPs.setObject(1, id);
                userPs.execute();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteInUserdataById(UUID id) {
        try (Connection conn = udDs.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM \"user\" WHERE id = ?")) {
                ps.setObject(1, id);
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}