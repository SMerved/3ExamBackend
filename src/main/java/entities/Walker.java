package entities;

import dtos.OwnerDto;
import dtos.WalkerDto;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "walkers")
@NamedQuery(name = "Walker.deleteAllRows", query = "DELETE from Walker")
public class Walker implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "walker_id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;
    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @ManyToMany
    @JoinTable(
            name = "dog_walkers",
            joinColumns = @JoinColumn(name = "walker_id"),
            inverseJoinColumns = @JoinColumn(name = "dog_id"))
    private Set<Dog> dogs = new LinkedHashSet<>();

    public Walker() {
    }

    public Walker(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public Walker(String name, String address, String phone, Set<Dog> dogs) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.dogs = dogs;
    }

    public Walker(WalkerDto walkerDto) {
        this.id = walkerDto.getId();
        this.name = walkerDto.getName();
        this.address = walkerDto.getAddress();
        this.phone = walkerDto.getPhone();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<Dog> getDogs() {
        return dogs;
    }

    public void setDogs(Set<Dog> dogs) {
        this.dogs = dogs;
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
        Walker walker = (Walker) o;
        return Objects.equals(name, walker.name) && Objects.equals(address, walker.address) && Objects.equals(phone, walker.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address, phone);
    }

    @Override
    public String toString() {
        return "Walker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
