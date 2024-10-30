import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { PersonService } from '../../../services/person.service';

interface Person {
  id?: string;
  name: string;
  document: string;
  phoneNumber: string;
  email: string;
  birthDate: string;
  documentTypeId: string;
}

interface DocumentType {
  value: number;
  viewValue: string;
}

@Component({
  selector: 'app-add-person-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatDialogContent
  ],
  template: `
    <mat-dialog-content>
      <form [formGroup]="personForm" (ngSubmit)="onSubmit()">
        <mat-form-field appearance="fill">
          <mat-label>Nome</mat-label>
          <input matInput formControlName="name" required />
          <mat-error *ngIf="personForm.get('name')?.hasError('required')">O nome não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Documento</mat-label>
          <input matInput formControlName="document" required />
          <mat-error *ngIf="personForm.get('document')?.hasError('required')">O documento não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Tipo de Documento</mat-label>
          <mat-select formControlName="documentTypeId" required>
            <mat-option *ngFor="let type of documentTypes" [value]="type.value">
              {{ type.viewValue }}
            </mat-option>
          </mat-select>
          <mat-error *ngIf="personForm.get('documentTypeId')?.hasError('required')">O tipo de documento não pode ser nulo.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Telefone</mat-label>
          <input matInput formControlName="phoneNumber" required />
          <mat-error *ngIf="personForm.get('phoneNumber')?.hasError('required')">O número de telefone não pode estar em branco.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Email</mat-label>
          <input matInput formControlName="email" required type="email" />
          <mat-error *ngIf="personForm.get('email')?.hasError('required')">O email não pode estar em branco.</mat-error>
          <mat-error *ngIf="personForm.get('email')?.hasError('email')">O email deve ser válido.</mat-error>
        </mat-form-field>

        <mat-form-field appearance="fill">
          <mat-label>Data de Nascimento</mat-label>
          <input matInput formControlName="birthDate" required type="date" />
          <mat-error *ngIf="personForm.get('birthDate')?.hasError('required')">A data de nascimento não pode ser nula.</mat-error>
        </mat-form-field>
        
        <div mat-dialog-actions class="div-btns">
          <button mat-button color="warn" (click)="onCancel()" class="btn-cancelar">Cancelar</button>
          <button mat-button class="btn-adicionar" type="submit" [disabled]="loading || personForm.invalid">Salvar</button>
        </div>
      </form>
    </mat-dialog-content>
  `,
  styles: [`
    mat-form-field {
      width: 100%; 
      margin-bottom: 16px;
    }

    button {
      min-width: 100px; 
    }

    .btn-cancelar {
      background-color: red;
      padding: 5px;
      color: white;
      border-radius: 5px;
      font-size: 15px;
      border: none;
      transition: background-color 0.3s, color 0.3s;
    }
    .btn-cancelar:hover {
      background-color: #ff3333;
    }

    .btn-adicionar {
      background-color: green;
      padding: 5px;
      color: white;
      border-radius: 5px;
      font-size: 15px;
      border: none;
      transition: background-color 0.3s, color 0.3s;
    }
    .btn-adicionar:hover {
      background-color: #00cc00;
    }

    .div-btns {
      display: flex;
      justify-content: space-between;
    }
  `]
})
export class AddPersonDialogComponent implements OnInit {
  personForm!: FormGroup;
  
  loading: boolean = false;

  documentTypes: DocumentType[] = [
    { value: 1, viewValue: 'CPF' },
    { value: 2, viewValue: 'CNPJ' },
    { value: 3, viewValue: 'Passaporte' }
  ];

  constructor(
    public dialogRef: MatDialogRef<AddPersonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: Person,
    private fb: FormBuilder,
    private personService: PersonService
  ) {}

  ngOnInit(): void {
    // Inicializa o FormGroup
    this.personForm = this.fb.group({
      name: ['', Validators.required],
      document: ['', Validators.required],
      documentTypeId: [null, Validators.required],
      phoneNumber: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      birthDate: ['', Validators.required]
    });

    // Preenche o formulário se houver dados da pessoa
    if (this.data) {
      this.personForm.patchValue({
        name: this.data.name,
        document: this.data.document,
        documentTypeId: this.data.documentTypeId,
        phoneNumber: this.data.phoneNumber,
        email: this.data.email,
        birthDate: this.data.birthDate
      });
    }
  }

  onSubmit() {
    if (this.personForm.invalid) return;
    const personData = { ...this.personForm.value, id: this.data?.id }; // Adiciona o ID se existir

    const personObservable: Observable<any> = personData.id
      ? this.personService.editPerson(personData) // Se existir ID, edita
      : this.personService.addPerson(personData); // Caso contrário, adiciona

    personObservable.subscribe(
      (response) => {
        alert('Pessoa salva com sucesso!'); 
        window.location.reload();
        this.dialogRef.close(response); 
      },
      (error) => {
        alert('Erro ao salvar pessoa. Tente novamente.'); 
      },
      () => {
      }
    );
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
