import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ConditionSelection } from 'app/entities/enumerations/condition-selection.model';
import { ConditionAppliquer } from 'app/entities/enumerations/condition-appliquer.model';
import { IDeliberation, Deliberation } from '../deliberation.model';

import { DeliberationService } from './deliberation.service';

describe('Service Tests', () => {
  describe('Deliberation Service', () => {
    let service: DeliberationService;
    let httpMock: HttpTestingController;
    let elemDefault: IDeliberation;
    let expectedResult: IDeliberation | IDeliberation[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DeliberationService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        critereSelection: ConditionSelection.MOYENNE_EGALE_A,
        valeurSelectionDebut: 0,
        valeurSelectionFin: 0,
        critereAppliquer: ConditionAppliquer.AJOUTER,
        valeurAppliquer: 0,
        dateDeliberation: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dateDeliberation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Deliberation', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dateDeliberation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDeliberation: currentDate,
          },
          returnedFromService
        );

        service.create(new Deliberation()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Deliberation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            critereSelection: 'BBBBBB',
            valeurSelectionDebut: 1,
            valeurSelectionFin: 1,
            critereAppliquer: 'BBBBBB',
            valeurAppliquer: 1,
            dateDeliberation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDeliberation: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Deliberation', () => {
        const patchObject = Object.assign(
          {
            valeurAppliquer: 1,
          },
          new Deliberation()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dateDeliberation: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Deliberation', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            critereSelection: 'BBBBBB',
            valeurSelectionDebut: 1,
            valeurSelectionFin: 1,
            critereAppliquer: 'BBBBBB',
            valeurAppliquer: 1,
            dateDeliberation: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dateDeliberation: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Deliberation', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDeliberationToCollectionIfMissing', () => {
        it('should add a Deliberation to an empty array', () => {
          const deliberation: IDeliberation = { id: 123 };
          expectedResult = service.addDeliberationToCollectionIfMissing([], deliberation);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(deliberation);
        });

        it('should not add a Deliberation to an array that contains it', () => {
          const deliberation: IDeliberation = { id: 123 };
          const deliberationCollection: IDeliberation[] = [
            {
              ...deliberation,
            },
            { id: 456 },
          ];
          expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, deliberation);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Deliberation to an array that doesn't contain it", () => {
          const deliberation: IDeliberation = { id: 123 };
          const deliberationCollection: IDeliberation[] = [{ id: 456 }];
          expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, deliberation);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(deliberation);
        });

        it('should add only unique Deliberation to an array', () => {
          const deliberationArray: IDeliberation[] = [{ id: 123 }, { id: 456 }, { id: 86389 }];
          const deliberationCollection: IDeliberation[] = [{ id: 123 }];
          expectedResult = service.addDeliberationToCollectionIfMissing(deliberationCollection, ...deliberationArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const deliberation: IDeliberation = { id: 123 };
          const deliberation2: IDeliberation = { id: 456 };
          expectedResult = service.addDeliberationToCollectionIfMissing([], deliberation, deliberation2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(deliberation);
          expect(expectedResult).toContain(deliberation2);
        });

        it('should accept null and undefined values', () => {
          const deliberation: IDeliberation = { id: 123 };
          expectedResult = service.addDeliberationToCollectionIfMissing([], null, deliberation, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(deliberation);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
