import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { TokenService } from 'app/core/auth/token.service';
import { StatutRequete } from 'app/entities/enumerations/statut-requete.model';
import { RequeteDeleteDialogComponent } from 'app/entities/requete/delete/requete-delete-dialog.component';
import { IRequete } from 'app/entities/requete/requete.model';
import { RequeteService } from 'app/entities/requete/service/requete.service';
import { combineLatest } from 'rxjs';

@Component({
  selector: 'jhi-requete-simple',
  templateUrl: './requete-simple.component.html',
  styleUrls: ['./requete-simple.component.scss'],
})
export class RequeteSimpleComponent implements OnInit {
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
  account: any | null = null;
  ins!: any;

  constructor(
    protected tokenService: TokenService,
    protected requeteService: RequeteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.ins = this.tokenService.getInscription();
    this.handleNavigation();
  }

  refresh(): void {
    this.loadPage();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.requeteService
      .queryByStudentByType(this.ins.id, 2, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
      })
      .subscribe(
        (res: HttpResponse<IRequete[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  trackId(index: number, item: IRequete): number {
    return item.id!;
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

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      if (pageNumber !== this.page) {
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IRequete[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/etudiant/requetes-simple'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
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
