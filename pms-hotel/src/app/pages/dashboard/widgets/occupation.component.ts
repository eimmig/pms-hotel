import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import Chart from 'chart.js/auto';
import { InterativeChart } from '../../../models/interativeChart';
import { DashboardService } from '../../../services/dashboard.service';

@Component({
  selector: 'app-occupation',
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
export class OccupationComponent implements OnInit {
  @ViewChild('chart', { static: true }) chart!: ElementRef;
  chartData: InterativeChart | null = null; // Armazenando dados do gráfico
  private chartInstance!: Chart; // Instância do gráfico

  constructor(private dashboardService: DashboardService) { }

  ngOnInit() {
    this.dashboardService.getOccupancyData().subscribe({
      next: (data) => {
        this.chartData = data; // Armazenando dados recebidos
        this.createChart(); // Criando o gráfico com os dados recebidos
      },
      error: (err) => {
        console.error('Erro ao buscar dados de ocupação:', err);
      }
    });
  }

  createChart() {
    // Se um gráfico já existir, destruímos a instância anterior
    if (this.chartInstance) {
      this.chartInstance.destroy();
    }

    // Criando uma nova instância do gráfico
    this.chartInstance = new Chart(this.chart.nativeElement, {
      type: 'line',
      data: {
        labels: this.chartData?.labels || [],
        datasets: [{
          label: this.chartData?.label || 'Ocupação',
          data: this.chartData?.data || [], 
          fill: 'start',
          borderColor: 'rgb(255, 99, 132)',
          backgroundColor: 'rgba(255, 99, 132, 0.5)',
        }]
      },
      options: {
        maintainAspectRatio: false,
        elements: {
          line: {
            tension: 0.4 
          }
        }
      }
    });
  }
}
