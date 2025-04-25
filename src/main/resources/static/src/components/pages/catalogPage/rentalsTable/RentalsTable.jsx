import React, { useState, useEffect } from 'react';
import './RentalsTable.css';

const RentalsTable = ({ onClose }) => {
    const [rentals, setRentals] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        fetch('http://localhost:8080/api/rentals')
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch rentals');
                return response.json();
            })
            .then(data => {
                // Берем только последние 10 записей
                const lastTenRentals = data.slice(-10).reverse();
                setRentals(lastTenRentals);
            })
            .catch(error => {
                console.error('Error loading rentals:', error);
                setError('Ошибка загрузки данных о бронированиях');
            })
            .finally(() => setLoading(false));
    }, []);

    if (loading) {
        return <div className="rentals-table-loading">Загрузка...</div>;
    }

    if (error) {
        return <div className="rentals-table-error">{error}</div>;
    }

    return (
        <div className="rentals-table-overlay" onClick={onClose}>
            <div className="rentals-table" onClick={e => e.stopPropagation()}>
                <button className="close-button" onClick={onClose}>×</button>
                <h2>Последние бронирования</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Имя клиента</th>
                            <th>Телефон</th>
                            <th>Дата начала</th>
                            <th>Дата окончания</th>
                            <th>Модель велосипеда</th>
                            <th>Пункт аренды</th>
                            <th>Фактическая дата возврата</th>
                        </tr>
                    </thead>
                    <tbody>
                        {rentals.map(rental => (
                            <tr key={rental.id}>
                                <td>{rental.client.name}</td>
                                <td>{rental.client.phone}</td>
                                <td>{new Date(rental.startDate).toLocaleDateString()}</td>
                                <td>{new Date(rental.endDate).toLocaleDateString()}</td>
                                <td>{rental.bicycle.model}</td>
                                <td>{rental.bicycle.rentalPoint.location}</td>
                                <td>
                                    {rental.actualReturnDate 
                                        ? new Date(rental.actualReturnDate).toLocaleDateString()
                                        : 'Не возвращен'}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};

export default RentalsTable; 