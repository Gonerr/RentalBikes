import React from 'react';
import './FilterBar.css';

const FilterBar = ({ filters, onFilterChange }) => {
    const handleChange = (e) => {
        const { name, value } = e.target;
        onFilterChange({
            ...filters,
            [name]: value
        });
    };

    return (
        <div className="filter-bar">
            <div className="filter-group">
                <label>Тип велосипеда:</label>
                <select
                    name="type"
                    value={filters.type}
                    onChange={handleChange}
                >
                    <option value="">Все типы</option>
                    <option value="MOUNTAIN">Горный</option>
                    <option value="ROAD">Шоссейный</option>
                    <option value="CITY">Городской</option>
                </select>
            </div>
            <div className="filter-group">
                <label>Пункт аренды:</label>
                <select
                    name="rentalPoint"
                    value={filters.rentalPoint}
                    onChange={handleChange}
                >
                    <option value="">Все пункты</option>
                    {filters.rentalPoints.map(point => (
                        <option key={point.id} value={point.id}>
                            {point.location} - {point.street}, {point.building}
                        </option>
                    ))}
                </select>
            </div>
            <div className="filter-group">
                <label>Цена (₽/час):</label>
                <div className="price-range">
                    <input
                        type="number"
                        name="minPrice"
                        value={filters.minPrice}
                        onChange={handleChange}
                        placeholder="Мин"
                    />
                    <span>-</span>
                    <input
                        type="number"
                        name="maxPrice"
                        value={filters.maxPrice}
                        onChange={handleChange}
                        placeholder="Макс"
                    />
                </div>
            </div>
            <div className="filter-group">
                <label>Доступность:</label>
                <select
                    name="availability"
                    value={filters.availability}
                    onChange={handleChange}
                >
                    <option value="">Все</option>
                    <option value="available">Доступные сейчас</option>
                    <option value="unavailable">Недоступные сейчас</option>
                </select>
            </div>
        </div>
    );
};

export default FilterBar; 