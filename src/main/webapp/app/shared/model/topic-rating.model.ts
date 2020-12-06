export interface ITopicRating {
  id?: number;
  repetitions?: number;
  topicId?: number;
  userId?: number;
}

export const defaultValue: Readonly<ITopicRating> = {};
