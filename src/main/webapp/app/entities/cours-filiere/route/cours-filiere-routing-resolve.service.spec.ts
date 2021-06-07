jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICoursFiliere, CoursFiliere } from '../cours-filiere.model';
import { CoursFiliereService } from '../service/cours-filiere.service';

import { CoursFiliereRoutingResolveService } from './cours-filiere-routing-resolve.service';

describe('Service Tests', () => {
  describe('CoursFiliere routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CoursFiliereRoutingResolveService;
    let service: CoursFiliereService;
    let resultCoursFiliere: ICoursFiliere | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CoursFiliereRoutingResolveService);
      service = TestBed.inject(CoursFiliereService);
      resultCoursFiliere = undefined;
    });

    describe('resolve', () => {
      it('should return ICoursFiliere returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoursFiliere = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCoursFiliere).toEqual({ id: 123 });
      });

      it('should return new ICoursFiliere if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoursFiliere = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCoursFiliere).toEqual(new CoursFiliere());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoursFiliere = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCoursFiliere).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
