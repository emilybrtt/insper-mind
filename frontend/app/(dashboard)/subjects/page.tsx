"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { BookOpen, Search, ChevronRight, Clock, Users, Filter } from "lucide-react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
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
import { disciplinaApi, cursoApi, PageResponse, Disciplina, Curso } from "@/lib/api"
import { PageLoader } from "@/components/loading"
import { ErrorState, EmptyState } from "@/components/error-state"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"

export default function SubjectsPage() {
  const [disciplines, setDisciplines] = useState<PageResponse<Disciplina> | null>(null)
  const [courses, setCourses] = useState<PageResponse<Curso> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState("")
  const [selectedCourse, setSelectedCourse] = useState<string>("all")

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        setError(null)
        const [disciplinesData, coursesData] = await Promise.all([
          disciplinaApi.list(0, 100),
          cursoApi.list(0, 50),
        ])
        setDisciplines(disciplinesData)
        setCourses(coursesData)
      } catch (err) {
        setError("Failed to load subjects")
        console.error("[v0] Error fetching subjects:", err)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const filteredDisciplines = disciplines?.content?.filter((discipline) => {
    const matchesSearch =
      discipline.nome.toLowerCase().includes(search.toLowerCase()) ||
      discipline.descricao?.toLowerCase().includes(search.toLowerCase())
    return matchesSearch
  })

  if (loading) return <PageLoader />

  if (error) {
    return (
      <ErrorState
        title="Failed to load subjects"
        message={error}
        onRetry={() => window.location.reload()}
      />
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Subjects</h1>
        <p className="text-muted-foreground mt-1">
          Browse all available subjects and disciplines
        </p>
      </div>

      {/* Filters */}
      <div className="flex flex-col gap-3 sm:flex-row">
        <div className="relative flex-1 max-w-md">
          <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
          <Input
            placeholder="Search subjects..."
            value={search}
            onChange={(e) => setSearch(e.target.value)}
            className="pl-9"
          />
        </div>
        <Select value={selectedCourse} onValueChange={setSelectedCourse}>
          <SelectTrigger className="w-full sm:w-[200px]">
            <SelectValue placeholder="Filter by course" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="all">All Courses</SelectItem>
            {courses?.content?.map((course) => (
              <SelectItem key={course.id} value={String(course.id)}>
                {course.nome}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      {/* Subjects Grid */}
      {filteredDisciplines?.length ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {filteredDisciplines.map((discipline) => (
            <Link key={discipline.id} href={`/subjects/${discipline.id}`}>
              <Card className="h-full hover:border-primary/50 hover:shadow-lg transition-all duration-200 group">
                <CardHeader className="pb-3">
                  <div className="flex items-start justify-between">
                    <div className="rounded-xl bg-emerald-500/10 p-3">
                      <BookOpen className="h-6 w-6 text-emerald-600 dark:text-emerald-400" />
                    </div>
                    <ChevronRight className="h-5 w-5 text-muted-foreground group-hover:text-primary transition-colors" />
                  </div>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div>
                    <CardTitle className="text-lg line-clamp-1">{discipline.nome}</CardTitle>
                    <CardDescription className="line-clamp-2 mt-1">
                      {discipline.descricao || "No description available"}
                    </CardDescription>
                  </div>
                  <div className="flex items-center gap-3 flex-wrap">
                    <Badge variant="outline" className="text-xs">
                      <Clock className="mr-1 h-3 w-3" />
                      {discipline.cargaHoraria}h
                    </Badge>
                    {discipline.docentes?.length > 0 && (
                      <div className="flex items-center gap-1">
                        <div className="flex -space-x-2">
                          {discipline.docentes.slice(0, 3).map((docente) => (
                            <Avatar key={docente.id} className="h-6 w-6 border-2 border-background">
                              <AvatarFallback className="text-[10px] bg-primary/10 text-primary">
                                {docente.nome.charAt(0)}
                              </AvatarFallback>
                            </Avatar>
                          ))}
                        </div>
                        {discipline.docentes.length > 3 && (
                          <span className="text-xs text-muted-foreground">
                            +{discipline.docentes.length - 3}
                          </span>
                        )}
                      </div>
                    )}
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      ) : (
        <EmptyState
          icon={BookOpen}
          title="No subjects found"
          description={search ? "Try adjusting your search terms" : "No subjects are available yet"}
        />
      )}
    </div>
  )
}
