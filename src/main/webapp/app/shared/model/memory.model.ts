import { IComment } from 'app/shared/model/comment.model';
import { IMemoryPicture } from 'app/shared/model/memory-picture.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';

export interface IMemory {
  id?: number;
  title?: string;
  isPrivate?: boolean;
  isAnnonymos?: boolean;
  comments?: IComment[];
  pictures?: IMemoryPicture[];
  textId?: number;
  writerId?: number;
  tageds?: IUserPerDepartment[];
  departmentId?: number;
}

export const defaultValue: Readonly<IMemory> = {
  isPrivate: false,
  isAnnonymos: false,
};
