"use client";
import { useEffect, useState } from "react";

export default function Productos() {
  const [productos, setProductos] = useState([]);
  const [message, setMessage] = useState("Cargando productos...");

  useEffect(() => {
    const fetchProductos = async () => {
      try {
        const response = await fetch("http://localhost:8090/api/productos", {
          method: "GET",
          credentials: "include", // Esto enviará las cookies, incluyendo la cookie httpOnly JWT
        });

        if (response.ok) {
          const data = await response.json();
          setProductos(data.products);
          setMessage("Productos cargados correctamente.");
        } else {
          setMessage("No se pudieron cargar los productos.");
        }
      } catch (error) {
        console.error("Error al obtener productos:", error);
        setMessage("Error al obtener productos.");
      }
    };

    fetchProductos();
  }, []);

  return (
    <div className="min-h-screen p-8">
      <h1 className="text-2xl font-bold mb-4">Productos Asociados al Usuario con microservicios</h1>
      <p>{message}</p>
      <ul className="list-disc">
        {productos.length > 0 ? (
          productos.map((producto) => (
            <li key={producto.id_producto}>
              <strong>Nombre:</strong> {producto.nombre} <br />
              <strong>ID Producto:</strong> {producto.id_producto}
            </li>
          ))
        ) : (
          <p>No hay productos disponibles.</p>
        )}
      </ul>
    </div>
  );
}
