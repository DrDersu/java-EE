package javaee.hometask7.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketItem {
    private String name;
    private double price;
    private int amount;
    private Date date;
}
