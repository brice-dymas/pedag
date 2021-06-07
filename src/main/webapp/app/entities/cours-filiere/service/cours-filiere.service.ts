import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICoursFiliere, getCoursFiliereIdentifier } from '../cours-filiere.model';

export type EntityResponseType = HttpResponse<ICoursFiliere>;
export type EntityArrayResponseType = HttpResponse<ICoursFiliere[]>;

@Injectable({ providedIn: 'root' })
export class CoursFiliereService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/cours-filieres');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(coursFiliere: ICoursFiliere): Observable<EntityResponseType> {
    return this.http.post<ICoursFiliere>(this.resourceUrl, coursFiliere, { observe: 'response' });
  }

  update(coursFiliere: ICoursFiliere): Observable<EntityResponseType> {
    return this.http.put<ICoursFiliere>(`${this.resourceUrl}/${getCoursFiliereIdentifier(coursFiliere) as number}`, coursFiliere, {
      observe: 'response',
    });
  }

  partialUpdate(coursFiliere: ICoursFiliere): Observable<EntityResponseType> {
    return this.http.patch<ICoursFiliere>(`${this.resourceUrl}/${getCoursFiliereIdentifier(coursFiliere) as number}`, coursFiliere, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICoursFiliere>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICoursFiliere[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCoursFiliereToCollectionIfMissing(
    coursFiliereCollection: ICoursFiliere[],
    ...coursFilieresToCheck: (ICoursFiliere | null | undefined)[]
  ): ICoursFiliere[] {
    const coursFilieres: ICoursFiliere[] = coursFilieresToCheck.filter(isPresent);
    if (coursFilieres.length > 0) {
      const coursFiliereCollectionIdentifiers = coursFiliereCollection.map(
        coursFiliereItem => getCoursFiliereIdentifier(coursFiliereItem)!
      );
      const coursFilieresToAdd = coursFilieres.filter(coursFiliereItem => {
        const coursFiliereIdentifier = getCoursFiliereIdentifier(coursFiliereItem);
        if (coursFiliereIdentifier == null || coursFiliereCollectionIdentifiers.includes(coursFiliereIdentifier)) {
          return false;
        }
        coursFiliereCollectionIdentifiers.push(coursFiliereIdentifier);
        return true;
      });
      return [...coursFilieresToAdd, ...coursFiliereCollection];
    }
    return coursFiliereCollection;
  }
}
