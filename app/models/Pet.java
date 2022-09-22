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

    @Column
    private String productId;

    @Column
    private String color;

    public Pet(String name, PetType petType, String description, BigDecimal price, String productId, String color) {
        this.name = name;
        this.petType = petType;
        this.description = description;
        this.price = price;
        this.productId = productId;
        this.color = color;
    }

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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
