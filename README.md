# Insper Mind

Projeto final da disciplina de Arquitetura de Objetos do 3o semestre de Ciencia da Computacao do Insper - 2026.1.

O Insper Mind e uma plataforma academica para centralizar informacoes uteis aos alunos, incluindo cursos, semestres, disciplinas, docentes, eletivas, materiais de estudo, comentarios, favoritos e usuarios. A proposta e oferecer uma interface simples integrada a uma API REST, com persistencia em banco de dados e organizacao orientada a objetos.

## Links do projeto

- Frontend: https://project-n6wej.vercel.app/#/
- Backend/API: http://3.237.223.11:8080/
- Swagger UI: http://3.237.223.11:8080/swagger-ui/index.html

## Contexto academico

O projeto foi desenvolvido para consolidar conceitos de arquitetura de software orientada a objetos, com foco em:

- Modelagem de dominio clara.
- Separacao de responsabilidades entre controllers, services, repositories, entidades e DTOs.
- API REST funcional.
- Persistencia de dados.
- Integracao entre frontend React e backend Spring Boot.
- Regras de negocio posicionadas principalmente na camada de service.

## Tecnologias

- Java 25
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- PostgreSQL
- Spring Validation
- Springdoc OpenAPI/Swagger
- BCrypt
- Docker
- GitHub Actions
- React no frontend

## Dominio

O backend modela os principais conceitos do sistema por meio das seguintes classes e tipos de dominio:

- `Usuario`: representa um usuario da plataforma, com cadastro, login, senha criptografada, comentarios e favoritos.
- `Curso`: representa um curso disponivel no sistema e agrupa seus semestres.
- `Semestre`: representa a organizacao academica por semestre dentro de um curso.
- `Disciplina`: representa uma disciplina de um semestre, sua formula de avaliacao, criterio de barreira e relacao com docente e comentarios.
- `Eletiva`: especializacao de `Disciplina`, com carga horaria e semestre minimo, sem ficar presa a um curso especifico.
- `Docente`: representa professores vinculados a disciplinas.
- `Material`: representa materiais academicos compartilhados por usuarios.
- `TipoMaterial`: enum que classifica materiais como prova antiga, resumo, lista, PDF, livro e outros tipos.
- `Comentario`: representa relatos/comentarios feitos por usuarios.
- `Favorito`: representa itens salvos por usuarios, como materiais ou eletivas.

## Arquitetura do backend

```text
src/main/java/br/insper/insperMind
|-- comentario
|-- common
|-- curso
|-- disciplina
|-- docente
|-- eletiva
|-- favorito
|-- material
|-- semestre
`-- usuario
```

Cada modulo segue uma organizacao em camadas:

- `Controller`: expoe endpoints REST e recebe requisicoes HTTP.
- `Service`: concentra regras de negocio, validacoes e orquestracao entre entidades.
- `Repository`: isola o acesso ao banco usando Spring Data JPA.
- `DTO`: define os contratos de entrada e saida da API.
- `Exception`: representa erros especificos do dominio quando aplicavel.

## Funcionalidades

- Cadastro, listagem, atualizacao e remocao logica de usuarios.
- Login de usuario com senha protegida por BCrypt.
- Cadastro e consulta de cursos, semestres, disciplinas, docentes, eletivas e materiais.
- Criacao e edicao de comentarios.
- Curtidas em comentarios.
- Favoritos para materiais e eletivas.
- Edicao e delecao de materiais restritas ao usuario criador.
- Listagens paginadas via `Pageable`.
- Documentacao interativa da API via Swagger.

## Atualizacoes recentes

- `Semestre` agora recebe `cursoId` nos DTOs de criacao e edicao, reforcando o vinculo com `Curso`.
- `Disciplina` agora recebe `semestreId` nos DTOs de criacao e edicao, reforcando o vinculo com `Semestre`.
- `Eletiva` continua sendo uma especializacao de `Disciplina`, mas nao fica vinculada a semestre regular; ao editar, o semestre e mantido como `null`.
- A edicao de `Material` exige `emailUsuario` no corpo da requisicao e valida se o email pertence ao criador do material.
- A remocao logica de `Material` exige o header `emailUsuario` e tambem valida se o usuario e o criador.
- A resposta de `Favorito` agora preserva o `id` do favorito e retorna o item salvo separadamente em `itemId`, com `tipoItem` definido como `MATERIAL` ou `ELETIVA`.

## Endpoints principais

A API publicada usa a base:

```text
http://3.237.223.11:8080
```

Rotas principais:

- `/usuario`
- `/usuario/login`
- `/curso`
- `/semestre`
- `/disciplina`
- `/docente`
- `/eletivas`
- `/material`
- `/comentario`
- `/favorito`

Para ver metodos HTTP, payloads, parametros e respostas, acesse:

```text
http://3.237.223.11:8080/swagger-ui/index.html
```

### Observacoes sobre payloads

- `POST /semestre` e `PUT /semestre/{id}` usam `cursoId` para associar o semestre a um curso.
- `POST /disciplina` e `PUT /disciplina/{id}` usam `semestreId` para associar a disciplina a um semestre.
- `PUT /material/{id}` usa `emailUsuario` no corpo para autorizar a edicao pelo criador.
- `DELETE /material/{id}` usa o header `emailUsuario` para autorizar a remocao logica pelo criador.
- `POST /favorito` usa `emailUsuario`, `itemId` e `tipoItem`; `tipoItem` deve indicar se o item salvo e `MATERIAL` ou `ELETIVA`.

## Como rodar localmente

### Requisitos

- Java 25
- Maven ou Maven Wrapper
- Acesso a um banco PostgreSQL

### Variaveis de ambiente

O backend espera as credenciais do PostgreSQL por variaveis de ambiente:

```bash
POSTGRES_USERNAME=seu_usuario
POSTGRES_PASSWORD=sua_senha
```

A URL do banco esta configurada em `src/main/resources/application.properties`.

### Executar com Maven Wrapper

No Windows:

```bash
./mvnw.cmd spring-boot:run
```

No Linux/macOS:

```bash
./mvnw spring-boot:run
```

Por padrao, a API sobe em:

```text
http://localhost:8080
```

## Build

No Windows:

```bash
./mvnw.cmd package
```

No Linux/macOS:

```bash
./mvnw package
```

O arquivo `.jar` e gerado em `target/`.

## Docker

Depois de gerar o `.jar`, crie a imagem:

```bash
docker build -t insper-mind .
```

Execute o container:

```bash
docker run -p 8080:8080 \
  -e POSTGRES_USERNAME=seu_usuario \
  -e POSTGRES_PASSWORD=sua_senha \
  --name insper-mind \
  insper-mind
```

## Deploy

O deploy do backend esta configurado em `.github/workflows/deploy.yml`.

Fluxo atual:

1. O workflow roda em pushes para a branch `main`.
2. O projeto e buildado com Maven.
3. Uma imagem Docker e criada e enviada para o Docker Hub.
4. A instancia EC2 atualiza o container via SSH.
5. O container e publicado na porta `8080`.

Secrets usados no workflow:

- `DOCKER_USER`
- `DOCKER_TOKEN`
- `EC2_HOST`
- `EC2_SSH_KEY`
- `AIVEN_USERNAME`
- `AIVEN_PASSWORD`

## Banco de dados

A aplicacao usa PostgreSQL com Hibernate/JPA. A configuracao atual usa:

```properties
spring.jpa.hibernate.ddl-auto=update
```

Isso permite que o Hibernate atualize o schema conforme as entidades da aplicacao.

## Aderencia aos requisitos da disciplina

- Back-end em Java com Spring Boot.
- API REST funcional.
- Persistencia com PostgreSQL e Spring Data JPA.
- Separacao em camadas: controllers, services, repositories, DTOs e entidades.
- Modelagem com 10 tipos principais de dominio.
- Uso de composicao em relacionamentos como curso-semestres, semestre-disciplinas, usuario-comentarios, usuario-favoritos, docente-disciplinas, curso-materiais e favoritos associados a materiais/eletivas.
- Uso de heranca em `Eletiva`, que estende `Disciplina`.
- Frontend em React integrado ao backend publicado.
