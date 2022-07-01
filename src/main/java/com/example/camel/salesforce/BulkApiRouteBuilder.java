package com.example.camel.salesforce;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.salesforce.SalesforceEndpointConfig;
import org.apache.camel.component.salesforce.api.dto.bulkv2.ContentTypeEnum;
import org.apache.camel.component.salesforce.api.dto.bulkv2.JobStateEnum;
import org.apache.camel.component.salesforce.api.dto.bulkv2.OperationEnum;
import org.apache.camel.component.salesforce.api.dto.bulkv2.QueryJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Component
public class BulkApiRouteBuilder extends RouteBuilder {

    @Autowired
    GetCompletedJobProcessor getCompletedJobProcessor;

    @Override
    public void configure() throws Exception {

        from("scheduler:test?delay=360000").process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    QueryJob queryJob = new QueryJob();
                    queryJob.setOperation(OperationEnum.QUERY_ALL);
                    queryJob.setQuery("SELECT Id, Name from Account" );
                    exchange.getIn().setBody(queryJob);
                }
            })
        .log("starting..... . . . . .  .  .  .   .   .   .")
        .to("salesforce:bulk2CreateQueryJob")
        .onCompletion()
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    System.out.println(exchange.getIn().getHeader("JOB_ID"));
                    QueryJob createdJob = (QueryJob) exchange.getIn().getBody();
                    exchange.getIn().setHeader(SalesforceEndpointConfig.JOB_ID,createdJob.getId() );
                }
            })
            .loopDoWhile(simple("${body.state} !=~ 'JobComplete'"))
                .log("Into a loop")
                .delay(1000)
                .to("salesforce:bulk2GetQueryJob")
//                .process(getCompletedJobProcessor)
            .end() //ending loop
        .end()
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    QueryJob createdJob = (QueryJob) exchange.getIn().getBody();
                    exchange.getIn().setHeader(SalesforceEndpointConfig.JOB_ID,createdJob.getId() );
                    exchange.getIn().setHeader(SalesforceEndpointConfig.MAX_RECORDS, 2);
                    exchange.getIn().setHeader(SalesforceEndpointConfig.CONTENT_TYPE, ContentTypeEnum.CSV);
                }
            })
            .to("salesforce:bulk2GetQueryJobResults")
//                .unmarshal()
//                .csv()
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
//                    List<Account> data = (List<Account>) exchange.getIn().getBody();
//                    System.out.println(data);
                    System.out.println("complete");
                }
            })

        ;

    }
}
