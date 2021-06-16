jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IModule, Module } from '../module.model';
import { ModuleService } from '../service/module.service';

import { ModuleRoutingResolveService } from './module-routing-resolve.service';

describe('Service Tests', () => {
  describe('Module routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ModuleRoutingResolveService;
    let service: ModuleService;
    let resultModule: IModule | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ModuleRoutingResolveService);
      service = TestBed.inject(ModuleService);
      resultModule = undefined;
    });

    describe('resolve', () => {
      it('should return IModule returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultModule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultModule).toEqual({ id: 123 });
      });

      it('should return new IModule if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultModule = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultModule).toEqual(new Module());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultModule = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultModule).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
