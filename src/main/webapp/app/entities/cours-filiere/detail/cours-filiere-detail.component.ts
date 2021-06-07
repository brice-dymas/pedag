import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICoursFiliere } from '../cours-filiere.model';

@Component({
  selector: 'jhi-cours-filiere-detail',
  templateUrl: './cours-filiere-detail.component.html',
})
export class CoursFiliereDetailComponent implements OnInit {
  coursFiliere: ICoursFiliere | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coursFiliere }) => {
      this.coursFiliere = coursFiliere;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
