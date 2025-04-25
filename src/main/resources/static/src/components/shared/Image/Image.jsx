import React from 'react';
import PropTypes from 'prop-types';
import styles from "./Image.module.css";
import Arrow from "../arrow.png";

const Image = ({src, position, setHoveredIndex, index}) => {
    let imageClass = '';

    if (position === 'center') {
        imageClass = styles.hovered;
    } else if (position === 'left') {
        imageClass = styles.shrinkLeft;
    } else if (position === 'right') {
        imageClass = styles.shrinkRight;
    } else {
        imageClass = styles.shrink;
    }

    return (
        <div
            className={`${styles.imageContainer} ${imageClass}`}
            style={{ transform: `translateX(${position === 'left' ? '-100%' : position === 'right' ? '100%' : '0'})` }}
            onMouseEnter={() => setHoveredIndex(index)}
            onMouseLeave={() => setHoveredIndex(null)}
        >
            <img className={`${styles.picture} picture`}
                 src={src}
                 alt={`Slide ${index + 1}`}
            />
            {position === 'center' && (
                <div className={styles.annotation}>
                    <div className={styles.number}>
                        01
                        <img src={Arrow} className={styles.arrow} alt="arrow" />
                    </div>
                    <div className={styles.text_annotation}>
                        <h2 className={styles.Header}>Экотропы</h2>
                        <span className={styles.description}> Какой-то текст-описание к разделу галереи с популярными экотропами нажмите далее для перехода </span>
                    </div>
                </div>
            )}
        </div>
    );
};

Image.propTypes = {
    src: PropTypes.string.isRequired,
    position: PropTypes.oneOf(['left', 'center', 'right']).isRequired,
    setHoveredIndex: PropTypes.func.isRequired,
    index: PropTypes.number.isRequired
};

export default Image;