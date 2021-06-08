import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-matieres-etudiant',
  templateUrl: './matieres-etudiant.component.html',
  styleUrls: ['./matieres-etudiant.component.scss'],
})
export class MatieresEtudiantComponent implements OnInit {
  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    const x = 1 + 1;
  }
}
