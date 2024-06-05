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

@Component({
  selector: 'app-category-scores',
  templateUrl: './category-scores.component.html',
  styleUrls: ['./category-scores.component.scss'],
})
export class CategoryScoresComponent {
  @ViewChild(MatAccordion)
  accordion: MatAccordion = new MatAccordion;
  categoryScores: CategoryScores[];

  constructor(private categoryService:CategoryService) {
    this.categoryScores = [];
  }

  ngAfterViewInit() {
    this.categoryService.getCategoryScores().subscribe({
      next: (response) => {
        this.categoryScores = response.sort((a, b) => a.id - b.id);
        console.log(response);
      },
      error: (err) => {
        console.log(err);
      }
    });

  }

  activateScore(id: number) {
    this.categoryService.activateCategoryScores(id).subscribe({
      next: (response) => {

        this.categoryService.getCategoryScores().subscribe({
          next: (response) => {
            this.categoryScores = response.sort((a, b) => a.id - b.id);
            console.log(response);
          },
          error: (err) => {
            console.log(err);
          },
        });
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
