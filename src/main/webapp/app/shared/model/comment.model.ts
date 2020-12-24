import { IPicture } from 'app/shared/model/picture.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { IMemory } from 'app/shared/model/memory.model';

export interface IComment {
  id?: number;
  text?: string;
  pictures?: IPicture[];
  writer?: IUserPerDepartment;
  memory?: IMemory;
}

export const defaultValue: Readonly<IComment> = {};
