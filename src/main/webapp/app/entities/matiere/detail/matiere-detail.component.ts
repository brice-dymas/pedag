import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { DataUtils } from 'app/core/util/data-util.service';
import { PieceJointeDeleteDialogComponent } from 'app/entities/piece-jointe/delete/piece-jointe-delete-dialog.component';
import { IPieceJointe } from 'app/entities/piece-jointe/piece-jointe.model';
import { PieceJointeService } from 'app/entities/piece-jointe/service/piece-jointe.service';
import { combineLatest } from 'rxjs';

import { IMatiere } from '../matiere.model';

@Component({
  selector: 'jhi-matiere-detail',
  templateUrl: './matiere-detail.component.html',
})
export class MatiereDetailComponent implements OnInit {
  matiere: IMatiere | null = null;
  pieceJointes?: IPieceJointe[];
  isLoading = false;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page?: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected dataUtils: DataUtils,
    protected router: Router,
    protected modalService: NgbModal,
    private pieceJointeService: PieceJointeService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ matiere }) => {
      this.matiere = matiere;
      this.handleNavigation(matiere.id);
    });
  }

  refresh(): void {
    if (this.matiere) {
      this.loadPage(Number(this.matiere.id));
    }
  }

  previousState(): void {
    window.history.back();
  }

  loadPage(id: number, page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.pieceJointeService
      .queryByMatiere(id, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
      })
      .subscribe(
        (res: HttpResponse<IPieceJointe[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  trackId(index: number, item: IPieceJointe): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(pieceJointe: IPieceJointe): void {
    const modalRef = this.modalService.open(PieceJointeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pieceJointe = pieceJointe;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.refresh();
      }
    });
  }

  protected handleNavigation(id: number): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      if (pageNumber !== this.page) {
        this.loadPage(id, pageNumber, true);
      }
    });
  }

  protected onSuccess(data: IPieceJointe[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/matieres', this.matiere?.id], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pieceJointes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
