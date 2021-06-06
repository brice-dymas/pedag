import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAnneeAcademique, getAnneeAcademiqueIdentifier } from '../annee-academique.model';

export type EntityResponseType = HttpResponse<IAnneeAcademique>;
export type EntityArrayResponseType = HttpResponse<IAnneeAcademique[]>;

@Injectable({ providedIn: 'root' })
export class AnneeAcademiqueService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/annee-academiques');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(anneeAcademique: IAnneeAcademique): Observable<EntityResponseType> {
    return this.http.post<IAnneeAcademique>(this.resourceUrl, anneeAcademique, { observe: 'response' });
  }

  update(anneeAcademique: IAnneeAcademique): Observable<EntityResponseType> {
    return this.http.put<IAnneeAcademique>(
      `${this.resourceUrl}/${getAnneeAcademiqueIdentifier(anneeAcademique) as number}`,
      anneeAcademique,
      { observe: 'response' }
    );
  }

  partialUpdate(anneeAcademique: IAnneeAcademique): Observable<EntityResponseType> {
    return this.http.patch<IAnneeAcademique>(
      `${this.resourceUrl}/${getAnneeAcademiqueIdentifier(anneeAcademique) as number}`,
      anneeAcademique,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IAnneeAcademique>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IAnneeAcademique[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAnneeAcademiqueToCollectionIfMissing(
    anneeAcademiqueCollection: IAnneeAcademique[],
    ...anneeAcademiquesToCheck: (IAnneeAcademique | null | undefined)[]
  ): IAnneeAcademique[] {
    const anneeAcademiques: IAnneeAcademique[] = anneeAcademiquesToCheck.filter(isPresent);
    if (anneeAcademiques.length > 0) {
      const anneeAcademiqueCollectionIdentifiers = anneeAcademiqueCollection.map(
        anneeAcademiqueItem => getAnneeAcademiqueIdentifier(anneeAcademiqueItem)!
      );
      const anneeAcademiquesToAdd = anneeAcademiques.filter(anneeAcademiqueItem => {
        const anneeAcademiqueIdentifier = getAnneeAcademiqueIdentifier(anneeAcademiqueItem);
        if (anneeAcademiqueIdentifier == null || anneeAcademiqueCollectionIdentifiers.includes(anneeAcademiqueIdentifier)) {
          return false;
        }
        anneeAcademiqueCollectionIdentifiers.push(anneeAcademiqueIdentifier);
        return true;
      });
      return [...anneeAcademiquesToAdd, ...anneeAcademiqueCollection];
    }
    return anneeAcademiqueCollection;
  }
}
