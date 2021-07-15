import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeliberation, getDeliberationIdentifier } from '../deliberation.model';

export type EntityResponseType = HttpResponse<IDeliberation>;
export type EntityArrayResponseType = HttpResponse<IDeliberation[]>;

@Injectable({ providedIn: 'root' })
export class DeliberationService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/deliberations');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(deliberation: IDeliberation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliberation);
    return this.http
      .post<IDeliberation>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(deliberation: IDeliberation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliberation);
    return this.http
      .put<IDeliberation>(`${this.resourceUrl}/${getDeliberationIdentifier(deliberation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(deliberation: IDeliberation): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(deliberation);
    return this.http
      .patch<IDeliberation>(`${this.resourceUrl}/${getDeliberationIdentifier(deliberation) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDeliberation>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDeliberation[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDeliberationToCollectionIfMissing(
    deliberationCollection: IDeliberation[],
    ...deliberationsToCheck: (IDeliberation | null | undefined)[]
  ): IDeliberation[] {
    const deliberations: IDeliberation[] = deliberationsToCheck.filter(isPresent);
    if (deliberations.length > 0) {
      const deliberationCollectionIdentifiers = deliberationCollection.map(
        deliberationItem => getDeliberationIdentifier(deliberationItem)!
      );
      const deliberationsToAdd = deliberations.filter(deliberationItem => {
        const deliberationIdentifier = getDeliberationIdentifier(deliberationItem);
        if (deliberationIdentifier == null || deliberationCollectionIdentifiers.includes(deliberationIdentifier)) {
          return false;
        }
        deliberationCollectionIdentifiers.push(deliberationIdentifier);
        return true;
      });
      return [...deliberationsToAdd, ...deliberationCollection];
    }
    return deliberationCollection;
  }

  protected convertDateFromClient(deliberation: IDeliberation): IDeliberation {
    return Object.assign({}, deliberation, {
      dateDeliberation: deliberation.dateDeliberation?.isValid() ? deliberation.dateDeliberation.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDeliberation = res.body.dateDeliberation ? dayjs(res.body.dateDeliberation) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((deliberation: IDeliberation) => {
        deliberation.dateDeliberation = deliberation.dateDeliberation ? dayjs(deliberation.dateDeliberation) : undefined;
      });
    }
    return res;
  }
}
