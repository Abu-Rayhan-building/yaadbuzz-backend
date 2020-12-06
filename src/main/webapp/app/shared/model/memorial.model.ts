export interface IMemorial {
  id?: number;
  anonymousCommentId?: number;
  notAnonymousCommentId?: number;
  writerId?: number;
  recipientId?: number;
}

export const defaultValue: Readonly<IMemorial> = {};
