package guru.qa.niffler.db.repository.spend;


import guru.qa.niffler.db.DataSourceProvider;
import guru.qa.niffler.db.Database;
import guru.qa.niffler.db.model.SpendEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.UUID;

public class SpendRepositoryJdbc implements SpendRepository {

    private final DataSource authDs = DataSourceProvider.INSTANCE.dataSource(Database.SPEND);

    @Override
    public SpendEntity createSpend(SpendEntity spendEntity) {

        try (Connection conn = authDs.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement spendPs = conn.prepareStatement(
                    "INSERT INTO \"spend\" " +
                            "(username, currency, spend_date, category_id, amount, description) " +
                            "VALUES (?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
                 PreparedStatement categoryPs = conn.prepareStatement(
                         "INSERT INTO \"category\" " +
                                 "(category, username) " +
                                 "VALUES (?, ?)",
                         PreparedStatement.RETURN_GENERATED_KEYS)
            ) {
                UUID categoryId;
                try (ResultSet keys = categoryPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        categoryId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }
                categoryPs.setString(1, spendEntity.getCategory().getCategory());
                categoryPs.setString(2, spendEntity.getUsername());
                categoryPs.execute();

                spendPs.setString(1, spendEntity.getUsername());
                spendPs.setString(2, spendEntity.getCurrency().name());
                spendPs.setDate(3, (Date) spendEntity.getSpendDate());
                spendPs.setObject(4, categoryId);
                spendPs.setDouble(5, spendEntity.getAmount());
                spendPs.setString(6, spendEntity.getDescription());

                spendPs.executeUpdate();


                UUID spendId;
                try (ResultSet keys = spendPs.getGeneratedKeys()) {
                    if (keys.next()) {
                        spendId = UUID.fromString(keys.getString("id"));
                    } else {
                        throw new IllegalStateException("Can`t find id");
                    }
                }

                conn.commit();
                spendEntity.setId(spendId);
            } catch (Exception e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return spendEntity;
    }
}