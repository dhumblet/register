package org.cashregister.webapp.ws;

import org.cashregister.common.ws.Sale;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by derkhumblet on 16/12/15.
 */
@Path("hello")
public class RestExample {

    @GET
    @Path("{name}")
    public String sayHello(@PathParam("name") String name){
        StringBuilder stringBuilder = new StringBuilder("SandBox | Hello ");
        stringBuilder.append(name).append("!");

        return stringBuilder.toString();
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Sale getSale(){
        Sale sale = new Sale();
        sale.setTestString("Test JSON");
        return sale;
    }

    @POST
    @Path("/post")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRecord(Sale sale){
        String result = "Record entered: "+ sale;
        return Response.status(201).entity(result).build();
    }

}
