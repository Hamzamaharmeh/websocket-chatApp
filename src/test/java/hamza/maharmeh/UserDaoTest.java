package hamza.maharmeh;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

public class UserDaoTest {
    private static DataSource dataSource = new HikariDataSource(
            new HikariConfig("dataSource.test.properties")
    );

}
