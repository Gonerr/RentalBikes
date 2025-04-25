import React, { useState, useEffect } from 'react';

import BicycleCard from './bicycleCard/BicycleCard';
import FilterBar from '../../UI/filterBar/FilterBar';
import RentForm from '../../layout/rentForm/RentForm';
import AddBicycleForm from './addBicycleForm/AddBicycleForm';
import RentalsTable from './rentalsTable/RentalsTable';
import styles from './CatalogPage.module.css';

const CatalogPage = () => {
    const [bicycles, setBicycles] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [selectedBicycle, setSelectedBicycle] = useState(null);
    const [showAddForm, setShowAddForm] = useState(false);
    const [showRentalsTable, setShowRentalsTable] = useState(false);
    const [isAdmin, setIsAdmin] = useState(false);
    const [filters, setFilters] = useState({
        type: '',
        rentalPoint: '',
        minPrice: '',
        maxPrice: '',
        availability: '',
        rentalPoints: []
    });

    useEffect(() => {
        // Загрузка пунктов аренды
        fetch('http://localhost:8080/api/rental-points')
            .then(response => {
                if (!response.ok) throw new Error('Failed to fetch rental points');
                return response.json();
            })
            .then(data => setFilters(prev => ({ ...prev, rentalPoints: data })))
            .catch(error => {
                console.error('Error loading rental points:', error);
                setError('Ошибка загрузки пунктов аренды');
            });
    }, []);

    useEffect(() => {
        
        const fetchBicycles = async () => {
            try {
                setLoading(true);
                const queryParams = new URLSearchParams();
                if (filters.type) queryParams.append('type', filters.type);
                if (filters.rentalPoint) queryParams.append('rentalPoint', filters.rentalPoint);
                if (filters.minPrice) queryParams.append('minPrice', filters.minPrice);
                if (filters.maxPrice) queryParams.append('maxPrice', filters.maxPrice);
                if (filters.availability) queryParams.append('availability', filters.availability);

                const response = await fetch(`http://localhost:8080/api/bicycles?${queryParams}`);
                if (!response.ok) throw new Error('Failed to fetch bicycles');
                const data = await response.json();
                setBicycles(data);
                setError(null);
            } catch (err) {
                console.error('Error fetching bicycles:', err);
                setError('Ошибка загрузки велосипедов');
            } finally {
                setLoading(false);
            }
        };

        fetchBicycles();
    }, [filters]);

    const handleRent = (bicycle) => {
        setSelectedBicycle(bicycle);
    };

    const handleRentSubmit = async (rentalData) => {
        try {
            const response = await fetch('http://localhost:8080/api/rentals', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    bicycleId: rentalData.bicycleId,
                    clientName: rentalData.customerName,
                    clientEmail: rentalData.customerEmail,
                    clientPhone: rentalData.customerPhone,
                    startDate: rentalData.startDate,
                    endDate: rentalData.endDate
                }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create rental');
            }
            
            // Обновляем список велосипедов
            const updatedBicycles = await fetch('http://localhost:8080/api/bicycles').then(res => res.json());
            setBicycles(updatedBicycles);
            setSelectedBicycle(null);
            alert('Аренда успешно оформлена!');
        } catch (err) {
            console.error('Error creating rental:', err);
            alert(err.message);
        }
    };

    const handleAddBicycle = async (bicycleData) => {
        try {
            const response = await fetch('http://localhost:8080/api/bicycles', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(bicycleData),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Failed to create bicycle');
            }

            // Обновляем список велосипедов
            const updatedBicycles = await fetch('http://localhost:8080/api/bicycles').then(res => res.json());
            setBicycles(updatedBicycles);
            setShowAddForm(false);
            alert('Велосипед успешно добавлен!');
        } catch (err) {
            console.error('Error creating bicycle:', err);
            setError(err.message);
        }
    };

    const handleAdminLogin = () => {
        const password = prompt('Введите пароль администратора:');
        if (password === 'admin') {
            setIsAdmin(true);
            setShowAddForm(true);
        } else {
            alert('Неверный пароль');
        }
    };

    const handleShowRentals = () => {
        const password = prompt('Введите пароль администратора:');
        if (password === 'admin') {
            setShowRentalsTable(true);
        } else {
            alert('Неверный пароль');
        }
    };

    if (loading) {
        return (
            <div className={styles.catalogPage}>
                <div className={styles.loading}>Загрузка...</div>
            </div>
        );
    }

    return (
        <div className={styles.catalogPage}>
            <div className={styles.catalogContainer}>
                <h1>Каталог велосипедов</h1>
                {error && <div className={styles.error}>{error}</div>}
                <FilterBar filters={filters} onFilterChange={setFilters} />
                <div className={styles.bicyclesGrid}>
                    {bicycles.length === 0 ? (
                        <div className={styles.noBicycles}>Велосипеды не найдены</div>
                    ) : (
                        bicycles.map(bicycle => (
                            <BicycleCard
                                key={bicycle.id}
                                bicycle={bicycle}
                                onRent={() => handleRent(bicycle)}
                            />
                        ))
                    )}
                </div>
                <div className={styles.adminButtons}>
                    
                        <button 
                            className={styles.addButton}
                            onClick={handleAdminLogin}
                        >
                            Добавить велосипед
                        </button>
                    <button 
                        className={styles.showRentalsButton}
                        onClick={handleShowRentals}
                    >
                        Показать бронирования
                    </button>
                </div>
            </div>
            {selectedBicycle && (
                <RentForm
                    bicycle={selectedBicycle}
                    onClose={() => setSelectedBicycle(null)}
                    onSubmit={handleRentSubmit}
                />
            )}
            {showAddForm && (
                <AddBicycleForm
                    onClose={() => {
                        setShowAddForm(false);
                        setIsAdmin(false);
                    }}
                    onSubmit={handleAddBicycle}
                />
            )}
            {showRentalsTable && (
                <RentalsTable
                    onClose={() => setShowRentalsTable(false)}
                />
            )}
        </div>
    );
};

export default CatalogPage; 