import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICoursFiliere, CoursFiliere } from '../cours-filiere.model';

import { CoursFiliereService } from './cours-filiere.service';

describe('Service Tests', () => {
  describe('CoursFiliere Service', () => {
    let service: CoursFiliereService;
    let httpMock: HttpTestingController;
    let elemDefault: ICoursFiliere;
    let expectedResult: ICoursFiliere | ICoursFiliere[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CoursFiliereService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        quotaHoraire: 0,
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

      it('should create a CoursFiliere', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new CoursFiliere()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a CoursFiliere', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quotaHoraire: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a CoursFiliere', () => {
        const patchObject = Object.assign({}, new CoursFiliere());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of CoursFiliere', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            quotaHoraire: 1,
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

      it('should delete a CoursFiliere', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCoursFiliereToCollectionIfMissing', () => {
        it('should add a CoursFiliere to an empty array', () => {
          const coursFiliere: ICoursFiliere = { id: 123 };
          expectedResult = service.addCoursFiliereToCollectionIfMissing([], coursFiliere);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(coursFiliere);
        });

        it('should not add a CoursFiliere to an array that contains it', () => {
          const coursFiliere: ICoursFiliere = { id: 123 };
          const coursFiliereCollection: ICoursFiliere[] = [
            {
              ...coursFiliere,
            },
            { id: 456 },
          ];
          expectedResult = service.addCoursFiliereToCollectionIfMissing(coursFiliereCollection, coursFiliere);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a CoursFiliere to an array that doesn't contain it", () => {
          const coursFiliere: ICoursFiliere = { id: 123 };
          const coursFiliereCollection: ICoursFiliere[] = [{ id: 456 }];
          expectedResult = service.addCoursFiliereToCollectionIfMissing(coursFiliereCollection, coursFiliere);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(coursFiliere);
        });

        it('should add only unique CoursFiliere to an array', () => {
          const coursFiliereArray: ICoursFiliere[] = [{ id: 123 }, { id: 456 }, { id: 22428 }];
          const coursFiliereCollection: ICoursFiliere[] = [{ id: 123 }];
          expectedResult = service.addCoursFiliereToCollectionIfMissing(coursFiliereCollection, ...coursFiliereArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const coursFiliere: ICoursFiliere = { id: 123 };
          const coursFiliere2: ICoursFiliere = { id: 456 };
          expectedResult = service.addCoursFiliereToCollectionIfMissing([], coursFiliere, coursFiliere2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(coursFiliere);
          expect(expectedResult).toContain(coursFiliere2);
        });

        it('should accept null and undefined values', () => {
          const coursFiliere: ICoursFiliere = { id: 123 };
          expectedResult = service.addCoursFiliereToCollectionIfMissing([], null, coursFiliere, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(coursFiliere);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
