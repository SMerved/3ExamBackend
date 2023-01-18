package dtos;

import entities.Walker;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class WalkerDto implements Serializable {
    private final Long id;
    @NotNull
    private final String name;
    @NotNull
    private final String address;
    @NotNull
    private final String phone;

    public WalkerDto(Long id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public  WalkerDto(Walker walker){
        this.id = walker.getId();
        this.name = walker.getName();
        this.address = walker.getAddress();
        this.phone = walker.getPhone();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WalkerDto entity = (WalkerDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.address, entity.address) &&
                Objects.equals(this.phone, entity.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, phone);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "address = " + address + ", " +
                "phone = " + phone + ")";
    }
}
