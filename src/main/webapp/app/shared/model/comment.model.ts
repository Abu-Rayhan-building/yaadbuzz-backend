import { IPicture } from 'app/shared/model/picture.model';

export interface IComment {
  id?: number;
  text?: string;
  pictures?: IPicture[];
  writerId?: number;
  memoryId?: number;
}

export const defaultValue: Readonly<IComment> = {};
