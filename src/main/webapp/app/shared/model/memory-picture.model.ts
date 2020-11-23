export interface IMemoryPicture {
  id?: number;
  imageContentType?: string;
  image?: any;
  memoryId?: number;
}

export const defaultValue: Readonly<IMemoryPicture> = {};
