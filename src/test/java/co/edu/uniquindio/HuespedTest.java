package co.edu.uniquindio;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class HuespedTest {

    private Huesped huesped;
    private Apartamento aptoEstandar;
    private Apartamento aptoSuite;
    private List<Apartamento> listaGeneralApartamentos;

    private static final Logger LOG = Logger.getLogger(HuespedTest.class.getName());

    @BeforeEach
    void crearObjetosClases() {
        huesped = new Huesped("10940011333", "Mariana mendez", LocalDate.of(2006, 9, 20), "3109876543", "marianaM@gmail.com", "clave123");

        aptoEstandar = new Apartamento("APT-101", "Estándar", 1, 2, "Vista interior", 80000L);
        aptoSuite = new Apartamento("APT-202", "Suite Ejecutiva", 2, 4, "Vista al mar", 150000L);

        listaGeneralApartamentos = new ArrayList<>();
        listaGeneralApartamentos.add(aptoEstandar);
        listaGeneralApartamentos.add(aptoSuite);
    }

    /* Actualizar datos de perfil del huésped */
    @Test
    @DisplayName("Prueba: Actualizar Perfil de Huésped")
    void testActualizarPerfil() {
        LOG.info("Iniciando prueba: Actualizar Perfil de Huésped");

        huesped.actualizarPerfil("Mariana Mendez Morales", LocalDate.of(2007, 9, 20), "3150000000", "mariana.nuevo@gmail.com");

        assertEquals("Mariana Mendez Morales", huesped.getNombre());
        assertEquals("3150000000", huesped.getTelefono());
        assertEquals("mariana.nuevo@gmail.com", huesped.getCorreo());

        LOG.info("Finalizando prueba: Actualizar Perfil de Huésped");
    }

    /* Agregar reserva al historial impidiendo registros duplicados */
    @Test
    @DisplayName("Prueba: Agregar Reserva a Historial sin Duplicados")
    void testAgregarReservaHistorialDuplicados() {
        LOG.info("Iniciando prueba: Agregar Reserva a Historial sin Duplicados");

        Reserva reserva = new Reserva("FOL-100", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5), Canal.PORTAL, huesped, aptoEstandar, null, null);

        assertTrue(huesped.agregarReservaHistorial(reserva));
        assertFalse(huesped.agregarReservaHistorial(reserva));
        assertEquals(1, huesped.getMisReservas().size());

        LOG.info("Finalizando prueba: Agregar Reserva a Historial sin Duplicados");
    }

    /* Consultar disponibilidad filtrando por capacidad del apartamento */
    @Test
    @DisplayName("Prueba: Consultar Apartamentos Disponibles por Capacidad")
    void testConsultarApartamentosDisponiblesPorCapacidad() {
        LOG.info("Iniciando prueba: Consultar Apartamentos Disponibles por Capacidad");

        LocalDate inicio = LocalDate.of(2026, 12, 10);
        LocalDate fin = LocalDate.of(2026, 12, 15);
        int personas = 3; // Solo aptoSuite tiene capacidad suficiente (4 personas)

        List<Apartamento> disponibles = huesped.consultarApartamentosDisponibles(listaGeneralApartamentos, new ArrayList<>(), inicio, fin, personas);

        assertEquals(1, disponibles.size());
        assertEquals("APT-202", disponibles.get(0).getCodigo());

        LOG.info("Finalizando prueba: Consultar Apartamentos Disponibles por Capacidad");
    }

    /* Realizar abono en reserva desde la cuenta del huésped */
    @Test
    @DisplayName("Prueba: Realizar Abono a Reserva de Huésped")
    void testRealizarAbonoExitoso() {
        LOG.info("Iniciando prueba: Realizar Abono a Reserva de Huésped");

        Reserva reserva = new Reserva("FOL-200", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5), Canal.PORTAL, huesped, aptoEstandar, null, null);
        huesped.agregarReservaHistorial(reserva);

        // Se registra el pago directamente a través del Folio de la reserva
        reserva.getFolio().registrarPago(150000, MedioPago.TRANSFERENCIA);

        assertEquals(150000.0, reserva.getTotalAbonado());

        LOG.info("Finalizando prueba: Realizar Abono a Reserva de Huésped");
    }

    /* Cancelar reserva en estado PENDIENTE o CONFIRMADA */
    @Test
    @DisplayName("Prueba: Cancelar Reserva de Huésped")
    void testCancelarReservaExitoso() {
        LOG.info("Iniciando prueba: Cancelar Reserva de Huésped");

        Reserva reserva = new Reserva("FOL-300", LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 5), Canal.PORTAL, huesped, aptoEstandar, null, null);
        huesped.agregarReservaHistorial(reserva);

        boolean cancelada = huesped.cancelarReserva("FOL-300");

        assertTrue(cancelada);
        assertEquals(EstadoReserva.CANCELADA, reserva.getEstado());

        LOG.info("Finalizando prueba: Cancelar Reserva de Huésped");
    }
}