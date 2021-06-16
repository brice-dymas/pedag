import { IFiliere } from 'app/entities/filiere/filiere.model';

export interface IModule {
  id?: number;
  libelle?: string;
  code?: string;
  credit?: number | null;
  filiere?: IFiliere;
}

export class Module implements IModule {
  constructor(
    public id?: number,
    public libelle?: string,
    public code?: string,
    public credit?: number | null,
    public filiere?: IFiliere
  ) {}
}

export function getModuleIdentifier(module: IModule): number | undefined {
  return module.id;
}
