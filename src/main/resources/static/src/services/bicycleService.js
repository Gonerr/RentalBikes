const API_URL = 'http://localhost:8080/api';

const defaultHeaders = {
    'Content-Type': 'application/json',
    'Accept': 'application/json',
    // Добавьте здесь заголовки аутентификации, если они нужны
    // 'Authorization': 'Bearer your-token-here'
};

export const bicycleService = {
    // Получить все велосипеды
    getAllBicycles: async () => {
        try {
            const response = await fetch(`${API_URL}/bicycles`, {
                method: 'GET',
                headers: defaultHeaders,
                credentials: 'include' // Для работы с куками
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch bicycles: ${response.status} ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching bicycles:', error);
            throw error;
        }
    },

    // Получить велосипед по ID
    getBicycleById: async (id) => {
        try {
            const response = await fetch(`${API_URL}/bicycles/${id}`, {
                method: 'GET',
                headers: defaultHeaders,
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch bicycle: ${response.status} ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching bicycle:', error);
            throw error;
        }
    },

    // Арендовать велосипед
    rentBicycle: async (bicycleId) => {
        try {
            const response = await fetch(`${API_URL}/bicycles/${bicycleId}/rent`, {
                method: 'POST',
                headers: defaultHeaders,
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error(`Failed to rent bicycle: ${response.status} ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error renting bicycle:', error);
            throw error;
        }
    },

    // Вернуть велосипед
    returnBicycle: async (bicycleId) => {
        try {
            const response = await fetch(`${API_URL}/bicycles/${bicycleId}/return`, {
                method: 'POST',
                headers: defaultHeaders,
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error(`Failed to return bicycle: ${response.status} ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error returning bicycle:', error);
            throw error;
        }
    },

    // Получить велосипеды по типу
    getBicyclesByType: async (type) => {
        try {
            const response = await fetch(`${API_URL}/bicycles/type/${type}`, {
                method: 'GET',
                headers: defaultHeaders,
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch bicycles by type: ${response.status} ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching bicycles by type:', error);
            throw error;
        }
    },

    // Получить доступные велосипеды
    getAvailableBicycles: async () => {
        try {
            const response = await fetch(`${API_URL}/bicycles/available`, {
                method: 'GET',
                headers: defaultHeaders,
                credentials: 'include'
            });
            if (!response.ok) {
                throw new Error(`Failed to fetch available bicycles: ${response.status} ${response.statusText}`);
            }
            return await response.json();
        } catch (error) {
            console.error('Error fetching available bicycles:', error);
            throw error;
        }
    }
}; 