import { AlertCircle, RefreshCw } from "lucide-react"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"

interface ErrorStateProps {
  title?: string
  message?: string
  onRetry?: () => void
}

export function ErrorState({
  title = "Something went wrong",
  message = "An unexpected error occurred. Please try again.",
  onRetry,
}: ErrorStateProps) {
  return (
    <Card className="border-destructive/50">
      <CardContent className="flex flex-col items-center justify-center py-10 text-center">
        <div className="rounded-full bg-destructive/10 p-3 mb-4">
          <AlertCircle className="h-6 w-6 text-destructive" />
        </div>
        <h3 className="text-lg font-semibold mb-2">{title}</h3>
        <p className="text-sm text-muted-foreground max-w-sm mb-4">{message}</p>
        {onRetry && (
          <Button onClick={onRetry} variant="outline" size="sm">
            <RefreshCw className="mr-2 h-4 w-4" />
            Try Again
          </Button>
        )}
      </CardContent>
    </Card>
  )
}

interface EmptyStateProps {
  icon?: React.ElementType
  title: string
  description?: string
  action?: React.ReactNode
}

export function EmptyState({
  icon: Icon = AlertCircle,
  title,
  description,
  action,
}: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center py-10 text-center">
      <div className="rounded-full bg-muted p-3 mb-4">
        <Icon className="h-6 w-6 text-muted-foreground" />
      </div>
      <h3 className="text-lg font-semibold mb-2">{title}</h3>
      {description && (
        <p className="text-sm text-muted-foreground max-w-sm mb-4">{description}</p>
      )}
      {action}
    </div>
  )
}
