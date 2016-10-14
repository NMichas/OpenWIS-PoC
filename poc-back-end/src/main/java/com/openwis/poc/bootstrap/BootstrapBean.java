package com.openwis.poc.bootstrap;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.UUID;
import org.ops4j.pax.cdi.api.OsgiService;
import javax.inject.Inject;

/**
 * Created by nassos on 6/10/16.
 */
@Singleton
public class BootstrapBean {
	public static final String ES_SERVER = "http://elasticsearch:9200";
	
    @PostConstruct
    public void bootstrap() throws Exception {
    	OkHttpClient client = new OkHttpClient();
    	
    	// Setup index type.
        String json = "{" +
                "\"mappings\": {" +
                    "\"metadata\": {" +
                        "\"date_detection\": false," +
                        "\"numeric_detection\": false" +
            "}}}";

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
        
    }
}
