"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import {
  FileText,
  Search,
  Filter,
  Star,
  Download,
  ExternalLink,
  Plus,
  SlidersHorizontal,
} from "lucide-react"
import { Card, CardContent } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select"
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog"
import { Label } from "@/components/ui/label"
import { Textarea } from "@/components/ui/textarea"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { materialApi, disciplinaApi, cursoApi, PageResponse, Material, MaterialTipo, Disciplina, Curso } from "@/lib/api"
import { PageLoader } from "@/components/loading"
import { ErrorState, EmptyState } from "@/components/error-state"
import { useAuth } from "@/lib/auth-context"

const materialTypes: { value: MaterialTipo; label: string }[] = [
  { value: "PROVA_ANTIGA", label: "Past Exam" },
  { value: "RESUMO", label: "Summary" },
  { value: "EXERCICIO_RESOLVIDO", label: "Solved Exercise" },
  { value: "LISTA", label: "Exercise List" },
  { value: "PDF", label: "PDF" },
  { value: "LIVRO", label: "Book" },
  { value: "OUTRO", label: "Other" },
]

const materialTypeLabels: Record<string, string> = {
  PROVA_ANTIGA: "Past Exam",
  RESUMO: "Summary",
  EXERCICIO_RESOLVIDO: "Solved Exercise",
  LISTA: "Exercise List",
  PDF: "PDF",
  LIVRO: "Book",
  OUTRO: "Other",
}

const materialTypeColors: Record<string, string> = {
  PROVA_ANTIGA: "bg-red-500/10 text-red-600 dark:text-red-400",
  RESUMO: "bg-blue-500/10 text-blue-600 dark:text-blue-400",
  EXERCICIO_RESOLVIDO: "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400",
  LISTA: "bg-purple-500/10 text-purple-600 dark:text-purple-400",
  PDF: "bg-orange-500/10 text-orange-600 dark:text-orange-400",
  LIVRO: "bg-amber-500/10 text-amber-600 dark:text-amber-400",
  OUTRO: "bg-gray-500/10 text-gray-600 dark:text-gray-400",
}

export default function MaterialsPage() {
  const { user } = useAuth()
  const [materials, setMaterials] = useState<PageResponse<Material> | null>(null)
  const [disciplines, setDisciplines] = useState<PageResponse<Disciplina> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState("")
  const [selectedType, setSelectedType] = useState<string>("all")
  const [selectedDiscipline, setSelectedDiscipline] = useState<string>("all")
  const [isCreateOpen, setIsCreateOpen] = useState(false)
  const [isSubmitting, setIsSubmitting] = useState(false)

  // Form state
  const [newMaterial, setNewMaterial] = useState({
    titulo: "",
    descricao: "",
    tipo: "RESUMO" as MaterialTipo,
    link: "",
    disciplinaId: "",
  })

  const fetchMaterials = async () => {
    try {
      setLoading(true)
      setError(null)
      const [materialsData, disciplinesData] = await Promise.all([
        materialApi.list({
          tipo: selectedType !== "all" ? (selectedType as MaterialTipo) : undefined,
          disciplinaId: selectedDiscipline !== "all" ? Number(selectedDiscipline) : undefined,
          page: 0,
          size: 50,
        }),
        disciplinaApi.list(0, 100),
      ])
      setMaterials(materialsData)
      setDisciplines(disciplinesData)
    } catch (err) {
      setError("Failed to load materials")
      console.error("[v0] Error fetching materials:", err)
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    fetchMaterials()
  }, [selectedType, selectedDiscipline])

  const handleCreateMaterial = async () => {
    if (!newMaterial.titulo || !newMaterial.disciplinaId) return

    try {
      setIsSubmitting(true)
      await materialApi.create({
        titulo: newMaterial.titulo,
        descricao: newMaterial.descricao,
        tipo: newMaterial.tipo,
        link: newMaterial.link || undefined,
        disciplinaId: Number(newMaterial.disciplinaId),
      })
      setIsCreateOpen(false)
      setNewMaterial({
        titulo: "",
        descricao: "",
        tipo: "RESUMO",
        link: "",
        disciplinaId: "",
      })
      fetchMaterials()
    } catch (err) {
      console.error("[v0] Error creating material:", err)
    } finally {
      setIsSubmitting(false)
    }
  }

  const handleLike = async (id: number, e: React.MouseEvent) => {
    e.preventDefault()
    e.stopPropagation()
    try {
      await materialApi.curtir(id)
      fetchMaterials()
    } catch (err) {
      console.error("[v0] Error liking material:", err)
    }
  }

  const filteredMaterials = materials?.content?.filter((material) => {
    const matchesSearch =
      material.titulo.toLowerCase().includes(search.toLowerCase()) ||
      material.descricao?.toLowerCase().includes(search.toLowerCase())
    return matchesSearch
  })

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
      year: "numeric",
    })
  }

  if (loading && !materials) return <PageLoader />

  if (error) {
    return (
      <ErrorState
        title="Failed to load materials"
        message={error}
        onRetry={fetchMaterials}
      />
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Materials Repository</h1>
          <p className="text-muted-foreground mt-1">
            Browse and share study materials with your peers
          </p>
        </div>
        <Dialog open={isCreateOpen} onOpenChange={setIsCreateOpen}>
          <DialogTrigger asChild>
            <Button>
              <Plus className="mr-2 h-4 w-4" />
              Add Material
            </Button>
          </DialogTrigger>
          <DialogContent className="sm:max-w-[500px]">
            <DialogHeader>
              <DialogTitle>Add New Material</DialogTitle>
              <DialogDescription>
                Share study materials with your peers
              </DialogDescription>
            </DialogHeader>
            <div className="space-y-4 py-4">
              <div className="space-y-2">
                <Label htmlFor="titulo">Title</Label>
                <Input
                  id="titulo"
                  placeholder="Material title"
                  value={newMaterial.titulo}
                  onChange={(e) => setNewMaterial({ ...newMaterial, titulo: e.target.value })}
                />
              </div>
              <div className="space-y-2">
                <Label htmlFor="descricao">Description</Label>
                <Textarea
                  id="descricao"
                  placeholder="Brief description of the material"
                  value={newMaterial.descricao}
                  onChange={(e) => setNewMaterial({ ...newMaterial, descricao: e.target.value })}
                />
              </div>
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label htmlFor="tipo">Type</Label>
                  <Select
                    value={newMaterial.tipo}
                    onValueChange={(value) => setNewMaterial({ ...newMaterial, tipo: value as MaterialTipo })}
                  >
                    <SelectTrigger>
                      <SelectValue />
                    </SelectTrigger>
                    <SelectContent>
                      {materialTypes.map((type) => (
                        <SelectItem key={type.value} value={type.value}>
                          {type.label}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="disciplina">Subject</Label>
                  <Select
                    value={newMaterial.disciplinaId}
                    onValueChange={(value) => setNewMaterial({ ...newMaterial, disciplinaId: value })}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select subject" />
                    </SelectTrigger>
                    <SelectContent>
                      {disciplines?.content?.map((discipline) => (
                        <SelectItem key={discipline.id} value={String(discipline.id)}>
                          {discipline.nome}
                        </SelectItem>
                      ))}
                    </SelectContent>
                  </Select>
                </div>
              </div>
              <div className="space-y-2">
                <Label htmlFor="link">Link (optional)</Label>
                <Input
                  id="link"
                  type="url"
                  placeholder="https://..."
                  value={newMaterial.link}
                  onChange={(e) => setNewMaterial({ ...newMaterial, link: e.target.value })}
                />
              </div>
            </div>
            <DialogFooter>
              <Button variant="outline" onClick={() => setIsCreateOpen(false)}>
                Cancel
              </Button>
              <Button
                onClick={handleCreateMaterial}
                disabled={isSubmitting || !newMaterial.titulo || !newMaterial.disciplinaId}
              >
                {isSubmitting ? "Creating..." : "Create Material"}
              </Button>
            </DialogFooter>
          </DialogContent>
        </Dialog>
      </div>

      {/* Filters */}
      <div className="flex flex-col gap-3 sm:flex-row">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search materials..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="pl-9"
          />
        </div>
        <Select value={selectedType} onValueChange={setSelectedType}>
          <SelectTrigger className="w-full sm:w-[180px]">
            <SelectValue placeholder="All Types" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Types</SelectItem>
            {materialTypes.map((type) => (
              <SelectItem key={type.value} value={type.value}>
                {type.label}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <Select value={selectedDiscipline} onValueChange={setSelectedDiscipline}>
          <SelectTrigger className="w-full sm:w-[200px]">
            <SelectValue placeholder="All Subjects" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Subjects</SelectItem>
            {disciplines?.content?.map((discipline) => (
              <SelectItem key={discipline.id} value={String(discipline.id)}>
                {discipline.nome}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      {/* Results count */}
      {filteredMaterials && (
        <p className="text-sm text-muted-foreground">
          {filteredMaterials.length} material{filteredMaterials.length !== 1 ? "s" : ""} found
        </p>
      )}

      {/* Materials Grid */}
      {filteredMaterials?.length ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {filteredMaterials.map((material) => (
            <Card
              key={material.id}
              className="hover:border-primary/50 hover:shadow-lg transition-all duration-200 group"
            >
              <CardContent className="p-4">
                <div className="flex flex-col h-full">
                  <div className="flex items-start justify-between gap-2 mb-3">
                    <Badge className={`${materialTypeColors[material.tipo]} border-0 text-xs`}>
                      {materialTypeLabels[material.tipo]}
                    </Badge>
                    {material.link && (
                      <a
                        href={material.link}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-muted-foreground hover:text-primary transition-colors"
                        onClick={(e) => e.stopPropagation()}
                      >
                        <ExternalLink className="h-4 w-4" />
                      </a>
                    )}
                  </div>
                  <h3 className="font-semibold line-clamp-2 mb-1">{material.titulo}</h3>
                  <p className="text-sm text-muted-foreground line-clamp-2 mb-3 flex-1">
                    {material.descricao || "No description"}
                  </p>
                  {material.disciplinaNome && (
                    <Badge variant="outline" className="text-xs mb-3 w-fit">
                      {material.disciplinaNome}
                    </Badge>
                  )}
                  <div className="flex items-center justify-between pt-3 border-t border-border">
                    <div className="flex items-center gap-2">
                      <Avatar className="h-6 w-6">
                        <AvatarFallback className="text-[10px] bg-primary/10 text-primary">
                          {material.usuario.nome.charAt(0)}
                        </AvatarFallback>
                      </Avatar>
                      <span className="text-xs text-muted-foreground truncate max-w-[100px]">
                        {material.usuario.nome}
                      </span>
                    </div>
                    <div className="flex items-center gap-3">
                      <span className="text-xs text-muted-foreground">
                        {formatDate(material.createdAt)}
                      </span>
                      <button
                        onClick={(e) => handleLike(material.id, e)}
                        className={`flex items-center gap-1 text-xs transition-colors ${
                          material.curtidoPorUsuario
                            ? "text-amber-500"
                            : "text-muted-foreground hover:text-amber-500"
                        }`}
                      >
                        <Star className={`h-3.5 w-3.5 ${material.curtidoPorUsuario ? "fill-current" : ""}`} />
                        {material.curtidas}
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
          icon={FileText}
          title="No materials found"
          description={
            search || selectedType !== "all" || selectedDiscipline !== "all"
              ? "Try adjusting your filters"
              : "Be the first to share study materials"
          }
          action={
            <Button onClick={() => setIsCreateOpen(true)}>
              <Plus className="mr-2 h-4 w-4" />
              Add Material
            </Button>
          }
        />
      )}
    </div>
  )
}
