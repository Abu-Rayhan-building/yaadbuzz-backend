export interface IComment {
  id?: number;
  text?: string;
  writerId?: number;
  memoryId?: number;
}

export const defaultValue: Readonly<IComment> = {};
