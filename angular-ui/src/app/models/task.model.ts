import { User } from './user.model';

export enum TaskStatus {
    PENDING = 'PENDING',
    IN_PROGRESS = 'IN_PROGRESS',
    COMPLETED = 'COMPLETED',
    CANCELLED = 'CANCELLED'
}

export interface Task {
    id?: number;
    title: string;
    description?: string;
    status: TaskStatus;
    createdBy: User;
    assignedTo?: User;
    createdAt?: string;
    updatedAt?: string;
}

export interface TaskRequest {
  title: string;
  description?: string;
  status: string;
  assignedToId?: number;
}