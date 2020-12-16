import { ITopicVote } from 'app/shared/model/topic-vote.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';

export interface ITopic {
  id?: number;
  title?: string;
  votes?: ITopicVote[];
  departmentId?: number;
  voters?: IUserPerDepartment[];
}

export const defaultValue: Readonly<ITopic> = {};
