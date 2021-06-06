jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IInscription, Inscription } from '../inscription.model';
import { InscriptionService } from '../service/inscription.service';

import { InscriptionRoutingResolveService } from './inscription-routing-resolve.service';

describe('Service Tests', () => {
  describe('Inscription routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: InscriptionRoutingResolveService;
    let service: InscriptionService;
    let resultInscription: IInscription | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(InscriptionRoutingResolveService);
      service = TestBed.inject(InscriptionService);
      resultInscription = undefined;
    });

    describe('resolve', () => {
      it('should return IInscription returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInscription = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInscription).toEqual({ id: 123 });
      });

      it('should return new IInscription if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInscription = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultInscription).toEqual(new Inscription());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultInscription = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultInscription).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
