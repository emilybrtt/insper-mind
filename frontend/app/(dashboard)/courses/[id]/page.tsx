"use client"

import { useEffect, useState } from "react"
import { useParams } from "next/navigation"
import Link from "next/link"
import {
  GraduationCap,
  BookOpen,
  ChevronRight,
  ArrowLeft,
  Users,
  Clock,
} from "lucide-react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import {
  Accordion,
  AccordionContent,
  AccordionItem,
  AccordionTrigger,
} from "@/components/ui/accordion"
import { cursoApi, semestreApi, disciplinaApi, Curso, Semestre, Disciplina, PageResponse } from "@/lib/api"
import { PageLoader } from "@/components/loading"
import { ErrorState } from "@/components/error-state"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"

export default function CourseDetailPage() {
  const params = useParams()
  const courseId = Number(params.id)
  
  const [course, setCourse] = useState<Curso | null>(null)
  const [semesters, setSemesters] = useState<Semestre[]>([])
  const [disciplines, setDisciplines] = useState<Disciplina[]>([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState<string | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)
        setError(null)
        
        const [courseData, semestersData, disciplinesData] = await Promise.all([
          cursoApi.getById(courseId),
          semestreApi.list(0, 50),
          disciplinaApi.list(0, 100),
        ])
        
        setCourse(courseData)
        setSemesters(semestersData.content || [])
        setDisciplines(disciplinesData.content || [])
      } catch (err) {
        setError("Failed to load course details")
        console.error("[v0] Error fetching course:", err)
      } finally {
        setLoading(false)
      }
    }
    
    if (courseId) fetchData()
  }, [courseId])

  if (loading) return <PageLoader />

  if (error || !course) {
    return (
      <ErrorState
        title="Failed to load course"
        message={error || "Course not found"}
        onRetry={() => window.location.reload()}
      />
    )
  }

  // Group disciplines by semester
  const getDisciplinesBySemester = (semesterId: number) => {
    return disciplines.filter((d) => d.semestreId === semesterId)
  }

  return (
    <div className="space-y-6">
      {/* Back button */}
      <Button variant="ghost" size="sm" asChild className="-ml-2">
        <Link href="/courses">
          <ArrowLeft className="mr-2 h-4 w-4" />
          Back to Courses
        </Link>
      </Button>

      {/* Course Header */}
      <div className="flex flex-col gap-4 sm:flex-row sm:items-start sm:justify-between">
        <div className="flex items-start gap-4">
          <div className="rounded-xl bg-primary/10 p-4 shrink-0">
            <GraduationCap className="h-8 w-8 text-primary" />
          </div>
          <div>
            <h1 className="text-2xl font-bold tracking-tight lg:text-3xl">{course.nome}</h1>
            <p className="text-muted-foreground mt-1 max-w-2xl">
              {course.descricao || "No description available"}
            </p>
            <div className="flex items-center gap-4 mt-3 text-sm text-muted-foreground">
              <div className="flex items-center gap-1">
                <BookOpen className="h-4 w-4" />
                <span>{semesters.length} Semesters</span>
              </div>
              <div className="flex items-center gap-1">
                <Clock className="h-4 w-4" />
                <span>{disciplines.length} Subjects</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Semesters */}
      <div className="space-y-4">
        <h2 className="text-lg font-semibold">Curriculum Structure</h2>
        
        {semesters.length > 0 ? (
          <Accordion type="multiple" className="space-y-3">
            {semesters.map((semester) => {
              const semesterDisciplines = getDisciplinesBySemester(semester.id)
              return (
                <AccordionItem
                  key={semester.id}
                  value={`semester-${semester.id}`}
                  className="border rounded-lg px-4 bg-card"
                >
                  <AccordionTrigger className="hover:no-underline py-4">
                    <div className="flex items-center gap-3">
                      <Badge variant="secondary" className="font-mono">
                        {semester.numero}
                      </Badge>
                      <span className="font-medium">Semester {semester.numero}</span>
                      <span className="text-sm text-muted-foreground">
                        ({semesterDisciplines.length} subjects)
                      </span>
                    </div>
                  </AccordionTrigger>
                  <AccordionContent className="pb-4">
                    {semesterDisciplines.length > 0 ? (
                      <div className="grid gap-3 sm:grid-cols-2">
                        {semesterDisciplines.map((discipline) => (
                          <Link key={discipline.id} href={`/subjects/${discipline.id}`}>
                            <Card className="hover:border-primary/50 hover:shadow-md transition-all duration-200 h-full">
                              <CardContent className="p-4">
                                <div className="flex items-start justify-between gap-2">
                                  <div className="min-w-0 flex-1">
                                    <h4 className="font-medium text-sm line-clamp-1">
                                      {discipline.nome}
                                    </h4>
                                    <p className="text-xs text-muted-foreground line-clamp-1 mt-0.5">
                                      {discipline.descricao || "No description"}
                                    </p>
                                    <div className="flex items-center gap-2 mt-2">
                                      <Badge variant="outline" className="text-[10px]">
                                        {discipline.cargaHoraria}h
                                      </Badge>
                                      {discipline.docentes?.length > 0 && (
                                        <div className="flex items-center gap-1 text-[10px] text-muted-foreground">
                                          <Users className="h-3 w-3" />
                                          {discipline.docentes.length} instructor{discipline.docentes.length !== 1 ? "s" : ""}
                                        </div>
                                      )}
                                    </div>
                                  </div>
                                  <ChevronRight className="h-4 w-4 text-muted-foreground shrink-0" />
                                </div>
                              </CardContent>
                            </Card>
                          </Link>
                        ))}
                      </div>
                    ) : (
                      <p className="text-sm text-muted-foreground text-center py-4">
                        No subjects assigned to this semester yet
                      </p>
                    )}
                  </AccordionContent>
                </AccordionItem>
              )
            })}
          </Accordion>
        ) : (
          <Card>
            <CardContent className="py-10 text-center text-muted-foreground">
              <BookOpen className="h-8 w-8 mx-auto mb-2 opacity-50" />
              <p>No semesters have been added to this course yet</p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  )
}
