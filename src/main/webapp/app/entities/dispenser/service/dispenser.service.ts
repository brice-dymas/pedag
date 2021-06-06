import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDispenser, getDispenserIdentifier } from '../dispenser.model';

export type EntityResponseType = HttpResponse<IDispenser>;
export type EntityArrayResponseType = HttpResponse<IDispenser[]>;

@Injectable({ providedIn: 'root' })
export class DispenserService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/dispensers');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(dispenser: IDispenser): Observable<EntityResponseType> {
    return this.http.post<IDispenser>(this.resourceUrl, dispenser, { observe: 'response' });
  }

  update(dispenser: IDispenser): Observable<EntityResponseType> {
    return this.http.put<IDispenser>(`${this.resourceUrl}/${getDispenserIdentifier(dispenser) as number}`, dispenser, {
      observe: 'response',
    });
  }

  partialUpdate(dispenser: IDispenser): Observable<EntityResponseType> {
    return this.http.patch<IDispenser>(`${this.resourceUrl}/${getDispenserIdentifier(dispenser) as number}`, dispenser, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDispenser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDispenser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDispenserToCollectionIfMissing(
    dispenserCollection: IDispenser[],
    ...dispensersToCheck: (IDispenser | null | undefined)[]
  ): IDispenser[] {
    const dispensers: IDispenser[] = dispensersToCheck.filter(isPresent);
    if (dispensers.length > 0) {
      const dispenserCollectionIdentifiers = dispenserCollection.map(dispenserItem => getDispenserIdentifier(dispenserItem)!);
      const dispensersToAdd = dispensers.filter(dispenserItem => {
        const dispenserIdentifier = getDispenserIdentifier(dispenserItem);
        if (dispenserIdentifier == null || dispenserCollectionIdentifiers.includes(dispenserIdentifier)) {
          return false;
        }
        dispenserCollectionIdentifiers.push(dispenserIdentifier);
        return true;
      });
      return [...dispensersToAdd, ...dispenserCollection];
    }
    return dispenserCollection;
  }
}
