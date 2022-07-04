package hai2022.team.bususersapp.interfaces;

public interface ReservationsListiner {
    void numOfReservations(int num);
    void onReservationListener(boolean status);
    void isReserved(boolean status);
    void ReservedBusId(String id);
}
