package com.example.camel.salesforce;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;

@Component
@EnableAutoConfiguration
public class CDCRouteBuilder extends RouteBuilder {

    @Autowired
    MessageProcessor messageProceesor;

    @Override
    public void configure() throws Exception {
        from("salesforce:data/ChangeEvents?replayId=-1").log("being notified of all change events")
                .bean(messageProceesor);
    }
}
