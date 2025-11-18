export function buildQueryParams(page: number, size: number, q: string, status: string, prioridade: string) {
  return {
    page,
    size,
    q: q?.trim() || '',
    status: status || '',
    prioridade: prioridade || ''
  };
}
