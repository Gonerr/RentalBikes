import React from 'react';
import PropTypes from 'prop-types';
import styles from './BicycleCard.module.css';

const BicycleCard = ({ bicycle, onRent }) => {
    const {
        bicycle_id,
        bikeType,
        manufacturer,
        characteristics,
        model,
        suspensionType,
        wheelSize,
        remainingPiece,
        available,
        // Городской велосипед
        hasBasket,
        hasFenders,
        hasBelt,
        // Шоссейный велосипед
        tireWidth,
        isAero
    } = bicycle;

    const renderSpecificDetails = () => {
        switch (bikeType) {
            case 'MOUNTAIN':
                return (
                    <>
                        <p>Тип подвески: {suspensionType}</p>
                        <p>Размер колес: {wheelSize}&quot;</p>
                        <p>Количество передач: {characteristics.gearCount}</p>
                        <p>Вес: {characteristics.weight} кг</p>
                        <p>Фонари: {characteristics.hasLights ? 'Да' : 'Нет'}</p>
                    </>
                );
            case 'CITY':
                return (
                    <>
                        <p>Размер колес: {wheelSize}&quot;</p>
                        <p>Количество передач: {characteristics.gearCount}</p>
                        <p>Вес: {characteristics.weight} кг</p>
                        <p>Фонари: {characteristics.hasLights ? 'Да' : 'Нет'}</p>
                        <p>Корзина: {hasBasket ? 'Да' : 'Нет'}</p>
                        <p>Крылья: {hasFenders ? 'Да' : 'Нет'}</p>
                        <p>Звонок: {hasBelt ? 'Да' : 'Нет'}</p>
                    </>
                );
            case 'ROAD':
                return (
                    <>
                        <p>Размер колес: {wheelSize}&quot;</p>
                        <p>Количество передач: {characteristics.gearCount}</p>
                        <p>Вес: {characteristics.weight} кг</p>
                        <p>Фонари: {characteristics.hasLights ? 'Да' : 'Нет'}</p>
                        <p>Ширина шин: {tireWidth} мм</p>
                        <p>Аэродинамика: {isAero ? 'Да' : 'Нет'}</p>
                    </>
                );
            default:
                return null;
        }
    };

    const handleRent = () => {
        if (onRent) {
            onRent(bicycle_id);
        }
    };

    return (
        <div className={styles.card}>
            <div className={styles.header}>
                <h3>{manufacturer.name} {model}</h3>
                <span className={styles.type}>{bikeType}</span>
            </div>
            <div className={styles.details}>
                <p className={styles.price}>{remainingPiece} ₽/час</p>
                <p className={styles.availability}>
                    {available ? 'Доступен' : 'Недоступен'}
                </p>
                <p className={styles.manufacturer}>
                    Производитель: {manufacturer.name} ({manufacturer.country})
                </p>
                {renderSpecificDetails()}
            </div>
            <button 
                className={styles.rentButton}
                disabled={!available}
                onClick={handleRent}
            >
                Арендовать
            </button>
        </div>
    );
};

BicycleCard.propTypes = {
    bicycle: PropTypes.shape({
        bicycle_id: PropTypes.number.isRequired,
        bikeType: PropTypes.oneOf(['MOUNTAIN', 'CITY', 'ROAD']).isRequired,
        manufacturer: PropTypes.shape({
            name: PropTypes.string.isRequired,
            country: PropTypes.string.isRequired
        }).isRequired,
        characteristics: PropTypes.shape({
            id: PropTypes.number.isRequired,
            type: PropTypes.string.isRequired,
            weight: PropTypes.number.isRequired,
            gearCount: PropTypes.number.isRequired,
            hasLights: PropTypes.bool.isRequired,
            characteristics: PropTypes.string.isRequired
        }).isRequired,
        model: PropTypes.string.isRequired,
        // Горный велосипед
        suspensionType: PropTypes.string,
        wheelSize: PropTypes.number.isRequired,
        // Городской велосипед
        hasBasket: PropTypes.bool,
        hasFenders: PropTypes.bool,
        hasBelt: PropTypes.bool,
        // Шоссейный велосипед
        tireWidth: PropTypes.number,
        isAero: PropTypes.bool,
        remainingPiece: PropTypes.number.isRequired,
        available: PropTypes.bool.isRequired,
        totalBicycles: PropTypes.number.isRequired
    }).isRequired,
    onRent: PropTypes.func.isRequired
};

export default BicycleCard; 