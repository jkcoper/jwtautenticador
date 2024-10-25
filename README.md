# Proyecto de Ejercicio: Autenticación con Microservicios

Este proyecto es un ejercicio de autenticación utilizando microservicios que incluye un servicio de bienvenida, un servicio de productos, y un microservicio de manejo de claves RSA.

## Descripción del Proyecto

El proyecto consiste en los siguientes microservicios:

1. **Servicio de Autenticación** (`my-jwt-app`): Maneja el inicio de sesión y la generación de tokens JWT para la autenticación.
2. **Servicio de Productos** (`nombre-del-proyecto`): Proporciona funcionalidad para gestionar productos.
3. **Microservicio de Manejo de Claves RSA** (`key`): Administra las claves RSA utilizadas para la firma de tokens.
4. **Servicios de Demostración** (`demo` y `demo2`): Proveen datos de prueba para los usuarios y productos.

### Usuarios

Para acceder al proyecto, puedes utilizar las siguientes credenciales de usuario:

/* {
    "users": [
        {
            "id_user": "FRT56YH",
            "username": "admin",
            "password": "admin123"
        },
        {
            "id_user": "87FGG55",
            "username": "user",
            "password": "user123"
        }
    ]
} */

### Productos de Ejemplo

Los productos que puedes utilizar para validar el funcionamiento del proyecto son:

{
    "products": [
        {
            "id_producto": "67HJ87",
            "nombre": "Producto A",
            "id_user": "FRT56YH"
        },
        {
            "id_producto": "98JK89",
            "nombre": "Producto B",
            "id_user": "87FGG55"
        }
    ]
}

### Ejecución del Proyecto

Para ejecutar el proyecto, sigue estos pasos:

[^1]: **Clona el repositorio:**

git clone <URL_DE_TU_REPOSITORIO>
cd <NOMBRE_DEL_REPOSITORIO>

[^2]: **Construye y levanta los servicios:**

docker-compose up -d --build


[^3]: **Accede a la aplicación: Abre tu navegador y dirígete a http://localhost para acceder a la interfaz del proyecto.**

> [!NOTE]
>Notas
>Asegúrate de tener Docker y Docker Compose instalados en tu máquina antes de ejecutar el proyecto.
>Puedes detener los servicios en ejecución presionando Ctrl + C en la terminal donde se ejecutó docker-compose up.