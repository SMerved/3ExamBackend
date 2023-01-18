package dtos;

import entities.Dog;
import entities.Walker;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DogDto implements Serializable {
    private final Long id;
    @NotNull
    private final String name;
    @NotNull
    private final String breed;
    @NotNull
    private final String image;
    @NotNull
    private final String gender;
    @NotNull
    private final String birthdate;
    private final OwnerDto owner;
    private final Set<WalkerDto> walkers;

    public DogDto(Long id, String name, String breed, String image, String gender, String birthdate, OwnerDto owner, Set<WalkerDto> walkers) {
        this.id = id;
        this.name = name;
        this.breed = breed;
        this.image = image;
        this.gender = gender;
        this.birthdate = birthdate;
        this.owner = owner;
        this.walkers = walkers;
    }

    public DogDto(Dog dog){
        this.id = dog.getId();
        this.name = dog.getName();
        this.breed = dog.getBreed();
        this.image = dog.getImage();
        this.gender = dog.getGender();
        this.birthdate = dog.getBirthdate();
        this.owner = new OwnerDto(dog.getOwner());
        this.walkers = new HashSet<>();
        for (Walker walker : dog.getWalkers()) {
            this.walkers.add(new WalkerDto(walker));
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getImage() {
        return image;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public OwnerDto getOwner() {
        return owner;
    }

    public Set<WalkerDto> getWalkers() {
        return walkers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DogDto entity = (DogDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.breed, entity.breed) &&
                Objects.equals(this.image, entity.image) &&
                Objects.equals(this.gender, entity.gender) &&
                Objects.equals(this.birthdate, entity.birthdate) &&
                Objects.equals(this.owner, entity.owner) &&
                Objects.equals(this.walkers, entity.walkers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, breed, image, gender, birthdate, owner, walkers);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "breed = " + breed + ", " +
                "image = " + image + ", " +
                "gender = " + gender + ", " +
                "birthdate = " + birthdate + ", " +
                "owner = " + owner + ", " +
                "walkers = " + walkers + ")";
    }
}
