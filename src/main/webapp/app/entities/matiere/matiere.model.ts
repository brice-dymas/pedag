import { IModule } from 'app/entities/module/module.model';

export interface IMatiere {
  id?: number;
  libelle?: string;
  code?: string;
  credit?: number | null;
  module?: IModule;
}

export class Matiere implements IMatiere {
  constructor(public id?: number, public libelle?: string, public code?: string, public credit?: number | null, public module?: IModule) {}
}

export function getMatiereIdentifier(matiere: IMatiere): number | undefined {
  return matiere.id;
}
