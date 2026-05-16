const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || "http://3.237.223.11:8080"

interface RequestOptions extends RequestInit {
  params?: Record<string, string | number | undefined>
}

class ApiError extends Error {
  constructor(public status: number, message: string) {
    super(message)
    this.name = "ApiError"
  }
}

async function request<T>(endpoint: string, options: RequestOptions = {}): Promise<T> {
  const { params, ...fetchOptions } = options
  
  let url = `${API_BASE_URL}${endpoint}`
  
  if (params) {
    const searchParams = new URLSearchParams()
    Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined) {
        searchParams.append(key, String(value))
      }
    })
    const queryString = searchParams.toString()
    if (queryString) {
      url += `?${queryString}`
    }
  }

  const token = typeof window !== "undefined" ? localStorage.getItem("token") : null
  
  const headers: Record<string, string> = {
    "Content-Type": "application/json",
  }

  if (token) {
    headers["Authorization"] = `Bearer ${token}`
  }

  if (fetchOptions.headers) {
    Object.assign(headers, fetchOptions.headers)
  }

  const response = await fetch(url, {
    ...fetchOptions,
    headers,
  })

  if (!response.ok) {
    const errorData = await response.json().catch(() => ({}))
    throw new ApiError(response.status, errorData.message || "An error occurred")
  }

  if (response.status === 204) {
    return {} as T
  }

  return response.json()
}

// Types
export interface PageResponse<T> {
  content: T[]
  totalPages: number
  totalElements: number
  size: number
  number: number
  first: boolean
  last: boolean
  empty: boolean
}

export interface Usuario {
  id: number
  nome: string
  email: string
  role: "ALUNO" | "ADMIN"
  ativo: boolean
  createdAt: string
}

export interface LoginRequest {
  email: string
  senha: string
}

export interface RegisterRequest {
  nome: string
  email: string
  senha: string
}

export interface TokenResponse {
  token: string
}

export interface Curso {
  id: number
  nome: string
  descricao: string
  semestres?: Semestre[]
}

export interface Semestre {
  id: number
  numero: number
  cursoId: number
  disciplinas?: Disciplina[]
}

export interface Disciplina {
  id: number
  nome: string
  descricao: string
  cargaHoraria: number
  semestreId: number
  docentes: DocenteResumo[]
}

export interface DocenteResumo {
  id: number
  nome: string
  email: string
}

export interface Docente {
  id: number
  nome: string
  email: string
  especialidade: string
  bio: string
  foto?: string
}

export interface Material {
  id: number
  titulo: string
  descricao: string
  tipo: MaterialTipo
  link?: string
  arquivoUrl?: string
  disciplinaId: number
  disciplinaNome?: string
  cursoId?: number
  cursoNome?: string
  usuario: {
    id: number
    nome: string
    email: string
  }
  curtidas: number
  curtidoPorUsuario?: boolean
  createdAt: string
}

export type MaterialTipo = 
  | "PROVA_ANTIGA" 
  | "RESUMO" 
  | "EXERCICIO_RESOLVIDO" 
  | "LISTA" 
  | "PDF" 
  | "LIVRO" 
  | "OUTRO"

export interface PostForum {
  id: number
  titulo: string
  conteudo: string
  categoria: ForumCategoria
  usuario: {
    id: number
    nome: string
    email: string
  }
  curtidas: number
  curtidoPorUsuario?: boolean
  totalComentarios: number
  createdAt: string
  updatedAt?: string
}

export type ForumCategoria = "ADMINISTRATIVO" | "TECNICO" | "GERAL"

export interface Comentario {
  id: number
  conteudo: string
  usuario: {
    id: number
    nome: string
    email: string
  }
  curtidas: number
  curtidoPorUsuario?: boolean
  comentarioPaiId?: number
  respostas?: Comentario[]
  createdAt: string
}

export interface Favorito {
  id: number
  tipoItem: "MATERIAL" | "POST_FORUM" | "DISCIPLINA"
  itemId: number
  createdAt: string
}

// API Functions

// Auth
export const authApi = {
  login: (data: LoginRequest) =>
    request<TokenResponse>("/usuario/login", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  register: (data: RegisterRequest) =>
    request<Usuario>("/usuario", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  getMe: () => request<Usuario>("/usuario/me"),
  
  getUser: (id: number) => request<Usuario>(`/usuario/${id}`),
  
  listUsers: (page = 0, size = 20) =>
    request<PageResponse<Usuario>>("/usuario", {
      params: { page, size },
    }),
}

// Cursos
export const cursoApi = {
  list: (page = 0, size = 20) =>
    request<PageResponse<Curso>>("/curso", {
      params: { page, size },
    }),
  
  getById: (id: number) => request<Curso>(`/curso/${id}`),
  
  create: (data: { nome: string; descricao: string }) =>
    request<Curso>("/curso", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: { nome?: string; descricao?: string }) =>
    request<Curso>(`/curso/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  delete: (id: number) =>
    request<void>(`/curso/${id}`, { method: "DELETE" }),
}

// Semestres
export const semestreApi = {
  list: (page = 0, size = 20) =>
    request<PageResponse<Semestre>>("/semestre", {
      params: { page, size },
    }),
  
  getById: (id: number) => request<Semestre>(`/semestre/${id}`),
}

// Disciplinas
export const disciplinaApi = {
  list: (page = 0, size = 20) =>
    request<PageResponse<Disciplina>>("/disciplina", {
      params: { page, size },
    }),
  
  getById: (id: number) => request<Disciplina>(`/disciplina/${id}`),
}

// Docentes
export const docenteApi = {
  list: (page = 0, size = 20) =>
    request<PageResponse<Docente>>("/docente", {
      params: { page, size },
    }),
  
  getById: (id: number) => request<Docente>(`/docente/${id}`),
}

// Materiais
export const materialApi = {
  list: (filters?: {
    cursoId?: number
    disciplinaId?: number
    emailUsuario?: string
    tipo?: MaterialTipo
    page?: number
    size?: number
  }) =>
    request<PageResponse<Material>>("/material", {
      params: {
        ...filters,
        page: filters?.page ?? 0,
        size: filters?.size ?? 20,
      },
    }),
  
  getById: (id: number) => request<Material>(`/material/${id}`),
  
  create: (data: {
    titulo: string
    descricao: string
    tipo: MaterialTipo
    link?: string
    disciplinaId: number
  }) =>
    request<Material>("/material", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: {
    titulo?: string
    descricao?: string
    tipo?: MaterialTipo
    link?: string
  }) =>
    request<Material>(`/material/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  curtir: (id: number) =>
    request<Material>(`/material/${id}/curtir`, { method: "PATCH" }),
  
  delete: (id: number) =>
    request<void>(`/material/${id}`, { method: "DELETE" }),
}

// Forum
export const forumApi = {
  list: (filters?: {
    categoria?: ForumCategoria
    page?: number
    size?: number
  }) =>
    request<PageResponse<PostForum>>("/forum", {
      params: {
        ...filters,
        page: filters?.page ?? 0,
        size: filters?.size ?? 20,
      },
    }),
  
  getById: (id: number) => request<PostForum>(`/forum/${id}`),
  
  create: (data: {
    titulo: string
    conteudo: string
    categoria: ForumCategoria
  }) =>
    request<PostForum>("/forum", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: {
    titulo?: string
    conteudo?: string
    categoria?: ForumCategoria
  }) =>
    request<PostForum>(`/forum/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  curtir: (id: number) =>
    request<PostForum>(`/forum/${id}/curtir`, { method: "PATCH" }),
  
  delete: (id: number) =>
    request<void>(`/forum/${id}`, { method: "DELETE" }),
}

// Comentarios
export const comentarioApi = {
  list: (filters?: {
    idDisciplina?: number
    idMaterial?: number
    comentarioPaiId?: number
    page?: number
    size?: number
  }) =>
    request<PageResponse<Comentario>>("/comentario", {
      params: {
        ...filters,
        page: filters?.page ?? 0,
        size: filters?.size ?? 20,
      },
    }),
  
  getById: (id: number) => request<Comentario>(`/comentario/${id}`),
  
  create: (data: {
    conteudo: string
    disciplinaId?: number
    materialId?: number
    postForumId?: number
    comentarioPaiId?: number
  }) =>
    request<Comentario>("/comentario", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  update: (id: number, data: { conteudo: string }) =>
    request<Comentario>(`/comentario/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  
  curtir: (id: number) =>
    request<Comentario>(`/comentario/${id}/curtir`, { method: "PATCH" }),
  
  delete: (id: number) =>
    request<void>(`/comentario/${id}`, { method: "DELETE" }),
}

// Favoritos
export const favoritoApi = {
  list: (page = 0, size = 20) =>
    request<PageResponse<Favorito>>("/favorito", {
      params: { page, size },
    }),
  
  add: (data: { tipoItem: string; itemId: number }) =>
    request<Favorito>("/favorito", {
      method: "POST",
      body: JSON.stringify(data),
    }),
  
  remove: (id: number) =>
    request<void>(`/favorito/${id}`, { method: "DELETE" }),
}

export { ApiError }
