import { CommonModule } from '@angular/common';
import { Component, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator'; 
import { MatSort } from '@angular/material/sort';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AddRoomRateDialogComponent } from './add-room-rate-dialog/add-room-rate-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { RoomRateService } from '../../services/room-rate.service';

interface RoomRate {
  id?: string;
  name: string;
  mondayRate: number;
  tuesdayRate: number;
  wednesdayRate: number;
  thursdayRate: number;
  fridayRate: number;
  saturdayRate: number;
  sundayRate: number;
  garageIncluded: boolean;
  roomRateId: string;
}

@Component({
  selector: 'app-room-rate',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    MatTableModule,
    MatInputModule,
    MatButtonModule,
    MatSortModule,
    FormsModule,
    MatIconModule,
    MatPaginatorModule,
  ],
  template: `
    <h1>Tarifas</h1>
    <div class="container mat-elevation-z3">
      <div class="filter-button-container">
        <mat-form-field>
          <mat-label>Filtrar</mat-label>
          <input matInput (keyup)="applyFilter($event)" placeholder="Filtrar Tarifa" />
        </mat-form-field>
        <button mat-raised-button color="primary" (click)="openAddRoomRateDialog()">
          Adicionar Tarifa
        </button>
      </div>
      
      <table mat-table [dataSource]="dataSource" matSort>
        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Nome</th>
          <td mat-cell *matCellDef="let roomRate">{{ roomRate.name }}</td>
        </ng-container>

        <ng-container matColumnDef="garageIncluded">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Garagem</th>
          <td mat-cell *matCellDef="let roomRate">{{ roomRate.garageIncluded ? 'Sim' : 'Não' }}</td>
        </ng-container>

        <ng-container matColumnDef="mondayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Segunda-Feira</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.mondayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="tuesdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Terça-Feira</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.tuesdayRate }}</td> 
        </ng-container>

        <ng-container matColumnDef="wednesdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Quarta-Feia</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.wednesdayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="thursdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Quinta-Feira</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.thursdayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="fridayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Sexta-Feira</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.fridayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="saturdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Sabado</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.saturdayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="sundayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Domingo</th>
          <td mat-cell *matCellDef="let roomRate">{{ 'R$' + roomRate.sundayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef class="text-center" style="width: 180px;">Ações</th>
          <td mat-cell *matCellDef="let roomRate" class="text-center" style="width: 180px;">
            <button mat-button (click)="editRoomRate(roomRate)">Editar</button>
            <button mat-button color="warn" (click)="deleteRoomRate(roomRate.id)">Excluir</button>
          </td>
        </ng-container>

        <tr mat-header-row *matHeaderRowDef="displayedColumns"></tr>
        <tr mat-row *matRowDef="let row; columns: displayedColumns;"></tr>
      </table>

      <mat-paginator [pageSizeOptions]="[10, 15, 20]" showFirstLastButtons></mat-paginator>
    </div>
  `,
  styles: [`
    .container {
      margin: 20px;
      border: 2px solid #ccc;
    }
    table {
      width: 100%;
    }
    th.mat-header-cell, td.mat-cell {
      padding: 8px; 
      margin: 0; 
    }
    .text-center {
      text-align: center; 
    }
    .filter-button-container {
      display: flex;              
      justify-content: space-between; 
      align-items: center; 
      margin-bottom: 16px; 
      > button { 
        margin-right: 16px; 
        padding: 15px;
      }
    }


  `]
})
export class RoomRateComponent {
  displayedColumns: string[] = ['name', 'garageIncluded', 'mondayRate', 'tuesdayRate', 'wednesdayRate', 'thursdayRate', 'fridayRate', 'saturdayRate', 'sundayRate', 'actions'];

  originalData: RoomRate[] = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<RoomRate>(this.originalData);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public dialog: MatDialog, private roomRateService: RoomRateService) {}

  ngOnInit(): void {
    this.fetchRoomRates();
  }

  fetchRoomRates() {
    this.roomRateService.getAllRoomRates().subscribe(
      (response) => {
        this.originalData = response.map((item: any) => {
          return {
            id: item.id,
            name: item.name,
            mondayRate: item.mondayRate,
            tuesdayRate: item.tuesdayRate,
            wednesdayRate: item.wednesdayRate,
            thursdayRate: item.thursdayRate,
            fridayRate: item.fridayRate,
            saturdayRate: item.saturdayRate,
            sundayRate: item.sundayRate,
            garageIncluded: item.garageIncluded
          } as RoomRate;
        });
    
        this.dataSource.data = this.originalData;
      },
      (error) => {
        console.error('Erro ao buscar tarifas:', error);
        alert('Não foi possível carregar a lista de tarifas.');
      },
      () => {
      }
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  openAddRoomRateDialog() {
    const dialogRef = this.dialog.open(AddRoomRateDialogComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.originalData.push(result);
        this.dataSource.data = this.originalData;  // Atualiza dataSource com o novo registro
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editRoomRate(roomRate: RoomRate) {
    const dialogRef = this.dialog.open(AddRoomRateDialogComponent, {
      width: '400px',
      data: roomRate
    });
  
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const index = this.originalData.findIndex(p => p.id === result.id);
        if (index !== -1) {
          this.originalData[index] = result; 
          this.dataSource.data = this.originalData; 
        }
      }
    });
  }

  deleteRoomRate(id: string) {
    if (confirm('Você tem certeza que deseja excluir esta tarifa?')) {
      this.roomRateService.deleteRoomRate(id).subscribe(
        () => {
          this.originalData = this.originalData.filter(roomRate => roomRate.id !== id);
          this.dataSource.data = this.originalData; 
          alert('Tarifa excluída com sucesso!'); 
        },
        (error) => {
          window.location.reload();
        },
        () => {
        }
      );
    }
  }
}