import { Component, input, signal } from '@angular/core';
import { Widget } from '../../models/dashboard';
import { CommonModule, NgComponentOutlet } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { WidgetOptionsComponent } from "./widget-options/widget-options.component";


@Component({
  selector: 'app-widget',
  standalone: true,
  imports: [CommonModule, NgComponentOutlet, MatButtonModule, MatIcon, WidgetOptionsComponent],
template: `
    <div class="container mat-elevation-z3" 
      [style.backgroundColor]="data().backgroudColor ?? 'white'" 
      [style.color]="data().color ?? 'inherit'"
    >
      <h3 class="m-0"> {{data().label}} </h3>
      <ng-container [ngComponentOutlet]="data().content" />
    </div>
  `,
  styles: [`
    :host {
      display: block;
      border-radius: 16px;
    }  

    .container {
      position: relative;
      height: 100%;
      width: 100%;
      padding: 32px;
      box-sizing: border-box;
      border-radius: inherit;
      overflow: hidden;
      font-size: 1.6rem;
    }

    .settins-button {
      position: absolute;
      top: 16px;
      right: 16px;
    }
  `],
  host: {
    '[style.grid-area]': '"span " + (data().rows ?? 1) + "/ span " + (data().cols ?? 1)'
  }
})
export class WidgetComponent {
  data = input.required<Widget>();

  showOptions = signal(false);
}
