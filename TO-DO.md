# Insper Mind v2 - Guia Completo de Desenvolvimento

## 1. FLUXO DE PÁGINAS E NAVEGAÇÃO

### 1.1 Estrutura Geral de Rotas

```
PUBLIC (não autenticado)
├── /login                    → Login
├── /cadastro                 → Cadastro de usuário
└── /                         → Landing page

PROTECTED (autenticado)
├── /dashboard                → Página inicial (Início)
├── /cursos                   → Listagem de cursos
├── /curso/:cursoId
│   ├── /semestres           → Estrutura do curso (semestres + disciplinas)
│   └── /docentes            → Docentes do curso
├── /docentes                 → Portal de contatos (todos os docentes)
├── /materiais               → Acervo de materiais (listagem geral)
├── /disciplina/:disciplinaId → Detalhe de disciplina + materiais + comentários
├── /meu-perfil              → Dados do usuário + minhas disciplinas favoritas
├── /meus-materiais          → Materiais enviados pelo usuário
├── /favoritos               → Materiais e eletivas salvos
├── /forum                   → Fórum de dúvidas
└── /admin                   → Painel de administração
```

### 1.2 Fluxo de Autenticação

**Não autenticado:**
1. Landing page → explicação da plataforma, call-to-action para login/cadastro
2. Login → email + senha → redirect para /dashboard
3. Cadastro → nome + email + senha → auto-login → redirect para /dashboard

**Autenticado:**
- Header/Menu permanente mostra nome do usuário + logout
- Menu lateral (mobile) ou superior (desktop) com navegação principal

### 1.3 Detalhes de Cada Página

#### **Landing Page (pública)**
- Hero section com valor da plataforma
- Botões: Login | Cadastro
- Cards com features: estrutura de cursos, materiais compartilhados, contatos docentes
- Testimonial/estatísticas (opcional)
- Footer com links

#### **Login**
- Email + Senha
- "Esqueci a senha" (futuro)
- Link para cadastro
- Validações de email/senha

#### **Cadastro**
- Nome completo
- Email (validar duplicação)
- Senha (mínimo 8 caracteres, requisitos)
- Confirmar senha
- Link para login

#### **Dashboard (Início)**
- Bem-vindo, [Nome do usuário]
- Cards rápidos:
    - Próximas disciplinas (semestre atual)
    - Materiais salvos recentemente
    - Eletivas em alta
- Últimos materiais compartilhados na plataforma
- Quick links para seções principais

#### **Cursos**
- Listagem de todos os cursos
- Card para cada curso com:
    - Nome
    - Descrição (breve)
    - Número de semestres
    - Botão "Explorar" → /curso/:cursoId/semestres

#### **Estrutura do Curso (Semestres + Disciplinas)**
- Breadcrumb: Cursos > [Curso]
- Abas ou accordion por semestre (1, 2, 3... 8)
- Para cada semestre:
    - Lista de disciplinas
    - Para cada disciplina:
        - Nome
        - Professor responsável (email + link para contato)
        - Link para detalhe da disciplina
        - Badge: Obrigatória | Eletiva | Optativa

#### **Portal de Contatos (Docentes)**
- Filtro por:
    - Curso
    - Semestre
    - Nome
- Tabela/Cards mostrando:
    - Nome do docente
    - Email (copiável)
    - Disciplinas que leciona
    - Link para detalhe do docente (currículo breve, se disponível)

#### **Detalhe de Disciplina**
- Breadcrumb: Cursos > [Curso] > [Semestre] > [Disciplina]
- Informações da disciplina:
    - Nome
    - Semestre
    - Docente(s) responsável(is)
    - Ementa
    - Critério de aprovação / Barreiras
    - Fórmula de avaliação
- **Abas:**
    1. **Materiais** → Acervo de materiais (provas antigas, resumos, etc.)
        - Filtro por tipo (prova, resumo, lista, PDF, livro, outro)
        - Ordenação (recente, popular, rating)
        - Card/linha para cada material com:
            - Título
            - Tipo
            - Uploader
            - Curtidas
            - Botão salvar (favorito)
            - Botão download/visualizar
    2. **Comentários** → Fórum de discussão específico da disciplina
        - Lista de comentários com:
            - Autor
            - Data
            - Texto
            - Curtidas
            - Botão responder (thread)
        - Caixa para novo comentário (se logado)

#### **Acervo de Materiais (Geral)**
- Filtros:
    - Curso
    - Disciplina
    - Tipo de material
    - Data (recente)
- Ordenação: Relevância, Curtidas, Recente
- Upload de novo material:
    - Disciplina
    - Tipo
    - Arquivo
    - Descrição
    - Validação de tamanho/tipo

#### **Meus Materiais**
- Lista de materiais enviados pelo usuário
- Ações: Editar, Deletar, Ver estatísticas (curtidas)

#### **Favoritos**
- Abas: Materiais | Eletivas
- Cards dos itens salvos
- Botão remover do favorito

#### **Meu Perfil**
- Dados do usuário:
    - Nome
    - Email
    - Opcional: Foto, Bio, Disciplinas favoritas
- Botão editar
- Botão logout
- Histórico de atividade (opcional)

#### **Fórum de Dúvidas (Novo)**
- Threads de discussão geral (não específicas de disciplina)
- Categorias: Administrativo, Técnico, Geral
- Criação de post:
    - Título
    - Categoria
    - Conteúdo
- Respostas e comentários em threads
- Votação (up/down) em posts

#### **Admin Panel (Opcional, futuro)**
- Gerenciamento de usuários (ativar/desativar)
- Gerenciamento de cursos, semestres, disciplinas, docentes
- Moderation de conteúdo (materiais, comentários)

---

## 2. REGRAS DE NEGÓCIO

### 2.1 Usuários
- Um email = um usuário único
- Senha criptografada com BCrypt
- Usuários podem ser desativados logicamente (soft delete)
- Qualquer usuário autenticado pode enviar materiais e comentários
- Usuários só podem editar/deletar seus próprios materiais e comentários

### 2.2 Cursos, Semestres, Disciplinas
- Um **Curso** agrupa **Semestres** (ex: 1º, 2º, 3º... 8º)
- Um **Semestre** agrupa **Disciplinas** obrigatórias
- Uma **Disciplina** pertence a um único Semestre
- Cada disciplina tem 1+ docente(s) responsável(is)
- Disciplinas têm critério de aprovação e fórmula de avaliação
- Eletivas NÃO ficam presas a semestres, são opcionais

### 2.3 Docentes
- Docente é uma entidade independente
- Um docente pode lecionar múltiplas disciplinas em múltiplos cursos
- Email do docente é único e serve como identificador na listagem

### 2.4 Materiais
- Material pertence a 1 disciplina
- Material tem tipo (prova, resumo, lista, PDF, livro, outro)
- Material é criado por um usuário específico
- Apenas o criador pode editar/deletar seu material
- Material pode ser "curtido" (liked) por qualquer usuário
- Material pode ser adicionado aos favoritos
- Histórico de downloads/views (opcional, para estatísticas)

### 2.5 Comentários
- Comentário pode ser:
    - Em disciplina (discussão geral)
    - Em material (discussão específica)
    - Em post do fórum (thread)
- Comentário pertence a um usuário
- Apenas o criador pode editar/deletar
- Comentário pode ter replies (subcomentários)
- Comentários podem ser curtidos

### 2.6 Favoritos
- Usuário pode favoritar:
    - Material
    - Eletiva
- Um usuário não pode favoritar o mesmo item 2x
- Favorito é pessoal e privado

### 2.7 Fórum
- Post pertence a um usuário e tem categoria
- Post pode ter respostas (comentários)
- Respostas também podem ser votadas
- Post é identificado por título único na categoria (ou ID)
- Moderadores podem remover posts/respostas inapropriadas

### 2.8 Validações
- Email deve ser válido
- Senha mínimo 8 caracteres
- Arquivo de material: max 100MB, tipos permitidos (PDF, DOC, PPT, etc.)
- Título/descrição: max 255 caracteres
- Conteúdo de comentário: max 2000 caracteres

---

## 3. ESPECIFICAÇÕES DE CLASSES (Domínio)

### 3.1 Entidades Existentes (já modeladas no backend)

```java
// Usuario
- id: UUID
- nome: String
- email: String (UNIQUE)
- senha: String (BCrypt)
- ativo: Boolean
- dataCriacao: LocalDateTime
- comentarios: List<Comentario>
- favoritos: List<Favorito>
- materiaisEnviados: List<Material>
- postsForumEnviados: List<PostForum>

// Curso
- id: UUID
- nome: String
- descricao: String (opcional)
- semestres: List<Semestre>

// Semestre
- id: UUID
- numero: Integer (1-8)
- cursoId: UUID
- disciplinas: List<Disciplina>

// Disciplina
- id: UUID
- nome: String
- ementa: String (opcional)
- semestreId: UUID
- docentes: List<Docente>
- criterioAprovacao: String
- formulaAvaliacao: String
- materiais: List<Material>
- comentarios: List<Comentario>

// Eletiva extends Disciplina
- id: UUID
- nome: String
- ementa: String
- cargaHoraria: Integer
- semestreMinimo: Integer
- semestreId: null (não vinculada)
- docentes: List<Docente>
- materiais: List<Material>

// Docente
- id: UUID
- nome: String
- email: String (UNIQUE)
- bio: String (opcional)
- disciplinas: List<Disciplina>

// Material
- id: UUID
- titulo: String
- descricao: String
- tipo: TipoMaterial (PROVA, RESUMO, LISTA, PDF, LIVRO, OUTRO)
- arquivo: String (caminho/URL)
- disciplinaId: UUID
- usuarioId: UUID (criador)
- dataCriacao: LocalDateTime
- curtidas: Integer (count)
- usuariosQueCurtiram: List<Usuario>
- comentarios: List<Comentario>

// TipoMaterial (Enum)
PROVA, RESUMO, LISTA, PDF, LIVRO, OUTRO

// Comentario
- id: UUID
- conteudo: String
- usuarioId: UUID
- dataCriacao: LocalDateTime
- materialId: UUID (opcional)
- disciplinaId: UUID (opcional)
- postForumId: UUID (opcional)
- curtidas: Integer
- respostas: List<Comentario> (subcomentários)
- comentarioPaiId: UUID (opcional, para replies)

// Favorito
- id: UUID
- usuarioId: UUID
- itemId: UUID (material ou eletiva)
- tipoItem: TipoFavorito (MATERIAL, ELETIVA)
- dataCriacao: LocalDateTime
```

### 3.2 Entidades Novas Necessárias

```java
// PostForum
- id: UUID
- titulo: String (UNIQUE per categoria)
- conteudo: String
- usuarioId: UUID (criador)
- categoria: CategoriaForum (ADMINISTRATIVO, TECNICO, GERAL)
- dataCriacao: LocalDateTime
- dataAtualizacao: LocalDateTime
- ativo: Boolean
- comentarios: List<Comentario> (respostas)
- curtidas: Integer
- usuariosQueCurtiram: List<Usuario>

// CategoriaForum (Enum)
ADMINISTRATIVO, TECNICO, GERAL

// CurtidaMaterial / CurtidaComentario (opcional, se precisar rastrear por usuário)
- id: UUID
- usuarioId: UUID
- materialId: UUID / comentarioId: UUID
- dataCriacao: LocalDateTime

// EstatisticasMaterial (opcional, futuro)
- id: UUID
- materialId: UUID
- downloads: Integer
- views: Integer
- rating: Double
```

### 3.3 DTOs (Data Transfer Objects)

**Padrão de nomes:**
- `*Request` para entrada (POST/PUT)
- `*Response` para saída

**Exemplos:**
```java
// Usuario
- UsuarioCreateRequest { nome, email, senha }
- UsuarioCriarLoginRequest { email, senha }
- UsuarioResponse { id, nome, email }
- UsuarioProfileResponse { id, nome, email, dataCriacao, materiaisEnviados[] }

// Disciplina
- DisciplinaCreateRequest { nome, semestreId, docenteIds[], criterioAprovacao, formulaAvaliacao }
- DisciplinaDetailResponse { id, nome, ementa, semestre{}, docentes[], materiais[], comentarios[] }

// Material
- MaterialCreateRequest { titulo, descricao, tipo, arquivo, disciplinaId }
- MaterialUpdateRequest { titulo, descricao, tipo }
- MaterialResponse { id, titulo, tipo, disciplina{}, usuario{}, curtidas, dataCriacao }

// PostForum
- PostForumCreateRequest { titulo, conteudo, categoria }
- PostForumUpdateRequest { titulo, conteudo }
- PostForumDetailResponse { id, titulo, conteudo, usuario{}, categoria, dataCriacao, comentarios[], curtidas }

// Comentario
- ComentarioCreateRequest { conteudo, (materialId | disciplinaId | postForumId) }
- ComentarioResponse { id, conteudo, usuario{}, dataCriacao, curtidas }
```

---

## 4. TAREFAS DE BACK-END

### 4.1 Novas Funcionalidades API

#### **PostForum Controller**
- [ ] `GET /forum` → listar posts (paginado, filtro por categoria)
- [ ] `GET /forum/:id` → detalhe de post + comentários
- [ ] `POST /forum` → criar novo post (autenticado)
- [ ] `PUT /forum/:id` → editar post (apenas criador)
- [ ] `DELETE /forum/:id` → deletar post (apenas criador ou admin)
- [ ] `POST /forum/:id/curtir` → curtir post

#### **Comentario em Material/Disciplina/Forum**
- [ ] Refatorar comentários para suportar 3 tipos de vínculo
- [ ] Endpoint de resposta (reply) a comentário

#### **Curtidas em Material/Comentario/PostForum**
- [ ] `POST /material/:id/curtir` → adicionar/remover curtida
- [ ] `POST /comentario/:id/curtir` → adicionar/remover curtida
- [ ] `POST /forum/:id/curtir` → adicionar/remover curtida
- [ ] Resposta deve indicar se usuário já curtiu (boolean isLiked)

#### **Favoritos (melhorias)**
- [ ] Validar duplicação (impedir favoritar 2x)
- [ ] Retornar resposta com itemId e tipoItem separados

#### **Listagens com filtros/busca**
- [ ] `GET /docentes?curso=:cursoId&semestre=:semestre&nome=:nome`
- [ ] `GET /materiais?curso=:cursoId&disciplina=:disciplina&tipo=:tipo&ordenacao=recente|popular`
- [ ] `GET /disciplina/:id/materiais?tipo=:tipo&ordenacao=...`

#### **Validações aprimoradas**
- [ ] Tamanho máximo de arquivo (100MB)
- [ ] Tipos MIME permitidos
- [ ] Validação de email duplicado no cadastro
- [ ] Validação de senha fraca

### 4.2 Melhorias de Infraestrutura

- [ ] Criar custom exception para PostForum (ex: `PostForumNaoEncontradoException`)
- [ ] Implementar `Pageable` em todas as listagens (já existe?)
- [ ] Adicionar logging nos services
- [ ] Testes unitários para services críticos (Usuario, Material, PostForum)
- [ ] Documentação Swagger atualizada (adicionar novos endpoints)

### 4.3 Storage de Arquivos

- [ ] Definir estratégia de armazenamento (local, S3, etc.)
- [ ] Endpoint para upload de material (POST /material/upload)
- [ ] Endpoint para download de material (GET /material/:id/download)
- [ ] Validação de tipo MIME

### 4.4 Autenticação/Autorização

- [ ] JWT ou Session-based (verificar implementação atual)
- [ ] Guard para rotas protegidas
- [ ] Refresh token (se JWT)
- [ ] Logout endpoint

### 4.5 Banco de Dados

- [ ] Migrations/scripts para tabelas novas (PostForum, CategoriaForum)
- [ ] Índices em colunas frequentes (email, usuarioId, disciplinaId)
- [ ] Seeders com dados de exemplo (cursos, docentes, semestres)

---

## 5. TAREFAS DE FRONT-END

### 5.1 Setup Inicial

- [ ] Criar novo projeto React (Vite ou Create React App)
- [ ] Configurar routing com React Router
- [ ] Setup de autenticação (JWT token em localStorage ou Context API)
- [ ] Criar interceptor HTTP para adicionar header de autenticação
- [ ] Setup de estado global (Context API ou Redux)
- [ ] Definir design system / UI library (Material-UI, Tailwind, etc.)

### 5.2 Páginas Públicas

- [ ] **Landing Page**
    - Hero, features, CTA (Login/Cadastro), footer
    - Responsive design

- [ ] **Login**
    - Form email + senha
    - Validações client-side
    - Link "Esqueci a senha" (componente desabilitado)
    - Link para cadastro
    - Tratamento de erro (email/senha incorretos)
    - Redirect automático se já logado

- [ ] **Cadastro**
    - Form nome + email + senha + confirmar senha
    - Validações client-side
    - Feedback de senha fraca
    - Duplicate email check (debounced)
    - Link para login
    - Auto-login após sucesso
    - Redirect para dashboard

### 5.3 Páginas Autenticadas

- [ ] **Dashboard (Início)**
    - Welcome message
    - Cards de quick access
    - Últimos materiais
    - Próximas disciplinas

- [ ] **Listagem de Cursos**
    - Grid/lista de cursos
    - Search/filtro por nome
    - Card com nome, descrição, semestres, botão explorar

- [ ] **Estrutura do Curso (Semestres + Disciplinas)**
    - Breadcrumb
    - Abas/accordion para semestres
    - Lista de disciplinas por semestre
    - Cards com nome, professor, link para detalhe

- [ ] **Portal de Contatos**
    - Filtros: curso, semestre, nome
    - Tabela ou cards de docentes
    - Email copiável (copy to clipboard)
    - Link para email (mailto)

- [ ] **Detalhe de Disciplina**
    - Breadcrumb
    - Info da disciplina (nome, professor, ementa, critério, fórmula)
    - Abas: Materiais | Comentários
    - **Aba Materiais:**
        - Filtro por tipo
        - Ordenação
        - Card/linha para cada material
        - Ações: download, salvar favorito, curtir
    - **Aba Comentários:**
        - Lista de comentários com autor, data, conteúdo
        - Likes em comentários
        - Replies (thread colapsável)
        - Form para novo comentário (se logado)

- [ ] **Acervo de Materiais**
    - Filtros: curso, disciplina, tipo, data
    - Ordenação: relevância, curtidas, recente
    - Grid/lista de materiais
    - Upload modal/form para novo material
    - Validação de arquivo

- [ ] **Meus Materiais**
    - Lista de materiais do usuário
    - Editar, deletar, ver stats
    - Link para detalhe

- [ ] **Favoritos**
    - Abas: Materiais | Eletivas
    - Cards dos itens salvos
    - Remover do favorito

- [ ] **Meu Perfil**
    - Dados do usuário (nome, email, foto)
    - Editar modal
    - Logout
    - Histórico (opcional)

- [ ] **Fórum de Dúvidas**
    - Listagem de posts
    - Filtro por categoria
    - Busca por título
    - Card/linha com título, categoria, autor, data
    - Link para detalhe do post
    - Botão criar novo post

- [ ] **Detalhe de Post Forum**
    - Breadcrumb
    - Título, categoria, autor, data
    - Conteúdo
    - Botões: editar (se criador), deletar (se criador)
    - Curtidas
    - Abas/seção de comentários (respostas)
    - Form para nova resposta (se logado)

### 5.4 Componentes Reutilizáveis

- [ ] **Header/Navbar**
    - Logo
    - Menu desktop (topo)
    - Menu mobile (hambúrguer)
    - User profile dropdown
    - Logout

- [ ] **Sidebar (Mobile)**
    - Menu colapsável
    - Links de navegação
    - Logout

- [ ] **Card de Disciplina**
    - Nome, professor, link

- [ ] **Card de Material**
    - Título, tipo, uploader, curtidas
    - Ações (download, salvar, curtir)

- [ ] **Card de Post Forum**
    - Título, categoria, autor, data, replies count

- [ ] **Comentário**
    - Autor, data, conteúdo
    - Curtidas
    - Botão reply

- [ ] **Form Material**
    - Seleção de disciplina
    - Tipo (select)
    - Arquivo (upload)
    - Descrição

- [ ] **Form Comentário**
    - Textarea
    - Botão enviar

- [ ] **Breadcrumb**
    - Links navegáveis

- [ ] **Pagination**
    - Para listagens paginadas

- [ ] **Loading Skeleton**
    - Para estados de carregamento

- [ ] **Toast/Snackbar**
    - Para notificações (sucesso, erro, etc.)

### 5.5 Estados e Lógica

- [ ] Context para usuário autenticado (nome, email, id)
- [ ] Context/Hook para token JWT
- [ ] Função de logout (limpar context + localStorage)
- [ ] Hook customizado para fetch com autenticação
- [ ] Hook customizado para paginação
- [ ] Hook customizado para filtros
- [ ] Tratamento de erros HTTP (401, 403, 404, 500, etc.)
- [ ] Debounce em buscas

### 5.6 Integração com API

- [ ] Criar client HTTP (axios/fetch com interceptor)
- [ ] Endpoints em constante (baseURL)
- [ ] Mapear all endpoints da API para front
- [ ] Tratamento de erro global
- [ ] Loading states em operações async

### 5.7 UX/Design

- [ ] Responsividade (mobile, tablet, desktop)
- [ ] Modo escuro (opcional)
- [ ] Acessibilidade (WCAG, labels, alt text)
- [ ] Feedback visual de ações (spinner, toast, confirmação)
- [ ] Validação em tempo real (form)
- [ ] Empty states (quando não há dados)
- [ ] 404/erro pages

### 5.8 Performance

- [ ] Code splitting (lazy load de páginas)
- [ ] Memoização de componentes pesados
- [ ] Otimização de imagens
- [ ] Caching de requisições HTTP (opcional)

### 5.9 Testes (Opcional mas recomendado)

- [ ] Testes unitários de componentes (Jest + React Testing Library)
- [ ] Testes de integração (fluxo login → dashboard → listar cursos)

---

## 6. PRIORIZAÇÃO RECOMENDADA

### **Phase 1: MVP Core** (Essencial)
**Back:**
- Endpoints de CRUD (cursos, semestres, disciplinas, docentes, materiais)
- Upload de arquivo
- Autenticação (login, cadastro)
- Validações básicas

**Front:**
- Login, cadastro, logout
- Estrutura de rotas
- Header/sidebar
- Listagem de cursos
- Detalhe de disciplina + materiais

### **Phase 2: Interação** (Importante)
**Back:**
- Comentários em disciplinas
- Curtidas em materiais
- Favoritos
- Listagens com filtros/ordenação

**Front:**
- Aba de comentários em disciplinas
- Curtidas de materiais
- Página de favoritos
- Filtros em listagens

### **Phase 3: Comunidade** (Legal ter)
**Back:**
- PostForum + comentários
- Fórum geral

**Front:**
- Página de fórum
- Criar/editar posts

### **Phase 4: Polimento** (Nice to have)
- Profil do usuário editável
- Estatísticas de materiais
- Busca avançada
- Admin panel

---

## 7. NOTAS TÉCNICAS

### **Arquitetura HTTP**
```
Requests sempre com:
- Header: Authorization: Bearer {token}
- Content-Type: application/json

Respostas padrão:
{
  "data": {...},
  "error": null,
  "timestamp": "2026-05-14T..."
}

Erro:
{
  "data": null,
  "error": "Descrição do erro",
  "timestamp": "..."
}
```

### **Estado Global (Front)**
```
AuthContext: { usuario, token, login, logout, isAuthenticated }
AppContext: { cursoAtual, semestreSelecionado, etc }
```

### **Convenção de Nomes**
- Variáveis: camelCase
- Classes: PascalCase
- Enums: UPPER_CASE
- Rotas: kebab-case
- Branches Git: feature/nome-feature | bugfix/nome-bug

---

Sucesso! 🚀