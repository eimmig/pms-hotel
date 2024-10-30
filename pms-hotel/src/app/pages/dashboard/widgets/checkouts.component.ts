import { Component } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { DashboardService } from '../../../services/dashboard.service';

@Component({
  selector: 'app-checkouts',
  standalone: true,
  imports: [MatIcon],
  template: `
    <div class="row mb-8">
      <mat-icon class="text-green">logout</mat-icon>
      <p class="stat">{{ chartData }}</p>
    </div>
  `,
  styles: [`
    .row {
      display: flex;
      align-items: center; 
      gap: 8px; 
      margin-top: 2rem;
      margin-bottom: 2rem;
      text-align:center;
      justify-content:center;
    }

    .stat {
      font-size: 4rem; 
      font-weight: bold; 
      margin: 0; 
    }

    .text-green {
      color: green;
    }
  `]
})
export class CheckoutsComponent {
  constructor(private dashboardService: DashboardService) { }
  
  chartData: number = 0;

  ngOnInit(): void {
    this.dashboardService.getCheckoutData().subscribe(data => this.chartData = data.numberValue);
  }
}
