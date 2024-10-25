"use client"; // Esto es necesario para habilitar el modo de cliente

import { useEffect } from 'react';
import { useRouter } from 'next/navigation';



export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // Redirigir a la página de inicio de sesión
    console.log("Redirigiendo a /login...");
    router.push('/login');
  }, [router]);

  return (
    <div className="grid grid-rows-[20px_1fr_20px] items-center justify-items-center min-h-screen p-8 pb-20 gap-16 sm:p-20 font-[family-name:var(--font-geist-sans)]">
      <h1>Información de la página de inicio</h1>
    </div>
  );
}
