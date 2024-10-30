import { Component, ElementRef, ViewChild, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { DashboardService } from '../../../services/dashboard.service';
import { InterativeChart } from '../../../models/interativeChart';

@Component({
  selector: 'app-roomtype',
  standalone: true,
  imports: [],
  template: `
    <div class="chart-container">
      <canvas #chart></canvas>
    </div>
  `,
  styles: [`
    .chart-container {
      width: 100%;
      height: calc(100% - 100px);
    }
  `]
})
export class RoomTypeChartComponent implements OnInit {
  @ViewChild('chart', { static: true }) chart!: ElementRef;
  chartData: InterativeChart | null = null;

  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    this.dashboardService.getRoomTypeData().subscribe({
      next: (data) => {
        this.chartData = data;
        this.createChart();
      },
      error: (err) => {
        console.error('Erro ao buscar dados de check-in:', err);
      }
    });
  }

  createChart() {
    function generateDistinctColors(numColors: number) {
      const colors = [];
      const step = 360 / numColors;
      for (let i = 0; i < numColors; i++) {
        const color = `hsl(${i * step}, 70%, 60%)`;
        colors.push(color);
      }
      return colors;
    }

    new Chart(this.chart.nativeElement, { 
      type: 'doughnut',
      data: {
        labels: this.chartData?.labels || [],
        datasets: [{
          data: this.chartData?.data || [],
          borderColor: 'white',
          backgroundColor: generateDistinctColors(2),
          hoverOffset: 4
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        layout: {
          padding: 10
        },
        plugins: {
          legend: {
            position: 'right',
            labels: {
              boxWidth: 30,
              padding: 20,
              font: {
                size: 16,
                weight: 'bold'
              },
              color: 'white'
            }
          }
        },
        elements: {
          line: {
            tension: 0.4
          }
        }
      }
    });
  }
}
