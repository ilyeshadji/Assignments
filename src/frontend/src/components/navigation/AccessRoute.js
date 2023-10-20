import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function AccessRoute({ children }) {
  const location = useLocation();

  useEffect(() => {
    if (!location.state?.section) {
      window.scrollTo(0, 0);
    }
  }, [location]);

  return <>{children}</>;
}

export default AccessRoute;
