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


public class OwnerFacade {

    private static EntityManagerFactory emf;
    private static OwnerFacade instance;

    private OwnerFacade() {
    }


    public static OwnerFacade getOwnerFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new OwnerFacade();
        }
        return instance;
    }


    public List<OwnerDto> getAllOwners(){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Owner> query = em.createQuery("SELECT o FROM Owner o", Owner.class);
        List<Owner> owners = query.getResultList();


        List<OwnerDto> ownerDtos = new ArrayList<>();
        for (Owner owner : owners) {
            ownerDtos.add(new OwnerDto(owner));
        }

        return ownerDtos;
    }

}
