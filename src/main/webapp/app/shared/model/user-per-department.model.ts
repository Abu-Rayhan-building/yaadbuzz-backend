import { ITopicVote } from 'app/shared/model/topic-vote.model';
import { IPicture } from 'app/shared/model/picture.model';
import { IUser } from 'app/shared/model/user.model';
import { IDepartment } from 'app/shared/model/department.model';
import { ITopic } from 'app/shared/model/topic.model';
import { IMemory } from 'app/shared/model/memory.model';

export interface IUserPerDepartment {
  id?: number;
  nicName?: string;
  bio?: string;
  topicAssigneds?: ITopicVote[];
  avatar?: IPicture;
  realUser?: IUser;
  department?: IDepartment;
  topicsVoteds?: ITopic[];
  tagedInMemoeries?: IMemory[];
}

export const defaultValue: Readonly<IUserPerDepartment> = {};
