"use client"

import { useState } from "react"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { useForm } from "react-hook-form"
import { zodResolver } from "@hookform/resolvers/zod"
import { z } from "zod"
import { Eye, EyeOff, GraduationCap, Loader2, Check } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Input } from "@/components/ui/input"
import { Label } from "@/components/ui/label"
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from "@/components/ui/card"
import { authApi, ApiError } from "@/lib/api"

const registerSchema = z.object({
  nome: z.string().min(2, "Name must be at least 2 characters"),
  email: z.string().email("Please enter a valid email address"),
  senha: z
    .string()
    .min(8, "Password must be at least 8 characters")
    .regex(/[A-Z]/, "Password must contain at least one uppercase letter")
    .regex(/[a-z]/, "Password must contain at least one lowercase letter")
    .regex(/[0-9]/, "Password must contain at least one number"),
  confirmSenha: z.string(),
}).refine((data) => data.senha === data.confirmSenha, {
  message: "Passwords do not match",
  path: ["confirmSenha"],
})

type RegisterFormData = z.infer<typeof registerSchema>

const passwordRequirements = [
  { regex: /.{8,}/, label: "At least 8 characters" },
  { regex: /[A-Z]/, label: "One uppercase letter" },
  { regex: /[a-z]/, label: "One lowercase letter" },
  { regex: /[0-9]/, label: "One number" },
]

export default function RegisterPage() {
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirmPassword, setShowConfirmPassword] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [success, setSuccess] = useState(false)
  const router = useRouter()

  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
  })

  const password = watch("senha", "")

  const onSubmit = async (data: RegisterFormData) => {
    try {
      setError(null)
      await authApi.register({
        nome: data.nome,
        email: data.email,
        senha: data.senha,
      })
      setSuccess(true)
      setTimeout(() => {
        router.push("/login")
      }, 2000)
    } catch (err) {
      if (err instanceof ApiError) {
        setError(err.message)
      } else {
        setError("An unexpected error occurred. Please try again.")
      }
    }
  }

  if (success) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-background px-4 py-12">
        <Card className="w-full max-w-md border-border/50 shadow-lg">
          <CardContent className="flex flex-col items-center py-10">
            <div className="rounded-full bg-emerald-500/10 p-3 mb-4">
              <Check className="h-8 w-8 text-emerald-500" />
            </div>
            <h2 className="text-xl font-semibold mb-2">Account Created</h2>
            <p className="text-sm text-muted-foreground text-center">
              Your account has been created successfully. Redirecting to login...
            </p>
          </CardContent>
        </Card>
      </div>
    )
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-background px-4 py-12">
      <div className="w-full max-w-md">
        {/* Logo */}
        <div className="flex flex-col items-center mb-8">
          <div className="flex h-12 w-12 items-center justify-center rounded-xl bg-primary mb-4">
            <GraduationCap className="h-7 w-7 text-primary-foreground" />
          </div>
          <h1 className="text-2xl font-bold tracking-tight">InsperMind</h1>
          <p className="text-sm text-muted-foreground mt-1">Academic Resource Portal</p>
        </div>

        <Card className="border-border/50 shadow-lg">
          <CardHeader className="space-y-1 pb-4">
            <CardTitle className="text-xl text-center">Create an account</CardTitle>
            <CardDescription className="text-center">
              Enter your details to get started
            </CardDescription>
          </CardHeader>
          <form onSubmit={handleSubmit(onSubmit)}>
            <CardContent className="space-y-4">
              {error && (
                <div className="rounded-lg bg-destructive/10 border border-destructive/20 px-4 py-3">
                  <p className="text-sm text-destructive">{error}</p>
                </div>
              )}

              <div className="space-y-2">
                <Label htmlFor="nome">Full Name</Label>
                <Input
                  id="nome"
                  type="text"
                  placeholder="Your full name"
                  autoComplete="name"
                  {...register("nome")}
                  className={errors.nome ? "border-destructive" : ""}
                />
                {errors.nome && (
                  <p className="text-xs text-destructive">{errors.nome.message}</p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="email">Email</Label>
                <Input
                  id="email"
                  type="email"
                  placeholder="your.email@insper.edu.br"
                  autoComplete="email"
                  {...register("email")}
                  className={errors.email ? "border-destructive" : ""}
                />
                {errors.email && (
                  <p className="text-xs text-destructive">{errors.email.message}</p>
                )}
              </div>

              <div className="space-y-2">
                <Label htmlFor="password">Password</Label>
                <div className="relative">
                  <Input
                    id="password"
                    type={showPassword ? "text" : "password"}
                    placeholder="Create a password"
                    autoComplete="new-password"
                    {...register("senha")}
                    className={errors.senha ? "border-destructive pr-10" : "pr-10"}
                  />
                  <button
                    type="button"
                    onClick={() => setShowPassword(!showPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                  >
                    {showPassword ? (
                      <EyeOff className="h-4 w-4" />
                    ) : (
                      <Eye className="h-4 w-4" />
                    )}
                  </button>
                </div>
                {/* Password requirements */}
                <div className="space-y-1 pt-2">
                  {passwordRequirements.map((req, index) => {
                    const isValid = req.regex.test(password)
                    return (
                      <div
                        key={index}
                        className={`flex items-center gap-2 text-xs ${
                          isValid ? "text-emerald-600 dark:text-emerald-400" : "text-muted-foreground"
                        }`}
                      >
                        <div
                          className={`h-1.5 w-1.5 rounded-full ${
                            isValid ? "bg-emerald-500" : "bg-muted-foreground/30"
                          }`}
                        />
                        {req.label}
                      </div>
                    )
                  })}
                </div>
              </div>

              <div className="space-y-2">
                <Label htmlFor="confirmPassword">Confirm Password</Label>
                <div className="relative">
                  <Input
                    id="confirmPassword"
                    type={showConfirmPassword ? "text" : "password"}
                    placeholder="Confirm your password"
                    autoComplete="new-password"
                    {...register("confirmSenha")}
                    className={errors.confirmSenha ? "border-destructive pr-10" : "pr-10"}
                  />
                  <button
                    type="button"
                    onClick={() => setShowConfirmPassword(!showConfirmPassword)}
                    className="absolute right-3 top-1/2 -translate-y-1/2 text-muted-foreground hover:text-foreground transition-colors"
                  >
                    {showConfirmPassword ? (
                      <EyeOff className="h-4 w-4" />
                    ) : (
                      <Eye className="h-4 w-4" />
                    )}
                  </button>
                </div>
                {errors.confirmSenha && (
                  <p className="text-xs text-destructive">{errors.confirmSenha.message}</p>
                )}
              </div>
            </CardContent>
            <CardFooter className="flex flex-col gap-4 pt-2">
              <Button type="submit" className="w-full" disabled={isSubmitting}>
                {isSubmitting ? (
                  <>
                    <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                    Creating account...
                  </>
                ) : (
                  "Create account"
                )}
              </Button>
              <p className="text-sm text-center text-muted-foreground">
                Already have an account?{" "}
                <Link href="/login" className="text-primary font-medium hover:underline">
                  Sign in
                </Link>
              </p>
            </CardFooter>
          </form>
        </Card>
      </div>
    </div>
  )
}
