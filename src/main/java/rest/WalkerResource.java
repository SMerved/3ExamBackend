package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.WalkerDto;
import facades.WalkerFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


//Todo Remove or change relevant parts before ACTUAL use
@Path("walkers")
public class WalkerResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final WalkerFacade FACADE =  WalkerFacade.getWalkerFacade(EMF);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @RolesAllowed("user")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllWalkers() {
        List<WalkerDto> walkerDtos = FACADE.getAllWalkers();
        System.out.println(walkerDtos);

        return Response.ok().entity(GSON.toJson(walkerDtos)).build();
    }

}
