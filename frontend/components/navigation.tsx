"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import {
  LayoutDashboard,
  BookOpen,
  FileText,
  Users,
  MessageSquare,
  GraduationCap,
  Menu,
  X,
  Moon,
  Sun,
  LogOut,
  Settings,
} from "lucide-react"
import { useState } from "react"
import { useTheme } from "next-themes"
import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import { ScrollArea } from "@/components/ui/scroll-area"
import { Avatar, AvatarFallback } from "@/components/ui/avatar"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useAuth } from "@/lib/auth-context"
import { Separator } from "@/components/ui/separator"

const navItems = [
  { href: "/dashboard", label: "Dashboard", icon: LayoutDashboard },
  { href: "/courses", label: "Course Structure", icon: GraduationCap },
  { href: "/subjects", label: "Subjects", icon: BookOpen },
  { href: "/materials", label: "Materials", icon: FileText },
  { href: "/contacts", label: "Contacts", icon: Users },
  { href: "/forum", label: "Forum", icon: MessageSquare },
]

function NavItem({
  href,
  label,
  icon: Icon,
  isActive,
  onClick,
}: {
  href: string
  label: string
  icon: React.ElementType
  isActive: boolean
  onClick?: () => void
}) {
  return (
    <Link
      href={href}
      onClick={onClick}
      className={cn(
        "flex items-center gap-3 rounded-lg px-3 py-2.5 text-sm font-medium transition-all duration-200",
        isActive
          ? "bg-primary text-primary-foreground shadow-sm"
          : "text-muted-foreground hover:bg-accent hover:text-foreground"
      )}
    >
      <Icon className="h-5 w-5 shrink-0" />
      <span>{label}</span>
    </Link>
  )
}

export function Sidebar() {
  const pathname = usePathname()
  const { theme, setTheme } = useTheme()
  const { user, logout } = useAuth()

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase()
      .slice(0, 2)
  }

  return (
    <aside className="hidden lg:flex fixed left-0 top-0 z-40 h-screen w-64 flex-col border-r border-border bg-card">
      {/* Logo */}
      <div className="flex h-16 items-center gap-2 border-b border-border px-6">
        <div className="flex h-8 w-8 items-center justify-center rounded-lg bg-primary">
          <GraduationCap className="h-5 w-5 text-primary-foreground" />
        </div>
        <span className="text-lg font-semibold tracking-tight">InsperMind</span>
      </div>

      {/* Navigation */}
      <ScrollArea className="flex-1 px-3 py-4">
        <nav className="flex flex-col gap-1">
          {navItems.map((item) => (
            <NavItem
              key={item.href}
              href={item.href}
              label={item.label}
              icon={item.icon}
              isActive={pathname === item.href}
            />
          ))}
        </nav>
      </ScrollArea>

      {/* Footer */}
      <div className="border-t border-border p-4">
        <div className="flex items-center justify-between mb-3">
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            className="h-9 w-9"
          >
            <Sun className="h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
            <Moon className="absolute h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
            <span className="sr-only">Toggle theme</span>
          </Button>
        </div>
        <Separator className="mb-3" />
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button className="flex w-full items-center gap-3 rounded-lg px-2 py-2 text-left hover:bg-accent transition-colors">
              <Avatar className="h-9 w-9">
                <AvatarFallback className="bg-primary/10 text-primary text-sm font-medium">
                  {user?.nome ? getInitials(user.nome) : "U"}
                </AvatarFallback>
              </Avatar>
              <div className="flex-1 min-w-0">
                <p className="text-sm font-medium truncate">{user?.nome || "User"}</p>
                <p className="text-xs text-muted-foreground truncate">{user?.email}</p>
              </div>
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56">
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            <DropdownMenuItem>
              <Settings className="mr-2 h-4 w-4" />
              Settings
            </DropdownMenuItem>
            <DropdownMenuSeparator />
            <DropdownMenuItem onClick={logout} className="text-destructive focus:text-destructive">
              <LogOut className="mr-2 h-4 w-4" />
              Log out
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </aside>
  )
}

export function MobileNav() {
  const pathname = usePathname()
  const [isOpen, setIsOpen] = useState(false)
  const { theme, setTheme } = useTheme()
  const { user, logout } = useAuth()

  const getInitials = (name: string) => {
    return name
      .split(" ")
      .map((n) => n[0])
      .join("")
      .toUpperCase()
      .slice(0, 2)
  }

  return (
    <>
      {/* Mobile Header */}
      <header className="lg:hidden fixed top-0 left-0 right-0 z-50 flex h-14 items-center justify-between border-b border-border bg-card/95 backdrop-blur supports-[backdrop-filter]:bg-card/80 px-4">
        <div className="flex items-center gap-2">
          <div className="flex h-7 w-7 items-center justify-center rounded-lg bg-primary">
            <GraduationCap className="h-4 w-4 text-primary-foreground" />
          </div>
          <span className="text-base font-semibold">InsperMind</span>
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            className="h-9 w-9"
          >
            <Sun className="h-4 w-4 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
            <Moon className="absolute h-4 w-4 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
          </Button>
          <Button
            variant="ghost"
            size="icon"
            onClick={() => setIsOpen(!isOpen)}
            className="h-9 w-9"
          >
            {isOpen ? <X className="h-5 w-5" /> : <Menu className="h-5 w-5" />}
          </Button>
        </div>
      </header>

      {/* Mobile Menu Overlay */}
      {isOpen && (
        <div className="lg:hidden fixed inset-0 z-40 bg-background/80 backdrop-blur-sm" onClick={() => setIsOpen(false)}>
          <div
            className="fixed top-14 right-0 bottom-0 w-64 bg-card border-l border-border p-4"
            onClick={(e) => e.stopPropagation()}
          >
            <ScrollArea className="h-full">
              <nav className="flex flex-col gap-1 mb-6">
                {navItems.map((item) => (
                  <NavItem
                    key={item.href}
                    href={item.href}
                    label={item.label}
                    icon={item.icon}
                    isActive={pathname === item.href}
                    onClick={() => setIsOpen(false)}
                  />
                ))}
              </nav>
              <Separator className="my-4" />
              <div className="flex items-center gap-3 px-2 py-2 mb-4">
                <Avatar className="h-9 w-9">
                  <AvatarFallback className="bg-primary/10 text-primary text-sm font-medium">
                    {user?.nome ? getInitials(user.nome) : "U"}
                  </AvatarFallback>
                </Avatar>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-medium truncate">{user?.nome || "User"}</p>
                  <p className="text-xs text-muted-foreground truncate">{user?.email}</p>
                </div>
              </div>
              <Button
                variant="ghost"
                className="w-full justify-start text-destructive hover:text-destructive hover:bg-destructive/10"
                onClick={() => {
                  logout()
                  setIsOpen(false)
                }}
              >
                <LogOut className="mr-2 h-4 w-4" />
                Log out
              </Button>
            </ScrollArea>
          </div>
        </div>
      )}

      {/* Bottom Navigation */}
      <nav className="lg:hidden fixed bottom-0 left-0 right-0 z-50 border-t border-border bg-card/95 backdrop-blur supports-[backdrop-filter]:bg-card/80">
        <div className="flex items-center justify-around py-2">
          {navItems.slice(0, 5).map((item) => {
            const Icon = item.icon
            const isActive = pathname === item.href
            return (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  "flex flex-col items-center gap-1 px-3 py-1.5 rounded-lg transition-colors",
                  isActive
                    ? "text-primary"
                    : "text-muted-foreground hover:text-foreground"
                )}
              >
                <Icon className="h-5 w-5" />
                <span className="text-[10px] font-medium">{item.label.split(" ")[0]}</span>
              </Link>
            )
          })}
        </div>
      </nav>
    </>
  )
}
