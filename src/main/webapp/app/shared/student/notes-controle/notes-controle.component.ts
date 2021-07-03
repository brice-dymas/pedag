import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { AccountService } from 'app/core/auth/account.service';
import { TokenService } from 'app/core/auth/token.service';
import { INote } from 'app/entities/note/note.model';
import { NoteService } from 'app/entities/note/service/note.service';
import { combineLatest, Subscription } from 'rxjs';

@Component({
  selector: 'jhi-notes-controle',
  templateUrl: './notes-controle.component.html',
  styleUrls: ['./notes-controle.component.scss'],
})
export class NotesControleComponent implements OnInit {
  notes?: INote[];
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
    protected noteService: NoteService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected tokenService: TokenService,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.ins = this.tokenService.getInscription();
    this.handleNavigation();
  }

  refresh(): void {
    this.handleNavigation();
  }
  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.noteService
      .queryByStudentAndTypeExam(this.ins.id, 0, {
        page: pageToLoad - 1,
        size: this.itemsPerPage,
      })
      .subscribe(
        (res: HttpResponse<INote[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  trackId(index: number, item: INote): number {
    return item.id!;
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

  protected onSuccess(data: INote[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/etudiant/notes-controle'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
        },
      });
    }
    this.notes = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
