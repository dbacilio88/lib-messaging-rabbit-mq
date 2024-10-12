# lib-messaging-rabbit-mq

lib-messaging-rabbit-mq

## Exchange

Este componente es responsable de recibir los mensajes enviados por un `productor` al agente y colocarlos en la `cola`
adecuada según la `clave de enrutamiento`.

Esto significa que un `productor` no envía mensajes directamente a la `cola`, sino que los envía al `intercambio` con la
`clave de enrutamiento`.

### Tipos de Exchange

- **`Direct`**: Obtiene la `clave de enrutamiento` del mensaje y colóquela en la `cola` asociada con este `intercambio`
  y la `clave de enrutamiento`.

- **`Topic`**: Enviar un mensaje a una `cola` que coincida con el patrón de `clave de enrutamiento`.

- **`Fanout`**: Envía el mensaje a todas las `colas` asociadas con un `intercambio`, sin importar la
  `clave de enrutamiento`

## Routing Key

Es la clave que utilizan un `intercambio` para saber dónde enviar mensajes y, a su vez, es la clave que utilizan las
`colas` para conectarse a al `intercambio`.

## Queue

Es el elemento que almacena los mensajes del `intercambio` y los envía a los `consumidores` que escuchan esos mensajes.

## Binding

Las `colas` se vinculan a un `intercambio` a través de la `clave de enrutamiento`.

## Virtual Host

Un virtual host en RabbitMQ es un mecanismo que permite la segmentación de recursos dentro de un servidor RabbitMQ. Cada
`virtual host` actúa como un entorno aislado donde puedes crear `colas`, `intercambios` y enlaces de manera independiente.
Esto significa que puedes tener múltiples aplicaciones o entornos que utilicen el mismo servidor RabbitMQ sin interferir
entre sí.

## Data

- **`Queue`**: Colas
- **`Exchange`**: Intercambio
- **`Routing Key`**: Clave de enrutamiento
- **`Producer`**: Productor
- **`Consumer`**: Consumidor
