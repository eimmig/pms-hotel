import { Component, inject } from '@angular/core';
import { WidgetComponent } from "../../components/widget/widget.component";
import { DashboardService } from '../../services/dashboard.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [WidgetComponent],
  providers: [DashboardService],
  template: `
    <h2> Dashboard </h2>

    <div class="dashboad-widgets">
      @for (w of store.widgets(); track w.id) {
        <app-widget [data]="w"  />
      }
    </div>
  `,
  styles: [`
      .dashboad-widgets {
        display: grid;
        grid-template-columns: repeat(3, minmax(200px, 1fr));
        grid-auto-rows: 150px;
        gap: 16px
      }
  `]
})
export class DashboardComponent {

  store = inject(DashboardService);

}
