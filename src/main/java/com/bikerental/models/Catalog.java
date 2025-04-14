package com.bikerental.models;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "catalogs")
@Getter @Setter
public class Catalog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    @JoinColumn(name = "catalog_id") // вместо mappedBy
    private List<Bicycle> bicycles = new ArrayList<>();

    public Catalog() {
    }

    /**
     * Добавляет велосипед в каталог
     * @param bicycle - велосипед для добавления
     */
    public void addBicycle(Bicycle bicycle) {
        if (bicycle != null) {
            bicycles.add(bicycle);
        }
    }

    /**
     * Удаляет велосипед из каталога
     * @param bicycle - велосипед для удаления
     */
    public void removeBicycle(Bicycle bicycle) {
        bicycles.remove(bicycle);
    }

    /**
     * Поиск велосипедов по типу
     * @param type - тип велосипеда для поиска
     * @return список велосипедов указанного типа
     */
    public List<Bicycle> searchBicyclesByType(String type) {
        return bicycles.stream()
                .filter(b -> b.getCharacteristics().getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * Поиск велосипедов по производителю
     * @param manufacturer - производитель для поиска
     * @return список велосипедов указанного производителя
     */
    public List<Bicycle> searchBicyclesByManufacturer(Manufacturer manufacturer) {
        return bicycles.stream()
                .filter(b -> b.getManufacturer().equals(manufacturer))
                .collect(Collectors.toList());
    }

    /**
     * Получает список всех велосипедов в каталоге
     * @return список всех велосипедов
     */
    public List<Bicycle> getAllBicycles() {
        return new ArrayList<>(bicycles);
    }


    /**
     * Получает количество велосипедов в каталоге
     * @return количество велосипедов
     */
    public int getBicyclesCount() {
        return bicycles.size();
    }
}
