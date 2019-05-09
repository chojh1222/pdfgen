package com.bestiansoft.pdfgen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*");
            }
        };
    }

    // @Configuration
    // @EnableJpaRepositories(
    //     basePackages="com.bestiansoft.pdfgen.repo",
    //     entityManagerFactoryRef = "entityManagerFactory",
    //     transactionManagerRef = "transactionManager")
    // static class DbArticleJpaRepositoriesConfig {
    // }

    
    // // @Bean
    // // @ConfigurationProperties(prefix = "datasource.user")
    // // public DataSource userDataSource() {
    // //     return DataSourceBuilder.create().build();
    // // }

    // @Bean(name = "entityManagerFactoryUser")
    // public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(
    //     EntityManagerFactoryBuilder builder, DataSource dataSource) {

    //     return builder.dataSource(dataSource)
    //         .packages("com.bestiansoft.pdfgen.repo")
    //         .build();
    // }

    // // @Bean(name = "transactionManagerUser")
    // // @Primary
    // // PlatformTransactionManager userTransactionManagerMain(
    // //     EntityManagerFactoryBuilder builder) {
    // // return new JpaTransactionManager(userEntityManagerFactory(builder).getObject());
    // // }
    
    // @Configuration
    // @EnableJpaRepositories(
    //     basePackages="com.bestiansoft.pdfgen.repo",
    //     // entityManagerFactoryRef = "entityManagerFactoryUser",
    //     transactionManagerRef = "transactionManagerUser")
    // static class DbUserJpaRepositoriesConfig {
    // }
}