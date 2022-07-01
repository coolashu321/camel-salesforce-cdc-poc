package com.example.camel.salesforce;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.language.bean.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfiguration
@EnableAutoConfiguration
public class SalesforceCDCPoc {

    public static void main(String[] args) {
        SpringApplication.run(SalesforceCDCPoc.class, args);
    }

    @Bean(ref = "camelContext")
    CamelContext camelContext() {
        return new DefaultCamelContext();
    }
}
