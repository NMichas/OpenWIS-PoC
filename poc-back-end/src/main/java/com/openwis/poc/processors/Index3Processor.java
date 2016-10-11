package com.openwis.poc.processors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.camel.Exchange;
import org.json.JSONObject;
import org.json.XML;

import javax.inject.Singleton;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Singleton
public class Index3Processor {

    private static OkHttpClient client = new OkHttpClient();

    private String xmlToJson1(String xml) throws IOException {
        ObjectMapper xmlMapper = new XmlMapper();
        List entries = xmlMapper.readValue(xml, List.class);
        ObjectMapper jsonMapper = new ObjectMapper();

        String json = jsonMapper.writeValueAsString(entries);

        if (json.indexOf("\"record\"") > -1) {
            json = json.substring(json.indexOf("\"record\"") - 1);
            if (json.endsWith("}]")) {
                json = json.substring(0, json.length() - 2);
            }
            if (json.endsWith("]")) {
                json = json.substring(0, json.length() - 1);
            }
        }

        return json;
    }

    private String xmlToJson2(String xml) throws IOException {
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj.toString();
    }


    public String handle(Exchange exchange) throws IOException, XMLStreamException, InterruptedException {
        String xml = exchange.getIn().getBody(String.class);

        // Fetch records list.
        //System.out.println("Got a doc to index.");

        // Convert XML to JSON.
        String json = xmlToJson2(xml);
//                    System.out.println(json);
//                    System.out.println("--------------------------------------------------------------------------------------");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://localhost:9200/openwis/metadata/" + UUID.randomUUID().toString())
                .put(body) //PUT
                .build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            //System.out.println("OK! ");
        }

        return "hello from Index3";
    }
}
