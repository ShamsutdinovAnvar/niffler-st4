package guru.qa.niffler.config;

import com.codeborne.selenide.Configuration;

public class DockerConfig implements Config {

    static final DockerConfig instance = new DockerConfig();

    private DockerConfig() {
    }

    static {
        Configuration.remote = "http://localhost:4444/wd/hub";
        Configuration.browser = "chrome";
    }

    @Override
    public String frontUrl() {
        return "http://frontend.niffler.dc";
    }

    @Override
    public String authUrl() {
        return "http://auth.niffler.dc:9000";
    }
    @Override
    public String spendUrl() {
        return "http://spend.niffler.dc:8093/";
    }
    @Override
    public String userdataUrl() {
        return "http://userdata.niffler.dc:8089/";
    }
    @Override
    public String gatewayUrl() {return null;}
    @Override
    public String jdbcHost() {
        return "niffler-all-db";
    }
    @Override
    public String currencyGrpcHost() {
        return "currency.niffler.dc";
    }

    @Override
    public String spendsGrpcHost() {
        return "spend.niffler.dc";
    }
}