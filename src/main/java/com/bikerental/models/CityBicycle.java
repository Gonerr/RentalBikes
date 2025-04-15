package com.bikerental.models;

import jakarta.persistence.*;

@Entity

@DiscriminatorValue("CITY")
@Table(name = "city_bicycles")
@PrimaryKeyJoinColumn(name = "bicycle_id")
public class CityBicycle extends Bicycle {

    @Column(name = "has_basket", nullable = false)
    private boolean hasBasket;     // Наличие корзины

    @Column(name = "has_fenders", nullable = false)
    private boolean hasFenders;     // Наличие крыльев

    @Column(name = "has_belt", nullable = false)
    private boolean hasBelt;        // Наличие звонка

    public CityBicycle() {
        this.hasBasket = false;
        this.hasFenders = false;
        this.hasBelt = false;
    }

    public CityBicycle(String model, Manufacturer manufacturer,
                       CharacteristicsOfBicycles characteristics,
                       boolean hasFenders, boolean hasBelt, boolean hasBasket) {
        super(model, manufacturer, characteristics);
        this.hasFenders = hasFenders;
        this.hasBelt = hasBelt;
        this.hasBasket = hasBasket;
    }

    // Специфичные методы для городского велосипеда
    public boolean hasBasket() { return hasBasket;}
    public void setHasBasket(boolean hasBasket) { this.hasBasket = hasBasket;}

    public boolean hasFenders() {return hasFenders;}
    public void setHasFenders(boolean hasFenders) {this.hasFenders = hasFenders;}

    public boolean hasBelt() {return hasBelt;}
    public void setHasBelt(boolean hasBelt) {this.hasBelt = hasBelt;}

    @Override
    public double getRemainingPiece() {
        // Базовая стоимость велосипеда
        double basePrice = 15000.0;

        // Наценки за дополнительные детали
        if (hasBasket) {
            basePrice += 1000.0;
        }
        if (hasFenders) {
            basePrice += 800.0;
        }
        if (hasBelt) {
            basePrice += 300.0;
        }

        // Коэффициент страны производителя
        double countryMultiplier = Manufacturer.getCountryMultiplier(getManufacturer().getCountry());

        return basePrice * countryMultiplier;
    }

}
