import { AmenityRecive } from "./amenityRecive";

export interface RoomRecive {
    roomId: string;
    roomNumber: string;
    amenities: AmenityRecive[];
}