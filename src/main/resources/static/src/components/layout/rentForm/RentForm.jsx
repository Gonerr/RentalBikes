import React, { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import "react-datepicker/dist/react-datepicker.css";
import './RentForm.css';

const RentForm = ({ bicycle, onClose, onSubmit }) => {
    const [formData, setFormData] = useState({
        startDate: null,
        endDate: null,
        rentalPoint: bicycle.rentalPoint.id,
        customerName: '',
        customerPhone: '',
        customerEmail: ''
    });

    const [totalCost, setTotalCost] = useState(0);
    const [activeRentals, setActiveRentals] = useState([]);
    const [error, setError] = useState('');
    const [isSubmitting, setIsSubmitting] = useState(false);

    useEffect(() => {
        // Загрузка активных аренд для этого велосипеда
        fetch(`http://localhost:8080/api/rentals/${bicycle.bicycle_id}`)
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch active rentals');
                return response.json();
            })
            .then(data => {
                // Убеждаемся, что data - это массив
                const rentals = Array.isArray(data) ? data : [data];
                setActiveRentals(rentals);
            })
            .catch(error => {
                console.error('Error loading active rentals:', error);
                setError('Ошибка загрузки данных о доступности');
            });
    }, [bicycle.bicycle_id]);

    useEffect(() => {
        // Расчет стоимости аренды
        if (formData.startDate && formData.endDate) {
            const days = Math.ceil((formData.endDate - formData.startDate) / (1000 * 60 * 60 * 24));
            setTotalCost(days * bicycle.dailyRentalPrice);
        }
    }, [formData.startDate, formData.endDate, bicycle.dailyRentalPrice]);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        setError('');
    };

    const handleDateChange = (dates) => {
        const [start, end] = dates;
        
        // Проверяем, что activeRentals - это массив
        if (!Array.isArray(activeRentals)) {
            setFormData(prev => ({
                ...prev,
                startDate: start,
                endDate: end
            }));
            return;
        }

        // Проверяем, что выбранный период не пересекается с активными арендами
        const isPeriodAvailable = !activeRentals.some(rental => {
            const rentalStart = new Date(rental.startDate);
            const rentalEnd = new Date(rental.endDate);
            return (start <= rentalEnd && end >= rentalStart);
        });

        if (!isPeriodAvailable) {
            setError('Выбранный период недоступен. Велосипед уже забронирован на эти даты.');
            return;
        }

        setFormData(prev => ({
            ...prev,
            startDate: start,
            endDate: end
        }));
        setError('');
    };

    const isDateAvailable = (date) => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        // Проверяем, что дата не в прошлом
        if (date < today) {
            return false;
        }

        // Проверяем, что activeRentals - это массив
        if (!Array.isArray(activeRentals)) {
            return true;
        }

        // Проверяем, что дата не попадает в период активной аренды
        return !activeRentals.some(rental => {
            const startDate = new Date(rental.startDate);
            const endDate = new Date(rental.endDate);
            return date >= startDate && date <= endDate;
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setIsSubmitting(true);
        
        try {
            if (!formData.startDate || !formData.endDate) {
                throw new Error('Пожалуйста, выберите даты аренды');
            }

            if (!formData.customerName || !formData.customerPhone || !formData.customerEmail) {
                throw new Error('Пожалуйста, заполните все поля формы');
            }

            const response = await fetch('http://localhost:8080/api/rentals', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    bicycleId: bicycle.bicycle_id,
                    clientName: formData.customerName,
                    clientEmail: formData.customerEmail,
                    clientPhone: formData.customerPhone,
                    startDate: formData.startDate.toISOString(),
                    endDate: formData.endDate.toISOString()
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка при создании аренды');
            }

            // Если мы дошли до этой точки, значит аренда успешно создана
            const rentalData = await response.json();
            onSubmit({
                ...formData,
                bicycleId: bicycle.bicycle_id,
                totalCost
            });
            onClose(); // Закрываем форму после успешного создания
        } catch (err) {
            console.error('Error creating rental:', err);
            setError(err.message || 'Произошла ошибка при создании аренды. Пожалуйста, попробуйте позже.');
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <div className="rent-form-overlay" onClick={onClose}>
            <div className="rent-form" onClick={e => e.stopPropagation()}>
                <button className="close-button" onClick={onClose}>×</button>
                <h2>Аренда велосипеда {bicycle.model}</h2>
                {error && <div className="error-message"> {error} Кажется, произошла ошибка! Попробуйте позже.</div>}
                <form onSubmit={handleSubmit}>
                    <div className="form-group">
                        <label>Период аренды:</label>
                        <DatePicker
                            selected={formData.startDate}
                            onChange={handleDateChange}
                            startDate={formData.startDate}
                            endDate={formData.endDate}
                            selectsRange
                            inline
                            minDate={new Date()}
                            filterDate={isDateAvailable}
                            monthsShown={2}
                            showDisabledMonthNavigation
                            dateFormat="dd.MM.yyyy"
                            placeholderText="Выберите даты"
                            className="date-picker"
                        />
                    </div>
                    <div className="form-group">
                        <label>Пункт аренды:</label>
                        <input
                            type="text"
                            value={`${bicycle.rentalPoint.location} - ${bicycle.rentalPoint.street}, ${bicycle.rentalPoint.building}`}
                            readOnly
                            className="readonly-input"
                        />
                    </div>
                    <div className="form-group">
                        <label>Имя:</label>
                        <input
                            type="text"
                            name="customerName"
                            value={formData.customerName}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Телефон:</label>
                        <input
                            type="tel"
                            name="customerPhone"
                            value={formData.customerPhone}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="form-group">
                        <label>Email:</label>
                        <input
                            type="email"
                            name="customerEmail"
                            value={formData.customerEmail}
                            onChange={handleChange}
                            required
                        />
                    </div>
                    <div className="total-cost">
                        Стоимость аренды: {totalCost.toFixed(2)} ₽
                    </div>
                    <div className="form-buttons">
                        <button 
                            type="submit" 
                            className="submit-button"
                            disabled={isSubmitting}
                        >
                            {isSubmitting ? 'Оформление...' : 'Арендовать'}
                        </button>
                        <button 
                            type="button" 
                            className="cancel-button" 
                            onClick={onClose}
                            disabled={isSubmitting}
                        >
                            Отмена
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default RentForm;