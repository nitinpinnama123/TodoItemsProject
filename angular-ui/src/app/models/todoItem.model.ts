import { User } from './user.model';

export enum TodoItemStatus {
    PENDING = 'PENDING',
    IN_PROGRESS = 'IN_PROGRESS',
    COMPLETED = 'COMPLETED',
    CANCELLED = 'CANCELLED'
}

export interface TodoItem {
    id?: number;
    title: string;
    description?: string;
    status: TodoItemStatus;
    createdBy: User;
    assignedTo?: User;
    createdAt?: string;
    updatedAt?: string;
}

export interface TodoItemRequest {
  title: string;
  description?: string;
  status: string;
  assignedToId?: number;
}