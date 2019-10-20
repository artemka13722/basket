package com.example.demo;


import com.example.demo.models.ProductModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("По окончанию ввода введите 'Чек'");

        Map<String, ProductModel> Basket = new HashMap<>();
        Map<String, ProductModel> Product = new HashMap<>();

        Scanner in = new Scanner(new File("Product.txt"));
        while (in.hasNextLine()){
            String s = in.nextLine();
            String[] part = s.split(" ");
            Product.put(part[0], new ProductModel(Double.parseDouble(part[1]) , Integer.parseInt(part[2])));
        }

        Scanner sc = new Scanner (System.in);

        while(sc.hasNext()) {
            String s = sc.nextLine();
            if(s.equals("Чек")) {
                double prices = 0;
                System.out.println("Назваине Цена Количество Сумма");
                for(Map.Entry<String, ProductModel> item : Basket.entrySet()){
                    System.out.printf("%s %s %s %s \n", item.getKey(), item.getValue().getPrice(),
                            item.getValue().getCount(), item.getValue().getPrice() * item.getValue().getCount());
                   prices += item.getValue().getPrice() * item.getValue().getCount();
                }
                System.out.println("Итого: " + prices);
                break;
            }
            String[] parts = s.split(" ");
            if(parts.length == 2){
                String productName = parts[0];
                Integer count = Integer.parseInt(parts[1]);

                ProductModel getProd = Product.get(productName);
                if(getProd != null){
                    if(getProd.getCount() >= count){
                        if(Basket.get(productName) == null){
                            Basket.put(productName, new ProductModel(getProd.getPrice(), count));
                            getProd.setCount(getProd.getCount()-count);
                        } else {
                            ProductModel basketProd = Basket.get(productName);
                            basketProd.setCount(basketProd.getCount()+count);
                            getProd.setCount(getProd.getCount()-count);
                        }
                    }else System.out.println("Error count");
                } else System.out.println("Error product");
            } else System.out.println("Error args");
        }
    }
}