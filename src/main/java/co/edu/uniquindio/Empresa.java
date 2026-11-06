package co.edu.uniquindio;

import javax.swing.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Empresa {
    private String nit;
    private String nombreAlojamiento;
    private List<Huesped> listaHuespedes;
    private List<Recepcionista> listaRecepcionistas;
    private List<Temporada> listaTemporadas;
    private List<Reserva> listaReservas;
    private List<Apartamento> listaApartamentos;
    private List<Empleado> listaEmpleados;
    private Administrador administrador;

    public Empresa(String nit, String nombreAlojamiento) {
        this.nit = nit;
        this.nombreAlojamiento = nombreAlojamiento;
        this.listaHuespedes = new ArrayList<>();
        this.listaRecepcionistas = new ArrayList<>();
        this.listaEmpleados = new ArrayList<>();
        this.listaApartamentos = new ArrayList<>();
        this.listaTemporadas = new ArrayList<>();
        this.listaReservas = new ArrayList<>();

        // Administrador de prueba
        this.administrador = new Administrador("41911188", "Jhon Mendez", LocalDate.of(1967, 11, 13), "3219393229", "mendezj@hotel.com", "adminPrincipal", "jhon-123");
    }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }
    public String getNombreAlojamiento() { return nombreAlojamiento; }
    public void setNombreAlojamiento(String nombreAlojamiento) { this.nombreAlojamiento = nombreAlojamiento; }


    public void abrirMenuRecepcionista() {
        Recepcionista recepcionistaAutenticado = Recepcionista.loginRecepcionista(listaRecepcionistas);
        if (recepcionistaAutenticado == null) {
            return;
        }
        int opcionRecepcionista;
        do {
            String menuRecepcionista = "⏔⏔⏔ ꒰ Menú recepcionista ꒱ ⏔⏔⏔ \n\n" +
                    "Selecciona una opción del siguiente menú: \n" +
                    "1. Registro de reservas para huespedes \n" +
                    "2. Recepción de reservas del canal Externo, con su código de plataforma\n" +
                    "3. Confrimar reservas pendientes \n" +
                    "4. Registro de chek-in (llegada) de los grupos \n" +
                    "5. Registro de chek-out (salida) de los grupos\n" +
                    "6. Registro de pagos y de cargos por servicios adicionales en el folio\n" +
                    "7. Cambiar el estado operativo de los apartamentos \n" +
                    "8. Registar novedades en apto \n" +
                    "9. Declarar un no-show \n" +
                    "0. Volver \n";
            String inputRecepcionista = JOptionPane.showInputDialog(menuRecepcionista);
            if (inputRecepcionista == null) {
                break;
            }
            opcionRecepcionista = Integer.parseInt(inputRecepcionista);
            switch (opcionRecepcionista) {
                case 1:

                    recepcionistaAutenticado.registrarReservaDirecta(listaHuespedes, listaApartamentos, listaReservas, listaTemporadas);

                    Huesped huespedAutenticado = Huesped.loginHuesped(listaHuespedes);

                    if (huespedAutenticado == null) {
                        break;
                    }
                    int opcionHuesped;
                    do {
                        String menuHuesped = "⏔⏔⏔ ꒰ Menú huésped ꒱ ⏔⏔⏔ \n\n" +
                                "Atendiendo a: " + huespedAutenticado.getNombre() + "\n\n" +
                                "Selecciona una opción del siguiente menú: \n" +
                                "1. Gestionar mis datos personales (Actualizar Perfil)\n" +
                                "2. Cotizar estancia con acompañantes\n" +
                                "3. Reservar por el portal\n" +
                                "4. Pago anticipado / abono al folio\n" +
                                "5. Consultar mis reservas, estado y folio\n" +
                                "6. Cancelar una reserva\n" +
                                "0. Volver al menú principal\n";
                        String inputHuesped = JOptionPane.showInputDialog(menuHuesped);
                        if (inputHuesped == null) {
                            break;
                        }
                        try {
                            opcionHuesped = Integer.parseInt(inputHuesped.trim());
                        } catch (NumberFormatException e) {
                            opcionHuesped = -1;
                        }

                        switch (opcionHuesped) {
                            case 1:
                                huespedAutenticado.actualizarPerfilMenu();
                                break;
                            case 2:
                                huespedAutenticado.cotizarEstanciaMenu(listaApartamentos, listaTemporadas);
                                break;
                            case 3:
                                huespedAutenticado.crearReservaPortalMenu(listaApartamentos, listaReservas, listaTemporadas);
                                break;
                            case 4:
                                huespedAutenticado.realizarAbonoMenu();
                                break;
                            case 5:
                                huespedAutenticado.consultarEstadoReservasFolioMenu();
                                break;
                            case 6:
                                huespedAutenticado.cancelarReservaMenu();
                                break;
                            case 0:
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opción válida ⚠");
                                break;
                        }
                    } while (opcionHuesped != 0);
                    break;
                case 2:
                    recepcionistaAutenticado.registrarReservaExterna(listaHuespedes, listaApartamentos, listaReservas, listaTemporadas);
                    break;
                case 3:
                    recepcionistaAutenticado.confirmarReserva(listaReservas);
                    break;
                case 4:
                    recepcionistaAutenticado.realizarCheckIn(listaReservas);
                    break;
                case 5:
                    recepcionistaAutenticado.realizarCheckOut(listaReservas);
                    break;
                case 6:
                    recepcionistaAutenticado.gestionarFolio(listaReservas);
                    break;
                case 7:
                    recepcionistaAutenticado.cambiarEstadoApartamento(listaApartamentos);
                    break;
                case 8:
                    recepcionistaAutenticado.registrarNovedadApartamento(listaApartamentos);
                    break;
                case 9:
                    recepcionistaAutenticado.declararNoShow(listaReservas);
                    break;
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opción valida ⚠");
                    break;
            }
        } while (opcionRecepcionista != 0);
    }

    public void abrirMenuAdmin (){
        /*if (!administrador.loginAdmin()) {
            return;
        }*/
        int opcionAdmin;
        do {
            String menuAdmin = "⏔⏔⏔ ꒰ Menú administrador/a ꒱ ⏔⏔⏔ \n\n" +
                    "Selecciona una opción del siguiente menú: \n" +
                    "1. Gestionar empleados (contratos, desvinculación, entre otros..)\n" +
                    "2. Modificar apartamentos \n" +
                    "3. Definir fechas de temporada y sus costos por apto \n" +
                    "4. Configurar la politica de cancelación \n" +
                    "5. Registro de rangos de fechas en que un apartamento no se puede vender y su motivo\n" +
                    "6. Control de acceso de cada rol mediante usuario y contraseña \n\n" +
                    " . ݁₊ ⊹ . ݁ Consulta de reportes avanzados: ݁ . ⊹ ₊ ݁.\n" +
                    "7. Ocupación por apartamento \n" +
                    "8. Ingresos por periodo \n" +
                    "9. Reservas por canal \n" +
                    "10. Saldos pendientes \n" +
                    "11. Aptos con novedades\n" +
                    "0. Volver \n";

            String inputAdmin = JOptionPane.showInputDialog(menuAdmin);
            if (inputAdmin == null) {
                break;
            }
            opcionAdmin = Integer.parseInt(inputAdmin);
            switch (opcionAdmin) {
                case 1:
                    administrador.gestionarEmpleados(listaEmpleados);
                    break;
                case 2:
                    administrador.gestionarApartamentos(listaApartamentos);
                    break;
                case 3:
                    administrador.gestionarTemporadaTarifas(listaApartamentos,listaTemporadas);
                    break;
                case 4:
                    administrador.configurarPoliticaCancelacionMenu();
                    break;
                case 5:
                    administrador.gestionarBloqueos(listaApartamentos);
                    break;
                case 6:
                    administrador.gestionarControlAcceso(listaRecepcionistas, listaEmpleados);
                    break;
                case 7:
                    administrador.reporteOcupacionApartamento(listaApartamentos, listaReservas);
                    break;
                case 8:
                    administrador.reporteIngresosPeriodo(listaReservas);
                    break;
                case 9:
                    administrador.reporteReservasPorCanal(listaReservas);
                    break;
                case 10:
                    administrador.reporteSaldosPendientes(listaReservas);
                    break;
                case 11:
                    administrador.reporteApartamentoConMasNovedades(listaApartamentos);
                    break;
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opción valida ⚠");
                    break;
            }
        } while (opcionAdmin != 0);
    }
    public void abrirMenuHuesped(){
        Huesped huespedAutenticado = Huesped.loginHuesped(listaHuespedes);
        if (huespedAutenticado == null) {
            return;
        }
        int opcionHuesped;
        do {
            String menuHuesped = "⏔⏔⏔ ꒰ Menú huesped ꒱ ⏔⏔⏔ \n\n" +
                    "Atendiendo a: " + huespedAutenticado.getNombre() + "\n\n" +
                    "Selecciona una opcion del siguiente menú: \n" +
                    "1. Registrar datos personales\n" +
                    "2. Gestionar datos personales\n" +
                    "3. Cotizar estancia\n" +
                    "4. Reservar por el portal (se pedirá registro de los ocupantes de su fecha de nacimiento, entre otros datos..)\n" +
                    "5. Pago anticipado y/o abono al folio (cuenta)\n" +
                    "6. Consultar reservas, estado y saldo de cada folio \n" +
                    "7. Cancelar una reserva pendiente o confirmada\n" +
                    "0. Volver \n";
            String inputHuesped = JOptionPane.showInputDialog(menuHuesped);
            if (inputHuesped == null) {
                break;
            }
            opcionHuesped = Integer.parseInt(inputHuesped);

            switch (opcionHuesped) {
                case 1:
                    Huesped.registrarHuespedMenu(listaHuespedes);
                    break;
                case 2:
                    huespedAutenticado.actualizarPerfilMenu();
                    break;
                case 3:
                    huespedAutenticado.cotizarEstanciaMenu(listaApartamentos, listaTemporadas);
                    break;
                case 4:
                    huespedAutenticado.crearReservaPortalMenu(listaApartamentos, listaReservas, listaTemporadas);
                    break;
                case 5:
                    huespedAutenticado.realizarAbonoMenu();
                    break;
                case 6:
                    huespedAutenticado.consultarEstadoReservasFolioMenu();
                    break;
                case 7:
                    huespedAutenticado.cancelarReservaMenu();
                    break;
                case 0:
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opción valida ⚠ ");
                    break;
            }
        } while (opcionHuesped != 0);
    }

    public Administrador getAdministrador() {
        return administrador;
    }
}