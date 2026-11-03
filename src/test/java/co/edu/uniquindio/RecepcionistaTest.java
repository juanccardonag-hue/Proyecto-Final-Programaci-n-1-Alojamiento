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
            titular = new Huesped("1094000222", "Carlos Pérez", LocalDate.of(1990, 8, 20), "3109876543", "carlos@gmail.com");
            apartamento = new Apartamento("APT-101", "Suite Ejecutiva", 2, 4, "Vista al jardín", 100000L);

            ocupantes = new ArrayList<>();
            ocupantes.add(new Ocupante("Carlos Pérez", LocalDate.of(1990, 8, 20)));
            ocupantes.add(new Ocupante("Ana Pérez", LocalDate.of(1993, 2, 10)));

            temporadas = new ArrayList<>();
            reservasExistentes = new ArrayList<>();
        }

        /* Registrar reserva directa correctamente */
        @Test
        @DisplayName("Prueba: Registrar Reserva Directa")
        void testRegistrarReservaDirectaCorrecto() {
            LOG.info("Iniciando prueba Registrar Reserva Directa correctamente: ");

            LocalDate entrada = LocalDate.of(2026, 11, 10);
            LocalDate salida = LocalDate.of(2026, 11, 12);

            Reserva reserva = recepcionista.registrarReservaDirecta("FOL-001", titular, apartamento, entrada, salida, ocupantes, temporadas);

            assertNotNull(reserva);
            assertEquals(Canal.DIRECTA, reserva.getCanal());
            assertEquals(EstadoReserva.PENDIENTE, reserva.getEstado());

            LOG.info("Finalizando prueba Registrar Reserva Directa correctamente");
        }

        /* Rechazar reserva externa por conflicto de canal */
        @Test
        @DisplayName("Prueba: Rechazar Reserva Externa por Conflicto de Canal")
        void testRegistrarReservaExternaConConflicto() {
            LOG.info("Iniciando prueba Rechazar Reserva Externa por conflicto de canal: ");

            LocalDate entrada1 = LocalDate.of(2026, 11, 10);
            LocalDate salida1 = LocalDate.of(2026, 11, 15);

            Reserva existente = recepcionista.registrarReservaDirecta("FOL-001", titular, apartamento, entrada1, salida1, ocupantes, temporadas);
            existente.cambiarEstado(EstadoReserva.CONFIRMADA);
            reservasExistentes.add(existente);

            // Se intenta registrar en fechas cruzadas (12 al 18 de noviembre)
            LocalDate entrada2 = LocalDate.of(2026, 11, 12);
            LocalDate salida2 = LocalDate.of(2026, 11, 18);

            Reserva externa = recepcionista.registrarReservaExterna("FOL-002", titular, apartamento, entrada2, salida2, ocupantes, temporadas, "EXT-999", reservasExistentes);

            assertNull(externa);

            LOG.info("Finalizando prueba: Rechazar Reserva Externa por conflicto de canal");
        }

        /* Realizar Check-In de reserva confirmada */
        @Test
        @DisplayName("Prueba: Realizar Check-In exitoso")
        void testRealizarCheckInExitoso() {
            LOG.info("Iniciando prueba Realizar Check-In exitoso: ");

            LocalDate entrada = LocalDate.now();
            LocalDate salida = entrada.plusDays(2);

            Reserva reserva = recepcionista.registrarReservaDirecta("FOL-003", titular, apartamento, entrada, salida, ocupantes, temporadas);
            recepcionista.confirmarReserva(reserva);

            boolean exitoCheckIn = recepcionista.realizarCheckIn(reserva);

            assertTrue(exitoCheckIn);
            assertEquals(EstadoReserva.EN_CURSO, reserva.getEstado());
            assertEquals(EstadoOperativo.OCUPADO, apartamento.getEstadoOperativo());

            LOG.info("Finalizando prueba: Realizar Check-In exitoso");
        }

        /* Calcular devolución por cancelación con más de 7 días de antelación */
        @Test
        @DisplayName("Prueba: Calcular Devolución Cancelación (>7 días)")
        void testCalcularDevolucionCancelacion() {
            LOG.info("Iniciando prueba: Calcular Devolución Cancelación: ");

            LocalDate entrada = LocalDate.of(2026, 12, 1);
            LocalDate salida = LocalDate.of(2026, 12, 5);

            Reserva reserva = recepcionista.registrarReservaDirecta("FOL-004", titular, apartamento, entrada, salida, ocupantes, temporadas);
            recepcionista.registrarPagoEnFolio(reserva, 100000, MedioPago.EFECTIVO);

            // Cancelación efectuada 21 días antes de la llegada
            LocalDate fechaCancelacion = LocalDate.of(2026, 11, 10);
            long devolucion = reserva.calcularDevolucionCancelacion(fechaCancelacion);

            assertEquals(100000, devolucion);

            LOG.info("Finalizando prueba Calcular Devolución Cancelación");
        }

        /* Calcular tarifa por noche con recargo de temporada */
        @Test
        @DisplayName("Prueba: Calcular Tarifa de Noche con Temporada")
        void testCalcularTarifaNocheConTemporada() {
            LOG.info("Iniciando prueba Calcular Tarifa de Noche con Temporada: ");

            LocalDate fechaNoche = LocalDate.of(2026, 12, 24);
            Temporada tempAlta = new Temporada(LocalDate.of(2026, 12, 20), LocalDate.of(2026, 12, 31), 0.20);

            // 2 pagantes, tarifa base 50.000 con 20% recargo = 60.000 * 2 = 120.000
            DetalleNocheReserva detalle = new DetalleNocheReserva(fechaNoche, 2, 50000, tempAlta);

            assertEquals(120000, detalle.calcularValorNoche());

            LOG.info("Finalizando prueba Calcular Tarifa de Noche con Temporada");
        }
    }

