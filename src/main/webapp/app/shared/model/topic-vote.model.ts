export interface ITopicVote {
  id?: number;
  repetitions?: number;
  topicId?: number;
  userId?: number;
}

export const defaultValue: Readonly<ITopicVote> = {};
