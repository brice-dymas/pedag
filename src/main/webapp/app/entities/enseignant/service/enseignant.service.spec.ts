import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Grade } from 'app/entities/enumerations/grade.model';
import { Statut } from 'app/entities/enumerations/statut.model';
import { IEnseignant, Enseignant } from '../enseignant.model';

import { EnseignantService } from './enseignant.service';

describe('Service Tests', () => {
  describe('Enseignant Service', () => {
    let service: EnseignantService;
    let httpMock: HttpTestingController;
    let elemDefault: IEnseignant;
    let expectedResult: IEnseignant | IEnseignant[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EnseignantService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        diplome: 'AAAAAAA',
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
        grade: Grade.M,
        email: 'AAAAAAA',
        telephone: 'AAAAAAA',
        statut: Statut.VACATAIRE,
        photoContentType: 'image/png',
        photo: 'AAAAAAA',
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

      it('should create a Enseignant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Enseignant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Enseignant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            diplome: 'BBBBBB',
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            grade: 'BBBBBB',
            email: 'BBBBBB',
            telephone: 'BBBBBB',
            statut: 'BBBBBB',
            photo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Enseignant', () => {
        const patchObject = Object.assign(
          {
            diplome: 'BBBBBB',
            telephone: 'BBBBBB',
            statut: 'BBBBBB',
            photo: 'BBBBBB',
          },
          new Enseignant()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Enseignant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            diplome: 'BBBBBB',
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            grade: 'BBBBBB',
            email: 'BBBBBB',
            telephone: 'BBBBBB',
            statut: 'BBBBBB',
            photo: 'BBBBBB',
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

      it('should delete a Enseignant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEnseignantToCollectionIfMissing', () => {
        it('should add a Enseignant to an empty array', () => {
          const enseignant: IEnseignant = { id: 123 };
          expectedResult = service.addEnseignantToCollectionIfMissing([], enseignant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(enseignant);
        });

        it('should not add a Enseignant to an array that contains it', () => {
          const enseignant: IEnseignant = { id: 123 };
          const enseignantCollection: IEnseignant[] = [
            {
              ...enseignant,
            },
            { id: 456 },
          ];
          expectedResult = service.addEnseignantToCollectionIfMissing(enseignantCollection, enseignant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Enseignant to an array that doesn't contain it", () => {
          const enseignant: IEnseignant = { id: 123 };
          const enseignantCollection: IEnseignant[] = [{ id: 456 }];
          expectedResult = service.addEnseignantToCollectionIfMissing(enseignantCollection, enseignant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(enseignant);
        });

        it('should add only unique Enseignant to an array', () => {
          const enseignantArray: IEnseignant[] = [{ id: 123 }, { id: 456 }, { id: 7536 }];
          const enseignantCollection: IEnseignant[] = [{ id: 123 }];
          expectedResult = service.addEnseignantToCollectionIfMissing(enseignantCollection, ...enseignantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const enseignant: IEnseignant = { id: 123 };
          const enseignant2: IEnseignant = { id: 456 };
          expectedResult = service.addEnseignantToCollectionIfMissing([], enseignant, enseignant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(enseignant);
          expect(expectedResult).toContain(enseignant2);
        });

        it('should accept null and undefined values', () => {
          const enseignant: IEnseignant = { id: 123 };
          expectedResult = service.addEnseignantToCollectionIfMissing([], null, enseignant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(enseignant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
