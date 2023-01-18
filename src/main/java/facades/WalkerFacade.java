package facades;

import dtos.OwnerDto;
import dtos.WalkerDto;
import entities.Owner;
import entities.Walker;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;


public class WalkerFacade {

    private static EntityManagerFactory emf;
    private static WalkerFacade instance;

    private WalkerFacade() {
    }


    public static WalkerFacade getWalkerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new WalkerFacade();
        }
        return instance;
    }


    public List<WalkerDto> getAllWalkers(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Walker> query = em.createQuery("SELECT w FROM Walker w", Walker.class);
        List<Walker> walkers = query.getResultList();


        List<WalkerDto> walkerDtos = new ArrayList<>();
        for (Walker walker : walkers) {
            walkerDtos.add(new WalkerDto(walker));
        }
        System.out.println(walkerDtos);
        return walkerDtos;
    }

}
