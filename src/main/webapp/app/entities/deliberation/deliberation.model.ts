import * as dayjs from 'dayjs';
import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';
import { IFiliere } from 'app/entities/filiere/filiere.model';
import { ConditionSelection } from 'app/entities/enumerations/condition-selection.model';
import { ConditionAppliquer } from 'app/entities/enumerations/condition-appliquer.model';

export interface IDeliberation {
  id?: number;
  critereSelection?: ConditionSelection;
  valeurSelectionDebut?: number;
  valeurSelectionFin?: number | null;
  critereAppliquer?: ConditionAppliquer;
  valeurAppliquer?: number;
  dateDeliberation?: dayjs.Dayjs | null;
  sessionExamen?: ISessionExamen;
  filiere?: IFiliere | null;
}

export class Deliberation implements IDeliberation {
  constructor(
    public id?: number,
    public critereSelection?: ConditionSelection,
    public valeurSelectionDebut?: number,
    public valeurSelectionFin?: number | null,
    public critereAppliquer?: ConditionAppliquer,
    public valeurAppliquer?: number,
    public dateDeliberation?: dayjs.Dayjs | null,
    public sessionExamen?: ISessionExamen,
    public filiere?: IFiliere | null
  ) {}
}

export function getDeliberationIdentifier(deliberation: IDeliberation): number | undefined {
  return deliberation.id;
}
