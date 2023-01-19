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
import java.util.Objects;

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

    public List<DogDto> getAllDogs() {
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
            if (dog.getOwner().getId() != null) {
                Owner owner = em.find(Owner.class, dog.getOwner().getId());
                dog.setOwner(owner);
                owner.getDogs().add(dog);
            } else {
                dog.getOwner().getDogs().add(dog);
            }
            em.persist(dog);
            for (Walker w : dog.getWalkers()) {
                if (w.getId() != null) {
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

    public DogDto upDog(DogDto dogDto) {
        EntityManager em = emf.createEntityManager();
        Dog dog = new Dog(dogDto);
        Dog d;
        try {
            em.getTransaction().begin();
            TypedQuery<Walker> query = (TypedQuery<Walker>) em.createNativeQuery("SELECT * FROM dog_walkers WHERE dog_id = ? ", Walker.class);
            query.setParameter(1, dog.getId());
            List<Walker> walkerList = query.getResultList();
            for (Walker walker : walkerList){
                walker.getDogs().removeIf(dog1 -> Objects.equals(dog1.getId(), dog.getId()));
                em.merge(walker);
            }
            if (dog.getOwner().getId() != null) {
                Owner owner = em.find(Owner.class, dog.getOwner().getId());
                owner.getDogs().removeIf(dog1 -> Objects.equals(dog1.getId(), dog.getId()));
                dog.getOwner().setDogs(owner.getDogs());
            }
            dog.getOwner().getDogs().add(dog);


            for (Walker w : dog.getWalkers()) {
                if (w.getId() != null) {
                    Walker oldWalker = em.find(Walker.class, w.getId());
                    dog.getWalkers().removeIf(walker1 -> Objects.equals(walker1.getId(), oldWalker.getId()));
                    dog.getWalkers().add(oldWalker);
                    w.setDogs(oldWalker.getDogs());
                }
                w.getDogs().add(dog);
                em.merge(w);
            }
            d = em.merge(dog);
            em.merge(dog.getOwner());

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new DogDto(d);
    }

}
