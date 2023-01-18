package facades;

import dtos.WalkerDto;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WalkerFacadeTest {
    private static EntityManagerFactory emf;
    private static WalkerFacade facade;

    private Walker w1, w2, w3;
    private Dog d1, d2, d3;
    private Owner o1, o2;

    public WalkerFacadeTest() {
    }
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = WalkerFacade.getWalkerFacade(emf);
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
    public void testGetAllWalkers(){
        List<WalkerDto> actual = facade.getAllWalkers();
        assert(actual.contains(new WalkerDto(w1)));
        assert(actual.contains(new WalkerDto(w2)));
        assert(actual.contains(new WalkerDto(w3)));
    }

}