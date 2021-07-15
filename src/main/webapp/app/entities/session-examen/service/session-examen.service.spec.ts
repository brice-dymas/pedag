import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { MoisAnnee } from 'app/entities/enumerations/mois-annee.model';
import { TypeExamen } from 'app/entities/enumerations/type-examen.model';
import { ISessionExamen, SessionExamen } from '../session-examen.model';

import { SessionExamenService } from './session-examen.service';

describe('Service Tests', () => {
  describe('SessionExamen Service', () => {
    let service: SessionExamenService;
    let httpMock: HttpTestingController;
    let elemDefault: ISessionExamen;
    let expectedResult: ISessionExamen | ISessionExamen[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SessionExamenService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        libelle: 'AAAAAAA',
        mois: MoisAnnee.JANVIER,
        annee: 0,
        type: TypeExamen.CONTROLE,
        actif: false,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a SessionExamen', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SessionExamen()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SessionExamen', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
            mois: 'BBBBBB',
            annee: 1,
            type: 'BBBBBB',
            actif: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SessionExamen', () => {
        const patchObject = Object.assign(
          {
            libelle: 'BBBBBB',
            annee: 1,
          },
          new SessionExamen()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SessionExamen', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
            mois: 'BBBBBB',
            annee: 1,
            type: 'BBBBBB',
            actif: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a SessionExamen', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSessionExamenToCollectionIfMissing', () => {
        it('should add a SessionExamen to an empty array', () => {
          const sessionExamen: ISessionExamen = { id: 123 };
          expectedResult = service.addSessionExamenToCollectionIfMissing([], sessionExamen);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sessionExamen);
        });

        it('should not add a SessionExamen to an array that contains it', () => {
          const sessionExamen: ISessionExamen = { id: 123 };
          const sessionExamenCollection: ISessionExamen[] = [
            {
              ...sessionExamen,
            },
            { id: 456 },
          ];
          expectedResult = service.addSessionExamenToCollectionIfMissing(sessionExamenCollection, sessionExamen);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SessionExamen to an array that doesn't contain it", () => {
          const sessionExamen: ISessionExamen = { id: 123 };
          const sessionExamenCollection: ISessionExamen[] = [{ id: 456 }];
          expectedResult = service.addSessionExamenToCollectionIfMissing(sessionExamenCollection, sessionExamen);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sessionExamen);
        });

        it('should add only unique SessionExamen to an array', () => {
          const sessionExamenArray: ISessionExamen[] = [{ id: 123 }, { id: 456 }, { id: 99872 }];
          const sessionExamenCollection: ISessionExamen[] = [{ id: 123 }];
          expectedResult = service.addSessionExamenToCollectionIfMissing(sessionExamenCollection, ...sessionExamenArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const sessionExamen: ISessionExamen = { id: 123 };
          const sessionExamen2: ISessionExamen = { id: 456 };
          expectedResult = service.addSessionExamenToCollectionIfMissing([], sessionExamen, sessionExamen2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(sessionExamen);
          expect(expectedResult).toContain(sessionExamen2);
        });

        it('should accept null and undefined values', () => {
          const sessionExamen: ISessionExamen = { id: 123 };
          expectedResult = service.addSessionExamenToCollectionIfMissing([], null, sessionExamen, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(sessionExamen);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
