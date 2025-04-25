package com.bikerental;
import com.bikerental.models.*;
import com.bikerental.repositories.*;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableScheduling
public class Main {

    @Transactional
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(Main.class, args);

        try {
            ManufacturerRepository manufacturerRepo = context.getBean(ManufacturerRepository.class);
            BicycleRepository bicycleRepo = context.getBean(BicycleRepository.class);
            RentalPointRepository rentalPointRepo = context.getBean(RentalPointRepository.class);
            ClientRepository clientRepo = context.getBean(ClientRepository.class);
            RentalRepository rentalRepo = context.getBean(RentalRepository.class);

            // 1. Создаем и сохраняем производителей
            Manufacturer trek = new Manufacturer("Trek", "USA");
            Manufacturer giant = new Manufacturer("Giant", "Taiwan");
            Manufacturer specialized = new Manufacturer("Specialized", "USA");
            Manufacturer cannondale = new Manufacturer("Cannondale", "USA");
            
            trek = manufacturerRepo.save(trek);
            giant = manufacturerRepo.save(giant);
            specialized = manufacturerRepo.save(specialized);
            cannondale = manufacturerRepo.save(cannondale);

            // 2. Создаем и сохраняем пункты проката
            RentalPoint lesnaya = new RentalPoint(
                "Метро Лесная",
                "Лесной проспект",
                "100",
                "+7-212-555-0101"
            );
            
            RentalPoint riverside = new RentalPoint(
                "Петроградская набережная",
                "Петроградская набережная",
                "475",
                "+7-212-555-0102"
            );
            
            RentalPoint prospectPark = new RentalPoint(
                "Проспект Просвещения",
                "ул. Художников",
                "95",
                "+7-212-555-0103"
            );
            
            RentalPoint batteryPark = new RentalPoint(
                "Парк Победы",
                "ул. Московский проспект",
                "1",
                "+7-212-555-0104"
            );

            lesnaya = rentalPointRepo.save(lesnaya);
            riverside = rentalPointRepo.save(riverside);
            prospectPark = rentalPointRepo.save(prospectPark);
            batteryPark = rentalPointRepo.save(batteryPark);

            // 3. Создаем и сохраняем велосипеды разных типов
            // Горные велосипеды
            MountainBicycle mountainBike1 = createMountainBike("Marlin", trek, "full", 27.5);
            MountainBicycle mountainBike2 = createMountainBike("Fuel", trek, "front", 29.0);
            MountainBicycle mountainBike3 = createMountainBike("Stumpjumper", specialized, "full", 29.0);
            MountainBicycle mountainBike4 = createMountainBike("Scalpel", cannondale, "full", 27.5);
            
            // Шоссейные велосипеды
            RoadBicycle roadBike1 = createRoadBike("Defy", giant, 25.0, true);
            RoadBicycle roadBike2 = createRoadBike("TCR", giant, 23.0, true);
            RoadBicycle roadBike3 = createRoadBike("Tarmac", specialized, 25.0, true);
            RoadBicycle roadBike4 = createRoadBike("SuperSix", cannondale, 23.0, true);
            
            // Городские велосипеды
            CityBicycle cityBike1 = createCityBike("City", giant, true, true, true);
            CityBicycle cityBike2 = createCityBike("Urban", trek, true, false, true);
            CityBicycle cityBike3 = createCityBike("Sirrus", specialized, true, true, true);
            CityBicycle cityBike4 = createCityBike("Quick", cannondale, true, true, false);

            // Сохраняем все велосипеды
            mountainBike1 = bicycleRepo.save(mountainBike1);
            mountainBike2 = bicycleRepo.save(mountainBike2);
            mountainBike3 = bicycleRepo.save(mountainBike3);
            mountainBike4 = bicycleRepo.save(mountainBike4);
            
            roadBike1 = bicycleRepo.save(roadBike1);
            roadBike2 = bicycleRepo.save(roadBike2);
            roadBike3 = bicycleRepo.save(roadBike3);
            roadBike4 = bicycleRepo.save(roadBike4);
            
            cityBike1 = bicycleRepo.save(cityBike1);
            cityBike2 = bicycleRepo.save(cityBike2);
            cityBike3 = bicycleRepo.save(cityBike3);
            cityBike4 = bicycleRepo.save(cityBike4);

            // Распределяем велосипеды по пунктам проката
            lesnaya.addBicycle(mountainBike1);
            lesnaya.addBicycle(roadBike1);
            lesnaya.addBicycle(cityBike1);
            
            riverside.addBicycle(mountainBike2);
            riverside.addBicycle(roadBike2);
            riverside.addBicycle(cityBike2);
            
            prospectPark.addBicycle(mountainBike3);
            prospectPark.addBicycle(roadBike3);
            prospectPark.addBicycle(cityBike3);
            
            batteryPark.addBicycle(mountainBike4);
            batteryPark.addBicycle(roadBike4);
            batteryPark.addBicycle(cityBike4);

            rentalPointRepo.save(lesnaya);
            rentalPointRepo.save(riverside);
            rentalPointRepo.save(prospectPark);
            rentalPointRepo.save(batteryPark);

            // 4. Создаем и сохраняем клиентов
            final Client client1 = new Client("John Smith", "john@example.com", "+1-555-123-4567");
            final Client client2 = new Client("Mary Johnson", "mary@example.com", "+1-555-765-4321");
            final Client client3 = new Client("David Brown", "david@example.com", "+1-555-987-6543");
            final Client client4 = new Client("Sarah Wilson", "sarah@example.com", "+1-555-456-7890");

            clientRepo.save(client1);
            clientRepo.save(client2);
            clientRepo.save(client3);
            clientRepo.save(client4);

            // 5. Демонстрация аренды и возврата велосипедов
            System.out.println("\n=== Начальное состояние ===");
            printRentalStatus(lesnaya, riverside, prospectPark, batteryPark);

            // Клиенты арендуют велосипеды
            client1.rentBicycle(mountainBike1, LocalDateTime.now().plusDays(1));
            client2.rentBicycle(roadBike2, LocalDateTime.now().plusHours(4));
            client3.rentBicycle(cityBike3, LocalDateTime.now().plusDays(2));
            client4.rentBicycle(mountainBike4, LocalDateTime.now().plusHours(6));

            // Сохраняем клиентов с их арендами
            clientRepo.save(client1);
            clientRepo.save(client2);
            clientRepo.save(client3);
            clientRepo.save(client4);

            // Сохраняем аренды
            client1.getRentalHistory().forEach(rental -> {
                rental.setClient(client1);
                rentalRepo.save(rental);
            });
            client2.getRentalHistory().forEach(rental -> {
                rental.setClient(client2);
                rentalRepo.save(rental);
            });
            client3.getRentalHistory().forEach(rental -> {
                rental.setClient(client3);
                rentalRepo.save(rental);
            });
            client4.getRentalHistory().forEach(rental -> {
                rental.setClient(client4);
                rentalRepo.save(rental);
            });

            System.out.println("\n=== После аренды ===");
            printRentalStatus(lesnaya, riverside, prospectPark, batteryPark);

            // Некоторые клиенты возвращают велосипеды
            client1.returnBicycle(mountainBike1);
            client3.returnBicycle(cityBike3);

            // Сохраняем клиентов после возврата
            clientRepo.save(client1);
            clientRepo.save(client3);

            // Сохраняем обновленные аренды
            client1.getRentalHistory().forEach(rental -> {
                rental.setClient(client1);
                rentalRepo.save(rental);
            });
            client3.getRentalHistory().forEach(rental -> {
                rental.setClient(client3);
                rentalRepo.save(rental);
            });

            System.out.println("\n=== После возврата ===");
            printRentalStatus(lesnaya, riverside, prospectPark, batteryPark);

        } finally {
            //context.close();
        }
    }

    private static MountainBicycle createMountainBike(String model, Manufacturer manufacturer,
                                                    String suspensionType, double wheelSize) {
        CharacteristicsOfBicycles chars = new CharacteristicsOfBicycles();
        chars.setFrameMaterial("Алюминий");
        chars.setWeight(12.5);
        chars.setNumberOfSpeeds(21);
        return new MountainBicycle(model, manufacturer, chars, suspensionType, wheelSize);
    }

    private static RoadBicycle createRoadBike(String model, Manufacturer manufacturer,
                                            double tireWidth, boolean isAero) {
        CharacteristicsOfBicycles chars = new CharacteristicsOfBicycles();
        chars.setFrameMaterial("Карбон");
        chars.setWeight(8.5);
        chars.setNumberOfSpeeds(22);
        return new RoadBicycle(model, manufacturer, chars, tireWidth, isAero);
    }

    private static CityBicycle createCityBike(String model, Manufacturer manufacturer,
                                            boolean hasFenders, boolean hasBelt, boolean hasBasket) {
        CharacteristicsOfBicycles chars = new CharacteristicsOfBicycles();
        chars.setFrameMaterial("Сталь");
        chars.setWeight(15.0);
        chars.setNumberOfSpeeds(7);
        return new CityBicycle(model, manufacturer, chars, hasFenders, hasBelt, hasBasket);
    }

    private static void printRentalStatus(RentalPoint... points) {
        for (RentalPoint point : points) {
            System.out.println("\nПункт проката: " + point.getLocation());
            System.out.println("Доступные велосипеды: " + point.getBicycles().size());
            point.getBicycles().forEach(bike -> 
                System.out.println("- " + bike.getModel() + " (" + bike.getBikeType() + ")")
            );
        }
    }
}

