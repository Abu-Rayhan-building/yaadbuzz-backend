import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { IMemory } from 'app/shared/model/memory.model';

export interface IDepartment {
  id?: number;
  name?: string;
  password?: string;
  userPerDepartments?: IUserPerDepartment[];
  memories?: IMemory[];
  avatarId?: number;
  ownerId?: number;
}

export const defaultValue: Readonly<IDepartment> = {};
