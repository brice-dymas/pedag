import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IAnneeAcademique, AnneeAcademique } from '../annee-academique.model';

import { AnneeAcademiqueService } from './annee-academique.service';

describe('Service Tests', () => {
  describe('AnneeAcademique Service', () => {
    let service: AnneeAcademiqueService;
    let httpMock: HttpTestingController;
    let elemDefault: IAnneeAcademique;
    let expectedResult: IAnneeAcademique | IAnneeAcademique[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(AnneeAcademiqueService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        libelle: 'AAAAAAA',
        current: false,
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

      it('should create a AnneeAcademique', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new AnneeAcademique()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a AnneeAcademique', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
            current: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a AnneeAcademique', () => {
        const patchObject = Object.assign({}, new AnneeAcademique());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of AnneeAcademique', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            libelle: 'BBBBBB',
            current: true,
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

      it('should delete a AnneeAcademique', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addAnneeAcademiqueToCollectionIfMissing', () => {
        it('should add a AnneeAcademique to an empty array', () => {
          const anneeAcademique: IAnneeAcademique = { id: 123 };
          expectedResult = service.addAnneeAcademiqueToCollectionIfMissing([], anneeAcademique);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(anneeAcademique);
        });

        it('should not add a AnneeAcademique to an array that contains it', () => {
          const anneeAcademique: IAnneeAcademique = { id: 123 };
          const anneeAcademiqueCollection: IAnneeAcademique[] = [
            {
              ...anneeAcademique,
            },
            { id: 456 },
          ];
          expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, anneeAcademique);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a AnneeAcademique to an array that doesn't contain it", () => {
          const anneeAcademique: IAnneeAcademique = { id: 123 };
          const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 456 }];
          expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, anneeAcademique);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(anneeAcademique);
        });

        it('should add only unique AnneeAcademique to an array', () => {
          const anneeAcademiqueArray: IAnneeAcademique[] = [{ id: 123 }, { id: 456 }, { id: 4614 }];
          const anneeAcademiqueCollection: IAnneeAcademique[] = [{ id: 123 }];
          expectedResult = service.addAnneeAcademiqueToCollectionIfMissing(anneeAcademiqueCollection, ...anneeAcademiqueArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const anneeAcademique: IAnneeAcademique = { id: 123 };
          const anneeAcademique2: IAnneeAcademique = { id: 456 };
          expectedResult = service.addAnneeAcademiqueToCollectionIfMissing([], anneeAcademique, anneeAcademique2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(anneeAcademique);
          expect(expectedResult).toContain(anneeAcademique2);
        });

        it('should accept null and undefined values', () => {
          const anneeAcademique: IAnneeAcademique = { id: 123 };
          expectedResult = service.addAnneeAcademiqueToCollectionIfMissing([], null, anneeAcademique, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(anneeAcademique);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
