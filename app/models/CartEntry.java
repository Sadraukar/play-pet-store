package models;

import java.math.BigDecimal;

public class CartEntry {
    private Pet pet;
    private int quantity;

    public CartEntry(Pet pet, int quantity) {
        this.pet = pet;
        this.quantity = quantity;
    }

    public Pet getPet() {
        return pet;
    }

    public void setPet(Pet pet) {
        this.pet = pet;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Calculates the pet price multiplied by the quantity of the pet
     * @return
     */
    public BigDecimal calculateCost() {
        return pet.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
