package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.DogDto;
import dtos.WalkerDto;
import entities.*;
import facades.DogFacade;
import facades.WalkerFacade;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DogResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static DogFacade facade;

    private Walker w1, w2, w3;
    private Dog d1, d2, d3;
    private Owner o1, o2;

    public DogResourceTest() {
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

        facade = DogFacade.getDogFacade(emf);

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
    public void testGetDogsFromOwner() throws Exception {
        List<DogDto> dogDtos;
        login("user", "test");
        dogDtos = given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/dogs/"+ o1.getId())
                .then()
                .extract().body().jsonPath().getList("", DogDto.class);

        assertThat(dogDtos, containsInAnyOrder(new DogDto(d1), new DogDto(d2)));
    }

    @Test
    public void testGetAllDogs() throws Exception {
        List<DogDto> dogDtos;
        login("user", "test");
        dogDtos = given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .when()
                .get("/dogs")
                .then()
                .extract().body().jsonPath().getList("", DogDto.class);

        assertThat(dogDtos, containsInAnyOrder(new DogDto(d1), new DogDto(d2), new DogDto(d3)));
    }

    @Test
    public void testCreateDog() {
        login("admin", "test");

        DogDto dogDto = new DogDto(d1);
        String requestBody = GSON.toJson(dogDto);
        DogDto createdDogDto;

        createdDogDto = given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(requestBody)
                .when()
                .post("dogs/admin/new")
                .then()
                .extract().body().jsonPath().getObject("", DogDto.class);
        System.out.println(createdDogDto);

        assertEquals(d1, new Dog(createdDogDto));
    }

    @Test
    public void testUpDog() {
        login("admin", "test");

        d1.setName("UpdatedName");
        d1.setBreed("UpdatedBreed");
        d1.setImage("UpdatedImage");
        d1.setGender("UpdatedGender");
        d1.setBirthdate("UpdatedBirthdate");
        d1.setOwner(o2);
        Set<Walker> updatedWalkers = new HashSet<>();
        d1.setWalkers(updatedWalkers);
        d1.getWalkers().add(w2);
        Walker walker = new Walker("Johnny", "Jagtvej 40", "11223344");
        d1.getWalkers().add(walker);
        DogDto dogDto = new DogDto(d1);
        String requestBody = GSON.toJson(dogDto);
        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(requestBody)
                .when()
                .put("/dogs/admin/update")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("UpdatedName"))
                .body("breed", equalTo("UpdatedBreed"))
                .body("image", equalTo("UpdatedImage"))
                .body("gender", equalTo("UpdatedGender"))
                .body("birthdate", equalTo("UpdatedBirthdate"));
    }

    @Test
    public void testDeleteDog() {
        login("admin", "test");
        DogDto dogDto = new DogDto(d2);
        String requestBody = GSON.toJson(dogDto);

        given()
                .header("Content-type", ContentType.JSON)
                .header("x-access-token", securityToken)
                .body(requestBody)
                .when()
                .put("/dogs/admin/delete")
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo(d2.getName()))
                .body("breed", equalTo(d2.getBreed()));
        List<DogDto> dogDtos = facade.getAllDogs();
        assert(!dogDtos.contains(new DogDto(d2)));
    }


}
