import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAnneeAcademique } from '../annee-academique.model';

@Component({
  selector: 'jhi-annee-academique-detail',
  templateUrl: './annee-academique-detail.component.html',
})
export class AnneeAcademiqueDetailComponent implements OnInit {
  anneeAcademique: IAnneeAcademique | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ anneeAcademique }) => {
      this.anneeAcademique = anneeAcademique;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
