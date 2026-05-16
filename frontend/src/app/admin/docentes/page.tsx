'use client';

import { useState } from 'react';
import useSWR, { mutate } from 'swr';
import { AuthGuard, AdminGuard } from '@/components/guards';
import { Sidebar } from '@/components/sidebar';
import { Card, Button, Input, Badge, EmptyState, LoadingSpinner } from '@/components/ui';
import { fetcher, api } from '@/lib/api';
import { Docente } from '@/lib/types';
import { Plus, Trash2, Edit, X, User } from 'lucide-react';

function AdminDocentesContent() {
  const { data: docentes, error, isLoading } = useSWR<Docente[]>('/docente', fetcher);
  const [showForm, setShowForm] = useState(false);
  const [editingId, setEditingId] = useState<string | null>(null);
  const [form, setForm] = useState({ nome: '', email: '', departamento: '' });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingId) {
        await api(`/docente/${editingId}`, { method: 'PUT', body: JSON.stringify(form) });
      } else {
        await api('/docente', { method: 'POST', body: JSON.stringify(form) });
      }
      mutate('/docente');
      setShowForm(false);
      setEditingId(null);
      setForm({ nome: '', email: '', departamento: '' });
    } catch (err) {
      console.error('Error saving docente:', err);
    }
  };

  const handleEdit = (docente: Docente) => {
    setForm({ nome: docente.nome, email: docente.email || '', departamento: docente.departamento || '' });
    setEditingId(docente.id);
    setShowForm(true);
  };

  const handleDelete = async (id: string) => {
    if (!confirm('Tem certeza que deseja excluir este docente?')) return;
    try {
      await api(`/docente/${id}`, { method: 'DELETE' });
      mutate('/docente');
    } catch (err) {
      console.error('Error deleting docente:', err);
    }
  };

  return (
    <div className="flex min-h-screen bg-background">
      <Sidebar />
      <main className="flex-1 p-8">
        <div className="mb-8 flex items-center justify-between">
          <div>
            <h1 className="text-3xl font-bold text-foreground">Gerenciar Docentes</h1>
            <p className="mt-1 text-muted-foreground">Adicione, edite ou remova docentes</p>
          </div>
          <Button onClick={() => { setShowForm(true); setEditingId(null); setForm({ nome: '', email: '', departamento: '' }); }}>
            <Plus className="mr-2 h-4 w-4" /> Novo Docente
          </Button>
        </div>

        {showForm && (
          <Card className="mb-6">
            <div className="flex items-center justify-between mb-4">
              <h3 className="text-lg font-semibold">{editingId ? 'Editar' : 'Novo'} Docente</h3>
              <button onClick={() => setShowForm(false)} className="text-muted-foreground hover:text-foreground">
                <X className="h-5 w-5" />
              </button>
            </div>
            <form onSubmit={handleSubmit} className="space-y-4">
              <Input label="Nome" value={form.nome} onChange={e => setForm({ ...form, nome: e.target.value })} required />
              <Input label="Email" type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} />
              <Input label="Departamento" value={form.departamento} onChange={e => setForm({ ...form, departamento: e.target.value })} />
              <div className="flex gap-3">
                <Button type="submit">{editingId ? 'Atualizar' : 'Criar'}</Button>
                <Button type="button" variant="secondary" onClick={() => setShowForm(false)}>Cancelar</Button>
              </div>
            </form>
          </Card>
        )}

        {isLoading ? (
          <LoadingSpinner />
        ) : error ? (
          <EmptyState icon={User} title="Erro ao carregar docentes" description="Tente novamente mais tarde" />
        ) : !docentes?.length ? (
          <EmptyState icon={User} title="Nenhum docente cadastrado" description="Adicione o primeiro docente" />
        ) : (
          <div className="grid gap-4">
            {docentes.map(docente => (
              <Card key={docente.id} className="flex items-center justify-between">
                <div>
                  <h3 className="font-semibold text-foreground">{docente.nome}</h3>
                  <p className="text-sm text-muted-foreground">{docente.email}</p>
                  {docente.departamento && <Badge variant="secondary">{docente.departamento}</Badge>}
                </div>
                <div className="flex gap-2">
                  <button onClick={() => handleEdit(docente)} className="p-2 text-muted-foreground hover:text-primary">
                    <Edit className="h-4 w-4" />
                  </button>
                  <button onClick={() => handleDelete(docente.id)} className="p-2 text-muted-foreground hover:text-red-500">
                    <Trash2 className="h-4 w-4" />
                  </button>
                </div>
              </Card>
            ))}
          </div>
        )}
      </main>
    </div>
  );
}

export default function AdminDocentesPage() {
  return (
    <AuthGuard>
      <AdminGuard>
        <AdminDocentesContent />
      </AdminGuard>
    </AuthGuard>
  );
}
