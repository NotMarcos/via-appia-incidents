export type Priority = 'BAIXA'|'MEDIA'|'ALTA';
export type Status = 'ABERTA'|'EM_ANDAMENTO'|'RESOLVIDA'|'CANCELADA';

export interface Incident {
  id: string;
  titulo: string;
  descricao?: string;
  prioridade: Priority;
  status: Status;
  responsavelEmail: string;
  tags?: string[];
  dataAbertura: string;
  dataAtualizacao: string;
}

export interface CreateIncidentDto {
  titulo: string;
  descricao?: string;
  prioridade: Priority;
  status?: Status;
  responsavelEmail: string;
  tags?: string[];
}

export interface UpdateIncidentDto extends Partial<CreateIncidentDto> {}
