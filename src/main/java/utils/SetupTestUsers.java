package utils;


import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.Set;

public class SetupTestUsers {


  public static void main(String[] args) {
    Walker w1, w2, w3;
    Dog d1, d2, d3, d4, d5, d6;
    Owner o1, o2;

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();

    Set<Walker> walkers1 = new HashSet<>();
    Set<Walker> walkers2 = new HashSet<>();
    Set<Walker> walkers3 = new HashSet<>();
    Set<Walker> walkers4 = new HashSet<>();
    Set<Walker> walkers5 = new HashSet<>();
    Set<Walker> walkers6 = new HashSet<>();

    w1= new Walker("Hans", "Jagtvej 60", "11223344");
    w2= new Walker("Bob", "Jagtvej 60", "11223344");
    w3= new Walker("Jens", "Jagtvej 60", "11223344");

    o1= new Owner("Owner1", "Lyngbyvej 10", "12345678");
    o2= new Owner("Owner2", "Lyngbyvej 10", "12345678");

    d1= new Dog("Lassi", "Golden Retriever", "https://hunderacer.dk/gallery_1600/golden-retriever-ligger-pa-graesset-med-sin-tennisbold-394126.webp", "Male", "10-5-2017", o1, walkers1);
    d2= new Dog("Fido", "Akita", "https://images.pexels.com/photos/1805164/pexels-photo-1805164.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Female", "10-7-2018", o1, walkers2);
    d3= new Dog("Bongo", "Labrador", "https://images.pexels.com/photos/733416/pexels-photo-733416.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Male", "13-1-2013", o2, walkers3);
    d4= new Dog("Buddy", "Golden Retriever", "https://hunderacer.dk/gallery_1600/golden-retriever-ligger-pa-graesset-med-sin-tennisbold-394126.webp", "Female", "12-6-2020", o2, walkers4);
    d5= new Dog("Pepper", "Akita", "https://images.pexels.com/photos/1805164/pexels-photo-1805164.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Female", "23-8-2021", o2, walkers5);
    d6= new Dog("Leo", "Labrador", "https://images.pexels.com/photos/733416/pexels-photo-733416.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1", "Male", "30-4-2014", o2, walkers6);

    w1.getDogs().add(d1);
    w1.getDogs().add(d2);
    w2.getDogs().add(d2);
    w1.getDogs().add(d4);
    w2.getDogs().add(d5);
    w3.getDogs().add(d6);
    w3.getDogs().add(d3);
    d1.getWalkers().add(w1);
    d2.getWalkers().add(w2);
    d2.getWalkers().add(w1);
    d3.getWalkers().add(w3);
    d4.getWalkers().add(w1);
    d5.getWalkers().add(w2);
    d6.getWalkers().add(w3);
    o1.getDogs().add(d1);
    o1.getDogs().add(d2);
    o2.getDogs().add(d3);
    o1.getDogs().add(d4);
    o1.getDogs().add(d5);
    o2.getDogs().add(d6);

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    em.persist(userRole);
    em.persist(adminRole);
    em.persist(d1);
    em.persist(d2);
    em.persist(d3);
    em.persist(d4);
    em.persist(d5);
    em.persist(d6);
    em.persist(w1);
    em.persist(w2);
    em.persist(w3);
    em.persist(o1);
    em.persist(o2);
    em.getTransaction().commit();
   
  }

}
