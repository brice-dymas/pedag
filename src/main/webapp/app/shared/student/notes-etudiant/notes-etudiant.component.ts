import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-notes-etudiant',
  templateUrl: './notes-etudiant.component.html',
  styleUrls: ['./notes-etudiant.component.scss'],
})
export class NotesEtudiantComponent implements OnInit {
  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    const x = 1 + 1;
  }
}
