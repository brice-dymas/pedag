jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDispenser, Dispenser } from '../dispenser.model';
import { DispenserService } from '../service/dispenser.service';

import { DispenserRoutingResolveService } from './dispenser-routing-resolve.service';

describe('Service Tests', () => {
  describe('Dispenser routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DispenserRoutingResolveService;
    let service: DispenserService;
    let resultDispenser: IDispenser | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DispenserRoutingResolveService);
      service = TestBed.inject(DispenserService);
      resultDispenser = undefined;
    });

    describe('resolve', () => {
      it('should return IDispenser returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDispenser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDispenser).toEqual({ id: 123 });
      });

      it('should return new IDispenser if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDispenser = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDispenser).toEqual(new Dispenser());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDispenser = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDispenser).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
