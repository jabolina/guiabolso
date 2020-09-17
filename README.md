# Guia Bolso

Código para o teste do Guia Bolso disponível [aqui](https://github.com/GuiaBolso/seja-um-guia-back). O projeto foi
utilizado:

* Java 8
* Gradle 
* Spring

O projeto já acompanha um arquivo de propriedades, localizado em `src/main/resources/application.yml`, com valores básicos
de:

```yaml
spring:
  application:
    name: guia-bolso

server:
  port: 8080
```

## Executando

O primeiro passo é necessário realizar a compilação do projeto, para isso execute:

```bash
$ ./gradlew build
```

Para execução:

```bash
$ java -jar build/libs/guiabolso*.jar
```

### Docker

Para construir a imagem, primeiro compile o projeto e em seguida execute:

```bash
$ docker build -t guiabolso .
$ docker run -d --network="host" guiabolso
```

## Requisições

Para testar a API, execute:

```bash
$ curl localhost:8080/<userid>/transacoes/<ano>/<mes>
```

Os dados sao gerados enquanto se executa, mas para cada novo usuário existem 50% de chance
de ele não existir e outros 50% de chance de serem gerados na hora. Requisições
para o mesmo `<userid> + <ano> + <mes>` vao retornar sempre o mesmo resultado enquanto a aplicação
não for finalizada. Caso a aplicação seja finalizada, todos os dados serão perdidos e não necessariamente
serão geradas as mesmas informações que existiam antes.
