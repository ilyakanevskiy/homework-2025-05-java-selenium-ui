package dev.ilyakanevskiy.config;

public enum User {
    LEARNER1("learner1"),
    LEARNER2("learner2"),
    LEARNER3("learner3"),
    ADMIN("admin");

    private final String configKeyPrefix;

    User(String configKeyPrefix) {
        this.configKeyPrefix = configKeyPrefix;
    }

    public String username() {
        return ConfigReader.getProperty(configKeyPrefix + "_username");
    }

    public String password() {
        return ConfigReader.getProperty(configKeyPrefix + "_password");
    }

    @Override
    public String toString() {
        return username();
    }
}
