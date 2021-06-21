import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { RequeteDeleteDialogComponent } from 'app/entities/requete/delete/requete-delete-dialog.component';
import { IRequete } from 'app/entities/requete/requete.model';
import { RequeteService } from 'app/entities/requete/service/requete.service';
import { combineLatest, Subscription } from 'rxjs';

@Component({
  selector: 'jhi-requete-etudiant',
  templateUrl: './requete-etudiant.component.html',
  styleUrls: ['./requete-etudiant.component.scss'],
})
export class RequeteEtudiantComponent implements OnInit, OnDestroy {
  requetes?: IRequete[];
  statutAttente = 'EN ATTENTE';
  statutFonde = 'FONDÉE';
  statutNonFonde = 'NON FONDÉE';
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
    protected requeteService: RequeteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
    this.handleNavigation(this.account.id);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  refresh(): void {
    this.loadPage(this.account.id);
  }

  loadPage(id: number, page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.requeteService
      .queryByStudent(id, {
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
        this.loadPage(this.account.id);
      }
    });
  }

  protected handleNavigation(id: number): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      if (pageNumber !== this.page) {
        this.loadPage(id, pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IRequete[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/etudiant/mes-requetes'], {
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
