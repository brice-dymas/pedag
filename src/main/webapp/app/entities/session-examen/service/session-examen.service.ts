import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISessionExamen, getSessionExamenIdentifier } from '../session-examen.model';

export type EntityResponseType = HttpResponse<ISessionExamen>;
export type EntityArrayResponseType = HttpResponse<ISessionExamen[]>;

@Injectable({ providedIn: 'root' })
export class SessionExamenService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/session-examen');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(sessionExamen: ISessionExamen): Observable<EntityResponseType> {
    return this.http.post<ISessionExamen>(this.resourceUrl, sessionExamen, { observe: 'response' });
  }

  update(sessionExamen: ISessionExamen): Observable<EntityResponseType> {
    return this.http.put<ISessionExamen>(`${this.resourceUrl}/${getSessionExamenIdentifier(sessionExamen) as number}`, sessionExamen, {
      observe: 'response',
    });
  }

  partialUpdate(sessionExamen: ISessionExamen): Observable<EntityResponseType> {
    return this.http.patch<ISessionExamen>(`${this.resourceUrl}/${getSessionExamenIdentifier(sessionExamen) as number}`, sessionExamen, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISessionExamen>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  queryActif(): Observable<EntityArrayResponseType> {
    return this.http.get<ISessionExamen[]>(`${this.resourceUrl}/actif`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISessionExamen[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryDeliberation(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISessionExamen[]>(`${this.resourceUrl}/deliberation`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSessionExamenToCollectionIfMissing(
    sessionExamenCollection: ISessionExamen[],
    ...sessionExamenToCheck: (ISessionExamen | null | undefined)[]
  ): ISessionExamen[] {
    const sessionExamen: ISessionExamen[] = sessionExamenToCheck.filter(isPresent);
    if (sessionExamen.length > 0) {
      const sessionExamenCollectionIdentifiers = sessionExamenCollection.map(
        sessionExamenItem => getSessionExamenIdentifier(sessionExamenItem)!
      );
      const sessionExamenToAdd = sessionExamen.filter(sessionExamenItem => {
        const sessionExamenIdentifier = getSessionExamenIdentifier(sessionExamenItem);
        if (sessionExamenIdentifier == null || sessionExamenCollectionIdentifiers.includes(sessionExamenIdentifier)) {
          return false;
        }
        sessionExamenCollectionIdentifiers.push(sessionExamenIdentifier);
        return true;
      });
      return [...sessionExamenToAdd, ...sessionExamenCollection];
    }
    return sessionExamenCollection;
  }
}
