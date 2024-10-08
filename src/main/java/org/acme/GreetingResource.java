package org.acme;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;



@Path("/greeting")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)

public class GreetingResource {
    @Path("/personalized/{name}")
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String personalizedHello(@PathParam("name") String name) {
        try {
            UserName userName = new UserName(name);
            userName.persist();
            return "Hello " + name + "! Your name has been  stored in the database.";
        }catch (Exception e) {
            return "An error occurred while storing your name.";
        }
    }
    @GET
    @Path("/findUser/{id}")
    public UserName getSingleUser(@PathParam("id") String id) {
        UserName userName =  UserName.findById(id);
        if(userName == null) {
            throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
        }
        return userName;
    }
    @DELETE
    @Path("/delete/{id}")
    @Transactional
    public String deleteUser(@PathParam("id") String id) {

        try{
            UserName userName =  UserName.findById(id);
            if(userName == null) {
                throw new WebApplicationException("User with id of " + id + " does not exist.", 404);
            }
            userName.delete();
            //userName.persist();
            return "The user with id of " + id + " has been deleted.";

        }catch (Exception e) {
            return "An error occurred while deleting the user. ";
        }
    }
    @PUT
    @Path("/update/{id}/{name}")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String update(@PathParam("id") String id,@PathParam("name") String name) {
        try{
            UserName userName = UserName.findById(id);
            System.out.println(userName.id);
            System.out.println(name);

            //userName.name = name;
            //UserName.update("name = ?1 where id = ?2 ",name,userName.id);
            userName.setName(name);
            userName.persist();
            return "The user " + name + " has been updated";
        } catch (Exception e) {

            return "An error occurred while updating the user. ";
        }

    }
}
