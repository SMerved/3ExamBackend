package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DogDto;
import dtos.WalkerDto;
import facades.DogFacade;
import facades.WalkerFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


//Todo Remove or change relevant parts before ACTUAL use
@Path("dogs")
public class DogResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final DogFacade FACADE =  DogFacade.getDogFacade(EMF);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @RolesAllowed({"user", "admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllDogs() {
        List<DogDto> dogDtos = FACADE.getAllDogs();

        return Response.ok().entity(GSON.toJson(dogDtos)).build();
    }

    @GET
    @Path("{id}")
    @RolesAllowed({"user", "admin"})
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllDogsFromOwner(@PathParam("id") String content) {
        Long ownerId = GSON.fromJson(content, Long.class);
        List<DogDto> dogDtos = FACADE.getDogsFromOwner(ownerId);

        return Response.ok().entity(GSON.toJson(dogDtos)).build();
    }

    @POST
    @Path("admin/new")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createDog(String content) {
        DogDto dogDto = GSON.fromJson(content, DogDto.class);
        DogDto newDogDto = FACADE.createDog(dogDto);
        System.out.println(newDogDto);
        return Response.ok().entity(GSON.toJson(newDogDto)).build();
    }

    @PUT
    @Path("admin/update")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response upDog(String content) throws EntityNotFoundException {
        DogDto dogDto = GSON.fromJson(content, DogDto.class);
        DogDto updatedDogDto = FACADE.upDog(dogDto);
        return Response.ok().entity(GSON.toJson(updatedDogDto)).build();
    }

    @PUT
    @Path("admin/delete")
    @RolesAllowed("admin")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response deleteDog(String content) throws EntityNotFoundException {
        System.out.println("hallo");
        DogDto dogDto = GSON.fromJson(content, DogDto.class);
        DogDto deletedDog = FACADE.deleteDog(dogDto);
        return Response.ok().entity(GSON.toJson(deletedDog)).build();
    }
}
