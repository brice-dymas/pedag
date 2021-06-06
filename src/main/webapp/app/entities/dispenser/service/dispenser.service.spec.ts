import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Semestre } from 'app/entities/enumerations/semestre.model';
import { IDispenser, Dispenser } from '../dispenser.model';

import { DispenserService } from './dispenser.service';

describe('Service Tests', () => {
  describe('Dispenser Service', () => {
    let service: DispenserService;
    let httpMock: HttpTestingController;
    let elemDefault: IDispenser;
    let expectedResult: IDispenser | IDispenser[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DispenserService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        semestre: Semestre.SEMESTRE1,
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

      it('should create a Dispenser', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dispenser()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dispenser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            semestre: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Dispenser', () => {
        const patchObject = Object.assign(
          {
            semestre: 'BBBBBB',
          },
          new Dispenser()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dispenser', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            semestre: 'BBBBBB',
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

      it('should delete a Dispenser', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDispenserToCollectionIfMissing', () => {
        it('should add a Dispenser to an empty array', () => {
          const dispenser: IDispenser = { id: 123 };
          expectedResult = service.addDispenserToCollectionIfMissing([], dispenser);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dispenser);
        });

        it('should not add a Dispenser to an array that contains it', () => {
          const dispenser: IDispenser = { id: 123 };
          const dispenserCollection: IDispenser[] = [
            {
              ...dispenser,
            },
            { id: 456 },
          ];
          expectedResult = service.addDispenserToCollectionIfMissing(dispenserCollection, dispenser);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Dispenser to an array that doesn't contain it", () => {
          const dispenser: IDispenser = { id: 123 };
          const dispenserCollection: IDispenser[] = [{ id: 456 }];
          expectedResult = service.addDispenserToCollectionIfMissing(dispenserCollection, dispenser);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dispenser);
        });

        it('should add only unique Dispenser to an array', () => {
          const dispenserArray: IDispenser[] = [{ id: 123 }, { id: 456 }, { id: 72514 }];
          const dispenserCollection: IDispenser[] = [{ id: 123 }];
          expectedResult = service.addDispenserToCollectionIfMissing(dispenserCollection, ...dispenserArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dispenser: IDispenser = { id: 123 };
          const dispenser2: IDispenser = { id: 456 };
          expectedResult = service.addDispenserToCollectionIfMissing([], dispenser, dispenser2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dispenser);
          expect(expectedResult).toContain(dispenser2);
        });

        it('should accept null and undefined values', () => {
          const dispenser: IDispenser = { id: 123 };
          expectedResult = service.addDispenserToCollectionIfMissing([], null, dispenser, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dispenser);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
