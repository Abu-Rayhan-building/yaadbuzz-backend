export interface IPicture {
  id?: number;
  imageContentType?: string;
  image?: any;
}

export const defaultValue: Readonly<IPicture> = {};
