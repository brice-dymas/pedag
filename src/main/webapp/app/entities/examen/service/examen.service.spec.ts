import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { TypeExamen } from 'app/entities/enumerations/type-examen.model';
import { Semestre } from 'app/entities/enumerations/semestre.model';
import { IExamen, Examen } from '../examen.model';

import { ExamenService } from './examen.service';

describe('Service Tests', () => {
  describe('Examen Service', () => {
    let service: ExamenService;
    let httpMock: HttpTestingController;
    let elemDefault: IExamen;
    let expectedResult: IExamen | IExamen[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ExamenService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dateExamen: currentDate,
        type: TypeExamen.CONTROLE,
        semestre: Semestre.SEMESTRE1,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateExamen: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Examen', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateExamen: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateExamen: currentDate,
          },
          returnedFromService
        );

        service.create(new Examen()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Examen', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateExamen: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            semestre: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateExamen: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Examen', () => {
        const patchObject = Object.assign(
          {
            type: 'BBBBBB',
            semestre: 'BBBBBB',
          },
          new Examen()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateExamen: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Examen', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dateExamen: currentDate.format(DATE_FORMAT),
            type: 'BBBBBB',
            semestre: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateExamen: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Examen', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addExamenToCollectionIfMissing', () => {
        it('should add a Examen to an empty array', () => {
          const examen: IExamen = { id: 123 };
          expectedResult = service.addExamenToCollectionIfMissing([], examen);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(examen);
        });

        it('should not add a Examen to an array that contains it', () => {
          const examen: IExamen = { id: 123 };
          const examenCollection: IExamen[] = [
            {
              ...examen,
            },
            { id: 456 },
          ];
          expectedResult = service.addExamenToCollectionIfMissing(examenCollection, examen);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Examen to an array that doesn't contain it", () => {
          const examen: IExamen = { id: 123 };
          const examenCollection: IExamen[] = [{ id: 456 }];
          expectedResult = service.addExamenToCollectionIfMissing(examenCollection, examen);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(examen);
        });

        it('should add only unique Examen to an array', () => {
          const examenArray: IExamen[] = [{ id: 123 }, { id: 456 }, { id: 25706 }];
          const examenCollection: IExamen[] = [{ id: 123 }];
          expectedResult = service.addExamenToCollectionIfMissing(examenCollection, ...examenArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const examen: IExamen = { id: 123 };
          const examen2: IExamen = { id: 456 };
          expectedResult = service.addExamenToCollectionIfMissing([], examen, examen2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(examen);
          expect(expectedResult).toContain(examen2);
        });

        it('should accept null and undefined values', () => {
          const examen: IExamen = { id: 123 };
          expectedResult = service.addExamenToCollectionIfMissing([], null, examen, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(examen);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
