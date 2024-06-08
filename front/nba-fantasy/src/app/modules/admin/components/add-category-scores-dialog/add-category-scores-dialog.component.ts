import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../../services/category.service';
import { AddCategoryScores } from '../../model/categoryScore';

@Component({
  selector: 'app-add-category-scores-dialog',
  templateUrl: './add-category-scores-dialog.component.html',
  styleUrls: ['./add-category-scores-dialog.component.scss']
})
export class AddCategoryScoresDialogComponent {

  hasError = false;

  addCategoryScoresForm = new FormGroup({
    pointScore: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required]),
    reboundScore: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required]),
    assistScore: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required]),
    stealScore: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required]),
    turnoverScore: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required]),
    blockScore: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required]),
    bonusMargin: new FormControl('', [Validators.min(-100), Validators.max(100), Validators.required])
  });


  categoryError = false;

  constructor(
    private dialogRef: MatDialogRef<AddCategoryScoresDialogComponent>,
    private categoryService: CategoryService
  ) {
  }


  addCategory() {
    const categoryScore: AddCategoryScores = {
      pointScore: Number(this.addCategoryScoresForm.value.pointScore),
      reboundScore: Number(this.addCategoryScoresForm.value.reboundScore),
      assistScore: Number(this.addCategoryScoresForm.value.assistScore),
      stealScore: Number(this.addCategoryScoresForm.value.stealScore),
      turnoverScore: Number(this.addCategoryScoresForm.value.turnoverScore),
      blockScore: Number(this.addCategoryScoresForm.value.blockScore),
      bonusMargin: Number(this.addCategoryScoresForm.value.bonusMargin),
    }

    if (this.addCategoryScoresForm.valid) {
      this.categoryService.addCategoryScores(categoryScore).subscribe({
        next: () => {
          this.dialogRef.close("success");
        },
        error: (error) => {
          if (error instanceof HttpErrorResponse) {
            this.categoryError = true;
          }
        },
      });
    }
  }
}
