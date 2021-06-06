jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPieceJointe, PieceJointe } from '../piece-jointe.model';
import { PieceJointeService } from '../service/piece-jointe.service';

import { PieceJointeRoutingResolveService } from './piece-jointe-routing-resolve.service';

describe('Service Tests', () => {
  describe('PieceJointe routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PieceJointeRoutingResolveService;
    let service: PieceJointeService;
    let resultPieceJointe: IPieceJointe | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PieceJointeRoutingResolveService);
      service = TestBed.inject(PieceJointeService);
      resultPieceJointe = undefined;
    });

    describe('resolve', () => {
      it('should return IPieceJointe returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPieceJointe = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPieceJointe).toEqual({ id: 123 });
      });

      it('should return new IPieceJointe if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPieceJointe = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPieceJointe).toEqual(new PieceJointe());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPieceJointe = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPieceJointe).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
