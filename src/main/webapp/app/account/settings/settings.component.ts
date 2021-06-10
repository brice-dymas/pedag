import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { ROLE_PROF, ROLE_STUDENT } from 'app/app.constants';

import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/auth/account.model';
import { LANGUAGES } from 'app/config/language.constants';
import { TokenService } from 'app/core/auth/token.service';
import { IEnseignant } from 'app/entities/enseignant/enseignant.model';
import { IInscription } from 'app/entities/inscription/inscription.model';

@Component({
  selector: 'jhi-settings',
  templateUrl: './settings.component.html',
})
export class SettingsComponent implements OnInit {
  account!: Account;
  success = false;
  ins!: any;
  ens!: any;

  pls: any;
  languages = LANGUAGES;
  settingsForm = this.fb.group({
    firstName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    lastName: [undefined, [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: [undefined, [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    langKey: [undefined],
  });

  constructor(
    private accountService: AccountService,
    private fb: FormBuilder,
    private translateService: TranslateService,
    private tokenService: TokenService
  ) {}

  ngOnInit(): void {
    this.accountService.identity().subscribe(account => {
      this.pls = account;
      if (account) {
        this.settingsForm.patchValue({
          firstName: account.firstName,
          lastName: account.lastName,
          email: account.email,
          langKey: account.langKey,
        });

        this.account = account;
        this.setExtraInfos(this.account.authorities);
      }
    });
  }

  setExtraInfos(roles: string[]): void {
    // if (roles.includes(ROLE_PROF)) {
    this.ens = this.tokenService.getEnseignant();
    // }
    // if (roles.includes(ROLE_STUDENT)) {
    this.ins = this.tokenService.getInscription();
    // }
  }

  save(): void {
    this.success = false;

    this.account.firstName = this.settingsForm.get('firstName')!.value;
    this.account.lastName = this.settingsForm.get('lastName')!.value;
    this.account.email = this.settingsForm.get('email')!.value;
    this.account.langKey = this.settingsForm.get('langKey')!.value;

    this.accountService.save(this.account).subscribe(() => {
      this.success = true;

      this.accountService.authenticate(this.account);

      if (this.account.langKey !== this.translateService.currentLang) {
        this.translateService.use(this.account.langKey);
      }
    });
  }
}
