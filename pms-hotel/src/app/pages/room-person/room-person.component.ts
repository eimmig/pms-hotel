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
import { AddPersonDialogComponent } from './add-person-dialog/add-person-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { PersonService } from '../../services/person.service';

interface Person {
  id?: string;
  name: string;
  document: string;
  phoneNumber: string;
  email: string;
  birthDate: string;
  documentTypeId: string;
}

@Component({
  selector: 'app-room-person',
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
    <h1>Hóspedes</h1>
    <div class="container mat-elevation-z3">
      <div class="filter-button-container">
        <mat-form-field>
          <mat-label>Filtrar</mat-label>
          <input matInput (keyup)="applyFilter($event)" placeholder="Filtrar Hóspedes" />
        </mat-form-field>
        <button mat-raised-button color="primary" (click)="openAddPersonDialog()">
          Adicionar Hóspede
        </button>
      </div>
      
      <table mat-table [dataSource]="dataSource" matSort>
        <ng-container matColumnDef="name">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Nome</th>
          <td mat-cell *matCellDef="let person">{{ person.name }}</td>
        </ng-container>

        <ng-container matColumnDef="document">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Documento</th>
          <td mat-cell *matCellDef="let person">{{ person.document }}</td>
        </ng-container>

        <ng-container matColumnDef="phoneNumber">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Telefone</th>
          <td mat-cell *matCellDef="let person">{{ person.phoneNumber }}</td>
        </ng-container>

        <ng-container matColumnDef="email">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Email</th>
          <td mat-cell *matCellDef="let person">{{ person.email }}</td>
        </ng-container>

        <ng-container matColumnDef="birthDate">
          <th mat-header-cell *matHeaderCellDef mat-sort-header class="text-center">Data de Nascimento</th>
          <td mat-cell *matCellDef="let person">{{ person.birthDate }}</td>
        </ng-container>

        <ng-container matColumnDef="actions">
          <th mat-header-cell *matHeaderCellDef class="text-center" style="width: 180px;">Ações</th>
          <td mat-cell *matCellDef="let person" class="text-center" style="width: 180px;">
            <button mat-button (click)="editPerson(person)">Editar</button>
            <button mat-button color="warn" (click)="deletePerson(person.id)">Excluir</button>
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
export class RoomPersonComponent {
  displayedColumns: string[] = ['name', 'document', 'phoneNumber', 'email', 'birthDate', 'actions'];

  originalData: Person[] = [];
  loading: boolean = false;

  dataSource = new MatTableDataSource<Person>(this.originalData);

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(public dialog: MatDialog, private personService: PersonService) {}

  ngOnInit(): void {
    this.fetchPersons();
  }

  fetchPersons() {
    this.personService.getAllPersons().subscribe(
      (response) => {
        this.originalData = response;
        this.dataSource.data = this.originalData;
      },
      (error) => {
        console.error('Erro ao buscar hóspedes:', error);
        alert('Não foi possível carregar a lista de hóspedes.');
      },
      () => {
      }
    );
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  openAddPersonDialog() {
    const dialogRef = this.dialog.open(AddPersonDialogComponent, {
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

  editPerson(person: Person) {
    const dialogRef = this.dialog.open(AddPersonDialogComponent, {
      width: '400px',
      data: person
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

  deletePerson(id: string) {
    if (confirm('Você tem certeza que deseja excluir esta pessoa?')) {
      this.personService.deletePerson(id).subscribe(
        () => {
          this.originalData = this.originalData.filter(person => person.id !== id);
          this.dataSource.data = this.originalData; 
          alert('Pessoa excluída com sucesso!'); 
        },
        (error) => {
          console.error('Erro ao excluir pessoa:', error);
          window.location.reload();
        },
        () => {
        }
      );
    }
  }
  
}