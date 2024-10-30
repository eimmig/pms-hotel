import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { BookingComponent } from './pages/booking/booking.component';
import { RoomTypeComponent } from './pages/room-type/room-type.component';
import { RoomComponent } from './pages/room/room.component';
import { RoomRateComponent } from './pages/room-rate/room-rate.component';
import { RoomPersonComponent } from './pages/room-person/room-person.component';
import { AmenitiesComponent } from './pages/amenities/amenities.component';
import { CheckinComponent } from './pages/booking/checkin/checkin.component';
import { CheckoutComponent } from './pages/booking/checkout/checkout.component';
import { ToCheckinComponent } from './pages/room-person/to-checkin/to-checkin.component';
import { ToCheckoutComponent } from './pages/room-person/to-checkout/to-checkout.component';

export const routes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: 'dashboard'
    },
    {
        path: 'dashboard',
        component: DashboardComponent
    },
    {
        path: 'booking',
        component: BookingComponent
    },
    {
        path: 'roomtype',
        component: RoomTypeComponent
    },
    {
        path: 'room',
        component: RoomComponent
    },
    {
        path: 'roomrate',
        component: RoomRateComponent
    },
    {
        path: 'person',
        component: RoomPersonComponent
    },
    {
        path:'amenities',
        component: AmenitiesComponent
    },
    {
        path: 'checkin',
        component: CheckinComponent
    },
    {
        path: 'checkout',
        component: CheckoutComponent
    },
    {
        path: 'personchechout',
        component: ToCheckoutComponent
    },
    {
        path: 'personcheckin',
        component: ToCheckinComponent
    }
];
