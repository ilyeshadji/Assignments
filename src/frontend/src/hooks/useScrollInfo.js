import {useCallback, useEffect, useState} from 'react';
import Scrolling from '../enum/Scrolling';

export function useScrollInfo() {
    const [y, setY] = useState(window.scrollY);
    const [direction, setDirection] = useState(null);

    const handleNavigation = useCallback(
        (e) => {
            const window = e.currentTarget;
            if (y > window.scrollY) {
                setDirection(Scrolling.UP);
            } else if (y < window.scrollY) {
                setDirection(Scrolling.DOWN);
            }
            setY(window.scrollY);
        },
        [y]
    );

    useEffect(() => {
        setY(window.scrollY);
        window.addEventListener('scroll', handleNavigation);

        return () => {
            window.removeEventListener('scroll', handleNavigation);
        };
    }, [handleNavigation]);

    return [direction, y];
}
