// User types
export interface Usuario {
  id?: string;
  nome: string;
  email: string;
  senha?: string;
  cpf?: string;
  cursoId?: string;
  tipoUsuario: "ALUNO" | "PROFESSOR" | "ADMIN";
}

export interface SaveUsuarioDTO {
  nome: string;
  email: string;
  senha: string;
  cpf?: string;
  cursoId?: string;
  tipoUsuario: "ALUNO" | "PROFESSOR" | "ADMIN";
}

// Course types
export interface Curso {
  id?: string;
  nome: string;
  descricao?: string;
  duracao?: number;
  semestres?: Semestre[];
}

export interface Semestre {
  id?: string;
  numero: number;
  cursoId?: string;
  disciplinas?: Disciplina[];
}

// Subject types
export interface Disciplina {
visibilidadeGeral: boolean;
  id?: string;
  nome: string;
  descricao?: string;
  cargaHoraria?: number;
  semestreId?: string;
  materiais?: Material[];
  docentes?: Docente[];
}

// Material types
export interface Material {
  id?: string;
  titulo: string;
  descricao?: string;
  url?: string;
  tipo: TipoMaterial;
  disciplinaId?: string;
  dataCriacao?: string;
}

export type TipoMaterial =
  | "PDF"
  | "VIDEO"
  | "LINK"
  | "DOCUMENTO"
  | "APRESENTACAO"
  | "OUTRO";

// Teacher types
export interface Docente {
  id?: string;
  nome: string;
  email: string;
  telefone?: string;
  departamento?: string;
  sala?: string;
  disciplinas?: Disciplina[];
}

// Forum types
export interface ForumPost {
  id?: string;
  titulo: string;
  conteudo: string;
  autorEmail: string;
  categoria: CategoriaForum;
  dataCriacao: string;
  curtidas?: number;
  comentarios?: Comentario[];
}

export type CategoriaForum =
  | "DUVIDA"
  | "DISCUSSAO"
  | "COMPARTILHAMENTO"
  | "FEEDBACK"
  | "OUTROS";

export interface Comentario {
  id?: string;
  postForumId?: string;
  autorEmail: string;
  conteudo: string;
  dataCriacao?: string;
}

// API Response types
export interface ApiError {
  message: string;
  status?: number;
}

export interface PaginatedResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}
