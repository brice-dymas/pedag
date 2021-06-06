import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPieceJointe, getPieceJointeIdentifier } from '../piece-jointe.model';

export type EntityResponseType = HttpResponse<IPieceJointe>;
export type EntityArrayResponseType = HttpResponse<IPieceJointe[]>;

@Injectable({ providedIn: 'root' })
export class PieceJointeService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/piece-jointes');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(pieceJointe: IPieceJointe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pieceJointe);
    return this.http
      .post<IPieceJointe>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(pieceJointe: IPieceJointe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pieceJointe);
    return this.http
      .put<IPieceJointe>(`${this.resourceUrl}/${getPieceJointeIdentifier(pieceJointe) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(pieceJointe: IPieceJointe): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(pieceJointe);
    return this.http
      .patch<IPieceJointe>(`${this.resourceUrl}/${getPieceJointeIdentifier(pieceJointe) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IPieceJointe>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IPieceJointe[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addPieceJointeToCollectionIfMissing(
    pieceJointeCollection: IPieceJointe[],
    ...pieceJointesToCheck: (IPieceJointe | null | undefined)[]
  ): IPieceJointe[] {
    const pieceJointes: IPieceJointe[] = pieceJointesToCheck.filter(isPresent);
    if (pieceJointes.length > 0) {
      const pieceJointeCollectionIdentifiers = pieceJointeCollection.map(pieceJointeItem => getPieceJointeIdentifier(pieceJointeItem)!);
      const pieceJointesToAdd = pieceJointes.filter(pieceJointeItem => {
        const pieceJointeIdentifier = getPieceJointeIdentifier(pieceJointeItem);
        if (pieceJointeIdentifier == null || pieceJointeCollectionIdentifiers.includes(pieceJointeIdentifier)) {
          return false;
        }
        pieceJointeCollectionIdentifiers.push(pieceJointeIdentifier);
        return true;
      });
      return [...pieceJointesToAdd, ...pieceJointeCollection];
    }
    return pieceJointeCollection;
  }

  protected convertDateFromClient(pieceJointe: IPieceJointe): IPieceJointe {
    return Object.assign({}, pieceJointe, {
      dateCreation: pieceJointe.dateCreation?.isValid() ? pieceJointe.dateCreation.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((pieceJointe: IPieceJointe) => {
        pieceJointe.dateCreation = pieceJointe.dateCreation ? dayjs(pieceJointe.dateCreation) : undefined;
      });
    }
    return res;
  }
}
