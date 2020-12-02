export interface IPicture {
  id?: number;
  imageContentType?: string;
  image?: any;
  commentId?: number;
}

export const defaultValue: Readonly<IPicture> = {};
