# 🪄 Proyecto Hogwarts

Desafío para PMDM sobre Hogwarts

---

## 🧩 Sprint 1

En este primer sprint voy a sentar las bases del proyecto.

### 🔹 Base de datos

Voy a crear la base de datos con las tablas necesarias para los usuarios, sus roles, las casas de Hogwarts y los niveles. También definiré las relaciones entre ellas, sus claves foraneas, etc...

### 🔹 Sombrero seleccionador

Haré un algoritmo que funcione como el sombrero seleccionador. Asignará a cada nuevo usuario una casa según ciertos criterios.

### 🔹 Login

Implementaré un sistema de inicio de sesión para que cada usuario pueda acceder con su nombre y contraseña.

### 🔹 Registro

Implementaré un sistema de registro para que cada usuario pueda registrarse con su usuario y contraseña y se le asigne una casa dependiendo de la respuesta del algoritmo.

### 🔹 Roles de usuario

Crearé diferentes roles dentro del sistema (por ejemplo, estudiante, profesor o administrador), cada uno con sus permisos (siendo Dumbeldore el administrador superior).

### 🔹 Niveles

Cada usuario tendrá un nivel asociado que podrá aumentar con el tiempo o según las actividades que se vayan añadiendo más adelante.

-----------------------

### 🔹 Usuarios registrados en la BD

Dumbledore - dumbledore@hogwarts.com - Dumbledore123

Prueba1 - prueba1@hogwarts.com - Prueba123

Prueba2 - prueba2@hogwarts.com - Prueba123

Prueba3 - prueba3@hogwarts.com - Prueba123

Prueba4 - prueba4@hogwarts.com - Prueba123

-----------------------

### 🔹 Sprint 2

Tenia pensado hacer lo que me faltaba, pero me faltó tiempo y no está todo implementado... 

Rehice el algoritmo y lo puse en Ktor, organizé las rutas, hice todos los cruds que pude pero no me dio tiempo a hacer todo, faltan que se puedan crear pocimas en general, que los profesores puedan dar de alta usuarios en asignaturas, que los usuarios puedan aprender hechizos porque por defecto les aparecen todos, la creacion de un usuario desde Dumbledore, el ranking de casas y algo más que seguramente se me haya pasado por mencionar...

Mi idea sobre el algoritmo del Sombrero Seleccionador ha sido hacerlo más realista como en la serie en base a preguntas, pero realmente no es lo que pedía el enunciado, también controla el umbral de personas máximas que puede haber en una casa y los reparte en base a eso, si hay mas de un número determinado se encarga de asignarlo lo mas justo posible aunque no le toque la casa que el usuario a registrar quería.

Como te comenté en clase mi idea sobre la foto de perfil del usuario era en base a que mostrase una imagen aleatoria de uno de los 4 personajes que están en el array, pero tampoco me dió tiempo a implementarlo como yo quería, el proyecto en si era muy ambicioso, y se podría haber gestionado mucho mejor en grupo...
