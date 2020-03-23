package ch.unige.subproject2.rest;


import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;


@Path("/hello2")
public class HelloWorldEndpoint {
    @GET
    @Produces("text/plain")
    public Response doGet() {
        return Response.ok("Hello from Subproject2!").build();
    }
}
