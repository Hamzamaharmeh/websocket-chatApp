package hamza.maharmeh.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DefaultDataSourceConfig {
    private static final HikariDataSource dataSource;
    static {
        var config =  new HikariConfig("datasource.properties");
        dataSource = new HikariDataSource(config);
    }
    public static DataSource getDataSource() {
        return dataSource;
    }
}
