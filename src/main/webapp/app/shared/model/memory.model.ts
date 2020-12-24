import { IComment } from 'app/shared/model/comment.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { IDepartment } from 'app/shared/model/department.model';

export interface IMemory {
  id?: number;
  title?: string;
  isPrivate?: boolean;
  comments?: IComment[];
  baseComment?: IComment;
  writer?: IUserPerDepartment;
  tageds?: IUserPerDepartment[];
  department?: IDepartment;
}

export const defaultValue: Readonly<IMemory> = {
  isPrivate: false,
};
