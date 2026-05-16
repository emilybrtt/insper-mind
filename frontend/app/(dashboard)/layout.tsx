"use client"

import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { Sidebar, MobileNav } from "@/components/navigation"
import { useAuth } from "@/lib/auth-context"
import { PageLoader } from "@/components/loading"

export default function DashboardLayout({
  children,
}: {
  children: React.ReactNode
}) {
  const { isAuthenticated, isLoading } = useAuth()
  const router = useRouter()

  useEffect(() => {
    if (!isLoading && !isAuthenticated) {
      router.push("/login")
    }
  }, [isAuthenticated, isLoading, router])

  if (isLoading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <PageLoader />
      </div>
    )
  }

  if (!isAuthenticated) {
    return null
  }

  return (
    <div className="min-h-screen bg-background">
      <Sidebar />
      <MobileNav />
      <main className="lg:pl-64 pt-14 lg:pt-0 pb-20 lg:pb-0">
        <div className="container mx-auto px-4 py-6 lg:px-6 lg:py-8 max-w-7xl">
          {children}
        </div>
      </main>
    </div>
  )
}
