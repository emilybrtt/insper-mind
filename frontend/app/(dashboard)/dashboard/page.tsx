"use client"

import { useEffect, useState } from "react"
import Link from "next/link"
import {
  BookOpen,
  FileText,
  Users,
  MessageSquare,
  TrendingUp,
  Clock,
  Star,
  ArrowRight,
  GraduationCap,
} from "lucide-react"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { Button } from "@/components/ui/button"
import { Badge } from "@/components/ui/badge"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import { useAuth } from "@/lib/auth-context"
import { cursoApi, materialApi, forumApi, PageResponse, Curso, Material, PostForum } from "@/lib/api"
import { CardSkeleton } from "@/components/loading"

const quickLinks = [
  { href: "/courses", label: "Courses", icon: GraduationCap, color: "bg-blue-500/10 text-blue-600 dark:text-blue-400" },
  { href: "/subjects", label: "Subjects", icon: BookOpen, color: "bg-emerald-500/10 text-emerald-600 dark:text-emerald-400" },
  { href: "/materials", label: "Materials", icon: FileText, color: "bg-amber-500/10 text-amber-600 dark:text-amber-400" },
  { href: "/forum", label: "Forum", icon: MessageSquare, color: "bg-purple-500/10 text-purple-600 dark:text-purple-400" },
]

export default function DashboardPage() {
  const { user } = useAuth()
  const [courses, setCourses] = useState<PageResponse<Curso> | null>(null)
  const [materials, setMaterials] = useState<PageResponse<Material> | null>(null)
  const [posts, setPosts] = useState<PageResponse<PostForum> | null>(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [coursesRes, materialsRes, postsRes] = await Promise.all([
          cursoApi.list(0, 3).catch(() => null),
          materialApi.list({ page: 0, size: 5 }).catch(() => null),
          forumApi.list({ page: 0, size: 3 }).catch(() => null),
        ])
        setCourses(coursesRes)
        setMaterials(materialsRes)
        setPosts(postsRes)
      } catch (error) {
        console.error("[v0] Failed to fetch dashboard data:", error)
      } finally {
        setLoading(false)
      }
    }
    fetchData()
  }, [])

  const getGreeting = () => {
    const hour = new Date().getHours()
    if (hour < 12) return "Good morning"
    if (hour < 18) return "Good afternoon"
    return "Good evening"
  }

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      month: "short",
      day: "numeric",
    })
  }

  const materialTypeLabels: Record<string, string> = {
    PROVA_ANTIGA: "Past Exam",
    RESUMO: "Summary",
    EXERCICIO_RESOLVIDO: "Solved Exercise",
    LISTA: "Exercise List",
    PDF: "PDF",
    LIVRO: "Book",
    OUTRO: "Other",
  }

  return (
    <div className="space-y-8">
      {/* Header */}
      <div className="flex flex-col gap-2">
        <h1 className="text-2xl font-bold tracking-tight lg:text-3xl text-balance">
          {getGreeting()}, {user?.nome?.split(" ")[0] || "Student"}
        </h1>
        <p className="text-muted-foreground">
          {"Here's what's happening in your academic portal today."}
        </p>
      </div>

      {/* Quick Links */}
      <div className="grid grid-cols-2 gap-3 sm:grid-cols-4">
        {quickLinks.map((link) => {
          const Icon = link.icon
          return (
            <Link key={link.href} href={link.href}>
              <Card className="hover:border-primary/50 hover:shadow-md transition-all duration-200 cursor-pointer h-full">
                <CardContent className="flex flex-col items-center justify-center p-4 text-center gap-3">
                  <div className={`rounded-xl p-2.5 ${link.color}`}>
                    <Icon className="h-5 w-5" />
                  </div>
                  <span className="text-sm font-medium">{link.label}</span>
                </CardContent>
              </Card>
            </Link>
          )
        })}
      </div>

      {/* Stats Overview */}
      <div className="grid grid-cols-2 gap-4 lg:grid-cols-4">
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-primary/10 p-2">
                <GraduationCap className="h-4 w-4 text-primary" />
              </div>
              <div>
                <p className="text-2xl font-bold">{courses?.totalElements ?? 0}</p>
                <p className="text-xs text-muted-foreground">Courses</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-emerald-500/10 p-2">
                <FileText className="h-4 w-4 text-emerald-600 dark:text-emerald-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{materials?.totalElements ?? 0}</p>
                <p className="text-xs text-muted-foreground">Materials</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-purple-500/10 p-2">
                <MessageSquare className="h-4 w-4 text-purple-600 dark:text-purple-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">{posts?.totalElements ?? 0}</p>
                <p className="text-xs text-muted-foreground">Forum Posts</p>
              </div>
            </div>
          </CardContent>
        </Card>
        <Card>
          <CardContent className="p-4">
            <div className="flex items-center gap-3">
              <div className="rounded-lg bg-amber-500/10 p-2">
                <TrendingUp className="h-4 w-4 text-amber-600 dark:text-amber-400" />
              </div>
              <div>
                <p className="text-2xl font-bold">Active</p>
                <p className="text-xs text-muted-foreground">Status</p>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Main Content Grid */}
      <div className="grid gap-6 lg:grid-cols-2">
        {/* Recent Materials */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <div>
              <CardTitle className="text-base font-semibold">Recent Materials</CardTitle>
              <CardDescription>Latest shared resources</CardDescription>
            </div>
            <Button variant="ghost" size="sm" asChild>
              <Link href="/materials">
                View all
                <ArrowRight className="ml-1 h-4 w-4" />
              </Link>
            </Button>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="space-y-3">
                {[1, 2, 3].map((i) => (
                  <div key={i} className="animate-pulse flex items-center gap-3">
                    <div className="h-10 w-10 rounded-lg bg-muted" />
                    <div className="flex-1 space-y-2">
                      <div className="h-4 w-2/3 bg-muted rounded" />
                      <div className="h-3 w-1/3 bg-muted rounded" />
                    </div>
                  </div>
                ))}
              </div>
            ) : materials?.content?.length ? (
              <div className="space-y-3">
                {materials.content.slice(0, 5).map((material) => (
                  <Link
                    key={material.id}
                    href={`/materials/${material.id}`}
                    className="flex items-center gap-3 rounded-lg p-2 -mx-2 hover:bg-accent transition-colors"
                  >
                    <div className="rounded-lg bg-accent p-2.5">
                      <FileText className="h-4 w-4 text-muted-foreground" />
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium truncate">{material.titulo}</p>
                      <div className="flex items-center gap-2 text-xs text-muted-foreground">
                        <Badge variant="secondary" className="text-[10px] px-1.5 py-0">
                          {materialTypeLabels[material.tipo] || material.tipo}
                        </Badge>
                        <span className="flex items-center gap-1">
                          <Star className="h-3 w-3" />
                          {material.curtidas}
                        </span>
                      </div>
                    </div>
                    <span className="text-xs text-muted-foreground">
                      {formatDate(material.createdAt)}
                    </span>
                  </Link>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-muted-foreground">
                <FileText className="h-8 w-8 mx-auto mb-2 opacity-50" />
                <p className="text-sm">No materials yet</p>
              </div>
            )}
          </CardContent>
        </Card>

        {/* Recent Forum Posts */}
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <div>
              <CardTitle className="text-base font-semibold">Forum Activity</CardTitle>
              <CardDescription>Latest discussions</CardDescription>
            </div>
            <Button variant="ghost" size="sm" asChild>
              <Link href="/forum">
                View all
                <ArrowRight className="ml-1 h-4 w-4" />
              </Link>
            </Button>
          </CardHeader>
          <CardContent>
            {loading ? (
              <div className="space-y-3">
                {[1, 2, 3].map((i) => (
                  <div key={i} className="animate-pulse flex items-start gap-3">
                    <div className="h-8 w-8 rounded-full bg-muted" />
                    <div className="flex-1 space-y-2">
                      <div className="h-4 w-3/4 bg-muted rounded" />
                      <div className="h-3 w-1/2 bg-muted rounded" />
                    </div>
                  </div>
                ))}
              </div>
            ) : posts?.content?.length ? (
              <div className="space-y-3">
                {posts.content.map((post) => (
                  <Link
                    key={post.id}
                    href={`/forum/${post.id}`}
                    className="flex items-start gap-3 rounded-lg p-2 -mx-2 hover:bg-accent transition-colors"
                  >
                    <Avatar className="h-8 w-8">
                      <AvatarFallback className="text-xs bg-primary/10 text-primary">
                        {post.usuario.nome.charAt(0).toUpperCase()}
                      </AvatarFallback>
                    </Avatar>
                    <div className="flex-1 min-w-0">
                      <p className="text-sm font-medium line-clamp-1">{post.titulo}</p>
                      <div className="flex items-center gap-2 text-xs text-muted-foreground mt-0.5">
                        <span>{post.usuario.nome}</span>
                        <span className="flex items-center gap-1">
                          <MessageSquare className="h-3 w-3" />
                          {post.totalComentarios}
                        </span>
                      </div>
                    </div>
                    <Badge
                      variant={
                        post.categoria === "ADMINISTRATIVO"
                          ? "destructive"
                          : post.categoria === "TECNICO"
                          ? "info"
                          : "secondary"
                      }
                      className="text-[10px] shrink-0"
                    >
                      {post.categoria}
                    </Badge>
                  </Link>
                ))}
              </div>
            ) : (
              <div className="text-center py-8 text-muted-foreground">
                <MessageSquare className="h-8 w-8 mx-auto mb-2 opacity-50" />
                <p className="text-sm">No forum posts yet</p>
              </div>
            )}
          </CardContent>
        </Card>
      </div>

      {/* Courses Section */}
      <Card>
        <CardHeader className="flex flex-row items-center justify-between pb-2">
          <div>
            <CardTitle className="text-base font-semibold">Available Courses</CardTitle>
            <CardDescription>Explore your academic programs</CardDescription>
          </div>
          <Button variant="ghost" size="sm" asChild>
            <Link href="/courses">
              View all
              <ArrowRight className="ml-1 h-4 w-4" />
            </Link>
          </Button>
        </CardHeader>
        <CardContent>
          {loading ? (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {[1, 2, 3].map((i) => (
                <CardSkeleton key={i} />
              ))}
            </div>
          ) : courses?.content?.length ? (
            <div className="grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
              {courses.content.map((course) => (
                <Link key={course.id} href={`/courses/${course.id}`}>
                  <Card className="hover:border-primary/50 hover:shadow-md transition-all duration-200 h-full">
                    <CardContent className="p-4">
                      <div className="flex items-start gap-3">
                        <div className="rounded-lg bg-primary/10 p-2.5 shrink-0">
                          <GraduationCap className="h-5 w-5 text-primary" />
                        </div>
                        <div className="min-w-0">
                          <h3 className="font-medium text-sm line-clamp-1">{course.nome}</h3>
                          <p className="text-xs text-muted-foreground line-clamp-2 mt-1">
                            {course.descricao || "No description available"}
                          </p>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </Link>
              ))}
            </div>
          ) : (
            <div className="text-center py-8 text-muted-foreground">
              <GraduationCap className="h-8 w-8 mx-auto mb-2 opacity-50" />
              <p className="text-sm">No courses available</p>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  )
}
