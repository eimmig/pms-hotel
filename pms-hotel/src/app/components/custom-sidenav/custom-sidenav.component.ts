import { Component, Input, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { RouterLink, RouterModule } from '@angular/router';
import { MenuItemComponent } from "../menu-item/menu-item.component";


export type MenuItem = {
  icon: string;
  label: string;
  route?: string;
  subItems?: MenuItem[];
}

@Component({
  selector: 'app-custom-sidenav',
  standalone: true,
  imports: [CommonModule, MatListModule, MatIconModule, RouterLink, RouterModule, MenuItemComponent],
  template: `
    <div class="sidenav-header">
      <img [width]="picSize()" [height]="picSize()" src="https://cdni.iconscout.com/illustration/premium/thumb/skyscraper-hotel-building-illustration-download-in-svg-png-gif-file-formats--seven-star-pack-people-illustrations-5868698.png"/>
      <div class="header-text">
        <p>Property Management System</p>
      </div>    
    </div>
    <mat-nav-list>
      @for (item of menuItems(); track item.label) {
        <app-menu-item [item]="item" [collapsed]="sidenavCollapsed()"/>
      }
    </mat-nav-list>
    
  `,
  styles: [`
    .sidenav-header {
      padding-top: 24px;
      text-align: center;

      > img {
        border-radius: 100%;
        object-fit: cover;
        margin-bottom: 10px;
      }
    }

    .header-text {
      > p {
        margin: 0;
        font-size: 0.8rem;
      }
    }

    :host * {
      transition: all 500ms ease-in-out;
    }
    
    `]
})
export class CustomSidenavComponent {

  sidenavCollapsed = signal(false);
  @Input() set collapsed(val: boolean) {
    this.sidenavCollapsed.set(val);
  }

  menuItems = signal<MenuItem[]>([
    { icon: 'home', label: 'Dashboard', route: '/dashboard' },
    { icon: 'book', label: 'Reserva', route: '/booking', subItems: [{ icon: 'check_circle', label: 'Check-in', route: '/checkin' }, { icon: 'logout', label: 'Checkout', route: '/checkout' },]},
    { icon: 'category', label: 'Categoria', route: '/roomtype' },
    { icon: 'hotel', label: 'Quarto', route: '/room' },
    { icon: 'attach_money', label: 'Tarifa', route: '/roomrate' },
    { icon: 'room_service', label: 'Extras', route: '/amenities' },
    { icon: 'people', label: 'Hóspede', route: '/person', subItems: [{ icon: 'find_in_page', label: 'Para Checkin ', route: '/personcheckin' }, { icon: 'person_search', label: 'Para Checkout', route: '/personchechout' }] },
  ])

  picSize = computed(() => this.sidenavCollapsed() ? '32' : '100');
}
