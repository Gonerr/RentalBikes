import React from 'react';
import nb from './Navbar.module.css';
import { Link } from 'react-router-dom';

const Navbar = () => {
    const pages = [
        { name: "Главная", path: "/" },
        { name: "Маршруты", path: "/routes" },
        { name: "Галерея", path: "/gallery" },
        { name: "О проекте", path: "/about" },
        { name: "Контакты", path: "/contacts" }
    ];

    return (
        <nav className={nb.navbar}>
            {/*Для вывода названий страниц и перехода на страницу при нажатии на них*/}
            {pages.map((page, index) => (
                <Link key={index} to={page.path} className={nb.navLink}>
                    {page.name}
                </Link>
            ))}
        </nav>
    );
};

export default Navbar;