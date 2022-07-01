package com.example.camel.salesforce;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



@Component
public class MessageProcessor implements Processor {
    Logger logger = LoggerFactory.getLogger(MessageProcessor.class);
    @Override
    public void process(Exchange exchange) throws Exception {
        logger.info(exchange.getMessage().getBody(String.class));
    }
}
