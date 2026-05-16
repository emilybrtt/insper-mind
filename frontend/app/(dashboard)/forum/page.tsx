"use client";

import { useState } from "react";
import useSWR from "swr";
import { forumApi, comentarioApi, PostForum, ForumCategoria, Comentario } from "@/lib/api";
import { useAuth } from "@/lib/auth-context";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Avatar, AvatarFallback } from "@/components/ui/avatar";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Label } from "@/components/ui/label";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { PageLoader, CardSkeleton } from "@/components/loading";
import { ErrorState } from "@/components/error-state";
import {
  Plus,
  MessageSquare,
  ThumbsUp,
  Clock,
  Search,
  Filter,
  Send,
  ArrowLeft,
} from "lucide-react";

const CATEGORIES: { value: ForumCategoria; label: string; color: string }[] = [
  { value: "ADMINISTRATIVO", label: "Administrativo", color: "bg-blue-500/10 text-blue-600 dark:text-blue-400" },
  { value: "TECNICO", label: "Tecnico", color: "bg-purple-500/10 text-purple-600 dark:text-purple-400" },
  { value: "GERAL", label: "Geral", color: "bg-green-500/10 text-green-600 dark:text-green-400" },
];

function getCategoryStyle(category: string) {
  return CATEGORIES.find((c) => c.value === category)?.color || "bg-muted text-muted-foreground";
}

function getCategoryLabel(category: string) {
  return CATEGORIES.find((c) => c.value === category)?.label || category;
}

function formatDate(dateString: string) {
  const date = new Date(dateString);
  const now = new Date();
  const diff = now.getTime() - date.getTime();
  const hours = Math.floor(diff / (1000 * 60 * 60));
  const days = Math.floor(hours / 24);

  if (hours < 1) return "Agora mesmo";
  if (hours < 24) return `${hours}h atras`;
  if (days < 7) return `${days}d atras`;
  return date.toLocaleDateString("pt-BR");
}

function PostCard({
  post,
  onClick,
}: {
  post: PostForum;
  onClick: () => void;
}) {
  return (
    <Card
      className="cursor-pointer transition-all hover:shadow-md hover:border-primary/20"
      onClick={onClick}
    >
      <CardContent className="p-4">
        <div className="flex gap-3">
          <Avatar className="h-10 w-10 shrink-0">
            <AvatarFallback className="bg-primary/10 text-primary text-sm">
              {post.usuario.nome.charAt(0).toUpperCase()}
            </AvatarFallback>
          </Avatar>
          <div className="flex-1 min-w-0">
            <div className="flex items-start justify-between gap-2">
              <div className="min-w-0">
                <h3 className="font-semibold text-foreground line-clamp-1">
                  {post.titulo}
                </h3>
                <p className="text-sm text-muted-foreground">
                  {post.usuario.nome}
                </p>
              </div>
              <Badge variant="secondary" className={getCategoryStyle(post.categoria)}>
                {getCategoryLabel(post.categoria)}
              </Badge>
            </div>
            <p className="mt-2 text-sm text-muted-foreground line-clamp-2">
              {post.conteudo}
            </p>
            <div className="mt-3 flex items-center gap-4 text-xs text-muted-foreground">
              <span className="flex items-center gap-1">
                <Clock className="h-3 w-3" />
                {formatDate(post.createdAt)}
              </span>
              <span className="flex items-center gap-1">
                <MessageSquare className="h-3 w-3" />
                {post.totalComentarios} comentarios
              </span>
              <span className="flex items-center gap-1">
                <ThumbsUp className="h-3 w-3" />
                {post.curtidas}
              </span>
            </div>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}

function PostDetail({
  post,
  onBack,
  onUpdated,
}: {
  post: PostForum;
  onBack: () => void;
  onUpdated: () => void;
}) {
  const [comment, setComment] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);
  
  const { data: comments, mutate: mutateComments } = useSWR(
    `forum-comments-${post.id}`,
    () => comentarioApi.list({ page: 0, size: 50 })
  );

  const handleLike = async () => {
    try {
      await forumApi.curtir(post.id);
      onUpdated();
    } catch (error) {
      console.error("Error liking post:", error);
    }
  };

  const handleComment = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!comment.trim()) return;

    setIsSubmitting(true);
    try {
      await comentarioApi.create({
        conteudo: comment,
        postForumId: post.id,
      });
      setComment("");
      mutateComments();
      onUpdated();
    } catch (error) {
      console.error("Error adding comment:", error);
    } finally {
      setIsSubmitting(false);
    }
  };

  const postComments = comments?.content || [];

  return (
    <div className="space-y-4">
      <Button variant="ghost" size="sm" onClick={onBack} className="gap-2">
        <ArrowLeft className="h-4 w-4" />
        Voltar
      </Button>

      <Card>
        <CardHeader>
          <div className="flex items-start justify-between gap-4">
            <div className="flex gap-3">
              <Avatar className="h-12 w-12">
                <AvatarFallback className="bg-primary/10 text-primary">
                  {post.usuario.nome.charAt(0).toUpperCase()}
                </AvatarFallback>
              </Avatar>
              <div>
                <CardTitle className="text-xl">{post.titulo}</CardTitle>
                <p className="text-sm text-muted-foreground mt-1">
                  {post.usuario.nome} • {formatDate(post.createdAt)}
                </p>
              </div>
            </div>
            <Badge variant="secondary" className={getCategoryStyle(post.categoria)}>
              {getCategoryLabel(post.categoria)}
            </Badge>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          <p className="text-foreground whitespace-pre-wrap">{post.conteudo}</p>
          <div className="flex items-center gap-4 pt-2">
            <Button variant="outline" size="sm" onClick={handleLike} className="gap-2">
              <ThumbsUp className="h-4 w-4" />
              {post.curtidas}
            </Button>
            <span className="text-sm text-muted-foreground flex items-center gap-1">
              <MessageSquare className="h-4 w-4" />
              {post.totalComentarios} comentarios
            </span>
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Comentarios</CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          {postComments.length > 0 ? (
            <ScrollArea className="max-h-[400px]">
              <div className="space-y-4">
                {postComments.map((comentario: Comentario, index: number) => (
                  <div key={comentario.id}>
                    <div className="flex gap-3">
                      <Avatar className="h-8 w-8">
                        <AvatarFallback className="bg-muted text-muted-foreground text-xs">
                          {comentario.usuario.nome.charAt(0).toUpperCase()}
                        </AvatarFallback>
                      </Avatar>
                      <div className="flex-1">
                        <div className="flex items-center gap-2">
                          <span className="font-medium text-sm">
                            {comentario.usuario.nome}
                          </span>
                          <span className="text-xs text-muted-foreground">
                            {formatDate(comentario.createdAt)}
                          </span>
                        </div>
                        <p className="text-sm text-muted-foreground mt-1">
                          {comentario.conteudo}
                        </p>
                      </div>
                    </div>
                    {index < postComments.length - 1 && (
                      <Separator className="mt-4" />
                    )}
                  </div>
                ))}
              </div>
            </ScrollArea>
          ) : (
            <p className="text-sm text-muted-foreground text-center py-8">
              Nenhum comentario ainda. Seja o primeiro a comentar!
            </p>
          )}

          <Separator />

          <form onSubmit={handleComment} className="flex gap-2">
            <Input
              placeholder="Escreva um comentario..."
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              disabled={isSubmitting}
            />
            <Button type="submit" size="icon" disabled={isSubmitting || !comment.trim()}>
              <Send className="h-4 w-4" />
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}

function CreatePostDialog({
  open,
  onOpenChange,
  onSuccess,
}: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSuccess: () => void;
}) {
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [formData, setFormData] = useState({
    titulo: "",
    conteudo: "",
    categoria: "GERAL" as ForumCategoria,
  });
  const [errors, setErrors] = useState<Record<string, string>>({});

  const validate = () => {
    const newErrors: Record<string, string> = {};
    if (!formData.titulo.trim()) {
      newErrors.titulo = "Titulo e obrigatorio";
    } else if (formData.titulo.length < 5) {
      newErrors.titulo = "Titulo deve ter pelo menos 5 caracteres";
    }
    if (!formData.conteudo.trim()) {
      newErrors.conteudo = "Conteudo e obrigatorio";
    } else if (formData.conteudo.length < 10) {
      newErrors.conteudo = "Conteudo deve ter pelo menos 10 caracteres";
    }
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!validate()) return;

    setIsSubmitting(true);
    try {
      await forumApi.create(formData);
      setFormData({ titulo: "", conteudo: "", categoria: "GERAL" });
      onOpenChange(false);
      onSuccess();
    } catch (error) {
      console.error("Error creating post:", error);
      setErrors({ submit: "Erro ao criar postagem. Tente novamente." });
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-[500px]">
        <DialogHeader>
          <DialogTitle>Nova Postagem</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="titulo">Titulo</Label>
            <Input
              id="titulo"
              placeholder="Digite o titulo da sua postagem"
              value={formData.titulo}
              onChange={(e) =>
                setFormData({ ...formData, titulo: e.target.value })
              }
            />
            {errors.titulo && (
              <p className="text-sm text-destructive">{errors.titulo}</p>
            )}
          </div>

          <div className="space-y-2">
            <Label htmlFor="categoria">Categoria</Label>
            <Select
              value={formData.categoria}
              onValueChange={(value) =>
                setFormData({ ...formData, categoria: value as ForumCategoria })
              }
            >
              <SelectTrigger>
                <SelectValue />
              </SelectTrigger>
              <SelectContent>
                {CATEGORIES.map((cat) => (
                  <SelectItem key={cat.value} value={cat.value}>
                    {cat.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          <div className="space-y-2">
            <Label htmlFor="conteudo">Conteudo</Label>
            <Textarea
              id="conteudo"
              placeholder="Descreva sua duvida, discussao ou compartilhamento..."
              rows={5}
              value={formData.conteudo}
              onChange={(e) =>
                setFormData({ ...formData, conteudo: e.target.value })
              }
            />
            {errors.conteudo && (
              <p className="text-sm text-destructive">{errors.conteudo}</p>
            )}
          </div>

          {errors.submit && (
            <p className="text-sm text-destructive">{errors.submit}</p>
          )}

          <div className="flex justify-end gap-2">
            <Button
              type="button"
              variant="outline"
              onClick={() => onOpenChange(false)}
            >
              Cancelar
            </Button>
            <Button type="submit" disabled={isSubmitting}>
              {isSubmitting ? "Publicando..." : "Publicar"}
            </Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
}

export default function ForumPage() {
  const [selectedPost, setSelectedPost] = useState<PostForum | null>(null);
  const [searchQuery, setSearchQuery] = useState("");
  const [categoryFilter, setCategoryFilter] = useState<string>("all");
  const [isCreateOpen, setIsCreateOpen] = useState(false);

  const {
    data: postsResponse,
    error,
    isLoading,
    mutate,
  } = useSWR("forum-posts", () => forumApi.list({ page: 0, size: 50 }));

  const posts = postsResponse?.content || [];

  const filteredPosts = posts.filter((post) => {
    const matchesSearch =
      !searchQuery ||
      post.titulo.toLowerCase().includes(searchQuery.toLowerCase()) ||
      post.conteudo.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory =
      categoryFilter === "all" || post.categoria === categoryFilter;
    return matchesSearch && matchesCategory;
  });

  const recentPosts = filteredPosts.slice(0, 10);
  const popularPosts = [...filteredPosts]
    .sort((a, b) => b.curtidas - a.curtidas)
    .slice(0, 10);

  if (isLoading) return <PageLoader />;
  if (error) return <ErrorState message="Erro ao carregar forum" onRetry={mutate} />;

  if (selectedPost) {
    return (
      <div className="space-y-6">
        <PostDetail
          post={selectedPost}
          onBack={() => setSelectedPost(null)}
          onUpdated={() => {
            mutate();
          }}
        />
      </div>
    );
  }

  return (
    <div className="space-y-6">
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold text-foreground">Forum de Discussao</h1>
          <p className="text-muted-foreground">
            Compartilhe duvidas e conhecimentos com a comunidade
          </p>
        </div>
        <Button onClick={() => setIsCreateOpen(true)} className="gap-2">
          <Plus className="h-4 w-4" />
          Nova Postagem
        </Button>
      </div>

      <div className="flex flex-col gap-3 sm:flex-row">
        <div className="relative flex-1">
          <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
          <Input
            placeholder="Buscar postagens..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            className="pl-9"
          />
        </div>
        <Select value={categoryFilter} onValueChange={setCategoryFilter}>
          <SelectTrigger className="w-full sm:w-[180px]">
            <Filter className="h-4 w-4 mr-2" />
            <SelectValue placeholder="Categoria" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">Todas</SelectItem>
            {CATEGORIES.map((cat) => (
              <SelectItem key={cat.value} value={cat.value}>
                {cat.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <Tabs defaultValue="recent" className="space-y-4">
        <TabsList>
          <TabsTrigger value="recent">Recentes</TabsTrigger>
          <TabsTrigger value="popular">Populares</TabsTrigger>
        </TabsList>

        <TabsContent value="recent" className="space-y-3">
          {recentPosts.length > 0 ? (
            recentPosts.map((post) => (
              <PostCard
                key={post.id}
                post={post}
                onClick={() => setSelectedPost(post)}
              />
            ))
          ) : (
            <Card>
              <CardContent className="py-12 text-center">
                <MessageSquare className="h-12 w-12 mx-auto text-muted-foreground/50" />
                <h3 className="mt-4 font-semibold">Nenhuma postagem encontrada</h3>
                <p className="text-sm text-muted-foreground mt-1">
                  {searchQuery || categoryFilter !== "all"
                    ? "Tente ajustar os filtros de busca"
                    : "Seja o primeiro a iniciar uma discussao!"}
                </p>
              </CardContent>
            </Card>
          )}
        </TabsContent>

        <TabsContent value="popular" className="space-y-3">
          {popularPosts.length > 0 ? (
            popularPosts.map((post) => (
              <PostCard
                key={post.id}
                post={post}
                onClick={() => setSelectedPost(post)}
              />
            ))
          ) : (
            <Card>
              <CardContent className="py-12 text-center">
                <ThumbsUp className="h-12 w-12 mx-auto text-muted-foreground/50" />
                <h3 className="mt-4 font-semibold">Nenhuma postagem popular</h3>
                <p className="text-sm text-muted-foreground mt-1">
                  As postagens mais curtidas aparecerao aqui
                </p>
              </CardContent>
            </Card>
          )}
        </TabsContent>
      </Tabs>

      <CreatePostDialog
        open={isCreateOpen}
        onOpenChange={setIsCreateOpen}
        onSuccess={mutate}
      />
    </div>
  );
}
