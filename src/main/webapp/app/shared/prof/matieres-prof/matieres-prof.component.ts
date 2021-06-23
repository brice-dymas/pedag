import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { IDispenser } from 'app/entities/dispenser/dispenser.model';
import { DispenserService } from 'app/entities/dispenser/service/dispenser.service';
import { RequeteDeleteDialogComponent } from 'app/entities/requete/delete/requete-delete-dialog.component';
import { SessionExamenService } from 'app/entities/session-examen/service/session-examen.service';
import { ISessionExamen } from 'app/entities/session-examen/session-examen.model';
import { combineLatest, Subscription } from 'rxjs';

@Component({
  selector: 'jhi-matieres-prof',
  templateUrl: './matieres-prof.component.html',
  styleUrls: ['./matieres-prof.component.scss'],
})
export class MatieresProfComponent implements OnInit, OnDestroy {
  dispensers?: IDispenser[];
  activeDipenser?: IDispenser[] | null = [];
  activeSessions?: ISessionExamen[] | null = [];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  account: any | null = null;
  authSubscription?: Subscription;

  editForm = this.fb.group({
    matiere: [null, [Validators.required]],
    sessionExamen: [null, [Validators.required]],
  });

  constructor(
    private accountService: AccountService,
    protected sessionService: SessionExamenService,
    protected dispenserService: DispenserService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected fb: FormBuilder,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => {
      this.account = account;
      this.loadDatas();
      this.handleNavigation();
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  loadDatas(): void {
    this.sessionService.queryActif().subscribe(data => {
      this.activeSessions = data.body;
    });
    this.dispenserService.queryByTeacherActive(this.account.id).subscribe(data => (this.activeDipenser = data.body));
  }

  trackSessionExamenById(index: number, item: ISessionExamen): number {
    return item.id!;
  }

  trackMatiereById(index: number, item: IDispenser): number {
    return item.id!;
  }

  save(): void {
    const formvalues = this.editForm.value;
    this.router.navigate(['/enseignant/matiere', formvalues.matiere.id, 'session', formvalues.sessionExamen.id, 'remplir-notes']);
  }

  refresh(): void {
    this.loadPage();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.dispenserService
      .queryByTeacher(this.account.id, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
      })
      .subscribe(
        (res: HttpResponse<IDispenser[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  trackId(index: number, item: IDispenser): number {
    return item.id!;
  }

  delete(requete: IDispenser): void {
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

  protected onSuccess(data: IDispenser[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/enseignant/mes-cours'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
        },
      });
    }
    this.dispensers = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
