import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICourriel, getCourrielIdentifier } from '../courriel.model';

export type EntityResponseType = HttpResponse<ICourriel>;
export type EntityArrayResponseType = HttpResponse<ICourriel[]>;

@Injectable({ providedIn: 'root' })
export class CourrielService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/courriels');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(courriel: ICourriel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courriel);
    return this.http
      .post<ICourriel>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(courriel: ICourriel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courriel);
    return this.http
      .put<ICourriel>(`${this.resourceUrl}/${getCourrielIdentifier(courriel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(courriel: ICourriel): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(courriel);
    return this.http
      .patch<ICourriel>(`${this.resourceUrl}/${getCourrielIdentifier(courriel) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICourriel>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICourriel[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCourrielToCollectionIfMissing(courrielCollection: ICourriel[], ...courrielsToCheck: (ICourriel | null | undefined)[]): ICourriel[] {
    const courriels: ICourriel[] = courrielsToCheck.filter(isPresent);
    if (courriels.length > 0) {
      const courrielCollectionIdentifiers = courrielCollection.map(courrielItem => getCourrielIdentifier(courrielItem)!);
      const courrielsToAdd = courriels.filter(courrielItem => {
        const courrielIdentifier = getCourrielIdentifier(courrielItem);
        if (courrielIdentifier == null || courrielCollectionIdentifiers.includes(courrielIdentifier)) {
          return false;
        }
        courrielCollectionIdentifiers.push(courrielIdentifier);
        return true;
      });
      return [...courrielsToAdd, ...courrielCollection];
    }
    return courrielCollection;
  }

  protected convertDateFromClient(courriel: ICourriel): ICourriel {
    return Object.assign({}, courriel, {
      dateCreation: courriel.dateCreation?.isValid() ? courriel.dateCreation.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateCreation = res.body.dateCreation ? dayjs(res.body.dateCreation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((courriel: ICourriel) => {
        courriel.dateCreation = courriel.dateCreation ? dayjs(courriel.dateCreation) : undefined;
      });
    }
    return res;
  }
}
