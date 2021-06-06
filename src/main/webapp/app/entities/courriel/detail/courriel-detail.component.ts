import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourriel } from '../courriel.model';

@Component({
  selector: 'jhi-courriel-detail',
  templateUrl: './courriel-detail.component.html',
})
export class CourrielDetailComponent implements OnInit {
  courriel: ICourriel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courriel }) => {
      this.courriel = courriel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
