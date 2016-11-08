package com.openwis.poc.processors;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import static com.openwis.poc.bootstrap.BootstrapBean.httpProxy;

public class Index2Processor {
    @EndpointInject(uri = "direct:index3.post")
    ProducerTemplate index3Producer;

    private static OkHttpClient client = httpProxy != null
            ? new OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    httpProxy.getHost(), httpProxy.getPort())))
            .build()
            : new OkHttpClient();

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
