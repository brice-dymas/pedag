import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEtudiant, getEtudiantIdentifier } from '../etudiant.model';

export type EntityResponseType = HttpResponse<IEtudiant>;
export type EntityArrayResponseType = HttpResponse<IEtudiant[]>;

@Injectable({ providedIn: 'root' })
export class EtudiantService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/etudiants');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(etudiant: IEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etudiant);
    return this.http
      .post<IEtudiant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(etudiant: IEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etudiant);
    return this.http
      .put<IEtudiant>(`${this.resourceUrl}/${getEtudiantIdentifier(etudiant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(etudiant: IEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(etudiant);
    return this.http
      .patch<IEtudiant>(`${this.resourceUrl}/${getEtudiantIdentifier(etudiant) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IEtudiant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IEtudiant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEtudiantToCollectionIfMissing(etudiantCollection: IEtudiant[], ...etudiantsToCheck: (IEtudiant | null | undefined)[]): IEtudiant[] {
    const etudiants: IEtudiant[] = etudiantsToCheck.filter(isPresent);
    if (etudiants.length > 0) {
      const etudiantCollectionIdentifiers = etudiantCollection.map(etudiantItem => getEtudiantIdentifier(etudiantItem)!);
      const etudiantsToAdd = etudiants.filter(etudiantItem => {
        const etudiantIdentifier = getEtudiantIdentifier(etudiantItem);
        if (etudiantIdentifier == null || etudiantCollectionIdentifiers.includes(etudiantIdentifier)) {
          return false;
        }
        etudiantCollectionIdentifiers.push(etudiantIdentifier);
        return true;
      });
      return [...etudiantsToAdd, ...etudiantCollection];
    }
    return etudiantCollection;
  }

  protected convertDateFromClient(etudiant: IEtudiant): IEtudiant {
    return Object.assign({}, etudiant, {
      dateNaissance: etudiant.dateNaissance?.isValid() ? etudiant.dateNaissance.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateNaissance = res.body.dateNaissance ? dayjs(res.body.dateNaissance) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((etudiant: IEtudiant) => {
        etudiant.dateNaissance = etudiant.dateNaissance ? dayjs(etudiant.dateNaissance) : undefined;
      });
    }
    return res;
  }
}
