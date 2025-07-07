package com.suveechi.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    // Shared password and driver class name
    private static final String PASSWORD = "SuV@@Jssl1";  
    private static final String DRIVER_CLASS_NAME = "com.microsoft.sqlserver.jdbc.SQLServerDriver";  

    // Database-specific URLs and usernames
    private static final String MIS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JMIS;encrypt=false";
    private static final String JIMS_URL = "jdbc:sqlserver://10.10.94.4:1433;databaseName=JIMS;encrypt=false";
    private static final String MIS2_URL = "jdbc:sqlserver://10.10.92.145:1433;databaseName=JMIS;encrypt=false";
    
    private static final String MIS_USERNAME = "suveechi";  
    private static final String JIMS_USERNAME = "suveechi";  
    private static final String MIS2_USERNAME = "suveechi"; 

    @Bean(name = "misDataSource")
    public DataSource misDataSource() {
        return createDataSource(MIS_URL, MIS_USERNAME);
    }
    
    @Bean(name = "misDataSource2")
    public DataSource misDataSource2() {
        return createDataSource(MIS2_URL, MIS2_USERNAME);
    }

    @Bean(name = "jimsDataSource")
    @Primary
    public DataSource jimsDataSource() {
        return createDataSource(JIMS_URL, JIMS_USERNAME);
    }

    // Utility method to create DataSource with database-specific username
    private DataSource createDataSource(String url, String username) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        dataSource.setUrl(url);
        dataSource.setUsername(username);  // Use the database-specific username
        dataSource.setPassword(PASSWORD);
        return dataSource;
    }
}
