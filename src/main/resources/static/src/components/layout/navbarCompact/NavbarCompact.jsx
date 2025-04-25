import React from 'react';
import { Link } from 'react-router-dom';
import styles from './NavbarCompact.module.css';
import logo from '../../shared/logo.drawio.png';

const NavbarCompact = () => {
    return (
        <nav className={styles.navbar}>
            <Link to="/" className={styles.navLink}>Главная</Link>
            <Link to="/catalog" className={styles.navLink}>Каталог</Link>
            <img className={styles.image} src={logo} alt="logo"/>
            <Link to="/about" className={styles.navLink}>О нас</Link>
            <Link to="/contacts" className={styles.navLink}>Контакты</Link>
        </nav>
    );
};

export default NavbarCompact;