import React, { useEffect } from 'react';
import './HomePage.module.css';
import { gsap } from '../../../utils/gsap/gsap.min';
import { ScrollTrigger } from '../../../utils/gsap/ScrollTrigger.min';
import { ScrollSmoother } from '../../../utils/gsap/ScrollSmoother.min';
import Slider from "../../layout/Slider/Slider";
import ParalaxedHero from "./ParalaxedHero/ParalaxedHero";
import InfoSection from "./InfoSection/infoSection";

// Регистрация плагинов
gsap.registerPlugin(ScrollTrigger, ScrollSmoother);

const HomePage = () => {
    useEffect(() => {
        const handleScroll = () => {
            document.body.style.setProperty('--scrollTop', `${window.scrollY}px`);
        };
        window.addEventListener('scroll', handleScroll);

        // Инициализация ScrollSmoother
        const smoother = ScrollSmoother.create({
            wrapper: '#wrapper',
            content: '#content',
            smooth: 1.5,
            effects: true,
        });

        // Настройка ScrollTrigger для каждой секции
        const sections = gsap.utils.toArray('.section');
        sections.forEach((section) => {
            ScrollTrigger.create({
                trigger: section,
                start: 'top top',
                end: 'top top',
                scrub: true,
                snap: {
                    snapTo: 1 / (sections.length - 1),
                    duration: 0.7,
                    ease: 'power1.inOut',
                },
                markers: true,
            });
        });

        return () => {
            smoother.kill();
            ScrollTrigger.getAll().forEach((trigger) => trigger.kill());
            window.removeEventListener('scroll', handleScroll);
        };
    }, []);

    return (
        <div className="homePage">
            <ParalaxedHero />
            <Slider />
            <InfoSection />
        </div>
    );
};

export default HomePage;
