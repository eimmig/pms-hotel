import { RoomIdAmenities } from "./roomIdAmenities";

export interface Booking {
    id?: string;
    startDate: string;
    endDate: string;
    personId: string; 
    status: string;
    roomList: RoomIdAmenities[];
}