package models;

import io.ebean.annotation.EnumValue;
import io.ebean.annotation.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name="pet")
public class Pet extends BaseModel {

    public enum PetType {
        @EnumValue("1") Dog,
        @EnumValue("2") Cat,
        @EnumValue("3") Fish,
        @EnumValue("4") Bird,
        @EnumValue("5") Reptile
    }

    private String name;

    @NotNull
    private PetType petType;

    @Column(length=255)
    private String description;

    @Column(scale=2)
    private BigDecimal price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PetType getPetType() {
        return petType;
    }

    public void setPetType(PetType petType) {
        this.petType = petType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
