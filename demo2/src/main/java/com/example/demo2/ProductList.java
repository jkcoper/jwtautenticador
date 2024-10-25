package com.example.demo2;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ProductList {
    @JsonProperty("products")
    private List<Producto> products;

    public List<Producto> getProducts() {
        return products;
    }

    public void setProducts(List<Producto> products) {
        this.products = products;
    }

}
