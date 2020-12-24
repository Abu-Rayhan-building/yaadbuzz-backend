import { ITopic } from 'app/shared/model/topic.model';
import { IUserPerDepartment } from 'app/shared/model/user-per-department.model';

export interface ITopicVote {
  id?: number;
  repetitions?: number;
  topic?: ITopic;
  user?: IUserPerDepartment;
}

export const defaultValue: Readonly<ITopicVote> = {};
