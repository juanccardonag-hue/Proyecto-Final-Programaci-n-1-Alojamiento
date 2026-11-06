package co.edu.uniquindio;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

class AdministradorTest {

    private Administrador admin;
    private List<Empleado> listaEmpleados;
    private List<Apartamento> listaApartamentos;
    private List<Temporada> listaTemporadas;
    private List<Reserva> listaReservas;
    private Apartamento apto1;
    private Apartamento apto2;
    private Huesped titular;

    private static final Logger LOG = Logger.getLogger(AdministradorTest.class.getName());

    @BeforeEach
    void crearObjetosClases() {
        admin = new Administrador("1094000000", "Camilo Grandas", LocalDate.of(1985, 5, 10), "3000000000", "admin@hotel.com", "admin", "admin123");

        listaEmpleados = new ArrayList<>();
        listaApartamentos = new ArrayList<>();
        listaTemporadas = new ArrayList<>();
        listaReservas = new ArrayList<>();

        apto1 = new Apartamento("APT-101", "Estándar", 1, 2, "Vista interior", 90000);
        apto2 = new Apartamento("APT-202", "Suite Ejecutiva", 2, 4, "Vista al mar", 150000);
        listaApartamentos.add(apto1);
        listaApartamentos.add(apto2);

        titular = new Huesped("1094000222", "Mariana Méndez", LocalDate.of(2000, 10, 20), "3109876543", "mariana@gmail.com","clave123");
    }

    /* Contratar empleado evitando duplicados por cédula */
    @Test
    @DisplayName("Prueba: Contratar Empleado sin Duplicados")
    void testContratarEmpleado() {
        LOG.info("Iniciando prueba: Contratar Empleado sin Duplicados");

        Recepcionista recep = new Recepcionista("1094000111", "Nancy Rodriguez", LocalDate.of(1995, 3, 15), "3001234567", "nancyR@hotel.com", "recep01", "admin123");

        assertTrue(admin.contratarEmpleado(listaEmpleados, recep));
        assertFalse(admin.contratarEmpleado(listaEmpleados, recep));
        assertEquals(1, listaEmpleados.size());

        LOG.info("Finalizando prueba: Contratar Empleado sin Duplicados");
    }

    /* Editar datos de perfil de un empleado */
    @Test
    @DisplayName("Prueba: Editar Empleado")
    void testEditarEmpleado() {
        LOG.info("Iniciando prueba: Editar Empleado");

        Recepcionista recep = new Recepcionista("1094000111", "Nancy Gómez", LocalDate.of(1995, 3, 15), "3001234567", "NancyG@hotel.com", "recep01", "admin123");
        listaEmpleados.add(recep);

        assertTrue(admin.editarEmpleado(recep, "Nancy Gómez Rodriguez", "3159998877", "nancy.nueva@hotel.com"));
        assertEquals("Nancy Gómez Rodriguez", recep.getNombre());
        assertEquals("3159998877", recep.getTelefono());
        assertEquals("nancy.nueva@hotel.com", recep.getCorreo());

        LOG.info("Finalizando prueba: Editar Empleado");
    }


    /* Crear un apartamento sin repetir código */
    @Test
    @DisplayName("Prueba: Crear Apartamento")
    void testCrearApartamento() {
        LOG.info("Iniciando prueba: Crear Apartamento");

        Apartamento nuevo = new Apartamento("APT-303", "Penthouse", 3, 6, "Vista panorámica", 350000);

        assertTrue(admin.crearApartamento(listaApartamentos, nuevo));
        assertFalse(admin.crearApartamento(listaApartamentos, nuevo));
        assertEquals(3, listaApartamentos.size());

        LOG.info("Finalizando prueba: Crear Apartamento");
    }

    /* Eliminación lógica de apartamento cambiando a FUERA_SERVICIO con motivo */
    @Test
    @DisplayName("Prueba: Eliminación Lógica de Apartamento")
    void testEliminarApto() {
        LOG.info("Iniciando prueba: Eliminación Lógica de Apartamento");

        assertTrue(admin.eliminarApto(apto1, "Daño en la tubería principal"));
        assertEquals(EstadoOperativo.FUERA_SERVICIO, apto1.getEstadoOperativo());
        assertEquals("Daño en la tubería principal", apto1.getMotivoFueraServicio());

        LOG.info("Finalizando prueba: Eliminación Lógica de Apartamento");
    }

    /* Registrar bloqueo de apartamento en rango de fechas */
    @Test
    @DisplayName("Prueba: Registrar Bloqueo en Apartamento")
    void testRegistrarBloqueo() {
        LOG.info("Iniciando prueba: Registrar Bloqueo en Apartamento");

        LocalDate inicio = LocalDate.of(2026, 12, 1);
        LocalDate fin = LocalDate.of(2026, 12, 5);

        assertTrue(admin.registrarBloqueo(apto1, inicio, fin, "Mantenimiento eléctrico"));
        assertEquals(1, apto1.getListBloqueo().size());

        LOG.info("Finalizando prueba: Registrar Bloqueo en Apartamento");
    }

    /* Reporte: Contar reservas filtradas por canal */
    @Test
    @DisplayName("Prueba: Reporte Reservas por Canal")
    void testContarReservasPorCanal() {
        LOG.info("Iniciando prueba: Reporte Reservas por Canal");

        Reserva res1 = new Reserva("FOL-001", LocalDate.of(2026, 11, 10), LocalDate.of(2026, 11, 15), Canal.DIRECTA, titular, apto1, null, null);
        Reserva res2 = new Reserva("FOL-002", LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 25), Canal.PORTAL, titular, apto2, null, null);
        listaReservas.add(res1);
        listaReservas.add(res2);

        assertEquals(1, admin.contarReservasPorCanal(listaReservas, Canal.DIRECTA));
        assertEquals(1, admin.contarReservasPorCanal(listaReservas, Canal.PORTAL));

        LOG.info("Finalizando prueba: Reporte Reservas por Canal");
    }
    /* Configurar la política de cancelación del alojamiento */
    @Test
    @DisplayName("Prueba: Configurar Política de Cancelación")
    void testConfigurarPoliticaCancelacion() {
        LOG.info("Iniciando prueba: Configurar Política de Cancelación");

        PoliticaCancelacion politica = new PoliticaCancelacion();

        assertTrue(admin.configurarPoliticaCancelacion(politica));
        assertFalse(admin.configurarPoliticaCancelacion(null));

        LOG.info("Finalizando prueba: Configurar Política de Cancelación");
    }

}