export interface ITopicRating {
  id?: number;
  repetitions?: number;
  ratingId?: number;
  userId?: number;
}

export const defaultValue: Readonly<ITopicRating> = {};
