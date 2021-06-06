import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPieceJointe } from '../piece-jointe.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-piece-jointe-detail',
  templateUrl: './piece-jointe-detail.component.html',
})
export class PieceJointeDetailComponent implements OnInit {
  pieceJointe: IPieceJointe | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pieceJointe }) => {
      this.pieceJointe = pieceJointe;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
