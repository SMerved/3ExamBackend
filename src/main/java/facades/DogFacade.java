package facades;

import dtos.DogDto;
import entities.Dog;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class DogFacade {

    private static EntityManagerFactory emf;
    private static DogFacade instance;

    private DogFacade() {
    }


    public static DogFacade getDogFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DogFacade();
        }
        return instance;
    }

    public List<DogDto> getDogsFromOwner(Long id) {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Dog> query = (TypedQuery<Dog>) em.createNativeQuery("SELECT * FROM dogs WHERE owner_id = ? ", Dog.class);
        query.setParameter(1, id);
        List<Dog> dogList = query.getResultList();
        List<DogDto> dogDtos = new ArrayList<>();
        for (Dog dog : dogList) {
            dogDtos.add(new DogDto(dog));
        }
        return dogDtos;
    }

    public List<DogDto> getAllDogs(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Dog> query = em.createQuery("SELECT d FROM Dog d", Dog.class);
        List<Dog> dogs = query.getResultList();


        List<DogDto> dogDtos = new ArrayList<>();
        for (Dog dog : dogs) {
            dogDtos.add(new DogDto(dog));
        }

        return dogDtos;
    }

}
