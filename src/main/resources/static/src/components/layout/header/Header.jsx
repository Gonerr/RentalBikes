import React from 'react';
import h from './Header.module.css';
import logo from '../../shared/logo_black.drawio.png';
import Navbar from "../navbar/Navbar";

const Header = () => {
    return (
        <div className={h.mainContainer}>
            <div className={h.upperContainer}>
                <Navbar/>
                <img className={h.image} src={logo} alt="logo"/>
            </div>
            <div className={"delimeter"}/>
        </div>
    );
};

export default Header;