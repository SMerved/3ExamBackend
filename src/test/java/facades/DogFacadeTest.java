package facades;

import dtos.DogDto;
import dtos.WalkerDto;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DogFacadeTest {
    private static EntityManagerFactory emf;
    private static DogFacade facade;

    private Walker w1, w2, w3;
    private Dog d1, d2, d3;
    private Owner o1, o2;

    public DogFacadeTest() {
    }
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = DogFacade.getDogFacade(emf);
    }


    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Dog.deleteAllRows").executeUpdate();
            em.createNamedQuery("Walker.deleteAllRows").executeUpdate();
            em.createNamedQuery("Owner.deleteAllRows").executeUpdate();

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

    @Test
    public void testGetDogsFromOwner() throws Exception {
        List<DogDto> actual = facade.getDogsFromOwner(o1.getId());
        assert (actual.contains(new DogDto(d1)));
        assert (actual.contains(new DogDto(d2)));
    }

    @Test
    public void testGetAllDogs(){
        List<DogDto> actual = facade.getAllDogs();
        assert(actual.contains(new DogDto(d1)));
        assert(actual.contains(new DogDto(d2)));
        assert(actual.contains(new DogDto(d3)));
    }

    @Test
    public void testCreateDog() throws Exception {
        Set<Walker> walkers = new HashSet<>();
        Walker walker= new Walker("Ole", "Jagtvej 60", "11223344");
        Dog expected = new Dog("Rover", "Golden Retriever", "DogImage", "Male", "08-10-2015", o1, walkers);
        expected.getWalkers().add(walker);
        o1.getDogs().add(expected);

        DogDto actualDTO = facade.createDog(new DogDto(expected));
        System.out.println(actualDTO);
        Dog actual = new Dog(actualDTO);
        System.out.println(actual);
        assertEquals(expected, actual);
    }
    @Test
    public void testUpDog() throws Exception {
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
        System.out.println(d1);
        Dog expectedDog = d1;
        System.out.println(expectedDog);
        DogDto actualDogDTO = facade.upDog(new DogDto(d1));
        Dog actualDog = new Dog(actualDogDTO);
        assertEquals(expectedDog, actualDog);
    }


}