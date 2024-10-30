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
import { AddRoomTypeDialogComponent } from './add-room-type-dialog/add-room-type-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { RoomTypeService } from '../../services/room-type.service';

interface RoomType {
  id?: number;
  name: string;
  abbreviation: string;
  maxPersons: number;
  rateId: string;
  rateName: string;
}

@Component({
  selector: 'app-room-type',
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
    <h1>Categorias</h1>
    <div class="container mat-elevation-z3">
      <div class="filter-button-container">
        <mat-form-field>
          <mat-label>Filtrar</mat-label>
          <input matInput (keyup)="applyFilter($event)" placeholder="Filtrar Categoria" />
        </mat-form-field>
        <button mat-raised-button color="primary" (click)="openAddRoomTypeDialog()">
          Adicionar Categoria
        </button>
      </div>
      
      <table mat-table [dataSource]="dataSource" matSort>
        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Nome</th>
          <td mat-cell *matCellDef="let roomType">{{ roomType.name }}</td>
        </ng-container>

        <ng-container matColumnDef="abbreviation">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Abreviação</th>
          <td mat-cell *matCellDef="let roomType">{{ roomType.abbreviation }}</td>
        </ng-container>

        <ng-container matColumnDef="maxPersons">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Máximo de Ocupantes</th>
          <td mat-cell *matCellDef="let roomType">{{ roomType.maxPersons }}</td>
        </ng-container>

        <ng-container matColumnDef="rateName">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Tarifa</th>
          <td mat-cell *matCellDef="let roomType">{{ roomType.rateName }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef class="text-center" style="width: 180px;">Ações</th>
          <td mat-cell *matCellDef="let roomType" class="text-center" style="width: 180px;">
            <button mat-button (click)="editRoomType(roomType)">Editar</button>
            <button mat-button color="warn" (click)="deleteRoomType(roomType.id)">Excluir</button>
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
export class RoomTypeComponent {
  displayedColumns: string[] = ['name', 'abbreviation', 'maxPersons', 'rateName', 'actions'];

  originalData: RoomType[] = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<RoomType>(this.originalData);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public dialog: MatDialog, private roomTypeService: RoomTypeService) {}

  ngOnInit(): void {
    this.fetchRoomTypes();
  }

  fetchRoomTypes() {
    this.roomTypeService.getAllRoomTypes().subscribe(
      (response) => {
        this.originalData = response.map((item: any) => {
          return {
            id: item.id,  
            name: item.name,
            abbreviation: item.abbreviation,
            maxPersons: item.maxPersons,
            rateId: item.rate.id,
            rateName: item.rate.name
          } as RoomType;
        });
    
        // Define o dataSource com os dados mapeados
        this.dataSource.data = this.originalData;
      },
      (error) => {
        console.error('Erro ao buscar categorias:', error);
        alert('Não foi possível carregar a lista de categorias.');
      },
      () => {
      }
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  openAddRoomTypeDialog() {
    const dialogRef = this.dialog.open(AddRoomTypeDialogComponent, {
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

  editRoomType(roomType: RoomType) {
    const dialogRef = this.dialog.open(AddRoomTypeDialogComponent, {
      width: '400px',
      data: roomType
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

  deleteRoomType(id: number) {
    if (confirm('Você tem certeza que deseja excluir esta Categoria?')) {
      this.roomTypeService.deleteRoomType(id).subscribe(
        () => {
          this.originalData = this.originalData.filter(roomType => roomType.id !== id);
          this.dataSource.data = this.originalData; 
          alert('Categoria excluída com sucesso!'); 
        },
        (error) => {
          console.error('Erro ao excluir categoria:', error);
        },
        () => {
        }
      );
    }
  }
}