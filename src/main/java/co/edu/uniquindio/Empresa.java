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

    public void menu() {
        int opcion;
        do {
            String menuPrinci = " ⊹₊˚‧︵‿₊୨ Menu Aparta-Suites UQ ୧₊‿︵‧˚₊⊹ \n\n" +
                    "⋅★ Selecciona un rol del siguiente menu: ★⋅ \n" +
                    "1. Soy huesped \n" +
                    "2. Soy administrador \n" +
                    "3. Soy recepcionista \n" +
                    "0. Salir del menu";

            String mostrarMenu = JOptionPane.showInputDialog(menuPrinci);
            if (mostrarMenu == null) {
                return;
            }
            opcion = Integer.parseInt(mostrarMenu);

            switch (opcion) {
                case 1:
                    int opcionHuesped;
                    do {
                        String menuHuesped = "⏔⏔⏔ ꒰ Menu huesped ꒱ ⏔⏔⏔ \n\n" +
                                "Selecciona una opcion del siguiente menu: \n" +
                                "1. Registar datos personales\n" +
                                "2. Gestionar datos personales\n" +
                                "3. Cotizar estancia\n" +
                                "4. Reservar por el portal (se pedira registro de los ocupantes donde se pide su fecha de nacimiento entre otros datos)\n" +
                                "5. Pago anticipado y/o abono al folio (cuenta)\n" +
                                "6. Conusltar reservas, estado, y saldo de cada folio \n" +
                                "7. Cancelar un reserva pendiente o confirmada\n" +
                                "0. Volver \n";
                        String inputHuesped = JOptionPane.showInputDialog(menuHuesped);
                        if (inputHuesped == null) {
                            break;
                        }
                        opcionHuesped = Integer.parseInt(inputHuesped);
                        switch (opcionHuesped) {
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            case 0:
                                break;
                            default:
                                JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opcion valida ⚠ ");
                                break;
                        }
                    } while (opcionHuesped != 0);
                    break;

                case 2:
                    if (!administrador.loginAdmin()) {
                        break;
                    }
                    int opcionAdmin;
                    do {
                        String menuAdmin = "⏔⏔⏔ ꒰ Menu administrador/a ꒱ ⏔⏔⏔ \n\n" +
                                "Selecciona una opcion del siguiente menu: \n" +
                                "1. Gestionar empleados (contratos, desvinculacion, entre otros)\n" +
                                "2. Modificar apartamentos \n" +
                                "3. Definir fechas de temporada y sus costos por apto \n" +
                                "4. Configurar la politica de cancelacion \n" +
                                "5. Registro de rangos de fechas en que un apartamento no se puede vender y su motivo\n" +
                                "6. Control de acceso de cada rol mediante usuario y contraseña \n\n" +
                                " . ݁₊ ⊹ . ݁ Consulta de reportes avanzados: ݁ . ⊹ ₊ ݁.\n" +
                                "7. Ocupacion por apartamento \n" +
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
                                JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opcion valida ⚠");
                                break;
                        }
                    } while (opcionAdmin != 0);
                    break;

                case 3:
                    Recepcionista recepcionistaAutenticado = Recepcionista.loginRecepcionista(listaRecepcionistas);

                        if (recepcionistaAutenticado == null) {
                        break;
                    }
                    int opcionRecepcionista;
                    do {
                        String menuRecepcionista = "⏔⏔⏔ ꒰ Menu recepcionista ꒱ ⏔⏔⏔ \n\n" +
                                "Selecciona una opcion del siguiente menu: \n" +
                                "1. Registro de reservas para huespedes \n" +
                                "2. Recepción de reservas del canal Externo, con su código de plataforma\n" +
                                "3. Confrimar reservas pendientes \n" +
                                "4. Registro de chek-in (llegada) de los grupos \n" +
                                "5. Registro de chek-out (salida) de los grupos\n" +
                                "6. Registro de pagos y de cargos por servicios adicionales en el folio\n" +
                                "7. Cambiar el estado operativo de los apartamentos \n" +
                                "8. Registar de novedades en apto \n" +
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
                                JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opcion valida ⚠");
                                break;
                        }
                    } while (opcionRecepcionista != 0);
                    break;

                case 0:
                    JOptionPane.showMessageDialog(null, "─── ⋆⋅☆ Programa finalizado ☆⋅⋆ ──");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, " ⚠ Por favor ingresa una opcion valida ⚠");
                    break;
            }
        } while (opcion != 0);
    }
}