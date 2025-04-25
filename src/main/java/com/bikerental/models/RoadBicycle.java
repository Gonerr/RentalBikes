package com.bikerental.models;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ROAD")
@Table(name = "road_bicycles")
@PrimaryKeyJoinColumn(name = "bicycle_id")
// Шоссейный велосипед
public class RoadBicycle extends Bicycle {
    private static final double BASE_PRICE = 30000.0;
    @Column(name = "tire_width", nullable = false)
    private double tireWidth;   // Ширина шины

    @Column(name = "is_aero", nullable = false)
    private boolean isAero;     // Наличие аэродинамики

    public RoadBicycle() {
    }

    public RoadBicycle(String model, Manufacturer manufacturer,
                       CharacteristicsOfBicycles characteristics,
                       double tireWidth, boolean isAero) {
        super(model, manufacturer, characteristics);
        this.tireWidth = tireWidth;
        this.isAero = isAero;
        setPrice(getRemainingPiece());
        updateDailyRentalPrice();
    }

    // Геттеры и сеттеры
    public double getTireWidth() {
        return tireWidth;
    }

    public void setTireWidth(double tireWidth) {
        this.tireWidth = tireWidth;
    }

    public boolean isAero() {
        return isAero;
    }

    public void setAero(boolean aero) {
        isAero = aero;
    }

    @Override
    public double getRemainingPiece() {
        double price = BASE_PRICE;

        // Наценка за аэродинамику
        if (isAero) {
            price += 15000.0;
        }

        // Корректировка за ширину шин
        if (tireWidth < 25.0) {
            price += 3000.0; // Ультра-узкие шины премиум класса
        } else if (tireWidth < 28.0) {
            price += 1000.0; // Стандартные гоночные
        }

        // Общий страновой коэффициент
        double countryMultiplier = Manufacturer.getCountryMultiplier(getManufacturer().getCountry());

        return price * countryMultiplier;
    }
}
