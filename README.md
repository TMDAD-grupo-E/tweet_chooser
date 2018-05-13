## Laboratorio 3 : Desarrollar un flujo cliente de RabbitMQ


Primera decisión: Se elige el profile fanout. Para ello, se coloca en el application.properties la siguiente línea:
                  spring.profiles.active=fanout
                  Otra posibilidad es lanzar la tarea con el siguiente comando:
                  SPRING_PROFILES_ACTIVE=fanout ./gradlew bootRun

Segunda decisión: Flow #3 implementado en la clase TwitterFlowFanout, ya que es el perfil que se elige

Tercera decisión: Para visualizar el resultado del Flow #3, se modifican, tanto el index.html como el functions.js
                  mostrando los trending topics en una lista


