import { RoomRecive } from "./roomRecive";

export interface BookingRecive {
    id?: string;
    startDate: string;
    endDate: string;
    personId: string; 
    personName: string;
    status: string;
    statusName: string;
    roomList: RoomRecive[];
}
  