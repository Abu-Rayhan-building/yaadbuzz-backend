import { ITopicRating } from 'app/shared/model/topic-rating.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';

export interface ITopic {
  id?: number;
  title?: string;
  ratings?: ITopicRating[];
  departmentId?: number;
  voters?: IUserPerDepartment[];
}

export const defaultValue: Readonly<ITopic> = {};
