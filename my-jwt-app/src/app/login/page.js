// src/app/login.js
"use client"; // Marca este archivo como un componente de cliente

import { useState } from 'react';
import { useRouter } from 'next/navigation';
import axios from 'axios';

export default function Login() {
  // Estado para almacenar el nombre de usuario, la contraseña y el mensaje, necesarios para manejar la interacción del usuario en el formulario.
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const router = useRouter();
// Función para gestionar el proceso de login, que se activa al enviar el formulario.
  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      // Realiza una solicitud POST a la API de autenticación, con las credenciales ingresadas por el usuario. Incluye cookies con la solicitud, necesario para mantener la sesión del usuario.
      const response = await axios.post('http://localhost:8081/api/login', { username, password }, {
        withCredentials: true, 
      });
      
      if (response.status === 200) {
        router.push('/homeUser');
        setMessage('Login successful');
      }
    } catch (error) {
      setMessage('Login failed: ' + error.response?.data || error.message);
    }
  };

  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Login</button>
      </form>
      <p>{message}</p>
    </div>
  );
}
