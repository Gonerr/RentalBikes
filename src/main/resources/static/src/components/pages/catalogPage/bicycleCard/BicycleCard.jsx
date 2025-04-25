import React, { useState, useEffect } from 'react';
import './BicycleCard.css';

const BicycleCard = ({ bicycle, onRent }) => {
    const [isAvailable, setIsAvailable] = useState(true);
    const {
        bicycle_id,
        bikeType,
        manufacturer,
        characteristics,
        model,
        // Горный велосипед
        suspensionType,
        wheelSize,
        // Городской велосипед
        hasBasket,
        hasFenders,
        hasBelt,
        // Шоссейный велосипед
        tireWidth,
        isAero,
        // Общие характеристики
        dailyRentalPrice
    } = bicycle;

    useEffect(() => {
        // Проверяем доступность велосипеда
        fetch('http://localhost:8080/api/rentals')
            .then(response => response.json())
            .then(rentals => {
                const hasActiveRental = rentals.some(rental => 
                    rental.bicycle.bicycle_id === bicycle_id && 
                    rental.actualReturnDate === null
                );
                setIsAvailable(!hasActiveRental);
            })
            .catch(error => console.error('Error checking availability:', error));
    }, [bicycle_id]);

    // Функция для преобразования типа велосипеда в читаемый формат
    const getBikeTypeLabel = (type) => {
        switch (type) {
            case 'MOUNTAIN':
                return 'Горный';
            case 'CITY':
                return 'Городской';
            case 'ROAD':
                return 'Шоссейный';
            default:
                return type;
        }
    };

    const renderCharacteristics = () => {
        switch (bikeType) {
            case 'MOUNTAIN':
                return (
                    <>
                        <p>Тип подвески: {suspensionType}</p>
                        <p>Размер колес: {wheelSize}&quot;</p>
                        <p>Количество передач: {characteristics.numberOfSpeeds}</p>
                        <p>Вес: {characteristics.weight} кг</p>
                        <p>Материал рамы: {characteristics.frameMaterial}</p>
                    </>
                );
            case 'CITY':
                return (
                    <>
                        <p>Количество передач: {characteristics.numberOfSpeeds}</p>
                        <p>Вес: {characteristics.weight} кг</p>
                        <p>Материал рамы: {characteristics.frameMaterial}</p>
                        <p>Корзина: {hasBasket ? 'Да' : 'Нет'}</p>
                        <p>Крылья: {hasFenders ? 'Да' : 'Нет'}</p>
                        <p>Звонок: {hasBelt ? 'Да' : 'Нет'}</p>
                    </>
                );
            case 'ROAD':
                return (
                    <>
                        <p>Количество передач: {characteristics.numberOfSpeeds}</p>
                        <p>Вес: {characteristics.weight} кг</p>
                        <p>Материал рамы: {characteristics.frameMaterial}</p>
                        <p>Ширина шин: {tireWidth} мм</p>
                        <p>Аэродинамика: {isAero ? 'Да' : 'Нет'}</p>
                    </>
                );
            default:
                return null;
        }
    };

    return (
        <div className="bicycle-card">
            <div className="bike-type-label">{getBikeTypeLabel(bikeType)}</div>
            <div className="bicycle-info">
                <h3>{manufacturer.name} {model}</h3>
                <div className="characteristics">
                    {renderCharacteristics()}
                </div>
               
                <p className="manufacturer"> Производитель: {manufacturer.name} ({manufacturer.country}) </p>
                <p className="price">{dailyRentalPrice.toFixed(2)} ₽/день</p>
             
                <button
                    className={`rent-button ${!isAvailable ? 'disabled' : ''}`}
                    onClick={() => isAvailable && onRent(bicycle)}
                    disabled={!isAvailable}
                >
                    {isAvailable ? 'Арендовать' : 'Недоступен'}
                </button>
            </div>
        </div>
    );
};

export default BicycleCard;