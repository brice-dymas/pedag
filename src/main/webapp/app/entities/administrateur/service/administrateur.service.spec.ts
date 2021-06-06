import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Grade } from 'app/entities/enumerations/grade.model';
import { IAdministrateur, Administrateur } from '../administrateur.model';

import { AdministrateurService } from './administrateur.service';

describe('Service Tests', () => {
  describe('Administrateur Service', () => {
    let service: AdministrateurService;
    let httpMock: HttpTestingController;
    let elemDefault: IAdministrateur;
    let expectedResult: IAdministrateur | IAdministrateur[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AdministrateurService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        nom: 'AAAAAAA',
        prenom: 'AAAAAAA',
        email: 'AAAAAAA',
        grade: Grade.M,
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

      it('should create a Administrateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Administrateur()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Administrateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            grade: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Administrateur', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            email: 'BBBBBB',
          },
          new Administrateur()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Administrateur', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            nom: 'BBBBBB',
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            grade: 'BBBBBB',
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

      it('should delete a Administrateur', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAdministrateurToCollectionIfMissing', () => {
        it('should add a Administrateur to an empty array', () => {
          const administrateur: IAdministrateur = { id: 123 };
          expectedResult = service.addAdministrateurToCollectionIfMissing([], administrateur);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(administrateur);
        });

        it('should not add a Administrateur to an array that contains it', () => {
          const administrateur: IAdministrateur = { id: 123 };
          const administrateurCollection: IAdministrateur[] = [
            {
              ...administrateur,
            },
            { id: 456 },
          ];
          expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, administrateur);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Administrateur to an array that doesn't contain it", () => {
          const administrateur: IAdministrateur = { id: 123 };
          const administrateurCollection: IAdministrateur[] = [{ id: 456 }];
          expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, administrateur);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(administrateur);
        });

        it('should add only unique Administrateur to an array', () => {
          const administrateurArray: IAdministrateur[] = [{ id: 123 }, { id: 456 }, { id: 69912 }];
          const administrateurCollection: IAdministrateur[] = [{ id: 123 }];
          expectedResult = service.addAdministrateurToCollectionIfMissing(administrateurCollection, ...administrateurArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const administrateur: IAdministrateur = { id: 123 };
          const administrateur2: IAdministrateur = { id: 456 };
          expectedResult = service.addAdministrateurToCollectionIfMissing([], administrateur, administrateur2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(administrateur);
          expect(expectedResult).toContain(administrateur2);
        });

        it('should accept null and undefined values', () => {
          const administrateur: IAdministrateur = { id: 123 };
          expectedResult = service.addAdministrateurToCollectionIfMissing([], null, administrateur, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(administrateur);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
