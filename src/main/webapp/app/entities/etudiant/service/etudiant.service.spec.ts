import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IEtudiant, Etudiant } from '../etudiant.model';

import { EtudiantService } from './etudiant.service';

describe('Service Tests', () => {
  describe('Etudiant Service', () => {
    let service: EtudiantService;
    let httpMock: HttpTestingController;
    let elemDefault: IEtudiant;
    let expectedResult: IEtudiant | IEtudiant[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EtudiantService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        matricule: 'AAAAAAA',
        nom: 'AAAAAAA',
        dateNaissance: currentDate,
        prenom: 'AAAAAAA',
        email: 'AAAAAAA',
        telephone: 'AAAAAAA',
        photoContentType: 'image/png',
        photo: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateNaissance: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Etudiant', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateNaissance: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.create(new Etudiant()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Etudiant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            matricule: 'BBBBBB',
            nom: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_FORMAT),
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            telephone: 'BBBBBB',
            photo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Etudiant', () => {
        const patchObject = Object.assign(
          {
            nom: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_FORMAT),
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            photo: 'BBBBBB',
          },
          new Etudiant()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Etudiant', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            matricule: 'BBBBBB',
            nom: 'BBBBBB',
            dateNaissance: currentDate.format(DATE_FORMAT),
            prenom: 'BBBBBB',
            email: 'BBBBBB',
            telephone: 'BBBBBB',
            photo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateNaissance: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Etudiant', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEtudiantToCollectionIfMissing', () => {
        it('should add a Etudiant to an empty array', () => {
          const etudiant: IEtudiant = { id: 123 };
          expectedResult = service.addEtudiantToCollectionIfMissing([], etudiant);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(etudiant);
        });

        it('should not add a Etudiant to an array that contains it', () => {
          const etudiant: IEtudiant = { id: 123 };
          const etudiantCollection: IEtudiant[] = [
            {
              ...etudiant,
            },
            { id: 456 },
          ];
          expectedResult = service.addEtudiantToCollectionIfMissing(etudiantCollection, etudiant);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Etudiant to an array that doesn't contain it", () => {
          const etudiant: IEtudiant = { id: 123 };
          const etudiantCollection: IEtudiant[] = [{ id: 456 }];
          expectedResult = service.addEtudiantToCollectionIfMissing(etudiantCollection, etudiant);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(etudiant);
        });

        it('should add only unique Etudiant to an array', () => {
          const etudiantArray: IEtudiant[] = [{ id: 123 }, { id: 456 }, { id: 28111 }];
          const etudiantCollection: IEtudiant[] = [{ id: 123 }];
          expectedResult = service.addEtudiantToCollectionIfMissing(etudiantCollection, ...etudiantArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const etudiant: IEtudiant = { id: 123 };
          const etudiant2: IEtudiant = { id: 456 };
          expectedResult = service.addEtudiantToCollectionIfMissing([], etudiant, etudiant2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(etudiant);
          expect(expectedResult).toContain(etudiant2);
        });

        it('should accept null and undefined values', () => {
          const etudiant: IEtudiant = { id: 123 };
          expectedResult = service.addEtudiantToCollectionIfMissing([], null, etudiant, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(etudiant);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
