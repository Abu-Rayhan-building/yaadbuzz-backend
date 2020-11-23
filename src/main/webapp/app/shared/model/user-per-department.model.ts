import { ITopicRating } from 'app/shared/model/topic-rating.model';
import { ICharateristicsRepetation } from 'app/shared/model/charateristics-repetation.model';
import { ITopic } from 'app/shared/model/topic.model';
import { IMemory } from 'app/shared/model/memory.model';

export interface IUserPerDepartment {
  id?: number;
  nicName?: string;
  bio?: string;
  topicAssigneds?: ITopicRating[];
  charateristicsRepetations?: ICharateristicsRepetation[];
  avatarId?: number;
  realUserId?: number;
  departmentId?: number;
  topicsVoteds?: ITopic[];
  tagedInMemoeries?: IMemory[];
}

export const defaultValue: Readonly<IUserPerDepartment> = {};
