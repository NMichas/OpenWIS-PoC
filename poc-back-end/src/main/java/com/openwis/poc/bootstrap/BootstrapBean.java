package com.openwis.poc.bootstrap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by nassos on 6/10/16.
 */
@Singleton
public class BootstrapBean {
    @PostConstruct
    public void bootstrap() throws IOException {
        OkHttpClient client = new OkHttpClient();

        String json = "{" +
                "\"mappings\": {" +
                    "\"metadata\": {" +
                        "\"date_detection\": false," +
                        "\"numeric_detection\": false" +
            "}}}";

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://localhost:9200/openwis/metadata/" + UUID.randomUUID().toString())
                .put(body)
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            System.out.println("Index initialised.");
        }
    }
}
