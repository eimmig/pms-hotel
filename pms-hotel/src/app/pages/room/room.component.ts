import { CommonModule } from '@angular/common';
import { AfterViewInit, Component, OnInit, ViewChild } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatTableModule, MatTableDataSource } from '@angular/material/table';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatSortModule } from '@angular/material/sort';
import { MatPaginator, MatPaginatorModule } from '@angular/material/paginator'; 
import { MatSort } from '@angular/material/sort';
import { FormsModule } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';
import { AddRoomDialogComponent } from './add-room-dialog/add-room-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { RoomService } from '../../services/room.service';

interface Room {
  id?: string;
  number: string;
  roomTypeId: string;
  roomTypeName: number;
  status: string;
  statusDisplay: string;
}

@Component({
  selector: 'app-room',
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
    <h1>Quartos</h1>
    <div class="container mat-elevation-z3">
      <div class="filter-button-container">
        <mat-form-field>
          <mat-label>Filtrar</mat-label>
          <input matInput (keyup)="applyFilter($event)" placeholder="Filtrar Quarto" />
        </mat-form-field>
        <button mat-raised-button color="primary" (click)="openAddRoomDialog()">
          Adicionar Quarto
        </button>
      </div>
      
      <table mat-table [dataSource]="dataSource" matSort>
        <ng-container matColumnDef="number">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Número</th>
          <td mat-cell *matCellDef="let room">{{ room.number }}</td>
        </ng-container>

        <ng-container matColumnDef="roomTypeName">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Categoria</th>
          <td mat-cell *matCellDef="let room">{{ room.roomTypeName }}</td>
        </ng-container>

        <ng-container matColumnDef="statusDisplay">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Status</th>
          <td mat-cell *matCellDef="let room">{{ room.statusDisplay }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef class="text-center" style="width: 180px;">Ações</th>
          <td mat-cell *matCellDef="let room" class="text-center" style="width: 180px;">
            <button mat-button (click)="editRoom(room)">Editar</button>
            <button mat-button color="warn" (click)="deleteRoom(room.id)">Excluir</button>
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
export class RoomComponent implements OnInit, AfterViewInit {
  displayedColumns: string[] = ['number', 'roomTypeName', 'statusDisplay', 'actions'];
  originalData: Room[] = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<Room>(this.originalData);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public dialog: MatDialog, private roomService: RoomService) {}

  ngOnInit(): void {
    this.fetchRooms();
  }

  parseRooms(jsonData: string): Room[] {
    const parsedData = JSON.parse(jsonData);
    return parsedData.map((item: any) => ({
      id: item.id, 
      number: item.number,
      roomTypeId: item.roomType.id,
      roomTypeName: item.roomType.name,
      status: item.status,
      statusDisplay: this.getStatusDisplay(item.status) 
    }));
  }

  private getStatusDisplay(status: string): string {
    switch (status) {
      case 'A':
        return 'Disponível';
      case 'O':
        return 'Ocupado';
      case 'M':
        return 'Manutenção';
      case 'I':
        return 'Inativo';
      default:
        return 'Desconhecido';
    }
  }

  fetchRooms() {
    this.roomService.getAllRooms().subscribe(
      (response) => {
        this.originalData = this.parseRooms(JSON.stringify(response)); 
        this.dataSource.data = this.originalData;
      },
      (error) => {
        console.error('Erro ao buscar quartos:', error);
        alert('Não foi possível carregar a lista de quartos.');
      },
      () => {
      }
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  openAddRoomDialog() {
    const dialogRef = this.dialog.open(AddRoomDialogComponent, {
      width: '400px',
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.originalData.push(result);
        this.dataSource.data = this.originalData;
      }
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  editRoom(room: Room) {
    const dialogRef = this.dialog.open(AddRoomDialogComponent, {
      width: '400px',
      data: room
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

  deleteRoom(id: string) {
    if (confirm('Você tem certeza que deseja excluir este quarto?')) {
      this.roomService.deleteRoom(id).subscribe(
        () => {
          this.originalData = this.originalData.filter(room => room.id !== id);
          this.dataSource.data = this.originalData;
          alert('Quarto excluído com sucesso!'); 
        },
        (error) => {
          console.error('Erro ao excluir quarto:', error);
          window.location.reload();
        },
        () => {
        }
      );
    }
  }
}