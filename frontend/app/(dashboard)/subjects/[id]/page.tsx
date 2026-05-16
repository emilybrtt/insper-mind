"use client"

import { useEffect, useState } from "react"
import { useParams } from "next/navigation"
import Link from "next/link"
import {
  BookOpen,
  ArrowLeft,
  Clock,
  Users,
  FileText,
  MessageSquare,
  Mail,
  ExternalLink,
  Star,
} from "lucide-react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { Separator } from "@/components/ui/separator"
import { disciplinaApi, materialApi, comentarioApi, Disciplina, Material, Comentario, PageResponse } from "@/lib/api"
import { PageLoader } from "@/components/loading"
import { ErrorState, EmptyState } from "@/components/error-state"

export default function SubjectDetailPage() {
  const params = useParams()
  const subjectId = Number(params.id)
  
  const [discipline, setDiscipline] = useState<Disciplina | null>(null)
  const [materials, setMaterials] = useState<PageResponse<Material> | null>(null)
  const [comments, setComments] = useState<PageResponse<Comentario> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  const materialTypeLabels: Record<string, string> = {
    PROVA_ANTIGA: "Past Exam",
    RESUMO: "Summary",
    EXERCICIO_RESOLVIDO: "Solved Exercise",
    LISTA: "Exercise List",
    PDF: "PDF",
    LIVRO: "Book",
    OUTRO: "Other",
  }

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        setError(null)
        
        const [disciplineData, materialsData, commentsData] = await Promise.all([
          disciplinaApi.getById(subjectId),
          materialApi.list({ disciplinaId: subjectId, page: 0, size: 20 }),
          comentarioApi.list({ idDisciplina: subjectId, page: 0, size: 20 }),
        ])
        
        setDiscipline(disciplineData)
        setMaterials(materialsData)
        setComments(commentsData)
      } catch (err) {
        setError("Failed to load subject details")
        console.error("[v0] Error fetching subject:", err)
      } finally {
        setLoading(false)
      }
    }
    
    if (subjectId) fetchData()
  }, [subjectId])

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    })
  }

  if (loading) return <PageLoader />

  if (error || !discipline) {
    return (
      <ErrorState
        title="Failed to load subject"
        message={error || "Subject not found"}
        onRetry={() => window.location.reload()}
      />
    )
  }

  return (
    <div className="space-y-6">
      {/* Back button */}
      <Button variant="ghost" size="sm" asChild className="-ml-2">
        <Link href="/subjects">
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to Subjects
        </Link>
      </Button>

      {/* Subject Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div className="flex items-start gap-4">
          <div className="rounded-xl bg-emerald-500/10 p-4 shrink-0">
            <BookOpen className="h-8 w-8 text-emerald-600 dark:text-emerald-400" />
          </div>
          <div>
            <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">{discipline.nome}</h1>
            <p className="text-muted-foreground mt-1 max-w-2xl">
              {discipline.descricao || "No description available"}
            </p>
            <div className="flex items-center gap-4 mt-3 flex-wrap">
              <Badge variant="outline">
                <Clock className="mr-1 h-3 w-3" />
                {discipline.cargaHoraria} hours
              </Badge>
              <Badge variant="secondary">
                <FileText className="mr-1 h-3 w-3" />
                {materials?.totalElements || 0} materials
              </Badge>
              <Badge variant="secondary">
                <MessageSquare className="mr-1 h-3 w-3" />
                {comments?.totalElements || 0} comments
              </Badge>
            </div>
          </div>
        </div>
      </div>

      {/* Instructors */}
      {discipline.docentes?.length > 0 && (
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-base">Instructors</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
              {discipline.docentes.map((docente) => (
                <div
                  key={docente.id}
                  className="flex items-center gap-3 rounded-lg border p-3"
                >
                  <Avatar className="h-10 w-10">
                    <AvatarFallback className="bg-primary/10 text-primary">
                      {docente.nome.split(" ").map((n) => n[0]).join("").slice(0, 2)}
                    </AvatarFallback>
                  </Avatar>
                  <div className="min-w-0 flex-1">
                    <p className="font-medium text-sm truncate">{docente.nome}</p>
                    <a
                      href={`mailto:${docente.email}`}
                      className="text-xs text-muted-foreground hover:text-primary flex items-center gap-1 truncate"
                    >
                      <Mail className="h-3 w-3 shrink-0" />
                      {docente.email}
                    </a>
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      {/* Tabs */}
      <Tabs defaultValue="materials" className="space-y-4">
        <TabsList>
          <TabsTrigger value="materials">
            <FileText className="mr-2 h-4 w-4" />
            Materials
          </TabsTrigger>
          <TabsTrigger value="discussion">
            <MessageSquare className="mr-2 h-4 w-4" />
            Discussion
          </TabsTrigger>
        </TabsList>

        <TabsContent value="materials" className="space-y-4">
          {materials?.content?.length ? (
            <div className="grid gap-3">
              {materials.content.map((material) => (
                <Card key={material.id} className="hover:border-primary/30 transition-colors">
                  <CardContent className="p-4">
                    <div className="flex items-start gap-4">
                      <div className="rounded-lg bg-accent p-2.5 shrink-0">
                        <FileText className="h-5 w-5 text-muted-foreground" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <div className="flex items-start justify-between gap-2">
                          <div>
                            <h4 className="font-medium line-clamp-1">{material.titulo}</h4>
                            <p className="text-sm text-muted-foreground line-clamp-1 mt-0.5">
                              {material.descricao}
                            </p>
                          </div>
                          {material.link && (
                            <Button variant="ghost" size="icon" asChild className="shrink-0">
                              <a href={material.link} target="_blank" rel="noopener noreferrer">
                                <ExternalLink className="h-4 w-4" />
                              </a>
                            </Button>
                          )}
                        </div>
                        <div className="flex items-center gap-3 mt-2 flex-wrap">
                          <Badge variant="secondary" className="text-xs">
                            {materialTypeLabels[material.tipo] || material.tipo}
                          </Badge>
                          <span className="flex items-center gap-1 text-xs text-muted-foreground">
                            <Star className="h-3 w-3" />
                            {material.curtidas} likes
                          </span>
                          <span className="text-xs text-muted-foreground">
                            by {material.usuario.nome}
                          </span>
                          <span className="text-xs text-muted-foreground">
                            {formatDate(material.createdAt)}
                          </span>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : (
            <EmptyState
              icon={FileText}
              title="No materials yet"
              description="Be the first to share study materials for this subject"
              action={
                <Button asChild>
                  <Link href="/materials/new">Add Material</Link>
                </Button>
              }
            />
          )}
        </TabsContent>

        <TabsContent value="discussion" className="space-y-4">
          {comments?.content?.length ? (
            <div className="space-y-3">
              {comments.content.map((comment) => (
                <Card key={comment.id}>
                  <CardContent className="p-4">
                    <div className="flex gap-3">
                      <Avatar className="h-8 w-8">
                        <AvatarFallback className="text-xs bg-primary/10 text-primary">
                          {comment.usuario.nome.charAt(0)}
                        </AvatarFallback>
                      </Avatar>
                      <div className="flex-1">
                        <div className="flex items-center gap-2">
                          <span className="font-medium text-sm">{comment.usuario.nome}</span>
                          <span className="text-xs text-muted-foreground">
                            {formatDate(comment.createdAt)}
                          </span>
                        </div>
                        <p className="text-sm mt-1">{comment.conteudo}</p>
                        <div className="flex items-center gap-3 mt-2">
                          <button className="flex items-center gap-1 text-xs text-muted-foreground hover:text-primary transition-colors">
                            <Star className="h-3 w-3" />
                            {comment.curtidas}
                          </button>
                        </div>
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : (
            <EmptyState
              icon={MessageSquare}
              title="No comments yet"
              description="Start the discussion about this subject"
            />
          )}
        </TabsContent>
      </Tabs>
    </div>
  )
}
