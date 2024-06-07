import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators, ValidationErrors, ValidatorFn, AbstractControl  } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { AddFilter } from '../../model/filter';
import { FilterService } from '../../services/filter.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-add-filter-dialog',
  templateUrl: './add-filter-dialog.component.html',
  styleUrls: ['./add-filter-dialog.component.scss']
})
export class AddFilterDialogComponent {
  hasError = false;

  nbaTeams: string[] = [
    'Atlanta Hawks',
    'Boston Celtics',
    'Brooklyn Nets',
    'Charlotte Hornets',
    'Chicago Bulls',
    'Cleveland Cavaliers',
    'Dallas Mavericks',
    'Denver Nuggets',
    'Detroit Pistons',
    'Golden State Warriors',
    'Houston Rockets',
    'Indiana Pacers',
    'Los Angeles Clippers',
    'Los Angeles Lakers',
    'Memphis Grizzlies',
    'Miami Heat',
    'Milwaukee Bucks',
    'Minnesota Timberwolves',
    'New Orleans Pelicans',
    'New York Knicks',
    'Oklahoma City Thunder',
    'Orlando Magic',
    'Philadelphia 76ers',
    'Phoenix Suns',
    'Portland Trail Blazers',
    'Sacramento Kings',
    'San Antonio Spurs',
    'Toronto Raptors',
    'Utah Jazz',
    'Washington Wizards'
  ];

  addFilterForm = new FormGroup({
    minPrice: new FormControl('', [Validators.min(0), Validators.max(200), Validators.required]),
    maxPrice: new FormControl('', [Validators.min(0), Validators.max(200), Validators.required]),
    team: new FormControl('', [Validators.required]),
    position: new FormControl('', [Validators.required]),
  }, {validators: [minMaxPriceValidator('minPrice', 'maxPrice')]});


  filterError = false;

  constructor(
    private dialogRef: MatDialogRef<AddFilterDialogComponent>,
    private filterService: FilterService
  ) {
  }


  addFilter() {
    const filter: AddFilter = {
      minPrice: Number(this.addFilterForm.value.minPrice),
      maxPrice: Number(this.addFilterForm.value.maxPrice),
      team: this.addFilterForm.value.team,
      position: Number(this.addFilterForm.value.position)
    }

    if (this.addFilterForm.valid) {
      this.filterService.addFilter(filter).subscribe({
        next: () => {
          this.dialogRef.close("success");
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.filterError = true;
          }
        },
      });
    }
  }
}



export function minMaxPriceValidator(controlName: string, checkControlName: string): ValidatorFn {
  return (controls: AbstractControl) => {
    const control = controls.get(controlName);
    const checkControl = controls.get(checkControlName);

    if (checkControl?.errors && !checkControl.errors['minMaxPrice']) {
      return null;
    }

    if (control?.value > checkControl?.value) {
      controls.get(checkControlName)?.setErrors({minMaxPrice: true});
      return {minMaxPrice: true};
    } else {
      return null;
    }
  };
}