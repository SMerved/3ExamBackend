package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.OwnerDto;
import dtos.WalkerDto;
import entities.*;
import facades.OwnerFacade;
import facades.WalkerFacade;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OwnerResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static OwnerFacade facade;

    private Walker w1, w2, w3;
    private Dog d1, d2, d3;
    private Owner o1, o2;

    public OwnerResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        facade = OwnerFacade.getOwnerFacade(emf);

    }


    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            em.createNamedQuery("Dog.deleteAllRows").executeUpdate();
            em.createNamedQuery("Walker.deleteAllRows").executeUpdate();
            em.createNamedQuery("Owner.deleteAllRows").executeUpdate();
            em.createQuery("delete from User").executeUpdate();
            em.createQuery("delete from Role").executeUpdate();

            Role userRole = new Role("user");
            Role adminRole = new Role("admin");
            User user = new User("user", "test");
            user.addRole(userRole);
            User admin = new User("admin", "test");
            admin.addRole(adminRole);
            User both = new User("user_admin", "test");
            both.addRole(userRole);
            both.addRole(adminRole);

            Set<Walker> walkers1 = new HashSet<>();
            Set<Walker> walkers2 = new HashSet<>();
            Set<Walker> walkers3 = new HashSet<>();


            w1= new Walker("Hans", "Jagtvej 60", "11223344");
            w2= new Walker("Bob", "Jagtvej 60", "11223344");
            w3= new Walker("Jens", "Jagtvej 60", "11223344");

            o1= new Owner("Owner1", "Lyngbyvej 10", "12345678");
            o2= new Owner("Owner2", "Lyngbyvej 10", "12345678");

            d1= new Dog("Dog1", "Golden Retriever", "DogImage", "Male", "10-5-2017", o1, walkers1);
            d2= new Dog("Dog2", "Golden Retriever", "DogImage", "Female", "10-5-2017", o1, walkers2);
            d3= new Dog("Dog3", "Labrador", "DogImage", "Male", "10-5-2017", o2, walkers3);

            w1.getDogs().add(d1);
            w1.getDogs().add(d2);
            w2.getDogs().add(d2);
            w3.getDogs().add(d3);
            d1.getWalkers().add(w1);
            d2.getWalkers().add(w2);
            d2.getWalkers().add(w1);
            d3.getWalkers().add(w3);
            o1.getDogs().add(d1);
            o1.getDogs().add(d2);
            o2.getDogs().add(d3);

            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.persist(both);
            em.persist(d1);
            em.persist(d2);
            em.persist(d3);
            em.persist(w1);
            em.persist(w2);
            em.persist(w3);
            em.persist(o1);
            em.persist(o2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    private static String securityToken;

    //Utility method to login and set the returned securityToken
    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/info").then().statusCode(200);
    }


    @Test
    public void testGetAllOwners() throws Exception {
        List<OwnerDto> ownerDtos;
        login("user", "test");
        ownerDtos = given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/owners")
                .then()
                .extract().body().jsonPath().getList("", OwnerDto.class);

        assertThat(ownerDtos, containsInAnyOrder(new OwnerDto(o1), new OwnerDto(o2)));
    }

}
