import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { StatutRequete } from 'app/entities/enumerations/statut-requete.model';
import { RejeterRequeteDialogComponent } from 'app/entities/requete/rejeter/rejeter-requete-dialog/rejeter-requete-dialog.component';
import { ValiderRequeteDialogComponent } from 'app/entities/requete/valider/valider-requete-dialog/valider-requete-dialog.component';
import * as dayjs from 'dayjs';
import { combineLatest } from 'rxjs';
import { RequeteDeleteDialogComponent } from '../delete/requete-delete-dialog.component';

import { IRequete, Requete } from '../requete.model';
import { RequeteService } from '../service/requete.service';

@Component({
  selector: 'jhi-requete',
  templateUrl: './requete.component.html',
})
export class RequeteComponent implements OnInit {
  requetes?: IRequete[];

  statutAttente = StatutRequete.EN_ATTENTE;
  statutFonde = StatutRequete.FONDE;
  statutNonFonde = StatutRequete.NON_FONDE;

  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected requeteService: RequeteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.requeteService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRequete[]>) => {
          // eslint-disable-next-line no-console
          console.log('requete ::::::::::::', res);
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
  }

  trackId(index: number, item: IRequete): number {
    return item.id!;
  }

  valider(item: IRequete): void {
    const req = this.getRequeteForValidation(item);
    req.statut = StatutRequete.FONDE;
    this.requeteService.partialUpdate(req).subscribe(() => this.loadPage());
  }

  rejeter(item: IRequete): void {
    const req = this.getRequeteForValidation(item);
    req.statut = StatutRequete.NON_FONDE;
    this.requeteService.partialUpdate(req).subscribe(() => this.loadPage());
  }

  canValidate(item: IRequete): boolean {
    return item.statut === this.statutAttente;
  }

  getRequeteForValidation(item: IRequete): IRequete {
    return {
      ...new Requete(),
      id: item.id,
      traiter: true,
      dateModification: dayjs(),
    };
  }

  delete(requete: IRequete): void {
    const modalRef = this.modalService.open(RequeteDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.requete = requete;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  rejeterRequete(requete: IRequete): void {
    const modalRef = this.modalService.open(RejeterRequeteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.requete = requete;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  validerRequete(requete: IRequete): void {
    const modalRef = this.modalService.open(ValiderRequeteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.requete = requete;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadPage();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IRequete[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/requete'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.requetes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
