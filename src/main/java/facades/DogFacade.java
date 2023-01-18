package facades;

import dtos.DogDto;
import entities.Dog;
import entities.Owner;
import entities.Walker;

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

    public DogDto createDog(DogDto dogDto) {
        EntityManager em = emf.createEntityManager();
        System.out.println(dogDto);
        Dog dog = new Dog(dogDto);

        try {
            em.getTransaction().begin();
            if (dog.getOwner().getId()!=null) {
                Owner owner = em.find(Owner.class, dog.getOwner().getId());
                dog.setOwner(owner);
                owner.getDogs().add(dog);
            }
            else {
                dog.getOwner().getDogs().add(dog);
            }
            em.persist(dog);
            for (Walker w : dog.getWalkers()){
                if (w.getId()!=null){
                    w = em.find(Walker.class, w.getId());
                }
                w.getDogs().add(dog);
                em.persist(w);
            }
            em.persist(dog.getOwner());
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return new DogDto(dog);
    }

}
