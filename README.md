
# Evaluación Backend N°2: Sistema de Gestión de Votos

Proyecto realizado con SpringBoot para la gestión de Partidos Políticos, Candidatos y Votos mediante peticiones REST.

## Descripción

El proyecto permite la creación, modificación y eliminación de Partidos Políticos y sus respectivos Candidatos, además de atribuirle votos a los mismos con el fin de simular una situación de elecciones electorales.

## Estructura base del proyecto

- Realizado con Java 17 utilizando el framework de SpringBoot.
- Documentación de la API generada y personalizada mediante OpenAPI y Swagger UI.
- Datos persistidos en la base de datos en memoria H2 mediante Spring Data JPA.
##  Ejecución

Para ejecutar el proyecto primero clonamos el repositorio:

```bash
  git clone https://github.com/Elcolora3x/evaluacion-backend-votos.git
```

Luego, abrimos el proyecto con el IDE de preferencia que tengan siempre y cuando soporte el lenguaje de programación Java y ejecutamos la clase "EvaluacionBackendVotosApplication".

# IMPORTANTE!

Una vez que se ejecuta el proyecto, la base de datos se encontrará vacía, ya que H2 es una base de datos en memoria. Para cargar la base de datos se implementó un endpoint /init en cada uno de los controladores del proyecto.

Como los votos necesitan el ID de un candidato y los candidatos necesitan estar asociados a un partido, el orden de ejecución de los endpoints /init es el siguiente:

1) init del controlador de Partidos políticos.
2) init del controlador de Candidatos.
3) init del controlador de Votos.

Si no se sigue este orden, la base de datos no se cargará correctamente y devolvera un error.

Nota: Los datos de cada uno de los endpoints se encuentran en el paquete "/docs" dentro de la raíz del proyecto, en el archivo "TestsDeControladoresEvaluacion2.json".



## Documentación

Click aquí para ver la [Documentacion de la API con Swagger](http://localhost:8080/swagger-ui/index.html).

## Captura de Pantalla Análisis SonarLint

![AnalisisSonar](/src/docs/AnalisisSonarLint.png)