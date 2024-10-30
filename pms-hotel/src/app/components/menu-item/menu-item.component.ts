import { Component, input, Input, signal } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import { MatListModule } from '@angular/material/list';
import { RouterModule } from '@angular/router';
import { MenuItem } from '../custom-sidenav/custom-sidenav.component';
import { animate, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-menu-item',
  standalone: true,
  animations: [
    trigger('expandContractMenu',[
      transition(':enter', [
        style({ height: '0px', opacity: '0' }),
        animate('500ms ease-in-out', style({ height: '*', opacity: '1' }))
      ]),
      transition(':leave', [
        animate('500ms ease-in-out', style({ height: '0px', opacity: '0' }))
      ])
    ])
  ],
  imports: [MatListModule, RouterModule, MatIcon],
  template: `
    <a 
      mat-list-item 
      class="menu-item"
      [routerLink]="item().route"
      (click)="toggleMenu()"
      routerLinkActive="selected-menu-item"
      #rla="routerLinkActive"
      [activated]="rla.isActive"
      >
      <mat-icon [fontSet]="rla.isActive ? 'material-icons' : 'material-icons-outlined'" matListItemIcon>{{ item().icon }}</mat-icon>
      @if (!collapsed()) {
        <span matListItemTitle>
          {{ item().label }}
        </span>
      }

      @if (item().subItems) {
        <span matListItemMeta> 
          @if (menuOpen()) {
            <mat-icon>expand_less</mat-icon>
          } @else {
            <mat-icon>expand_more</mat-icon>
          }
        </span>
      }
    </a>

    @if (item().subItems && menuOpen()) {
      <div @expandContractMenu>
        @for (subItem of item().subItems; track subItem.label) {
          <a 
            mat-list-item 
            class="menu-item"
            [class.indented]="!collapsed()"
            [routerLink]="subItem.route"
            routerLinkActive="selected-menu-item"
            #rla="routerLinkActive"
            [activated]="rla.isActive"
            >
            <mat-icon [fontSet]="rla.isActive ? 'material-icons' : 'material-icons-outlined'" matListItemIcon>{{ subItem.icon }}</mat-icon>
            @if (!collapsed()) {
              <span matListItemTitle>
                {{ subItem.label }}
              </span>
            }

            @if (subItem.subItems) {
              <span matListItemMeta> 
                @if (menuOpen()) {
                  <mat-icon>expand_less</mat-icon>
                } @else {
                  <mat-icon>expand_more</mat-icon>
                }
              </span>
            }
          </a>
        }
      </div>
    }
  `,
  styles: [`
    :host * {
      transition: all 500ms ease-in-out;
    }  

    .selected-menu-item {
      border-left: 3px solid;
      border-left-color: #586fed;
      background: rgba(0, 0, 0, 0.05);
    }

    .menu-item {
      border-left: 3px solid;
      border-left-color: rgba(0, 0, 0, 0);
    }

    .indented {
      --mat-list-list-item-leading-icon-start-space: 48px;
    }
    
  `]
})
export class MenuItemComponent {

  item = input.required<MenuItem>();

  collapsed = input(false);

  menuOpen = signal(false);

  toggleMenu() {
    if (!this.item().subItems) {
      return;
    }

    this.menuOpen.set(!this.menuOpen());
  }
}
