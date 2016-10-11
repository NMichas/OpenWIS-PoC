package com.openwis.poc.processors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Index2Processor {
    @EndpointInject(uri = "direct:index3.post")
    ProducerTemplate index3Producer;
    private static OkHttpClient client = new OkHttpClient();

    public String handle(Exchange exchange) throws IOException, XMLStreamException, InterruptedException {
        String url = exchange.getIn().getBody(String.class);

        // Fetch records list.
        //System.out.println("Fetching record: " + url);

        Request request = new Request.Builder().url(url).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            index3Producer.sendBody(response.body().string());
        } catch (Throwable e) {
            //System.out.println("!!!Error: " + e.getMessage());
        }


        return "hello from Index2";
    }
}
