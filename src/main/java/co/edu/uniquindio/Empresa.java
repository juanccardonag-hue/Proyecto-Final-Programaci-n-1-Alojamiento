package co.edu.uniquindio;

import javax.swing.*;

public class Empresa {
    private String nit;
    private String nombreAlojamiento;

    public Empresa(String nit, String nombreAlojamiento) {
        this.nit = nit;
        this.nombreAlojamiento = nombreAlojamiento;
    }

    public String getNit() {return nit;}
    public void setNit(String nit) {this.nit = nit;}
    public String getNombreAlojamiento() {return nombreAlojamiento;}
    public void setNombreAlojamiento(String nombreAlojamiento) {this.nombreAlojamiento = nombreAlojamiento;}

    public void menu (){
        int opcion;
        do {
            String menu = " ⊹₊˚‧︵‿₊୨ Menu Aparta-Suites UQ ୧₊‿︵‧˚₊⊹ \n\n" +
                    "⋅★ Selecciona un rol del siguiente menu: ★⋅ \n" +
                    "1. Soy huesped \n" +
                    "2. Soy administrador \n"+
                    "3. Soy recepcionista \n" +
                    "0. Salir del menu";

            String mostrarMenu = JOptionPane.showInputDialog(menu);
            if (mostrarMenu == null) {
                return;
            }
            opcion = Integer.parseInt(mostrarMenu);
            switch (opcion) {
                case 1:
                    String menuHuesped = "⏔⏔⏔ ꒰ Menu huesped ꒱ ⏔⏔⏔ \n\n" +
                            "Selecciona una opcion del siguiente menu: \n" +
                            "1. Registar datos personales\n"+
                            "2. Gestionar datos personales\n"+
                            "3. Cotizar estancia\n" +
                            "4. Reservar por el portal (se pedira registro de los ocupantes donde se pide su fecha de nacimiento entre otros datos)\n" +
                            "5. Pago anticipado y/o abono al folio (cuenta)\n" +
                            "6. Conusltar reservas, estado, y saldo de cada folio \n" +
                            "7. Cancelar un reserva pendiente  o confirmada\n" +
                            "0. Volver \n";
                    String inputHuesped = JOptionPane.showInputDialog(menuHuesped);
                    if (inputHuesped == null) {
                        break;
                    }
                    int opcionHuesped = Integer.parseInt(inputHuesped);
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
                    break;

                case 2:
                    String menuAdmin = "⏔⏔⏔ ꒰ Menu administrador/a ꒱ ⏔⏔⏔ \n\n" +
                            "Selecciona una opcion del siguiente menu: \n" +
                            "1. Gestionar empleados (contratos, desvinculacion, entre otros)\n"+
                            "2. Modificar apartamentos \n"+
                            "3. Definir fechas de temporada y sus costos por apto \n" +
                            "4. Configurar la politica de cancelacion \n" +
                            "5. Registro de  rangos de fechas en que un apartamento no se puede vender y su motivo\n" +
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
                    int opcionAdmin = Integer.parseInt(inputAdmin);
                    switch (opcionAdmin) {
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
                        case 8:
                            break;
                        case 9:
                            break;
                        case 10:
                            break;
                        case 11:
                            break;
                        case 0:
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opcion valida ⚠");
                            break;
                    }
                    break;

                case 3:
                    String menuRecepcionista = "⏔⏔⏔ ꒰ Menu recepcionista ꒱ ⏔⏔⏔ \n\n" +
                            "Selecciona una opcion del siguiente menu: \n" +
                            "1. Registro de reservas para huespedes \n"+
                            "2. Recepción de reservas del canal Externo, con su código de plataforma\n"+
                            "3. Confrimar reservas pendientes  \n" +
                            "4. Registro de chek-in (llegada) de los grupos \n" +
                            "5. Registro de chek-out (salida) de los grupos\n" +
                            "6. Registro de pagos y de cargos por servicios adicionales en el folio\n" +
                            "7. Cambiar el estado operativo de los apartamentos \n" +
                            "8. Registar de novedades en apto  \n" +
                            "9. Declarar un no-show \n" +
                            "0. Volver \n";
                    String inputRecepcionista = JOptionPane.showInputDialog(menuRecepcionista);
                    if (inputRecepcionista == null) {
                        break;
                    }
                    int opcionRecepcionista = Integer.parseInt(inputRecepcionista);
                    switch (opcionRecepcionista) {
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
                        case 8:
                            break;
                        case 9:
                            break;
                        case 0:
                            break;
                        default:
                            JOptionPane.showMessageDialog(null, "⚠ Por favor ingresa una opcion valida ⚠");
                            break;
                    }
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
