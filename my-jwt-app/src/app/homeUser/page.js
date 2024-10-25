"use client";
import { useEffect, useState } from "react";

export default function HomeUser() {
  const [username, setUsername] = useState('');
  const [welcomeMessage, setWelcomeMessage] = useState("");

  useEffect(() => {
    const fetchUsername = async () => {
      const response = await fetch("http://localhost:8081/api/home", {
        method: "GET",
        credentials: "include",  // Ensure cookies are sent
      });
    
      if (response.ok) {
        const data = await response.json();
        setWelcomeMessage(`${data.message}, ${data.username}`);
        setUsername(data.username);
      } else {
        console.error("Error fetching username");
      }
    };

    
    
    

    fetchUsername();
  }, []);

  const handleRedirectToProducts = () => {
    // Redirige a la nueva página de productos
    /* window.location.href = 'http://localhost:3030'; */
    window.location.href = '/productos/';
};

  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
      <main className="flex flex-col gap-8 row-start-2 items-center sm:items-start">
        <h1>{welcomeMessage}</h1>
        <h1>Bienvenido, {username}</h1>
        <button onClick={handleRedirectToProducts}>Ver Productos</button>
      </main>
    </div>
  );
}
