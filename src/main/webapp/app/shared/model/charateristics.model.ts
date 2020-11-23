import { ICharateristicsRepetation } from 'app/shared/model/charateristics-repetation.model';

export interface ICharateristics {
  id?: number;
  title?: string;
  charateristicsRepetations?: ICharateristicsRepetation[];
  departmentId?: number;
}

export const defaultValue: Readonly<ICharateristics> = {};
