import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { ICourriel } from 'app/entities/courriel/courriel.model';
import { CourrielDeleteDialogComponent } from 'app/entities/courriel/delete/courriel-delete-dialog.component';
import { CourrielService } from 'app/entities/courriel/service/courriel.service';
import { combineLatest, Subscription } from 'rxjs';

@Component({
  selector: 'jhi-courriel-etudiant',
  templateUrl: './courriel-etudiant.component.html',
  styleUrls: ['./courriel-etudiant.component.scss'],
})
export class CourrielEtudiantComponent implements OnInit, OnDestroy {
  courriels?: ICourriel[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  account: any | null = null;
  authSubscription?: Subscription;

  constructor(
    private accountService: AccountService,
    protected courrielService: CourrielService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.courrielService
      .queryForEtudiant(this.account.id, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
      })
      .subscribe(
        (res: HttpResponse<ICourriel[]>) => {
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
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.handleNavigation();
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  trackId(index: number, item: ICourriel): number {
    return item.id!;
  }

  delete(courriel: ICourriel): void {
    const modalRef = this.modalService.open(CourrielDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.courriel = courriel;
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

  protected onSuccess(data: ICourriel[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/etudiant/mes-courriels'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
        },
      });
    }
    this.courriels = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
