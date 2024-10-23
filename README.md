# ShareList-Back-Listas

Este proyecto está realizado para la asignatura Tecnologías y Sistemas Web de la carrera Ingeniería Informática en la ESI, donde esta aplicación permite crear listas una vez registrado y compartirlas con el resto de usuarios, pero con limitaciones si no has pagado, se describe más abajo.

Esta aplicación es responsive en todos los dispositivos, con un diseño bonito y vistoso.

## 🗄 Tabla de Contenido

- [Creadores](#construction_worker-creadores)
- [Módulos](#computer-módulos)
- [Video](#video_camera-video)
- [Requisitos](#%EF%B8%8F-requisitos)
- [Ejecución](#%EF%B8%8F-ejecución)
- [Documentación](#-documentación)
- [Contacto](#-contacto)

## :construction_worker: Creadores

- Alejandro Paniagua Rodríguez
- Andrés González Valera
- José Lara Navarro

## :computer: Módulos

- [Backend usuarios](https://github.com/AlejandroRodriguez1998/ShareList-Back-Usuarios)
- [Backend listas](https://github.com/AlejandroRodriguez1998/ShareList-Back-Listas) <span style="color: red;">&larr; Estas aquí.</span>
- [Frontend](https://github.com/AlejandroRodriguez1998/ShareList-Frontend)

## :video_camera: Video

Aquí tienes un [video]() haciéndote un tour por la aplicación. (Se subirá pronto...)

## ⚙️ Requisitos

- Java
- Spring
- Maven

> [!TIP]
> Nosotros hemos realizado la práctica y programación en el entorno **Eclipse**.

## 🛠️ Ejecución

1. Descargamos el repositorio.
2. Abrimos el entorno que sea compatible con proyectos Maven.
3. Vamos a file -> import -> Maven -> Existing Maven Projects.

## 📚 Documentación

La documentación proporcionada por los profesores y en la que está basada la aplicación es:

Se desea desarrollar una aplicación de lista de la compra compartida. Un posible escenario de uso es el siguiente:
  1. Pepe llega a la URL de la aplicación y crea dos listas de la compra, a las que debe poner un nombre (por ejemplo: “Casa” y “Cumpleaños”). Independientemente del nombre, el backend asigna a cada lista un identificador único. También marca a Pepe como propietario de las listas.
  2. Pepe añade un producto a “Casa” y una cantidad: “Leche”, 10. Luego añade otro: “Café”, 2. A medida que los añade, se van enviando al backend y se guardan en la base de datos. Pepe puede eliminar productos, que se eliminan inmediatamente de la base de datos.
  3. Pepe cierra el navegador.
  4. Pepe abre de nuevo el navegador y va a la URL. Automáticamente se le cargan sus listas de la compra. Pepe selecciona “Cumpleaños” y añade “Cacahuetes”, 1 y “Patatas fritas”, 2.
  5. Pepe selecciona “Compartir lista”. Se genera un mensaje con una URL y lo envía a su amiga Ana.
  6. Ana pincha en el enlace, acepta unirse y le aparece la lista “Cumpleaños”.
  7. Ana añade “Cervezas”, 10. Como Pepe está viendo la lista en ese momento, le aparece inmediatamente “Cervezas”, 10.
  8. Pepe, que está en la tienda, marca que ha comprado 5 cervezas. Inmediatamente, a Ana se le actualiza la cantidad pendiente.
  9. Ana compra otras 5, con lo que a Pepe se le actualiza la cantidad pendiente, que es ahora 0. La cantidad original (10 cervezas) permanece.
  10. Pepe, que es el propietario de las listas, puede eliminarlas completamente, pero Ana no.

La aplicación tiene algunas salvedades:
1. Los usuarios que no están registrados solo pueden crear 2 listas, cada lista puede tener un máximo de 10 productos y solo pueden compartirla con 1 persona.
2. Los usuarios registrados y que hayan pagado pueden crear tantas listas como quieran, añadir todos los productos que deseen y compartirlas con tantas personas como quieran. El pago se realiza con tarjeta, cuesta 3 euros y sirve para 1 año.
3. El propietario de una lista puede eliminar usuarios.

## ☎ Contacto

Cualquier duda o consulta, escríbenos a nuestro correo:

- alejandro.paniagua1@alu.uclm.es
- andres.gonzalez9@alu.uclm.es
- jose.lara3@alu.uclm.es
