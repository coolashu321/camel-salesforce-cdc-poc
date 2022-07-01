package com.example.camel.salesforce;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.salesforce.SalesforceEndpointConfig;
import org.apache.camel.component.salesforce.api.dto.bulkv2.ContentTypeEnum;
import org.apache.camel.component.salesforce.api.dto.bulkv2.JobStateEnum;
import org.apache.camel.component.salesforce.api.dto.bulkv2.OperationEnum;
import org.apache.camel.component.salesforce.api.dto.bulkv2.QueryJob;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class GetCompletedJobProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        QueryJob createdJob = (QueryJob) exchange.getIn().getBody();
        if(createdJob.getState() == JobStateEnum.JOB_COMPLETE) {
            exchange.getIn().setHeader(SalesforceEndpointConfig.JOB_ID,createdJob.getId() );
        }
    }
}
