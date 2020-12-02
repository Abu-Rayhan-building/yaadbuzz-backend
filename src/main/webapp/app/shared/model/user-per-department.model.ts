import { ITopicRating } from 'app/shared/model/topic-rating.model';
import { ITopic } from 'app/shared/model/topic.model';
import { IMemory } from 'app/shared/model/memory.model';

export interface IUserPerDepartment {
  id?: number;
  nicName?: string;
  bio?: string;
  topicAssigneds?: ITopicRating[];
  avatarId?: number;
  realUserId?: number;
  departmentId?: number;
  topicsVoteds?: ITopic[];
  tagedInMemoeries?: IMemory[];
}

export const defaultValue: Readonly<IUserPerDepartment> = {};
