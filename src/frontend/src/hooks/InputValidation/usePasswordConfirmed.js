import { useEffect, useState } from 'react';

export function usePasswordConfirmed(password, confirmedPassword) {
  const [passwordConfirmed, setPasswordConfirmed] = useState(false);

  useEffect(() => {
    if (password === confirmedPassword) {
      setPasswordConfirmed(true);
    } else {
      setPasswordConfirmed(false);
    }
  }, [password, confirmedPassword]);

  return passwordConfirmed;
}
