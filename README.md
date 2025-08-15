# HTTP Server - Taller 1 AREP

Un servidor HTTP básico implementado en Java que puede servir archivos estáticos (HTML, CSS, JavaScript, imágenes) y manejar servicios REST simples. Este proyecto demuestra los conceptos fundamentales de redes y protocolos HTTP mediante la implementación de un servidor web desde cero.

## Getting Started

Estas instrucciones te permitirán obtener una copia del proyecto funcionando en tu máquina local para propósitos de desarrollo y pruebas.

### Prerequisites

Para ejecutar este proyecto necesitas tener instalado:

- **Java 21** o superior
- **Apache Maven 3.6** o superior
- **Git** (para clonar el repositorio)

Para verificar si tienes Java instalado:

```
java -version
```

Para verificar si tienes Maven instalado:

```
mvn -version
```

### Installing

Sigue estos pasos para configurar el entorno de desarrollo:

1. **Clona el repositorio**

   ```
   git clone https://github.com/SebastianCardona-P/AREP-TALLER1.git
   cd AREP-TALLER1/httpserver
   ```

2. **Compila el proyecto**

   ```
   mvn clean compile
   ```

3. **Ejecuta el servidor**

   ```
   mvn clean compile exec:java
   ```

4. **Verifica que el servidor está funcionando**

   Abre tu navegador web y visita:

   ```
   http://localhost:35000
   ```

   Deberías ver una página con formularios de ejemplo que demuestran las capacidades del servidor.

## Running the tests

Para ejecutar las pruebas automatizadas del sistema:

```
mvn test
```

### Break down into end to end tests

Las pruebas end-to-end verifican que el servidor puede:

- Servir archivos HTML correctamente
- Entregar recursos estáticos (CSS, JavaScript, imágenes)
- Manejar servicios REST básicos
- Responder con códigos de estado HTTP apropiados

```
mvn test -Dtest=HttpServerIntegrationTest
```

### And coding style tests

Las pruebas de estilo de código verifican que el código sigue las convenciones de Java y está bien documentado:

```
mvn checkstyle:check
```

## Deployment

Para desplegar el servidor en un sistema de producción:

1. **Construye el JAR ejecutable:**

   ```
   mvn package
   ```

2. **Ejecuta el JAR:**

   ```
   java -jar target/httpserver-1.0-SNAPSHOT.jar
   ```

3. **Configura el firewall** para permitir conexiones en el puerto 35000

**Nota:** Para producción, considera cambiar el puerto por defecto editando la constante `PORT` en `HttpServer.java`

## Built With

- **Java 21** - Lenguaje de programación principal
- **Maven** - Gestión de dependencias y construcción del proyecto
- **Socket API** - Para la comunicación de red de bajo nivel
- **Java NIO** - Para manejo eficiente de archivos e I/O

## Funcionalidades

El servidor HTTP implementa las siguientes características:

- ✅ Servir archivos HTML estáticos
- ✅ Servir archivos CSS para estilos
- ✅ Servir archivos JavaScript
- ✅ Servir imágenes (PNG, JPG, ICO)
- ✅ Servicio REST simple (`/app/hello?name=valor`)
- ✅ Manejo de errores 404
- ✅ Soporte para múltiples clientes concurrentes

## Contributing

Si deseas contribuir al proyecto:

1. Haz fork del repositorio
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Versioning

Este proyecto usa [SemVer](http://semver.org/) para el versionado. Para ver las versiones disponibles, revisa los [tags en este repositorio](https://github.com/SebastianCardona-P/AREP-TALLER1/tags).

## Authors

- **Sebastian Cardona Parra** - _Trabajo inicial_ - [SebastianCardona-P](https://github.com/SebastianCardona-P)

## License

Este proyecto está licenciado bajo la Licencia MIT - ver el archivo [LICENSE.md](LICENSE.md) para detalles.

## Acknowledgments

- Inspirado en los conceptos de redes y protocolos HTTP
- Implementación educativa para el curso de Arquitecturas Empresariales (AREP)
- Gracias a los profesores y compañeros que contribuyeron con ideas y feedback
