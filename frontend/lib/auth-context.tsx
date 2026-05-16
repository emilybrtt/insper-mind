"use client"

import { createContext, useContext, useEffect, useState, ReactNode } from "react"
import { useRouter } from "next/navigation"
import { authApi, Usuario } from "./api"

interface AuthContextType {
  user: Usuario | null
  isLoading: boolean
  isAuthenticated: boolean
  login: (token: string) => void
  logout: () => void
  refreshUser: () => Promise<void>
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<Usuario | null>(null)
  const [isLoading, setIsLoading] = useState(true)
  const router = useRouter()

  const refreshUser = async () => {
    try {
      const token = localStorage.getItem("token")
      if (!token) {
        setUser(null)
        return
      }
      
      // Decode JWT to get user info
      const payload = JSON.parse(atob(token.split(".")[1]))
      setUser({
        id: payload.id || 0,
        nome: payload.nome || payload.sub?.split("@")[0] || "User",
        email: payload.sub || "",
        role: payload.role || "ALUNO",
        ativo: true,
        createdAt: new Date().toISOString(),
      })
    } catch (error) {
      console.error("[v0] Failed to refresh user:", error)
      localStorage.removeItem("token")
      setUser(null)
    }
  }

  useEffect(() => {
    const initAuth = async () => {
      setIsLoading(true)
      await refreshUser()
      setIsLoading(false)
    }
    initAuth()
  }, [])

  const login = (token: string) => {
    localStorage.setItem("token", token)
    refreshUser()
    router.push("/dashboard")
  }

  const logout = () => {
    localStorage.removeItem("token")
    setUser(null)
    router.push("/login")
  }

  return (
    <AuthContext.Provider
      value={{
        user,
        isLoading,
        isAuthenticated: !!user,
        login,
        logout,
        refreshUser,
      }}
    >
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error("useAuth must be used within an AuthProvider")
  }
  return context
}
