package com.bikerental;
import com.bikerental.models.*;
import com.bikerental.repositories.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;

@SpringBootApplication
public class Main {

    @Transactional
    public static void main(String[] args) {
        // Запускаем Spring Boot приложение
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        try {
            ManufacturerRepository manufacturerRepo = context.getBean(ManufacturerRepository.class);
            BicycleRepository bicycleRepo = context.getBean(BicycleRepository.class);
            RentalPointRepository rentalPointRepo = context.getBean(RentalPointRepository.class);
            ClientRepository clientRepo = context.getBean(ClientRepository.class);

            // 1. Создаем и сохраняем производителя
            Manufacturer manufacturer = new Manufacturer("Trek", "USA");
            manufacturer = manufacturerRepo.save(manufacturer);

            // 2. Создаем и сохраняем велосипеды с разными характеристиками
            MountainBicycle mountainBike1 = createMountainBike("Marlin", manufacturer, "Женская", 25.5);
            MountainBicycle mountainBike2 = createMountainBike("Fuel", manufacturer, "Спортивная", 30.0);
            mountainBike1 = bicycleRepo.save(mountainBike1);
            mountainBike2 = bicycleRepo.save(mountainBike2);

            // Создаем и сохраняем пункт проката
            RentalPoint point = new RentalPoint();
            point.setLocation("Central Park");
            point = rentalPointRepo.save(point);

            // Добавляем велосипеды в пункт проката
            point.addBicycle(mountainBike1);
            point.addBicycle(mountainBike2);
            rentalPointRepo.save(point);

            // Создаем и сохраняем клиента
            Client client = new Client();
            client.setName("Петров Иван");
            client.setContactInfo("Ivanov@example.com");
            client = clientRepo.save(client);

            // Аренда велосипеда
            client.rentBicycle(mountainBike1, point, LocalDateTime.now().plusDays(1));
            clientRepo.save(client);

            System.out.println("Client rented bikes: " + client.getRentedBicycles().size());
            System.out.println("Available bikes at point: " + point.getAvailableBicycles().size());

            // Возврат велосипеда
            client.returnBicycle(mountainBike1, point);
            clientRepo.save(client);

            System.out.println("After return - rented bikes: " + client.getRentedBicycles().size());
            System.out.println("Available bikes at point: " + point.getAvailableBicycles().size());

        } finally {
            context.close();
        }
    }
    private static MountainBicycle createMountainBike(String model, Manufacturer manufacturer,
                                                      String suspensionType, double wheelSize) {
        // Создаем новые характеристики для каждого велосипеда
        CharacteristicsOfBicycles chars = new CharacteristicsOfBicycles();
        chars.setType("Mountain");
        chars.setWeight(12.5);
        chars.setGearCount(21);
        chars.setHasLights(false);

        return new MountainBicycle(model, manufacturer, chars, suspensionType, wheelSize);
    }
}

