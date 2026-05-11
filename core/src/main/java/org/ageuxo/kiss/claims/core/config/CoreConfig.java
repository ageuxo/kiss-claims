package org.ageuxo.kiss.claims.core.config;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class CoreConfig {
    protected DatabaseConfig database = new DatabaseConfig();

    public DatabaseConfig database() {
        return database;
    }

    @ConfigSerializable
    public static class DatabaseConfig {
        protected String url = "localhost:3306";
        protected String username = "user";
        protected String password = "";

        public String url() {
            return url;
        }

        public String username() {
            return username;
        }

        public String password() {
            return password;
        }
    }

}
