# Directorio `web/` de JobTrackr

Este directorio contiene la parte frontend del proyecto **JobTrackr**, dividida en dos subproyectos diferenciados: la **landing page evaluable para el módulo de Lenguajes de Marcas** y una **SPA de pruebas para el backend**.

## Estructura general

- `landing/` – Landing page estática evaluable en **Lenguajes de Marcas**.
- `jobtrackr-web/` – Single Page Application (SPA) usada como interfaz de pruebas para el `backend/`.

---

## `landing/` – Parte evaluable (Lenguajes de Marcas)

La carpeta `landing/` contiene la **landing page principal del proyecto**, desarrollada exclusivamente con **HTML, CSS y JavaScript**.  
Es la parte del directorio `web/` que **se evalúa formalmente en el módulo de Lenguajes de Marcas**.

Características principales:

- Maquetación semántica en HTML5.
- Estilos organizados en CSS, siguiendo buenas prácticas de estructura y reutilización.
- Uso de JavaScript vanilla para interactividad básica (navegación, pequeños efectos, etc.).
- Enfoque en accesibilidad y diseño responsive para diferentes tamaños de pantalla.

---

## `jobtrackr-web/` – SPA para probar el backend

La carpeta `jobtrackr-web/` contiene una **Single Page Application (SPA)** creada con **React, React Router, Tailwind CSS y la librería de drag & drop `dnd-kit`**.  
Esta parte **no forma parte de la evaluación de Lenguajes de Marcas**: su objetivo es servir como **interfaz de pruebas** para consumir la API REST implementada en el directorio `backend/`.

Características principales:

- Kanban board para gestionar candidaturas de empleo (columnas y tarjetas arrastrables).
- Navegación de tipo SPA mediante React Router.
- Estilos basados en Tailwind CSS para prototipado rápido.
- Integración con el backend en Java (API REST) para probar operaciones CRUD sobre las candidaturas.

> Nota: Aunque `jobtrackr-web/` no es la parte evaluable en Lenguajes de Marcas, se incluye como interfaz para corroborar el funcionamiento del backend. No es responsive y aun falta muchas funcionalidades, pero es sencilla con el proposito de probar peticiones al backend.