"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import { GraduationCap, Search, ChevronRight, BookOpen, Users } from "lucide-react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Input } from "@/components/ui/input"
import { Badge } from "@/components/ui/badge"
import { cursoApi, PageResponse, Curso } from "@/lib/api"
import { PageLoader } from "@/components/loading"
import { ErrorState, EmptyState } from "@/components/error-state"

export default function CoursesPage() {
  const [courses, setCourses] = useState<PageResponse<Curso> | null>(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)
  const [search, setSearch] = useState("")

  useEffect(() => {
    const fetchCourses = async () => {
      try {
        setLoading(true)
        setError(null)
        const data = await cursoApi.list(0, 50)
        setCourses(data)
      } catch (err) {
        setError("Failed to load courses")
        console.error("[v0] Error fetching courses:", err)
      } finally {
        setLoading(false)
      }
    }
    fetchCourses()
  }, [])

  const filteredCourses = courses?.content?.filter(
    (course) =>
      course.nome.toLowerCase().includes(search.toLowerCase()) ||
      course.descricao?.toLowerCase().includes(search.toLowerCase())
  )

  if (loading) return <PageLoader />

  if (error) {
    return (
      <ErrorState
        title="Failed to load courses"
        message={error}
        onRetry={() => window.location.reload()}
      />
    )
  }

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">Course Structure</h1>
          <p className="text-muted-foreground mt-1">
            Browse all available academic programs
          </p>
        </div>
      </div>

      {/* Search */}
      <div className="relative max-w-md">
        <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
        <Input
          placeholder="Search courses..."
          value={search}
          onChange={(e) => setSearch(e.target.value)}
          className="pl-9"
        />
      </div>

      {/* Courses Grid */}
      {filteredCourses?.length ? (
        <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
          {filteredCourses.map((course) => (
            <Link key={course.id} href={`/courses/${course.id}`}>
              <Card className="h-full hover:border-primary/50 hover:shadow-lg transition-all duration-200 group">
                <CardHeader className="pb-3">
                  <div className="flex items-start justify-between">
                    <div className="rounded-xl bg-primary/10 p-3">
                      <GraduationCap className="h-6 w-6 text-primary" />
                    </div>
                    <ChevronRight className="h-5 w-5 text-muted-foreground group-hover:text-primary transition-colors" />
                  </div>
                </CardHeader>
                <CardContent className="space-y-3">
                  <div>
                    <CardTitle className="text-lg line-clamp-1">{course.nome}</CardTitle>
                    <CardDescription className="line-clamp-2 mt-1">
                      {course.descricao || "No description available"}
                    </CardDescription>
                  </div>
                  <div className="flex items-center gap-3 text-sm text-muted-foreground">
                    <div className="flex items-center gap-1">
                      <BookOpen className="h-4 w-4" />
                      <span>{course.semestres?.length || 0} semesters</span>
                    </div>
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      ) : (
        <EmptyState
          icon={GraduationCap}
          title="No courses found"
          description={search ? "Try adjusting your search terms" : "No courses are available yet"}
        />
      )}
    </div>
  )
}
