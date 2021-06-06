import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDispenser } from '../dispenser.model';

@Component({
  selector: 'jhi-dispenser-detail',
  templateUrl: './dispenser-detail.component.html',
})
export class DispenserDetailComponent implements OnInit {
  dispenser: IDispenser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dispenser }) => {
      this.dispenser = dispenser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
