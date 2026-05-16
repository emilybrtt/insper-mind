"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import {
  Users,
  Search,
  Mail,
  Building,
  BookOpen,
  ChevronRight,
  Filter,
} from "lucide-react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { Button } from "@/components/ui/button"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { docenteApi, authApi, PageResponse, Docente, Usuario } from "@/lib/api"
import { PageLoader } from "@/components/loading"
import { ErrorState, EmptyState } from "@/components/error-state"

export default function ContactsPage() {
  const [docentes, setDocentes] = useState<PageResponse<Docente> | null>(null)
  const [usuarios, setUsuarios] = useState<PageResponse<Usuario> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState("")
  const [activeTab, setActiveTab] = useState("instructors")

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        setError(null)
        const [docentesData, usuariosData] = await Promise.all([
          docenteApi.list(0, 50),
          authApi.listUsers(0, 50),
        ])
        setDocentes(docentesData)
        setUsuarios(usuariosData)
      } catch (err) {
        setError("Failed to load contacts")
        console.error("[v0] Error fetching contacts:", err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const filteredDocentes = docentes?.content?.filter(
    (docente) =>
      docente.nome.toLowerCase().includes(search.toLowerCase()) ||
      docente.email.toLowerCase().includes(search.toLowerCase()) ||
      docente.especialidade?.toLowerCase().includes(search.toLowerCase())
  )

  const filteredUsuarios = usuarios?.content?.filter(
    (usuario) =>
      usuario.nome.toLowerCase().includes(search.toLowerCase()) ||
      usuario.email.toLowerCase().includes(search.toLowerCase())
  )

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase()
      .slice(0, 2)
  }

  if (loading) return <PageLoader />

  if (error) {
    return (
      <ErrorState
        title="Failed to load contacts"
        message={error}
        onRetry={() => window.location.reload()}
      />
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Contacts Portal</h1>
        <p className="text-muted-foreground mt-1">
          Connect with instructors and fellow students
        </p>
      </div>

      {/* Search */}
      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
        <Input
          placeholder="Search by name, email, or specialty..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="pl-9"
        />
      </div>

      {/* Tabs */}
      <Tabs value={activeTab} onValueChange={setActiveTab} className="space-y-6">
        <TabsList>
          <TabsTrigger value="instructors">
            <Building className="mr-2 h-4 w-4" />
            Instructors ({docentes?.totalElements || 0})
          </TabsTrigger>
          <TabsTrigger value="students">
            <Users className="mr-2 h-4 w-4" />
            Students ({usuarios?.content?.filter(u => u.role === "ALUNO").length || 0})
          </TabsTrigger>
        </TabsList>

        {/* Instructors Tab */}
        <TabsContent value="instructors" className="space-y-4">
          {filteredDocentes?.length ? (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {filteredDocentes.map((docente) => (
                <Card
                  key={docente.id}
                  className="hover:border-primary/50 hover:shadow-lg transition-all duration-200"
                >
                  <CardContent className="p-5">
                    <div className="flex items-start gap-4">
                      <Avatar className="h-14 w-14 shrink-0">
                        <AvatarFallback className="bg-primary/10 text-primary text-lg font-semibold">
                          {getInitials(docente.nome)}
                        </AvatarFallback>
                      </Avatar>
                      <div className="min-w-0 flex-1">
                        <h3 className="font-semibold truncate">{docente.nome}</h3>
                        {docente.especialidade && (
                          <Badge variant="secondary" className="mt-1 text-xs">
                            {docente.especialidade}
                          </Badge>
                        )}
                        <a
                          href={`mailto:${docente.email}`}
                          className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-primary transition-colors mt-2"
                        >
                          <Mail className="h-3.5 w-3.5" />
                          <span className="truncate">{docente.email}</span>
                        </a>
                        {docente.bio && (
                          <p className="text-xs text-muted-foreground line-clamp-2 mt-2">
                            {docente.bio}
                          </p>
                        )}
                      </div>
                    </div>
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : (
            <EmptyState
              icon={Building}
              title="No instructors found"
              description={search ? "Try adjusting your search terms" : "No instructors available yet"}
            />
          )}
        </TabsContent>

        {/* Students Tab */}
        <TabsContent value="students" className="space-y-4">
          {filteredUsuarios?.filter(u => u.role === "ALUNO")?.length ? (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {filteredUsuarios
                .filter((usuario) => usuario.role === "ALUNO")
                .map((usuario) => (
                  <Card
                    key={usuario.id}
                    className="hover:border-primary/50 hover:shadow-lg transition-all duration-200"
                  >
                    <CardContent className="p-5">
                      <div className="flex items-start gap-4">
                        <Avatar className="h-12 w-12 shrink-0">
                          <AvatarFallback className="bg-emerald-500/10 text-emerald-600 dark:text-emerald-400 font-medium">
                            {getInitials(usuario.nome)}
                          </AvatarFallback>
                        </Avatar>
                        <div className="min-w-0 flex-1">
                          <div className="flex items-center gap-2">
                            <h3 className="font-semibold truncate">{usuario.nome}</h3>
                            {usuario.ativo && (
                              <span className="h-2 w-2 rounded-full bg-emerald-500 shrink-0" />
                            )}
                          </div>
                          <a
                            href={`mailto:${usuario.email}`}
                            className="flex items-center gap-1.5 text-sm text-muted-foreground hover:text-primary transition-colors mt-1"
                          >
                            <Mail className="h-3.5 w-3.5" />
                            <span className="truncate">{usuario.email}</span>
                          </a>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                ))}
            </div>
          ) : (
            <EmptyState
              icon={Users}
              title="No students found"
              description={search ? "Try adjusting your search terms" : "No students available yet"}
            />
          )}
        </TabsContent>
      </Tabs>

      {/* Quick Stats */}
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4 pt-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-primary/10 p-2">
                <Building className="h-4 w-4 text-primary" />
              </div>
              <div>
                <p className="text-2xl font-bold">{docentes?.totalElements || 0}</p>
                <p className="text-xs text-muted-foreground">Instructors</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-emerald-500/10 p-2">
                <Users className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">
                  {usuarios?.content?.filter((u) => u.role === "ALUNO").length || 0}
                </p>
                <p className="text-xs text-muted-foreground">Students</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-amber-500/10 p-2">
                <BookOpen className="h-4 w-4 text-amber-600 dark:text-amber-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">
                  {usuarios?.content?.filter((u) => u.role === "ADMIN").length || 0}
                </p>
                <p className="text-xs text-muted-foreground">Admins</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-500/10 p-2">
                <Mail className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{usuarios?.totalElements || 0}</p>
                <p className="text-xs text-muted-foreground">Total Users</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>
    </div>
  )
}
