jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAdministrateur, Administrateur } from '../administrateur.model';
import { AdministrateurService } from '../service/administrateur.service';

import { AdministrateurRoutingResolveService } from './administrateur-routing-resolve.service';

describe('Service Tests', () => {
  describe('Administrateur routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: AdministrateurRoutingResolveService;
    let service: AdministrateurService;
    let resultAdministrateur: IAdministrateur | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(AdministrateurRoutingResolveService);
      service = TestBed.inject(AdministrateurService);
      resultAdministrateur = undefined;
    });

    describe('resolve', () => {
      it('should return IAdministrateur returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrateur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdministrateur).toEqual({ id: 123 });
      });

      it('should return new IAdministrateur if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrateur = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultAdministrateur).toEqual(new Administrateur());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultAdministrateur = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultAdministrateur).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
