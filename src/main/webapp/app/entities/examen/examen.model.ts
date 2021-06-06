import * as dayjs from 'dayjs';
import { TypeExamen } from 'app/entities/enumerations/type-examen.model';
import { Semestre } from 'app/entities/enumerations/semestre.model';

export interface IExamen {
  id?: number;
  dateExamen?: dayjs.Dayjs;
  type?: TypeExamen;
  semestre?: Semestre;
}

export class Examen implements IExamen {
  constructor(public id?: number, public dateExamen?: dayjs.Dayjs, public type?: TypeExamen, public semestre?: Semestre) {}
}

export function getExamenIdentifier(examen: IExamen): number | undefined {
  return examen.id;
}
