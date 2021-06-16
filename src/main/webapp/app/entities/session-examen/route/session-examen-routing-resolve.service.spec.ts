jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ISessionExamen, SessionExamen } from '../session-examen.model';
import { SessionExamenService } from '../service/session-examen.service';

import { SessionExamenRoutingResolveService } from './session-examen-routing-resolve.service';

describe('Service Tests', () => {
  describe('SessionExamen routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: SessionExamenRoutingResolveService;
    let service: SessionExamenService;
    let resultSessionExamen: ISessionExamen | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(SessionExamenRoutingResolveService);
      service = TestBed.inject(SessionExamenService);
      resultSessionExamen = undefined;
    });

    describe('resolve', () => {
      it('should return ISessionExamen returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSessionExamen = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSessionExamen).toEqual({ id: 123 });
      });

      it('should return new ISessionExamen if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSessionExamen = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultSessionExamen).toEqual(new SessionExamen());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultSessionExamen = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultSessionExamen).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
