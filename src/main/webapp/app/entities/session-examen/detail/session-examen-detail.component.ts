import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISessionExamen } from '../session-examen.model';

@Component({
  selector: 'jhi-session-examen-detail',
  templateUrl: './session-examen-detail.component.html',
})
export class SessionExamenDetailComponent implements OnInit {
  sessionExamen: ISessionExamen | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sessionExamen }) => {
      this.sessionExamen = sessionExamen;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
