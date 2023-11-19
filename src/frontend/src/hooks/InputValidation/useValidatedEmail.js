import { useEffect, useState } from 'react';
import { emailRegex } from '../../utils/Regex';

export function useValidatedEmail() {
  const [validated, setValidated] = useState(false);
  const [email, setEmail] = useState('');

  useEffect(() => {
    if (emailRegex.test(email)) {
      setValidated(true);
    } else {
      setValidated(false);
    }
  }, [email]);

  return [email, setEmail, validated];
}
