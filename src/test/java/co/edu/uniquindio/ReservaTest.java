package co.edu.uniquindio;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class ReservaTest {

    private Reserva reserva;
    private Huesped titular;
    private Apartamento apartamento;
    private List<Ocupante> ocupantes;
    private List<Temporada> temporadas;

    private static final Logger LOG = Logger.getLogger(ReservaTest.class.getName());

    @BeforeEach
    void crearObjetosClases() {
        titular = new Huesped("1094000222", "Camilo Cardona", LocalDate.of(2000, 10, 20), "3109876543", "camiloc@gmail.com");
        apartamento = new Apartamento("APT-101", "Suite Ejecutiva", 2, 4, "Vista al jardín", 100000L);

        reserva = new Reserva("FOL-001", LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 15), Canal.DIRECTA, titular, apartamento, null, null);

        ocupantes = new ArrayList<>();
        ocupantes.add(new Ocupante("Mariana", LocalDate.of(1990, 8, 20)));
        ocupantes.add(new Ocupante("Ana Pérez", LocalDate.of(2025, 1, 1))); // Menor de 2 años (No pagante)

        temporadas = new ArrayList<>();
        temporadas.add(new Temporada(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30), 0.20)); // 20% recargo
    }

    /* Calcular la cantidad exacta de noches */
    @Test
    @DisplayName("Prueba: Calcular Noches de Estancia")
    void testCalcularNoches() {
        LOG.info("Iniciando prueba: Calcular Noches de Estancia");

        long noches = reserva.calcularNoches();

        assertEquals(5, noches);

        LOG.info("Finalizando prueba: Calcular Noches de Estancia");
    }

    /* Contar únicamente los ocupantes que generan tarifa */
    @Test
    @DisplayName("Prueba: Contar Ocupantes Pagantes")
    void testContarOcupantesPagantes() {
        LOG.info("Iniciando prueba: Contar Ocupantes Pagantes");

        reserva.agregarOcupante(ocupantes.get(0));
        reserva.agregarOcupante(ocupantes.get(1));

        int pagantes = reserva.contarOcupantesPagantes();

        assertEquals(1, pagantes);

        LOG.info("Finalizando prueba: Contar Ocupantes Pagantes");
    }

    /* Liquidar estancia aplicando recargo de temporada */
    @Test
    @DisplayName("Prueba: Liquidar Estancia con Temporada")
    void testLiquidarEstanciaConTemporada() {
        LOG.info("Iniciando prueba: Liquidar Estancia con Temporada");

        reserva.agregarOcupante(ocupantes.get(0)); // 1 pagante
        reserva.liquidarEstancia(temporadas); // 5 noches * (100.000 + 20% = 120.000) = 600.000

        assertEquals(600000, reserva.getValorTotal());
        assertEquals(5, reserva.getDetallesReserva().size());

        LOG.info("Finalizando prueba: Liquidar Estancia con Temporada");
    }

    /* Validar transiciones de estado de reserva permitidas */
    @Test
    @DisplayName("Prueba: Cambiar Estado de Reserva")
    void testCambiarEstadoTransicionValida() {
        LOG.info("Iniciando prueba: Cambiar Estado de Reserva (Transiciones Válidas)");

        assertTrue(reserva.cambiarEstado(EstadoReserva.CONFIRMADA));
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());

        assertTrue(reserva.cambiarEstado(EstadoReserva.EN_CURSO));
        assertEquals(EstadoReserva.EN_CURSO, reserva.getEstado());

        assertTrue(reserva.cambiarEstado(EstadoReserva.FINALIZADA));
        assertEquals(EstadoReserva.FINALIZADA, reserva.getEstado());

        LOG.info("Finalizando prueba: Cambiar Estado de Reserva (Transiciones Válidas)");
    }

    /* Detectar solapamiento de fechas para evitar sobreventa */
    @Test
    @DisplayName("Prueba: Detectar Cruce de Fechas")
    void testCruceConFechas() {
        LOG.info("Iniciando prueba: Detectar Cruce de Fechas");

        // Reserva actual: 10 al 15 de noviembre
        boolean cruza = reserva.cruceCon(LocalDate.of(2026, 11, 12), LocalDate.of(2026, 11, 18));
        boolean noCruza = reserva.cruceCon(LocalDate.of(2026, 11, 15), LocalDate.of(2026, 11, 20));

        assertTrue(cruza);
        assertFalse(noCruza);

        LOG.info("Finalizando prueba: Detectar Cruce de Fechas");
    }

    /* Registrar abono y confirmar automáticamente al alcanzar el 30% */
    @Test
    @DisplayName("Prueba: Registrar Abono y Confirmación Automática")
    void testRegistrarAbonoYConfirmacionAutomatica() {
        LOG.info("Iniciando prueba: Registrar Abono y Confirmación Automática");

        reserva.agregarOcupante(ocupantes.get(0));
        reserva.liquidarEstancia(temporadas); // Total: 600.000

        boolean registrado = reserva.registrarAbono(200000, MedioPago.TRANSFERENCIA); // 33.3% abonado

        assertTrue(registrado);
        assertEquals(200000, reserva.getTotalAbonado());
        assertEquals(400000, reserva.getSaldoFolio());
        assertEquals(EstadoReserva.CONFIRMADA, reserva.getEstado());

        LOG.info("Finalizando prueba: Registrar Abono y Confirmación Automática");
    }
}