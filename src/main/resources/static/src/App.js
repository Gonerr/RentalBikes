import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import './App.module.css';
import HomePage from './components/pages/homePage/HomePage';
import CatalogPage from './components/pages/catalogPage/CatalogPage';
// import Home from './Home';
// import RoutesPage from './RoutesPage';
// import Gallery from './Gallery';
// import About from './About';
// import Contacts from './Contacts';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<CatalogPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/catalog" element={<CatalogPage />} />
          {/*<Route path="/about" element={<About />} />*/}
          {/*<Route path="/contacts" element={<Contacts />} />*/}
        </Routes>
      </div>
    </Router>
  );
}

export default App;
