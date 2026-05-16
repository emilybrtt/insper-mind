# Insper Mind

Projeto final da disciplina de Arquitetura de Objetos do 3o semestre de Ciencia da Computacao do Insper - 2026.1.

O Insper Mind e uma plataforma academica para centralizar informacoes uteis aos alunos, incluindo cursos, semestres, disciplinas, docentes, eletivas, materiais de estudo, comentarios, favoritos e usuarios. A proposta e oferecer uma interface simples integrada a uma API REST, com persistencia em banco de dados e organizacao orientada a objetos.

## Grupo

- Emily Britto
- Gabriel Aguiar
- Giovanni
- Hector Mathias
- Vanessa

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


## Arquitetura do backend

## Base

A API publicada usa a base:

```text
http://3.237.223.11:8080
```

Para ver metodos HTTP, payloads, parametros e respostas, acesse:

```text
http://3.237.223.11:8080/swagger-ui/index.html
```

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

---

## Tech Stack

| Concern | Choice |
|---|---|
| Framework | Next.js 14 (App Router) or React + Vite |
| Language | TypeScript |
| State | Zustand (auth + UI) + TanStack Query (server state) |
| HTTP | Axios with request interceptor |
| Forms | React Hook Form + Zod |
| UI | Tailwind CSS + shadcn/ui |
| Routing | Next.js file-based or React Router v6 |

---

## Authentication & Token

- `POST /usuario/login` returns `{ token: "..." }`
- JWT contains claims: `sub` (email), `userId`, `role` (`USER` or `ADMIN`)
- Decode client-side with `jwt-decode` — no extra API call needed for user info
- Store token in memory (`authStore`) + `localStorage` for persistence across refreshes
- Attach to every request: `Authorization: Bearer <token>`
- On load: read `localStorage`, validate `exp` claim, restore session or clear
- Auto-logout: `setTimeout(logout, (exp - now) * 1000)` on login

---

## State Management

```ts
// authStore (Zustand)
interface AuthStore {
  token: string | null
  userId: number | null
  email: string | null
  nome: string | null
  role: 'USER' | 'ADMIN' | null
  login: (token: string) => void   // decodes JWT, sets all fields, starts logout timer
  logout: () => void               // clears all fields + localStorage
}
```

All server data via **TanStack Query**. Key patterns:

```ts
// Mutations use optimistic updates for likes/favorites
// Invalidate the relevant query on mutation settle
useQuery(['materiais', { disciplinaId, tipo }], fetchMateriais)
useMutation(curtirMaterial, { onMutate: optimisticToggle, onError: rollback })
```

---

## Axios Interceptor

```ts
axios.interceptors.request.use(config => {
  const token = useAuthStore.getState().token
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

axios.interceptors.response.use(
  res => res,
  err => {
    if (err.response?.status === 401) useAuthStore.getState().logout()
    return Promise.reject(err)
  }
)
```

---

## Route Structure

```
/                     → Home (authenticated)
/login                → Public
/register             → Public

/cursos               → Course list
/cursos/[id]          → Course detail (semesters)
/semestres/[id]       → Semester detail (disciplines)
/disciplinas/[id]     → Discipline detail (materials + comments)
/eletivas             → Elective list
/eletivas/[id]        → Elective detail

/materiais            → Material search
/materiais/[id]       → Material detail + comments

/forum                → Forum post list
/forum/[id]           → Forum post detail

/favoritos            → My favorites
/perfil               → My profile

/admin                → Admin dashboard (ADMIN only)
/admin/usuarios       → User management
/admin/cursos         → Course CRUD
/admin/semestres      → Semester CRUD
/admin/disciplinas    → Discipline CRUD
/admin/eletivas       → Elective CRUD
/admin/docentes       → Teacher CRUD
```

---

## Pages

### `/login`

**API:** `POST /usuario/login` → `{ token }`

**Fields:** email, senha

**Flow:**
1. Submit → call API → decode JWT → `authStore.login(token)` → redirect to `/`
2. Error 401 → show inline "Email ou senha inválidos"
3. Link to `/register`

**States:** idle | loading (disable button, show spinner) | error (inline message)

---

### `/register`

**API:** `POST /usuario` → `ResponseUsuarioDTO`

**Fields:** nome, email, senha (≥8), confirmar senha (client-only match)

**Flow:**
1. Zod validate → submit → success → redirect to `/login` with success toast
2. Error 409 → "Email já cadastrado"

---

### `/` — Home

**No API call.** Data from `authStore`.

**Content:**
- Welcome: "Olá, {nome}"
- Quick-access cards: Cursos, Materiais, Eletivas, Forum, Favoritos
- If ADMIN: extra card "Administração"

---

### `/cursos`

**API:** `GET /curso?page=0&size=20`

**Response fields used:** `id`, `nome`

**UI:** Paginated card grid. Each card shows nome + link to `/cursos/{id}`.

**ADMIN:** "Novo Curso" button → inline modal form (nome field).

---

### `/cursos/[id]`

**APIs:**
- `GET /curso/{id}` → `{ id, nome }`
- `GET /semestre?page=0&size=50` → filter client-side by `cursoId` **or** wait for backend filter endpoint

> Note: `GET /semestre` has no `cursoId` filter param in current API. Fetch all and filter by `semestre.cursoId` from response if `nomeCurso` isn't returned. Alternatively fetch once and cache.

**UI:** Course title + list of semester cards → click → `/semestres/{id}`

**ADMIN:** Edit course name (PUT /curso/{id}), delete button with confirm dialog.

---

### `/semestres/[id]`

**APIs:**
- `GET /semestre/{id}` → `{ id, nome, cursoId, nomeCurso }`  
  *(ResponseSemestreDTO — verify fields: id, nome, curso embedded)*
- `GET /disciplina?page=0&size=50` → filter client-side by `semestreId`

> Same note as above: `/disciplina` has no `semestreId` query param. Filter by `disciplina.semestreId` from response.

**UI:** Breadcrumb (Curso > Semestre). List of discipline cards → `/disciplinas/{id}`.

---

### `/disciplinas/[id]`

**APIs:**
- `GET /disciplina/{id}` → `{ id, nome, formulaAvaliacao, temDelta, criterioBarreira, semestreId, nomeSemestre, cursoId, nomeCurso, docentes[] }`
- `GET /material?disciplinaId={id}&page=0&size=20`
- `GET /comentario?idDisciplina={id}&page=0&size=20`

**UI:**
- Header: nome, breadcrumb, docentes list (chips)
- Info row: formulaAvaliacao | temDelta (badge) | criterioBarreira
- Two tabs: **Materiais** | **Relatos**
    - Materiais tab: `<MaterialList>` with type filter
    - Relatos tab: `<CommentThread>` for discipline-level comments

**Actions:**
- "Adicionar Material" button → `<MaterialUploadModal>`
- "Comentar" button → inline comment box

---

### `/eletivas`

**API:** `GET /eletivas?page=0&size=20`

**Response fields:** `id`, `nome`, `cargaHoraria`, `semestreMinimo`, `temDelta`, `docentes[]`

**UI:** Card grid. Each card: nome, `{cargaHoraria}h`, `a partir do {semestreMinimo}º semestre`, docente names, ⭐ favorite button.

**Favorite toggle:**
- `POST /favorito { eletivaId }` → star filled
- `DELETE /favorito/{favoritoId}` → star empty
- Store `favoritoId` in local state after first POST to enable delete

---

### `/eletivas/[id]`

**APIs:**
- `GET /eletivas/{id}`
- `GET /favorito` (to check if already favorited)

**UI:** Full detail: all fields, docentes, favorite button.

**ADMIN:** Edit/delete buttons.

---

### `/materiais`

**APIs:** `GET /material?cursoId=&disciplinaId=&tipo=&page=0&size=20`

**Filters (sidebar or top bar):**
- Curso (dropdown → `GET /curso`)
- Disciplina (dropdown → `GET /disciplina`, filtered by selected curso)
- Tipo (select: PROVA_ANTIGA | RESUMO | EXERCICIO_RESOLVIDO | LISTA | PDF | LIVRO | OUTRO)

**UI:** `<MaterialCard>` list with pagination.

**"Meus Materiais" toggle:** adds `emailUsuario={currentUserEmail}` param.

---

### `/materiais/[id]`

**APIs:**
- `GET /material/{id}` → `{ id, titulo, descricao, link, tipo, nomeUsuario, emailUsuario, disciplinaId, nomeDisciplina, dataCriacao }`
- `GET /comentario?idMaterial={id}&page=0&size=20`
- `PATCH /material/{id}/curtir`
- `POST /favorito { materialId }`

**UI:**
- Title, type badge, author, date, discipline link
- Link button (external) or download if `arquivo` field present
- Like button with count
- Favorite button
- Comments section: `<CommentThread idMaterial={id}>`
- If owner: Edit | Delete buttons
- If ADMIN: Delete button

**Owner check:** `emailUsuario === authStore.email`

---

### `/forum`

**API:** `GET /forum?categoria=&page=0&size=20`

**Category tabs:** TODOS | ADMINISTRATIVO | TÉCNICO | GERAL

**UI:** Post cards — titulo, categoria badge, author nome, dataCriacao (relative), curtidas count.

**"Novo Post" button** → `<PostForumModal>` (titulo, conteudo, categoria select)

---

### `/forum/[id]`

**APIs:**
- `GET /forum/{id}` → `{ id, titulo, conteudo, usuario: { id, nome, email }, categoria, dataCriacao, curtidas }`
- `PATCH /forum/{id}/curtir`

**UI:**
- Full post content (render as markdown or plain text)
- Category badge, author, date
- Like button with optimistic count
- If owner: Edit | Delete
- If ADMIN: Delete

> ⚠️ Forum posts do NOT support comments via API. Do not render a comment section here.

---

### `/favoritos`

**API:** `GET /favorito?tipo=&page=0&size=20`  
*(Always uses authenticated user — no email param needed)*

**Response fields:** `id`, `tipoItem` (MATERIAL|ELETIVA), `itemId`, `tituloMaterial`, `nomeEletiva`, `dataSalvo`

**Tabs:** TODOS | MATERIAL | ELETIVA

**UI:** Cards with item name, type badge, date saved, remove (🗑) button → `DELETE /favorito/{id}`

---

### `/perfil`

**APIs:**
- `GET /usuario/{userId}` *(userId from authStore)*  — ADMIN only by security rules
- `PATCH /usuario/{userId}` → `{ nome?, email?, senha? }`
- `GET /material?emailUsuario={email}&page=0&size=10`

> ⚠️ `GET /usuario/{id}` requires ADMIN role. For USER role, display profile data from the JWT claims (nome, email) directly — do not call the endpoint. Only call it if `role === 'ADMIN'`.

**UI:**
- Profile card: nome, email, role badge, dataCriacao
- Edit form: nome, email, nova senha (optional)
- "Meus Materiais" section below

---

### `/admin` — Dashboard

**No API.** Quick-link cards to each admin section.

Guard: redirect to `/` if `role !== 'ADMIN'`.

---

### `/admin/usuarios`

**APIs:**
- `GET /usuario?page=0&size=20`
- `PATCH /usuario/{id}/ativo?ativo={bool}`

**UI:** Table — nome, email, role, ativo (toggle switch), dataCriacao.

---

### `/admin/cursos`

**APIs:** Full CRUD on `/curso`

**UI:** Table + inline edit modal + confirm-delete dialog.

---

### `/admin/semestres`

**APIs:** Full CRUD on `/semestre`

**Fields:** nome, cursoId (select from GET /curso)

---

### `/admin/disciplinas`

**APIs:** Full CRUD on `/disciplina` + `/disciplina/{id}/docentes/{docenteId}`

**Fields:** nome, semestreId (select), docenteIds (multi-select from GET /docente), formulaAvaliacao, temDelta (checkbox), criterioBarreira

---

### `/admin/eletivas`

**APIs:** Full CRUD on `/eletivas` + `/eletivas/{id}/docentes/{docenteId}`

**Fields:** nome, cargaHoraria (number), semestreMinimo (number), docenteIds (multi-select), formulaAvaliacao, temDelta, criterioBarreira

---

### `/admin/docentes`

**APIs:** Full CRUD on `/docente`

**Fields:** nome, email

---

## Components

### `<AuthGuard>`
Wraps all private routes. Reads `authStore.token`. If null → redirect to `/login`.

### `<AdminGuard>`
Wraps `/admin/**`. Checks `authStore.role === 'ADMIN'`. If not → redirect to `/`.

### `<NavBar>`
- Logo → `/`
- Links: Cursos | Materiais | Eletivas | Forum | Favoritos
- Right: user nome + dropdown (Perfil, Sair)
- If ADMIN: "Admin" link
- Mobile: collapses to hamburger menu

### `<PaginatedList>`
Props: `queryKey`, `fetchFn`, `renderItem`, `pageSize`  
Renders numbered pagination controls. Syncs `page` to URL query param.

### `<MaterialCard>`
Props: `material: ResponseMaterialDTO`  
Shows: titulo, tipo badge, nomeDisciplina, nomeUsuario, dataCriacao (relative), curtidas.  
Clicking title → `/materiais/{id}`.  
Inline ❤️ like button (optimistic).  
Inline ⭐ favorite button.

### `<CommentThread>`
Props: `idDisciplina?`, `idMaterial?`

Fetches `GET /comentario?idDisciplina=&idMaterial=`.  
Renders flat list of root comments (where `comentarioPaiId === null`).  
Each comment has a "Responder" button → loads `GET /comentario?comentarioPaiId={id}` inline.  
Max 2 levels of visual indent on mobile, 4 on desktop.

### `<CommentItem>`
Props: `comentario: ResponseComentarioDTO`, `currentUserEmail`  
Shows: text, nomeUsuario, dataCriacao (relative), curtidas, like button, reply button.  
If owner: edit (inline textarea) + delete.  
If ADMIN: delete.

### `<LikeButton>`
Props: `count`, `liked`, `onToggle`  
Optimistic: immediately flips state + count, reverts on error.

### `<FavoriteButton>`
Props: `favoritoId: number | null`, `materialId?`, `eletivaId?`  
Star icon. If `favoritoId` → DELETE. Else → POST.

### `<MaterialUploadModal>`
Two tabs: **Link** | **Arquivo**

Link tab fields: titulo, descricao, link (URL), tipo (select), disciplinaId (pre-filled if on discipline page)  
→ `POST /material`

Arquivo tab fields: file input (PDF/DOC/DOCX/PPT, max 100MB), titulo, descricao, disciplinaId  
→ `POST /material/upload` (multipart)  
Show upload progress bar.

### `<PostForumModal>`
Fields: titulo, conteudo (textarea), categoria (select: ADMINISTRATIVO | TÉCNICO | GERAL)  
→ `POST /forum`

### `<ConfirmDialog>`
Props: `message`, `onConfirm`, `onCancel`  
Used before all delete actions.

### `<AdminTable>`
Generic table with edit (pencil icon → modal) + delete (trash icon → ConfirmDialog) per row.

### `<ErrorMessage>`
Inline error display. Reads `mensagem` from API error response body.

### `<LoadingSkeleton>`
Placeholder cards matching the shape of `<MaterialCard>` or list rows.

### `<EmptyState>`
Illustration + message for zero-result pages.

### `<TagBadge>`
Maps `TipoMaterial` enum to display label + color:
```
PROVA_ANTIGA      → "Prova Antiga"    → red
RESUMO            → "Resumo"          → blue
EXERCICIO_RESOLVIDO → "Ex. Resolvido" → green
LISTA             → "Lista"           → orange
PDF               → "PDF"             → gray
LIVRO             → "Livro"           → purple
OUTRO             → "Outro"           → neutral
```

---

## API Endpoint Map (complete)

| Action | Method | Endpoint | Body / Params |
|---|---|---|---|
| Register | POST | `/usuario` | `{ nome, email, senha }` |
| Login | POST | `/usuario/login` | `{ email, senha }` |
| My profile (ADMIN only) | GET | `/usuario/{id}` | — |
| Update profile | PATCH | `/usuario/{id}` | `{ nome?, email?, senha? }` |
| List users | GET | `/usuario` | `?page&size` |
| Set user active | PATCH | `/usuario/{id}/ativo` | `?ativo=true\|false` |
| List cursos | GET | `/curso` | `?page&size` |
| Get curso | GET | `/curso/{id}` | — |
| Create curso | POST | `/curso` | `{ nome }` |
| Edit curso | PUT | `/curso/{id}` | `{ nome?, ativo? }` |
| Delete curso | DELETE | `/curso/{id}` | — |
| List semestres | GET | `/semestre` | `?page&size` |
| Get semestre | GET | `/semestre/{id}` | — |
| Create semestre | POST | `/semestre` | `{ nome, cursoId }` |
| Edit semestre | PUT | `/semestre/{id}` | `{ nome?, cursoId?, ativo? }` |
| Delete semestre | DELETE | `/semestre/{id}` | — |
| List disciplinas | GET | `/disciplina` | `?page&size` |
| Get disciplina | GET | `/disciplina/{id}` | — |
| Create disciplina | POST | `/disciplina` | `{ nome, semestreId, docenteIds[], formulaAvaliacao, temDelta, criterioBarreira }` |
| Edit disciplina | PUT | `/disciplina/{id}` | `{ nome?, semestreId?, formulaAvaliacao?, temDelta?, criterioBarreira? }` |
| Add docente | POST | `/disciplina/{id}/docentes/{docenteId}` | — |
| Remove docente | DELETE | `/disciplina/{id}/docentes/{docenteId}` | — |
| Delete disciplina | DELETE | `/disciplina/{id}` | — |
| List eletivas | GET | `/eletivas` | `?page&size` |
| Get eletiva | GET | `/eletivas/{id}` | — |
| Create eletiva | POST | `/eletivas` | `{ nome, cargaHoraria, semestreMinimo, docenteIds[], formulaAvaliacao, temDelta, criterioBarreira }` |
| Edit eletiva | PUT | `/eletivas/{id}` | partial fields |
| Add docente | POST | `/eletivas/{id}/docentes/{docenteId}` | — |
| Remove docente | DELETE | `/eletivas/{id}/docentes/{docenteId}` | — |
| Delete eletiva | DELETE | `/eletivas/{id}` | — |
| List docentes | GET | `/docente` | `?page&size` |
| Get docente | GET | `/docente/{id}` | — |
| Create docente | POST | `/docente` | `{ nome, email }` |
| Edit docente | PATCH | `/docente/{id}` | `{ nome?, email? }` |
| Delete docente | DELETE | `/docente/{id}` | — |
| List materiais | GET | `/material` | `?cursoId&disciplinaId&emailUsuario&tipo&page&size` |
| Get material | GET | `/material/{id}` | — |
| Create material (link) | POST | `/material` | `{ titulo, descricao, link, tipo, disciplinaId }` |
| Upload material (file) | POST | `/material/upload` | multipart: `file, disciplinaId, titulo?, descricao?` |
| Edit material | PUT | `/material/{id}` | `{ titulo?, descricao?, link?, tipo?, disciplinaId? }` |
| Delete material | DELETE | `/material/{id}` | — |
| Like material | PATCH | `/material/{id}/curtir` | — |
| List comentarios | GET | `/comentario` | `?idDisciplina&idMaterial&comentarioPaiId&page&size` |
| Get comentario | GET | `/comentario/{id}` | — |
| Create comentario | POST | `/comentario` | `{ comentario, idDisciplina?, idMaterial?, comentarioPaiId? }` |
| Edit comentario | PUT | `/comentario/{id}` | `{ comentario?, ativo? }` |
| Like comentario | PATCH | `/comentario/{id}/curtir` | — |
| Delete comentario | DELETE | `/comentario/{id}` | — |
| List forum | GET | `/forum` | `?categoria&page&size` |
| Get post | GET | `/forum/{id}` | — |
| Create post | POST | `/forum` | `{ titulo, conteudo, categoria }` |
| Edit post | PUT | `/forum/{id}` | `{ titulo?, conteudo? }` |
| Like post | PATCH | `/forum/{id}/curtir` | — |
| Delete post | DELETE | `/forum/{id}` | — |
| List favoritos | GET | `/favorito` | `?tipo&page&size` |
| Create favorito | POST | `/favorito` | `{ materialId? } OR { eletivaId? }` |
| Delete favorito | DELETE | `/favorito/{id}` | — |

---

## User Flows

### Register → First Use
`/register` → submit → `/login` (success toast) → login → `/` (home)

### Browse Material
`/cursos` → click curso → `/cursos/{id}` → click semestre → `/semestres/{id}` → click disciplina → `/disciplinas/{id}` → Materiais tab → click material → `/materiais/{id}`

### Upload Material
`/disciplinas/{id}` → "Adicionar Material" → modal (Link or Arquivo tab) → submit → material appears in list

### Like + Favorite
Any material card or detail page → click ❤️ (optimistic) → click ⭐ → appears in `/favoritos`

### Post in Forum
`/forum` → "Novo Post" → modal → submit → post appears at top of list → click → `/forum/{id}`

### Comment on Discipline
`/disciplinas/{id}` → Relatos tab → "Comentar" → inline textarea → submit → comment appears → "Responder" on a comment → reply appears indented

### Admin: Create Discipline
`/admin/disciplinas` → "Nova Disciplina" → modal (fill all fields, multi-select docentes) → submit → appears in table

---

## Error & Loading States

| Situation | Behavior |
|---|---|
| Any fetch loading | Show `<LoadingSkeleton>` matching content shape |
| Zero results | Show `<EmptyState>` with contextual message |
| 401 from any request | Auto logout → redirect `/login` → toast "Sessão expirada" |
| 403 | Toast "Sem permissão para esta ação" |
| 404 | Inline "Não encontrado" — no full page redirect |
| 400 (validation) | Show `error.response.data.mensagem` inline near form |
| 409 (duplicate) | Show `error.response.data.mensagem` inline near field |
| 500 | Toast "Erro interno. Tente novamente." + retry button |
| File upload too large / wrong type | Client-side: "Arquivo deve ser PDF/DOC/DOCX/PPT, máximo 100MB" before submit |
| File upload in progress | Progress bar inside modal, disable submit button |
| Like/favorite fails | Roll back optimistic update, show toast "Erro ao salvar" |
| Form submit loading | Disable submit button + show spinner inside button |
| Network offline | Toast "Sem conexão com a internet" |

---

## Mobile Responsiveness

- **Breakpoints:** mobile-first. `sm: 640px` `md: 768px` `lg: 1024px`
- **NavBar:** hamburger at `< md`. Drawer slides from left.
- **Card grids:** 1 col mobile → 2 col `md` → 3 col `lg`
- **Admin tables:** horizontal scroll wrapper on mobile, sticky first column
- **Comment thread indent:** 12px per level on mobile (max 2 levels shown), 24px on desktop (max 4)
- **Modals:** full-screen bottom sheet on mobile, centered modal on desktop
- **Filters sidebar:** collapses to top filter bar with horizontal scroll on mobile
- **File upload:** native `<input type="file">`, no drag-and-drop required on mobile
- **Pagination:** show prev/next only on mobile; full number list on desktop
- **Font sizes:** scale down display headings on mobile (`text-2xl` → `text-xl`)

---

## UX Behaviors

- **Likes:** optimistic — flip immediately, revert on API error
- **Favorites:** optimistic — store pending state, revert on error
- **Comment reply:** clicking "Responder" scrolls to inline box below the parent comment, pre-fills `comentarioPaiId`
- **Comment edit:** click edit → text becomes inline `<textarea>` with save/cancel, no modal
- **Admin multi-select docentes:** searchable dropdown (combobox) — type to filter
- **Pagination:** syncs `?page=N` to URL so browser back works
- **Toast stack:** max 3 visible at once, auto-dismiss after 4s
- **Confirm dialog:** required for all deletes, soft-destructive styling (red button)
- **Active nav link:** highlighted based on current route
- **ADMIN badge:** visible in NavBar user menu when `role === 'ADMIN'`
- **Relative dates:** show "há 2 dias" / "há 5 min" using `date-fns/formatDistanceToNow`
- **Empty forum post list:** show "Seja o primeiro a postar!" CTA
- **Material tipo filter:** "Todos os tipos" default option clears the filter param
- **Session restore:** on app load, read `localStorage` → decode JWT → if `exp > now` restore session silently, else clear and show login