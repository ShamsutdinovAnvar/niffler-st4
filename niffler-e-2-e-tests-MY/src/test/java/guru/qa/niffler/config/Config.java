package guru.qa.niffler.config;

public interface Config {

    static Config getInstance() {
        return "docker".equals(System.getProperty("test.env"))
                ? DockerConfig.instance
                : LocalConfig.instance;
    }

    String frontUrl();

    String jdbcHost();

    default String jdbcUser() {
        return "postgres";
    }

    default String jdbcPassword() {
        return "secret";
    }

    default int jdbcPort() {
        return 5432;
    }


    default String spendServiceUrl() {
        return "http://127.0.0.1:8093";
    }

    default String currencyServiceUrl() {
        return "http://127.0.0.1:8091";
    }

    default String userDataServiceUrl() {
        return "http://127.0.0.1:8089";
    }
}