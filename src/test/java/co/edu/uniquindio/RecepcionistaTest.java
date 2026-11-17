package co.edu.uniquindio;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class RecepcionistaTest {
    private Recepcionista recepcionista;
    private Huesped titular;
    private Apartamento apartamento;
    private List<Ocupante> ocupantes;
    private List<Temporada> temporadas;
    private List<Reserva> reservasExistentes;

    private static final Logger LOG = Logger.getLogger(RecepcionistaTest.class.getName());

    @BeforeEach
    void crearObjetosClases() {
        recepcionista = new Recepcionista("1094000111", "Laura Gómez", LocalDate.of(1995, 3, 15), "3001234567", "laura@hotel.com", "recep01", "admin123");
        titular = new Huesped("1094000222", "Carlos Pérez", LocalDate.of(1990, 8, 20), "3109876543", "carlos@gmail.com", "carlos12");
        apartamento = new Apartamento("APT-101", "Suite Ejecutiva", 2, 4, "Vista al jardín", 100000L);

        ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Carlos Pérez", LocalDate.of(1990, 8, 20)));
        ocupantes.add(new Ocupante("Ana Pérez", LocalDate.of(1993, 2, 10)));

        temporadas = new ArrayList<>();
        reservasExistentes = new ArrayList<>();
    }

    // Registrar reserva directa
    @Test
    @DisplayName("Prueba: Registrar Reserva Directa")
    void testRegistrarReservaDirectaCorrecto() {
        LOG.info("Iniciando prueba Registrar Reserva Directa: ");

        LocalDate entrada = LocalDate.of(2026, 11, 10);
        LocalDate salida = LocalDate.of(2026, 11, 12);

        Reserva reserva = new Reserva("FOL-001", entrada, salida, Canal.DIRECTA, titular, apartamento, recepcionista, null);
        for (Ocupante oc : ocupantes) {
            reserva.agregarOcupante(oc);
        }
        reserva.liquidarEstancia(temporadas);
        titular.agregarReservaHistorial(reserva);

        assertNotNull(reserva);
        assertEquals(Canal.DIRECTA, reserva.getCanal());
        assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());

        LOG.info("Finalizando prueba Registrar Reserva Directa");
    }

    //Rechazar reserva externa por conflicto de canal
    @Test
    @DisplayName("Prueba: Rechazar Reserva Externa por Conflicto de Canal")
    void testRegistrarReservaExternaConConflicto() {
        LOG.info("Iniciando prueba Rechazar Reserva Externa por conflicto de canal: ");

        LocalDate entrada1 = LocalDate.of(2026, 11, 10);
        LocalDate salida1 = LocalDate.of(2026, 11, 15);

        Reserva existente = new Reserva("FOL-001", entrada1, salida1, Canal.DIRECTA, titular, apartamento, recepcionista, null);
        existente.cambiarEstado(EstadoReserva.CONFIRMADA);
        reservasExistentes.add(existente);

        // Se intenta registrar en fechas cruzadas (12 al 18 de noviembre)
        LocalDate entrada2 = LocalDate.of(2026, 11, 12);
        LocalDate salida2 = LocalDate.of(2026, 11, 18);

        Reserva externa = null;
        boolean hayConflicto = false;
        for (Reserva r : reservasExistentes) {
            if (r.getApartamento().getCodigo().equalsIgnoreCase(apartamento.getCodigo()) && r.estaActiva()) {
                if (r.cruceCon(entrada2, salida2)) {
                    hayConflicto = true;
                    break;
                }
            }
        }
        if (!hayConflicto) {
            externa = new Reserva("FOL-002", entrada2, salida2, Canal.EXTERNA, titular, apartamento, null, "EXT-999");
        }

        assertNull(externa);

        LOG.info("Finalizando prueba: Rechazar Reserva Externa por conflicto de canal");
    }

    //Realizar Check-In de reserva confirmada
    @Test
    @DisplayName("Prueba: Realizar Check-In")
    void testRealizarCheckInExitoso() {
        LOG.info("Iniciando prueba Realizar Check-In: ");

        LocalDate entrada = LocalDate.now();
        LocalDate salida = entrada.plusDays(2);

        apartamento.cambiarEstado(EstadoOperativo.PREPARADO, "Listo para recibir huéspedes");

        Reserva reserva = new Reserva("FOL-003", entrada, salida, Canal.DIRECTA, titular, apartamento, recepcionista, null);
        reserva.cambiarEstado(EstadoReserva.CONFIRMADA);

        boolean exitoCheckIn = false;
        if (reserva.getEstado() == EstadoReserva.CONFIRMADA && apartamento.getEstadoOperativo() == EstadoOperativo.PREPARADO) {
            reserva.cambiarEstado(EstadoReserva.EN_CURSO);
            apartamento.cambiarEstado(EstadoOperativo.OCUPADO, "Check-In realizado");
            exitoCheckIn = true;
        }

        assertTrue(exitoCheckIn);
        assertEquals(EstadoReserva.EN_CURSO, reserva.getEstado());
        assertEquals(EstadoOperativo.OCUPADO, apartamento.getEstadoOperativo());

        LOG.info("Finalizando prueba: Realizar Check-In ");
    }

    // Calcular devolución por cancelación con más de 7 días de antelación
    @Test
    @DisplayName("Prueba: Calcular Devolución Cancelación (>7 días)")
    void testCalcularDevolucionCancelacion() {
        LOG.info("Iniciando prueba: Calcular Devolución Cancelación: ");

        LocalDate entrada = LocalDate.of(2026, 12, 1);
        LocalDate salida = LocalDate.of(2026, 12, 5);

        Reserva reserva = new Reserva("FOL-004", entrada, salida, Canal.DIRECTA, titular, apartamento, recepcionista, null);
        titular.agregarReservaHistorial(reserva);

        reserva.getFolio().registrarPago(100000, MedioPago.EFECTIVO);

        double devolucion = titular.calcularDevolucion("FOL-004");

        assertEquals(100000.0, devolucion);

        LOG.info("Finalizando prueba Calcular Devolución Cancelación");
    }

    //Calcular tarifa por noche con recargo de temporada
    @Test
    @DisplayName("Prueba: Calcular Tarifa de Noche con Temporada")
    void testCalcularTarifaNocheConTemporada() {
        LOG.info("Iniciando prueba Calcular Tarifa de Noche con Temporada: ");

        LocalDate fechaNoche = LocalDate.of(2026, 12, 24);
        Temporada tempAlta = new Temporada(TipoTemporada.ALTA, LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 31), 0.20);

        DetalleNocheReserva detalle = new DetalleNocheReserva(fechaNoche, 2, 50000, tempAlta);

        assertEquals(120000, detalle.getSubtotal());

        LOG.info("Finalizando prueba Calcular Tarifa de Noche con Temporada");
    }
}