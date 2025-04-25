import React, { useState, useEffect } from 'react';
import './AddBicycleForm.css';

const AddBicycleForm = ({ onClose, onSubmit }) => {
    const [formData, setFormData] = useState({
        type: '',
        model: '',
        manufacturer: {
            name: '',
            country: ''
        },
        characteristics: {
            frameMaterial: '',
            weight: '',
            numberOfSpeeds: ''
        },
        rentalPoint: '',
        price: '',
        // Горный велосипед
        suspensionType: '',
        wheelSize: '',
        // Городской велосипед
        hasBasket: false,
        hasFenders: false,
        hasBelt: false,
        // Шоссейный велосипед
        tireWidth: '',
        isAero: false
    });

    const [manufacturers, setManufacturers] = useState([]);
    const [rentalPoints, setRentalPoints] = useState([]);
    const [error, setError] = useState('');

    useEffect(() => {
        // Загрузка производителей
        fetch('http://localhost:8080/api/manufacturers')
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch manufacturers');
                return response.json();
            })
            .then(data => setManufacturers(data))
            .catch(error => {
                console.error('Error loading manufacturers:', error);
                setError('Ошибка загрузки производителей');
            });

        // Загрузка пунктов аренды
        fetch('http://localhost:8080/api/rental-points')
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch rental points');
                return response.json();
            })
            .then(data => {
                console.log('Rental points:', data); // Для отладки
                setRentalPoints(data);
            })
            .catch(error => {
                console.error('Error loading rental points:', error);
                setError('Ошибка загрузки пунктов аренды');
            });
    }, []);

    const handleChange = (e) => {
        const { name, value, type, checked } = e.target;
        if (name.includes('.')) {
            const [parent, child] = name.split('.');
            setFormData(prev => ({
                ...prev,
                [parent]: {
                    ...prev[parent],
                    [child]: type === 'checkbox' ? checked : value
                }
            }));
        } else {
            setFormData(prev => ({
                ...prev,
                [name]: type === 'checkbox' ? checked : value
            }));
        }
        setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            // Валидация
            if (!formData.type || !formData.model || !formData.manufacturer.name || 
                !formData.characteristics.frameMaterial || !formData.rentalPoint || !formData.price) {
                throw new Error('Пожалуйста, заполните все обязательные поля');
            }

            // Дополнительная валидация в зависимости от типа велосипеда
            if (formData.type === 'MOUNTAIN' && (!formData.suspensionType || !formData.wheelSize)) {
                throw new Error('Для горного велосипеда необходимо указать тип подвески и размер колес');
            }
            if (formData.type === 'ROAD' && (!formData.tireWidth)) {
                throw new Error('Для шоссейного велосипеда необходимо указать ширину шин');
            }

            // Подготовка данных для отправки
            const requestData = {
                type: formData.type,
                model: formData.model,
                manufacturer: {
                    id: manufacturers.find(m => m.name === formData.manufacturer.name)?.id
                },
                characteristics: {
                    frameMaterial: formData.characteristics.frameMaterial,
                    weight: parseFloat(formData.characteristics.weight),
                    numberOfSpeeds: parseInt(formData.characteristics.numberOfSpeeds)
                },
                rentalPoint: parseInt(formData.rentalPoint),
                price: parseFloat(formData.price),
                // Специфичные поля для каждого типа
                ...(formData.type === 'MOUNTAIN' && {
                    suspensionType: formData.suspensionType,
                    wheelSize: parseFloat(formData.wheelSize)
                }),
                ...(formData.type === 'CITY' && {
                    hasBasket: formData.hasBasket,
                    hasFenders: formData.hasFenders,
                    hasBelt: formData.hasBelt
                }),
                ...(formData.type === 'ROAD' && {
                    tireWidth: parseFloat(formData.tireWidth),
                    isAero: formData.isAero
                })
            };

            onSubmit(requestData);
        } catch (err) {
            setError(err.message);
        }
    };

    const renderTypeSpecificFields = () => {
        switch (formData.type) {
            case 'MOUNTAIN':
                return (
                    <>
                        <div className="form-group">
                            <label>Тип подвески:</label>
                            <select
                                name="suspensionType"
                                value={formData.suspensionType}
                                onChange={handleChange}
                                required
                            >
                                <option value="">Выберите тип подвески</option>
                                <option value="full">Полная</option>
                                <option value="front">Передняя</option>
                                <option value="none">Без подвески</option>
                            </select>
                        </div>
                        <div className="form-group">
                            <label>Размер колес:</label>
                            <input
                                type="number"
                                name="wheelSize"
                                value={formData.wheelSize}
                                onChange={handleChange}
                                step="0.5"
                                min="24"
                                max="29"
                                required
                            />
                        </div>
                    </>
                );
            case 'CITY':
                return (
                    <>
                        <div className="form-group checkbox-group">
                            <label>
                                <input
                                    type="checkbox"
                                    name="hasBasket"
                                    checked={formData.hasBasket}
                                    onChange={handleChange}
                                />
                                Корзина
                            </label>
                            <label>
                                <input
                                    type="checkbox"
                                    name="hasFenders"
                                    checked={formData.hasFenders}
                                    onChange={handleChange}
                                />
                                Крылья
                            </label>
                            <label>
                                <input
                                    type="checkbox"
                                    name="hasBelt"
                                    checked={formData.hasBelt}
                                    onChange={handleChange}
                                />
                                Звонок
                            </label>
                        </div>
                    </>
                );
            case 'ROAD':
                return (
                    <>
                        <div className="form-group">
                            <label>Ширина шин (мм):</label>
                            <input
                                type="number"
                                name="tireWidth"
                                value={formData.tireWidth}
                                onChange={handleChange}
                                min="18"
                                max="32"
                                required
                            />
                        </div>
                        <div className="form-group checkbox-group">
                            <label>
                                <input
                                    type="checkbox"
                                    name="isAero"
                                    checked={formData.isAero}
                                    onChange={handleChange}
                                />
                                Аэродинамическая рама
                            </label>
                        </div>
                    </>
                );
            default:
                return null;
        }
    };

    return (
        <div className="add-form-overlay" onClick={onClose}>
            <div className="add-form" onClick={e => e.stopPropagation()}>
                <button className="close-button" onClick={onClose}>×</button>
                <h2>Добавить велосипед</h2>
                {error && <div className="error-message">{error}</div>}
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Тип велосипеда:</label>
                        <select
                            name="type"
                            value={formData.type}
                            onChange={handleChange}
                            required
                        >
                            <option value="">Выберите тип</option>
                            <option value="MOUNTAIN">Горный</option>
                            <option value="ROAD">Шоссейный</option>
                            <option value="CITY">Городской</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Модель:</label>
                        <input
                            type="text"
                            name="model"
                            value={formData.model}
                            onChange={handleChange}
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Производитель:</label>
                        <select
                            name="manufacturer.name"
                            value={formData.manufacturer.name}
                            onChange={handleChange}
                            required
                        >
                            <option value="">Выберите производителя</option>
                            {manufacturers.map(manufacturer => (
                                <option key={manufacturer.id} value={manufacturer.name}>
                                    {manufacturer.name} ({manufacturer.country})
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Материал рамы:</label>
                        <select
                            name="characteristics.frameMaterial"
                            value={formData.characteristics.frameMaterial}
                            onChange={handleChange}
                            required
                        >
                            <option value="">Выберите материал</option>
                            <option value="Алюминий">Алюминий</option>
                            <option value="Сталь">Сталь</option>
                            <option value="Карбон">Карбон</option>
                            <option value="Титан">Титан</option>
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Вес (кг):</label>
                        <input
                            type="number"
                            name="characteristics.weight"
                            value={formData.characteristics.weight}
                            onChange={handleChange}
                            step="0.1"
                            min="5"
                            max="30"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Количество передач:</label>
                        <input
                            type="number"
                            name="characteristics.numberOfSpeeds"
                            value={formData.characteristics.numberOfSpeeds}
                            onChange={handleChange}
                            min="1"
                            max="33"
                            required
                        />
                    </div>

                    <div className="form-group">
                        <label>Пункт аренды:</label>
                        <select
                            name="rentalPoint"
                            value={formData.rentalPoint}
                            onChange={handleChange}
                            required
                        >
                            <option value="">Выберите пункт аренды</option>
                            {rentalPoints.map(point => (
                                <option key={point.id} value={point.id}>
                                    {point.location} - {point.street}, {point.building}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="form-group">
                        <label>Цена (₽):</label>
                        <input
                            type="number"
                            name="price"
                            value={formData.price}
                            onChange={handleChange}
                            min="0"
                            step="100"
                            required
                        />
                    </div>

                    {renderTypeSpecificFields()}

                    <div className="form-buttons">
                        <button type="submit" className="submit-button">Добавить</button>
                        <button type="button" className="cancel-button" onClick={onClose}>Отмена</button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default AddBicycleForm; 