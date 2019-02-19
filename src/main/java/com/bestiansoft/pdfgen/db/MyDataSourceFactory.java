package com.bestiansoft.pdfgen.db;
  
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import org.mariadb.jdbc.MariaDbDataSource;
  
public class MyDataSourceFactory {
  
    public static DataSource getMariaDbDataSource() {
        Properties props = new Properties();
        FileInputStream fis = null;
        MariaDbDataSource mysqlDS = null;
        try {
            fis = new FileInputStream("classpath:db.properties");
            props.load(fis);
            mysqlDS = new MariaDbDataSource();
            mysqlDS.setUrl(props.getProperty("MYSQL_DB_URL"));
            mysqlDS.setUser(props.getProperty("MYSQL_DB_USERNAME"));
            mysqlDS.setPassword(props.getProperty("MYSQL_DB_PASSWORD"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mysqlDS;
    }
}