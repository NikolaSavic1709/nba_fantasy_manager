import { Component, ViewChild } from '@angular/core';
import {MatAccordion, MatExpansionModule} from '@angular/material/expansion';
import {MatDatepickerModule} from '@angular/material/datepicker';
import {MatInputModule} from '@angular/material/input';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {NativeDateAdapter} from '@angular/material/core';
import { CategoryService } from '../../services/category.service';
import { CategoryScores } from '../../model/categoryScore';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AddCategoryScoresDialogComponent } from '../add-category-scores-dialog/add-category-scores-dialog.component';

@Component({
  selector: 'app-category-scores',
  templateUrl: './category-scores.component.html',
  styleUrls: ['./category-scores.component.scss'],
})
export class CategoryScoresComponent {
  @ViewChild(MatAccordion)
  accordion: MatAccordion = new MatAccordion;
  categoryScores: CategoryScores[];

  constructor(private categoryService:CategoryService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar
  ) {
    this.categoryScores = [];
  }

  ngAfterViewInit() {
    this.refresh();
  }

  activateScore(id: number) {
    this.categoryService.activateCategoryScores(id).subscribe({
      next: (response) => {
        this.refresh();
      },
      error: (err) => {
        console.log(err);
      },
    });
  }


  addCategoryScores(){
    const dialogRef = this.dialog.open(AddCategoryScoresDialogComponent);

    dialogRef.afterClosed().subscribe(result => {
      if (result == "success") {
        this.openSnackBar("Category scores added successfully");
        this.refresh();
      }
    });
  }

  refresh() {
    this.categoryService.getCategoryScores().subscribe({
      next: (response) => {
        this.categoryScores = response.sort((a, b) => a.id - b.id);
        console.log(response);
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  openSnackBar = (message: string) => {
    this.snackBar.open(message, 'Close', {
      duration: 3000,
      verticalPosition: 'bottom',
      horizontalPosition: 'center',
    });
  }
}
