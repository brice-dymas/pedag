import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAdministrateur, getAdministrateurIdentifier } from '../administrateur.model';

export type EntityResponseType = HttpResponse<IAdministrateur>;
export type EntityArrayResponseType = HttpResponse<IAdministrateur[]>;

@Injectable({ providedIn: 'root' })
export class AdministrateurService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/administrateurs');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(administrateur: IAdministrateur): Observable<EntityResponseType> {
    return this.http.post<IAdministrateur>(this.resourceUrl, administrateur, { observe: 'response' });
  }

  update(administrateur: IAdministrateur): Observable<EntityResponseType> {
    return this.http.put<IAdministrateur>(`${this.resourceUrl}/${getAdministrateurIdentifier(administrateur) as number}`, administrateur, {
      observe: 'response',
    });
  }

  partialUpdate(administrateur: IAdministrateur): Observable<EntityResponseType> {
    return this.http.patch<IAdministrateur>(
      `${this.resourceUrl}/${getAdministrateurIdentifier(administrateur) as number}`,
      administrateur,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAdministrateur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAdministrateur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAdministrateurToCollectionIfMissing(
    administrateurCollection: IAdministrateur[],
    ...administrateursToCheck: (IAdministrateur | null | undefined)[]
  ): IAdministrateur[] {
    const administrateurs: IAdministrateur[] = administrateursToCheck.filter(isPresent);
    if (administrateurs.length > 0) {
      const administrateurCollectionIdentifiers = administrateurCollection.map(
        administrateurItem => getAdministrateurIdentifier(administrateurItem)!
      );
      const administrateursToAdd = administrateurs.filter(administrateurItem => {
        const administrateurIdentifier = getAdministrateurIdentifier(administrateurItem);
        if (administrateurIdentifier == null || administrateurCollectionIdentifiers.includes(administrateurIdentifier)) {
          return false;
        }
        administrateurCollectionIdentifiers.push(administrateurIdentifier);
        return true;
      });
      return [...administrateursToAdd, ...administrateurCollection];
    }
    return administrateurCollection;
  }
}
