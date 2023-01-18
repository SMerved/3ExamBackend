package utils;


import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.HashSet;
import java.util.Set;

public class SetupTestUsers {


  public static void main(String[] args) {
    Walker w1, w2, w3;
    Dog d1, d2, d3;
    Owner o1, o2;

    EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
    EntityManager em = emf.createEntityManager();
    
    // IMPORTAAAAAAAAAANT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    // This breaks one of the MOST fundamental security rules in that it ships with default users and passwords
    // CHANGE the three passwords below, before you uncomment and execute the code below
    // Also, either delete this file, when users are created or rename and add to .gitignore
    // Whatever you do DO NOT COMMIT and PUSH with the real passwords

    User user = new User("user", "test");
    User admin = new User("admin", "test");
    User both = new User("user_admin", "test");

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

    if(admin.getUserPass().equals("test")||user.getUserPass().equals("test")||both.getUserPass().equals("test"))
      throw new UnsupportedOperationException("You have not changed the passwords");

    em.getTransaction().begin();
    Role userRole = new Role("user");
    Role adminRole = new Role("admin");
    user.addRole(userRole);
    admin.addRole(adminRole);
    both.addRole(userRole);
    both.addRole(adminRole);
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
    System.out.println("PW: " + user.getUserPass());
    System.out.println("Testing user with OK password: " + user.verifyPassword("test"));
    System.out.println("Testing user with wrong password: " + user.verifyPassword("test1"));
    System.out.println("Created TEST Users");
   
  }

}
