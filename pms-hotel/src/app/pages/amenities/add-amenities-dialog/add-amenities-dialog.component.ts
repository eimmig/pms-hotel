import { CommonModule } from '@angular/common';
import { Component, Inject, OnInit } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA, MatDialogContent } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTabsModule } from '@angular/material/tabs';
import { FormGroup, FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSelectModule } from '@angular/material/select';
import { Observable } from 'rxjs';
import { AmenitiesService } from '../../../services/amenities.service';

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
  selector: 'app-add-amenities-dialog',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSelectModule,
    MatCheckboxModule,
    MatTabsModule,
    MatDialogContent
  ],
  template: `
    <mat-dialog-content class="dialog-content">
      <form [formGroup]="amenitiesForm" (ngSubmit)="onSubmit()" class="form-full">
        <mat-tab-group class="full-width-tab-group">
          <!-- Aba para Nome -->
          <mat-tab label="Informações Básicas">
            <div class="tab-content">
              <mat-form-field appearance="fill">
                <mat-label>Nome</mat-label>
                <input matInput formControlName="name" required placeholder="Digite o nome da categoria"/>
                <mat-error *ngIf="amenitiesForm.get('name')?.hasError('required')">O nome não pode estar em branco.</mat-error>
              </mat-form-field>
            </div>
          </mat-tab>

          <!-- Aba para Tarifas Diárias -->
          <mat-tab label="Tarifas Diárias">
            <div class="tab-content">
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Segunda</mat-label>
                <input type="number" matInput formControlName="mondayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Terça</mat-label>
                <input type="number" matInput formControlName="tuesdayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Quarta</mat-label>
                <input type="number" matInput formControlName="wednesdayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Quinta</mat-label>
                <input type="number" matInput formControlName="thursdayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Sexta</mat-label>
                <input type="number" matInput formControlName="fridayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Sábado</mat-label>
                <input type="number" matInput formControlName="saturdayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
              <mat-form-field appearance="fill">
                <mat-label>Tarifa de Domingo</mat-label>
                <input type="number" matInput formControlName="sundayRate" required placeholder="Digite a tarifa"/>
              </mat-form-field>
            </div>
          </mat-tab>
        </mat-tab-group>

        <div mat-dialog-actions class="div-btns">
          <button mat-button color="warn" (click)="onCancel()" class="btn-cancelar">Cancelar</button>
          <button mat-button class="btn-adicionar" type="submit" [disabled]="loading || amenitiesForm.invalid">
            <ng-container *ngIf="loading; else saveText">Salvando...</ng-container>
            <ng-template #saveText>Salvar</ng-template>
          </button>
        </div>
      </form>
    </mat-dialog-content>
  `,
  styles: [`
    .dialog-content {
      min-height: 400px; 
      max-height: 90vh; 
      overflow: hidden;  
      display: flex;
      flex-direction: column;
      align-items: stretch;
      padding: 20px;
    }

    .form-full {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      justify-content: space-between; 
    }

    .full-width-tab-group {
      flex-grow: 1;
    }

    .tab-content {
      display: flex;
      flex-direction: column;
      gap: 16px; 
      flex-grow: 1; 
      overflow: hidden;
    }

    mat-tab-body {
      display: flex;
      flex-direction: column;
      flex-grow: 1; 
      overflow: hidden;
    }

    mat-form-field {
      width: 100%;
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
      margin-top: 16px;
    }
  `]
})
export class AddAmenitiesDialogComponent implements OnInit {
  amenitiesForm!: FormGroup;
  loading: boolean = false;
  rates: amenities[] = []; 

  constructor(
    public dialogRef: MatDialogRef<AddAmenitiesDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: amenities,
    private fb: FormBuilder,
    private amenitiesService: AmenitiesService
  ) {}

  ngOnInit(): void {
    this.amenitiesForm = this.fb.group({
      name: ['', Validators.required],
      garageIncluded: [false],
      mondayRate: [0, Validators.required],
      tuesdayRate: [0, Validators.required],
      wednesdayRate: [0, Validators.required],
      thursdayRate: [0, Validators.required],
      fridayRate: [0, Validators.required],
      saturdayRate: [0, Validators.required],
      sundayRate: [0, Validators.required]
    });

    if (this.data) {
      this.amenitiesForm.patchValue(this.data);
    }
  }

  onSubmit() {
    if (this.amenitiesForm.invalid) return;
    const amenitiesData = { ...this.amenitiesForm.value, id: this.data?.id };

    this.loading = true; // Inicia o carregamento
    const amenitiesObservable: Observable<amenities> = amenitiesData.id
      ? this.amenitiesService.editAmenities(amenitiesData)
      : this.amenitiesService.addAmenities(amenitiesData);

    amenitiesObservable.subscribe(
      (response) => {
        alert('Extra salvo com sucesso!');
        window.location.reload();
        this.dialogRef.close(response);
      },
      (error) => {
        alert('Erro ao salvar extra. Tente novamente.');
      },
      () => {
      }
    );
  }

  onCancel(): void {
    this.dialogRef.close();
  }
}
