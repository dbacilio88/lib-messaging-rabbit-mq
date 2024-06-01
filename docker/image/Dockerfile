FROM ubuntu:latest

# Instalar dependencias y Erlang
RUN apt-get update && \
    apt-get install -y gnupg wget && \
    wget -O - https://packages.erlang-solutions.com/ubuntu/erlang_solutions.asc | apt-key add - && \
    echo "deb https://packages.erlang-solutions.com/ubuntu focal contrib" | tee /etc/apt/sources.list.d/erlang.list && \
    apt-get update && \
    apt-get install -y erlang

# Instalar RabbitMQ
RUN apt-get install -y rabbitmq-server

# Enable Management plugin
#RUN rabbitmq-plugins enable rabbitmq_management
# Enable Management plugin
RUN echo "[rabbitmq_management]." > etc/rabbitmq/enabled_plugins

# Iniciar RabbitMQ
RUN rabbitmq-server -detached

RUN echo "loopback_users.guest = false" > etc/rabbitmq/rabbitmq.conf

# Exponer el puerto de RabbitMQ
EXPOSE 5672 15672

# Comando por defecto al iniciar el contenedor
CMD ["rabbitmq-server"]
