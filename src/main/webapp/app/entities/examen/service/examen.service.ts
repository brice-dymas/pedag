import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExamen, getExamenIdentifier } from '../examen.model';

export type EntityResponseType = HttpResponse<IExamen>;
export type EntityArrayResponseType = HttpResponse<IExamen[]>;

@Injectable({ providedIn: 'root' })
export class ExamenService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/examen');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(examen: IExamen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examen);
    return this.http
      .post<IExamen>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(examen: IExamen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examen);
    return this.http
      .put<IExamen>(`${this.resourceUrl}/${getExamenIdentifier(examen) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(examen: IExamen): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(examen);
    return this.http
      .patch<IExamen>(`${this.resourceUrl}/${getExamenIdentifier(examen) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExamen>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExamen[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExamenToCollectionIfMissing(examenCollection: IExamen[], ...examenToCheck: (IExamen | null | undefined)[]): IExamen[] {
    const examen: IExamen[] = examenToCheck.filter(isPresent);
    if (examen.length > 0) {
      const examenCollectionIdentifiers = examenCollection.map(examenItem => getExamenIdentifier(examenItem)!);
      const examenToAdd = examen.filter(examenItem => {
        const examenIdentifier = getExamenIdentifier(examenItem);
        if (examenIdentifier == null || examenCollectionIdentifiers.includes(examenIdentifier)) {
          return false;
        }
        examenCollectionIdentifiers.push(examenIdentifier);
        return true;
      });
      return [...examenToAdd, ...examenCollection];
    }
    return examenCollection;
  }

  protected convertDateFromClient(examen: IExamen): IExamen {
    return Object.assign({}, examen, {
      dateExamen: examen.dateExamen?.isValid() ? examen.dateExamen.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateExamen = res.body.dateExamen ? dayjs(res.body.dateExamen) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((examen: IExamen) => {
        examen.dateExamen = examen.dateExamen ? dayjs(examen.dateExamen) : undefined;
      });
    }
    return res;
  }
}
