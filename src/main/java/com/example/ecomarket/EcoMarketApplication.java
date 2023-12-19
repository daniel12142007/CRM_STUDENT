package com.example.ecomarket;

import com.example.ecomarket.dto.request.RegisterUserRequest;
import com.example.ecomarket.model.Category;
import com.example.ecomarket.model.Product;
import com.example.ecomarket.repository.CategoryRepository;
import com.example.ecomarket.repository.ProductRepository;
import com.example.ecomarket.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class EcoMarketApplication {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AuthService authService;

    public static void main(String[] args) {
        SpringApplication.run(EcoMarketApplication.class, args);
    }

    @PostConstruct
    public void init() {
        RegisterUserRequest request = new RegisterUserRequest();
        request.setEmail("string@gmail.com");
        request.setPassword("daniel000daniel");
        request.setFullName("Daniel Ahatzhanov");
        log.error("token{}", authService.register(request));
        Category fruits = method("Фрукты");
        Category driedFruits = method("Сухофрукты");
        Category vegetables = method("Овощи");
        Category greenery = method("Зелень");
        Category teaCoffee = method("Чай кофе");
        Category dairy = method("Молочные продукты");

        Product apple = product(
                fruits,
                "Яблоко красная радуга сладкая",
                56,
                "Cочный плод яблони, который употребляется в пищу в свежем и запеченном виде, служит сырьём в кулинарии и для приготовления напитков."
        );
        Product orange = product(
                fruits,
                "Апельсины сладкий пакистанский",
                86,
                "Откройте для себя неповторимый вкус Пакистанских сладких апельсинов, представляющих собой идеальное сочетание сочности и сладости! Наши апельсины выращиваются под теплым пакистанским солнцем, что придает им насыщенный вкус и аромат."
        );
        Product dragons = product(
                fruits,
                "Драконий фрукт",
                86,
                "Погрузитесь в удивительный мир экзотики с нашим Драконьим фруктом! Этот волшебный плод, также известный как питайя, приносит не только потрясающий внешний вид, но и уникальный вкус, который покорит ваш вкусовой рецептор."
        );
    }

    private Category method(String title) {
        Category category = new Category();
        category.setTitle(title);
        category.setImage("image");
        return categoryRepository.save(category);
    }

    private Product product(Category category, String title, int price, String description) {
        Product product = new Product();
        product.setCount(0);
        product.setTitle(title);
        product.setPrice(price);
        product.setImage("image");
        product.setCategory(category);
        product.setDescription(description);
        return productRepository.save(product);
    }
}