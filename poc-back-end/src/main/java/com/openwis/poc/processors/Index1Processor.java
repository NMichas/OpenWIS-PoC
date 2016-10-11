package com.openwis.poc.processors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;

public class Index1Processor {
    @EndpointInject(uri = "direct:index2.post")
    ProducerTemplate index2Producer;

    private static OkHttpClient client = new OkHttpClient();

    public String handle(Exchange exchange) throws IOException, XMLStreamException, InterruptedException {
        String body = exchange.getIn().getBody(String.class);

        // Fetch records list.
        String url = body.split(",")[0];
        String dataset = body.split(",")[1];
        System.out.println("Listing identifiers for: " + url + " dataset: " + dataset);
        String resumptionToken = "";

        long startTime = Instant.now().toEpochMilli();
        int totalCounter = 0;
        while (resumptionToken != null) {
            String serviceUrl;
            if (resumptionToken != null && !resumptionToken.equals("")) {
                serviceUrl = url + "?verb=ListIdentifiers" + resumptionToken;
            } else {
                serviceUrl = url + "?verb=ListIdentifiers&metadataPrefix=iso19139&set=" + dataset;
            }

            System.out.println("Service url: " + serviceUrl);
            resumptionToken = null;

            String res = null;
            int retryCounter = 0;
            int maxRetries = 30;
            while (retryCounter < maxRetries) {
                res = fetch(serviceUrl);
                if (res != null) {
                    retryCounter = maxRetries;
                } else {
                    retryCounter++;
                    System.out.println("Retry [" + retryCounter + "].");
                    Thread.sleep(retryCounter * 1000);
                }
            }

            if (res == null) {
                continue;
            }
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(res));

            int counter = 0;
            String tagContent = null;

            while (reader.hasNext()) {
                int event = reader.next();
                switch (event) {
                    case XMLStreamConstants.CHARACTERS:
                        tagContent = reader.getText().trim();
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equals("identifier")) {
                            counter++;
                            index2Producer.sendBody(url + "?verb=GetRecord&metadataPrefix=iso19139&identifier=" + tagContent);
                        } else if (reader.getLocalName().equals("resumptionToken")) {
                            resumptionToken = "&resumptionToken=" + tagContent;
                        }
                        break;
                }
            }

            totalCounter += counter;
            System.out.println("*** Fetched " + counter + " documents [current total = " + totalCounter + "]");
        }
        long totalTimeSec = (Instant.now().toEpochMilli() - startTime)/1000l;
        System.out.println("**** Found total " + totalCounter + " documents. Processing speed: " + ((int)(totalCounter / totalTimeSec)));

        return "hello from Index1";
    }

    private String fetch(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Throwable e) {
            System.out.println("!!!Error: " + e.getMessage());
            return null;
        }
    }
}
