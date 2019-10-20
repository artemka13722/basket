package com.example.demo;


import com.example.demo.exception.NotFoundException;
import com.example.demo.models.BasketModel;
import com.example.demo.models.Input;
import com.example.demo.models.ProductModel;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@RestController
@RequestMapping("/basket")
public class ProductController {

    private Map<String, ProductModel> Product = new HashMap<>();
    private Map<String, BasketModel> Basket = new HashMap<>();

    public ProductController() throws FileNotFoundException {
        Scanner in = new Scanner(new File("Product.txt"));
        while (in.hasNextLine()){
            String s = in.nextLine();
            String[] part = s.split(" ");
            Product.put(part[0], new ProductModel(Double.parseDouble(part[1]) , Integer.parseInt(part[2])));
        }
    }

    @GetMapping("/getCart")
    public Map<String, BasketModel> getCart(){
        return Basket;
    }

    @GetMapping("/getProduct")
    public Map<String, ProductModel> getProduct(){
        return Product;
    }

    @GetMapping("/getAllPrice")
    public double getAllPrice(){
        double prices = 0;
        for(Map.Entry<String, BasketModel> item : Basket.entrySet()){
            prices += item.getValue().getAll_price();
        }
        return prices;
    }

    @PostMapping("/post")
    public Map<String, BasketModel> test(@RequestBody Input model){
        ProductModel getProd = Product.get(model.getProductName());
        if(getProd != null){
            if(getProd.getCount() >= model.getCount()){
                if(Basket.get(model.getProductName()) == null){
                    Basket.put(model.getProductName(), new BasketModel(getProd.getPrice(), model.getCount(),
                            getProd.getPrice() * model.getCount()));
                    getProd.setCount(getProd.getCount()-model.getCount());
                } else {
                    BasketModel basketProd = Basket.get(model.getProductName());
                    basketProd.setCount(basketProd.getCount()+model.getCount());
                    basketProd.setAll_price(basketProd.getCount() * basketProd.getPrice());
                    getProd.setCount(getProd.getCount()-model.getCount());
                }
            } else throw new NotFoundException("Error product count");
        } else throw new NotFoundException("No found product");
        return Basket;
    }
}
