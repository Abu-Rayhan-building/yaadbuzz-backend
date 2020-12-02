import { IComment } from 'app/shared/model/comment.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';

export interface IMemory {
  id?: number;
  title?: string;
  isPrivate?: boolean;
  isAnnonymos?: boolean;
  comments?: IComment[];
  textId?: number;
  writerId?: number;
  tageds?: IUserPerDepartment[];
  departmentId?: number;
}

export const defaultValue: Readonly<IMemory> = {
  isPrivate: false,
  isAnnonymos: false,
};
