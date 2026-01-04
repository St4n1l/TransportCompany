package org.example;

import lombok.RequiredArgsConstructor;
import org.example.service.MenuService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class Main implements CommandLineRunner {
    private final MenuService menuService;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        menuService.start();
    }
}
