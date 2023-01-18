package entities;

import dtos.DogDto;
import dtos.OwnerDto;
import dtos.WalkerDto;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "dogs")
@NamedQuery(name = "Dog.deleteAllRows", query = "DELETE from Dog")
public class Dog implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dog_id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "breed")
    private String breed;
    @NotNull
    @Column(name = "image")
    private String image;
    @NotNull
    @Column(name = "gender")
    private String gender;
    @NotNull
    @Column(name = "birthdate")
    private String birthdate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @ManyToMany(mappedBy = "dogs")
    private Set<Walker> walkers;

    public Dog() {
    }

    public Dog(String name, String breed, String image, String gender, String birthdate, Owner owner, Set<Walker> walkers) {
        this.name = name;
        this.breed = breed;
        this.image = image;
        this.gender = gender;
        this.birthdate = birthdate;
        this.owner = owner;
        this.walkers = walkers;
    }

    public Dog(DogDto dogDto){
        this.id = dogDto.getId();
        this.name = dogDto.getName();
        this.breed = dogDto.getBreed();
        this.image = dogDto.getImage();
        this.gender = dogDto.getGender();
        this.birthdate = dogDto.getBirthdate();
        this.owner = new Owner(dogDto.getOwner());
        this.walkers = new HashSet<>();
        for (WalkerDto walkerDto : dogDto.getWalkers()) {
            this.walkers.add(new Walker(walkerDto));
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Set<Walker> getWalkers() {
        return walkers;
    }

    public void setWalkers(Set<Walker> walkers) {
        this.walkers = walkers;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dog dog = (Dog) o;
        return Objects.equals(name, dog.name) && Objects.equals(breed, dog.breed) && Objects.equals(image, dog.image) && Objects.equals(gender, dog.gender) && Objects.equals(birthdate, dog.birthdate) && Objects.equals(owner, dog.owner) && Objects.equals(walkers, dog.walkers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, breed, image, gender, birthdate, owner, walkers);
    }

    @Override
    public String toString() {
        return "Dog{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", breed='" + breed + '\'' +
                ", image='" + image + '\'' +
                ", gender='" + gender + '\'' +
                ", birthdate='" + birthdate + '\'' +
                ", owner=" + owner +
                ", walkers=" + walkers +
                '}';
    }
}
