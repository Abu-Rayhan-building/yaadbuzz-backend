import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { IMemory } from 'app/shared/model/memory.model';
import { IPicture } from 'app/shared/model/picture.model';
import { IUser } from 'app/shared/model/user.model';

export interface IDepartment {
  id?: number;
  name?: string;
  password?: string;
  userPerDepartments?: IUserPerDepartment[];
  memories?: IMemory[];
  avatar?: IPicture;
  owner?: IUser;
}

export const defaultValue: Readonly<IDepartment> = {};
