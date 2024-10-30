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
import { AddAmenitiesDialogComponent } from './add-amenities-dialog/add-amenities-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { AmenitiesService } from '../../services/amenities.service';

interface amenities {
  id?: string;
  name: string;
  mondayRate: number;
  tuesdayRate: number;
  wednesdayRate: number;
  thursdayRate: number;
  fridayRate: number;
  saturdayRate: number;
  sundayRate: number;
  amenitiesId: string;
}

@Component({
  selector: 'app-amenities',
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
    <h1>Extras</h1>
    <div class="container mat-elevation-z3">
      <div class="filter-button-container">
        <mat-form-field>
          <mat-label>Filtrar</mat-label>
          <input matInput (keyup)="applyFilter($event)" placeholder="Filtrar Extra" />
        </mat-form-field>
        <button mat-raised-button color="primary" (click)="openAddAmenitiesDialog()">
          Adicionar Extra
        </button>
      </div>
      
      <table mat-table [dataSource]="dataSource" matSort>
        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Nome</th>
          <td mat-cell *matCellDef="let amenities">{{ amenities.name }}</td>
        </ng-container>

        <ng-container matColumnDef="mondayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Segunda-Feira</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.mondayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="tuesdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Terça-Feira</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.tuesdayRate }}</td> 
        </ng-container>

        <ng-container matColumnDef="wednesdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Quarta-Feia</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.wednesdayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="thursdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Quinta-Feira</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.thursdayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="fridayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Sexta-Feira</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.fridayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="saturdayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Sabado</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.saturdayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="sundayRate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Domingo</th>
          <td mat-cell *matCellDef="let amenities">{{ 'R$' + amenities.sundayRate }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef class="text-center" style="width: 180px;">Ações</th>
          <td mat-cell *matCellDef="let amenities" class="text-center" style="width: 180px;">
            <button mat-button (click)="editAmenities(amenities)">Editar</button>
            <button mat-button color="warn" (click)="deleteAmenities(amenities.id)">Excluir</button>
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
export class AmenitiesComponent {
  displayedColumns: string[] = ['name',  'mondayRate', 'tuesdayRate', 'wednesdayRate', 'thursdayRate', 'fridayRate', 'saturdayRate', 'sundayRate', 'actions'];

  originalData: amenities[] = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<amenities>(this.originalData);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public dialog: MatDialog, private amenitiesService: AmenitiesService) {}

  ngOnInit(): void {
    this.fetchAmenities();
  }

  fetchAmenities() {
    this.amenitiesService.getAllAmenities().subscribe(
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
            sundayRate: item.sundayRate
          } as amenities;
        });
    
        this.dataSource.data = this.originalData;
      },
      (error) => {
        console.error('Erro ao buscar extras:', error);
        alert('Não foi possível carregar a lista de extras.');
      },
      () => {
      }
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  openAddAmenitiesDialog() {
    const dialogRef = this.dialog.open(AddAmenitiesDialogComponent, {
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

  editAmenities(amenities: amenities) {
    const dialogRef = this.dialog.open(AddAmenitiesDialogComponent, {
      width: '400px',
      data: amenities
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

  deleteAmenities(id: string) {
    if (confirm('Você tem certeza que deseja excluir este extra?')) {
      this.amenitiesService.deleteAmenities(id).subscribe(
        () => {
          this.originalData = this.originalData.filter(amenities => amenities.id !== id);
          this.dataSource.data = this.originalData; 
          alert('Extra excluído com sucesso!'); 
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