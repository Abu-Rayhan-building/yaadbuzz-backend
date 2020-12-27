import { IUser } from 'app/shared/model/user.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';

export interface IUserExtra {
  id?: number;
  phone?: string;
  user?: IUser;
  defaultUserPerDepartment?: IUserPerDepartment;
}

export const defaultValue: Readonly<IUserExtra> = {};
