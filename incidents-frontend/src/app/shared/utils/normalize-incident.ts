export function normalizeIncidentFormValue(formValue: any) {
  return {
    titulo: formValue.titulo?.trim(),
    descricao: formValue.descricao?.trim(),
    prioridade: formValue.prioridade, // enum, não muda
    status: formValue.status,         // enum, não muda
    responsavelEmail: formValue.responsavelEmail?.trim().toLowerCase(),

    // normaliza tags: trim, remove repetidas, ignora vazias
    tags: Array.isArray(formValue.tags)
      ? [...new Set(
          formValue.tags
            .map((t: string) => t.trim())
            .filter((t: string) => t.length > 0)
        )]
      : []
  };
}
