import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { Account } from 'app/core/auth/account.model';
import { AccountService } from 'app/core/auth/account.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-dashboard-student',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;

  constructor(private accountService: AccountService, private router: Router) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  openProfile(): void {
    this.router.navigate(['/account/settings']);
  }

  openMesRequetes(): void {
    this.router.navigate(['/etudiant/mes-requetes']);
  }

  openMesCours(): void {
    this.router.navigate(['/etudiant/mes-cours']);
  }

  openMesNotes(): void {
    this.router.navigate(['/etudiant/mes-notes']);
  }
}
