import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ICourriel, Courriel } from '../courriel.model';

import { CourrielService } from './courriel.service';

describe('Service Tests', () => {
  describe('Courriel Service', () => {
    let service: CourrielService;
    let httpMock: HttpTestingController;
    let elemDefault: ICourriel;
    let expectedResult: ICourriel | ICourriel[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CourrielService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        sender: 'AAAAAAA',
        receiver: 'AAAAAAA',
        objet: 'AAAAAAA',
        message: 'AAAAAAA',
        dateCreation: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateCreation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Courriel', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateCreation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.create(new Courriel()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Courriel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sender: 'BBBBBB',
            receiver: 'BBBBBB',
            objet: 'BBBBBB',
            message: 'BBBBBB',
            dateCreation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Courriel', () => {
        const patchObject = Object.assign(
          {
            receiver: 'BBBBBB',
            objet: 'BBBBBB',
            dateCreation: currentDate.format(DATE_FORMAT),
          },
          new Courriel()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Courriel', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            sender: 'BBBBBB',
            receiver: 'BBBBBB',
            objet: 'BBBBBB',
            message: 'BBBBBB',
            dateCreation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateCreation: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Courriel', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCourrielToCollectionIfMissing', () => {
        it('should add a Courriel to an empty array', () => {
          const courriel: ICourriel = { id: 123 };
          expectedResult = service.addCourrielToCollectionIfMissing([], courriel);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courriel);
        });

        it('should not add a Courriel to an array that contains it', () => {
          const courriel: ICourriel = { id: 123 };
          const courrielCollection: ICourriel[] = [
            {
              ...courriel,
            },
            { id: 456 },
          ];
          expectedResult = service.addCourrielToCollectionIfMissing(courrielCollection, courriel);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Courriel to an array that doesn't contain it", () => {
          const courriel: ICourriel = { id: 123 };
          const courrielCollection: ICourriel[] = [{ id: 456 }];
          expectedResult = service.addCourrielToCollectionIfMissing(courrielCollection, courriel);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courriel);
        });

        it('should add only unique Courriel to an array', () => {
          const courrielArray: ICourriel[] = [{ id: 123 }, { id: 456 }, { id: 8293 }];
          const courrielCollection: ICourriel[] = [{ id: 123 }];
          expectedResult = service.addCourrielToCollectionIfMissing(courrielCollection, ...courrielArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const courriel: ICourriel = { id: 123 };
          const courriel2: ICourriel = { id: 456 };
          expectedResult = service.addCourrielToCollectionIfMissing([], courriel, courriel2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(courriel);
          expect(expectedResult).toContain(courriel2);
        });

        it('should accept null and undefined values', () => {
          const courriel: ICourriel = { id: 123 };
          expectedResult = service.addCourrielToCollectionIfMissing([], null, courriel, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(courriel);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
