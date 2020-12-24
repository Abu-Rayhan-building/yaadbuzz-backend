import { IComment } from 'app/shared/model/comment.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';
import { IDepartment } from 'app/shared/model/department.model';

export interface IMemorial {
  id?: number;
  anonymousComment?: IComment;
  notAnonymousComment?: IComment;
  writer?: IUserPerDepartment;
  recipient?: IUserPerDepartment;
  department?: IDepartment;
}

export const defaultValue: Readonly<IMemorial> = {};
