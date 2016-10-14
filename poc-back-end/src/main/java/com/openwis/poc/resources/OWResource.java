package com.openwis.poc.resources;

import com.openwis.poc.bootstrap.BootstrapBean;
import com.openwis.poc.dto.ServerDTO;
import com.openwis.poc.model.Server;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.UUID;

@Singleton
@Path("openwis")
@CrossOriginResourceSharing(allowAllOrigins = true)
@Transactional
public class OWResource {
    @EndpointInject(uri = "direct:index1.post")
    ProducerTemplate index1Producer;

    /** EntityManager setter. */
    @PersistenceContext(unitName = "openwis")
    private EntityManager em;

    private static OkHttpClient client = new OkHttpClient();

    @POST
    @Path("add-server")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addServer(ServerDTO serverDTO) {
        Server s = new Server();
        s.setId(UUID.randomUUID().toString());
        s.setUrl(serverDTO.getUrl());
        s.setDataset(serverDTO.getDataset());
        s.setServername(serverDTO.getServername());
        em.persist(s);

        return Response.ok().build();
    }

    @GET
    @Path("server-identify")
    @Produces(MediaType.APPLICATION_XML)
    public Response serverIdentify(@QueryParam("url") String url) throws IOException {
        Request request = new Request.Builder().url(url + "?verb=Identify").build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            return Response.ok(response.body().string()).build();
        }
    }

    @GET
    @Path("server-list-sets")
    @Produces(MediaType.APPLICATION_XML)
    public Response listSets(@QueryParam("url") String url) throws IOException {
        Request request = new Request.Builder().url(url + "?verb=ListSets").build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            return Response.ok(response.body().string()).build();
        }
    }

    @GET
    @Path("servers-list")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listServers()  {
        return Response.ok(
                em.createQuery("select s from Server s").getResultList()
        ).build();
    }

    @GET
    @Path("harvest")
    @Produces(MediaType.APPLICATION_JSON)
    public Response harvest(@QueryParam("id") String id)  {
        Server server = em.find(Server.class, id);
        index1Producer.sendBody(server.getUrl() + "," + server.getDataset());

        return Response.ok(
                "{ \"harvestingId\": \"1234\" }"
        ).build();
    }

    @GET
    @Path("browse")
    @Produces(MediaType.APPLICATION_JSON)
    public Response browse(@QueryParam("p") int page, @QueryParam("s") int size) throws IOException {
        Request request = new Request.Builder().url(
                BootstrapBean.ES_SERVER + "/openwis/metadata/_search/?size=" + size
                        + "&from=" + size * page).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            return Response.ok(response.body().string()).build();
        }
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("t") String term) throws IOException {
        Request request = new Request.Builder().url(
        		BootstrapBean.ES_SERVER + "/openwis/_search?size=30&q=" + term).build();
        try (okhttp3.Response response = client.newCall(request).execute()) {
            return Response.ok(response.body().string()).build();
        }
    }
}
