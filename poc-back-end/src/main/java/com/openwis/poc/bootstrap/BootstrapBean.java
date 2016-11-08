package com.openwis.poc.bootstrap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

@Singleton
public class BootstrapBean {
    public static final String ES_SERVER = "http://elasticsearch:9200";
    public static URL httpProxy;

    @PostConstruct
    public void bootstrap() throws Exception {
        OkHttpClient client = new OkHttpClient();

        // Get proxy info.
        try {
            httpProxy = new URL(System.getenv("HTTP_PROXY"));
        } catch (MalformedURLException e) {
            System.out.println("Could not parse HTTP_PROXY: " + e.getMessage());
        }

        // Setup index type.
        String json = "{" +
                "\"mappings\": {" +
                "\"metadata\": {" +
                "\"date_detection\": false," +
                "\"numeric_detection\": false" +
                "}}}";

        System.out.println("Initialising ES at: " + ES_SERVER);
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(ES_SERVER + "/openwis/metadata/" + UUID.randomUUID().toString())
                .put(body)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            System.out.println("Index initialised.");
        }

        // Disable mapping debug messages.
        json = "{\"transient\": {\"logger.action.index\": \"ERROR\"}}";
        body = RequestBody.create(JSON, json);
        request = new Request.Builder()
                .url(ES_SERVER + "/_cluster/settings")
                .put(body)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            System.out.println("Mapping debugging disabled.");
        }

        if (httpProxy != null) {
            System.out.println("Using proxy: " + httpProxy.toExternalForm());
        } else {
            System.out.println("No proxy configuration found, using direct Internet connection.");
        }
    }
}
