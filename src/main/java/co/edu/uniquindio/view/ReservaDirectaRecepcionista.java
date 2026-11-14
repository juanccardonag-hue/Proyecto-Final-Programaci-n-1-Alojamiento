package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class ReservaDirectaRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField txtCedulaHuesped;
    private JButton btnBuscarHuesped;
    private JLabel lblInfoHuesped;
    private JComboBox<String> comboApartamentos;
    private JTextField txtFechaEntrada;
    private JTextField txtFechaSalida;
    private JSpinner numeroPersonas;
    private JButton btnReservar;
    private JButton btnCancelar;

    private Empresa empresa;
    private Recepcionista recepcionistaActual;
    private Huesped huespedSeleccionado;

    public ReservaDirectaRecepcionista(JDialog parent, Empresa empresa, Recepcionista recepcionistaActual) {
        super(parent, "Reserva Canal Directo", true);
        this.empresa = empresa;
        this.recepcionistaActual = recepcionistaActual;

        if (contentPane != null) {
            setContentPane(contentPane);
            contentPane.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        }
        aplicarEstilos();
        cargarApartamentos();
        initListeners();
        setSize(520, 560);
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    private void aplicarEstilos() {
        aplicarBordeRedondo(txtCedulaHuesped);
        aplicarBordeRedondo(txtFechaEntrada);
        aplicarBordeRedondo(txtFechaSalida);

        if (numeroPersonas != null) {
            numeroPersonas.setModel(new SpinnerNumberModel(0, 0, 20, 1));
        }

        configurarBotonSencillo(btnBuscarHuesped, "Buscar");
        configurarBotonSencillo(btnReservar, "Confirmar Reserva");
        configurarBotonSencillo(btnCancelar, "Volver al Menú");
    }

    private void aplicarBordeRedondo(JTextField campo) {
        if (campo == null) return;
        campo.setOpaque(false);
        campo.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.AbstractBorder() {
                    @Override
                    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(Color.LIGHT_GRAY);
                        g2.drawRoundRect(x, y, width - 1, height - 1, 15, 15);
                        g2.dispose();
                    }
                },
                BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
    }

    private void configurarBotonSencillo(JButton boton, String texto) {
        if (boton == null) return;
        if (texto != null && !texto.isEmpty()) boton.setText(texto);

        Font fuenteNormal = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteHover = new Font("Segoe UI", Font.BOLD, 14);
        boton.setFont(fuenteNormal);

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setFont(fuenteHover);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
    }

    private void cargarApartamentos() {
        if (comboApartamentos == null) return;
        comboApartamentos.removeAllItems();
        if (empresa.getListaApartamentos() != null) {
            for (Apartamento apto : empresa.getListaApartamentos()) {
                comboApartamentos.addItem(apto.getCodigo() + " - " + apto.getNombre());
            }
        }
    }

    private void initListeners() {
        if (btnBuscarHuesped != null) btnBuscarHuesped.addActionListener(e -> buscarOAgregarHuesped());
        if (btnReservar != null) btnReservar.addActionListener(e -> procesarReserva());
        if (btnCancelar != null) btnCancelar.addActionListener(e -> dispose());
    }

    private void buscarOAgregarHuesped() {
        String cedula = txtCedulaHuesped.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa la cédula del huésped para buscar.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        huespedSeleccionado = buscarHuespedPorCedula(cedula);

        if (huespedSeleccionado != null) {
            lblInfoHuesped.setText("Huésped: " + huespedSeleccionado.getNombre());
            lblInfoHuesped.setForeground(new Color(0, 128, 0));
        } else {
            int opc = JOptionPane.showConfirmDialog(this,
                    "No se encontró un huésped registrado con la cédula " + cedula + ".\n¿Deseas abrir la ventana de registro para crearlo?",
                    "Huésped No Encontrado", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (opc == JOptionPane.YES_OPTION) {
                RegistroHuespedView ventanaRegistro = new RegistroHuespedView(null, empresa);
                ventanaRegistro.setVisible(true);

                huespedSeleccionado = buscarHuespedPorCedula(cedula);
                if (huespedSeleccionado != null) {
                    lblInfoHuesped.setText("Huésped: " + huespedSeleccionado.getNombre() + " (Registrado)");
                    lblInfoHuesped.setForeground(new Color(0, 128, 0));
                } else {
                    lblInfoHuesped.setText("Huésped: No seleccionado");
                    lblInfoHuesped.setForeground(Color.RED);
                }
            } else {
                lblInfoHuesped.setText("Huésped: No seleccionado");
                lblInfoHuesped.setForeground(Color.RED);
            }
        }
    }

    private Huesped buscarHuespedPorCedula(String cedula) {
        if (empresa.getListaHuespedes() != null) {
            for (Huesped h : empresa.getListaHuespedes()) {
                if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedula)) {
                    return h;
                }
            }
        }
        return null;
    }

    private void procesarReserva() {
        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona o registra un huésped primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (comboApartamentos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un apartamento.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar número de personas
        int totalPersonas = (numeroPersonas != null) ? (Integer) numeroPersonas.getValue() : 0;
        if (totalPersonas <= 0) {
            JOptionPane.showMessageDialog(this, "El número de personas debe ser al menos 1 (incluyendo al titular).", "Cantidad Inválida ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemApto = (String) comboApartamentos.getSelectedItem();
        String codApto = itemApto.split("-")[0].trim();

        Apartamento apto = null;
        if (empresa.getListaApartamentos() != null) {
            for (Apartamento a : empresa.getListaApartamentos()) {
                if (a.getCodigo().equalsIgnoreCase(codApto)) {
                    apto = a;
                    break;
                }
            }
        }

        if (apto == null) {
            JOptionPane.showMessageDialog(this, "No se encontró el apartamento seleccionado.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (totalPersonas > apto.getCapaMaxPersona()) {
            JOptionPane.showMessageDialog(this,
                    "El apartamento seleccionado tiene una capacidad máxima de " + apto.getCapaMaxPersona() + " personas.",
                    "Capacidad Excedida ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate entrada;
        LocalDate salida;
        try {
            entrada = LocalDate.parse(txtFechaEntrada.getText().trim());
            salida = LocalDate.parse(txtFechaSalida.getText().trim());
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Debe ser AAAA-MM-DD.", "Error de Formato ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!entrada.isBefore(salida)) {
            JOptionPane.showMessageDialog(this, "La fecha de entrada debe ser anterior a la de salida.", "Error de Fechas ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (apto.estaBloqueadoRango(entrada, salida)) {
            JOptionPane.showMessageDialog(this, "El apartamento está fuera de servicio o bloqueado en ese período.", "Apartamento No Disponible ⚠", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (empresa.getListaReservas() != null) {
            for (Reserva reser : empresa.getListaReservas()) {
                if (reser.getApartamento() != null && reser.getApartamento().getCodigo().equalsIgnoreCase(codApto) && reser.estaActiva()) {
                    if (reser.cruceCon(entrada, salida)) {
                        JOptionPane.showMessageDialog(this, "El apartamento ya cuenta con una reserva en esas fechas.", "Cruce de Fechas ⚠", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }
            }
        }

        try {
            String idFolio = "FOL-" + (empresa.getListaReservas().size() + 1);
            Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.DIRECTA, huespedSeleccionado, apto, recepcionistaActual, "");

            // Registrar al titular
            LocalDate fNacTitular = (huespedSeleccionado.getFechaNacimiento() != null)
                    ? huespedSeleccionado.getFechaNacimiento()
                    : LocalDate.of(2000, 1, 1);
            reserva.agregarOcupante(new Ocupante(huespedSeleccionado.getNombre(), fNacTitular));

            // Si hay más de 1 persona, pedir solo la fecha de nacimiento
            if (totalPersonas > 1) {
                for (int i = 1; i < totalPersonas; i++) {
                    LocalDate fNacAcomp = null;
                    while (fNacAcomp == null) {
                        String fNacStr = JOptionPane.showInputDialog(this,
                                "Ingresa la fecha de nacimiento del acompañante #" + i + " (AAAA-MM-DD):\n",
                                "Fecha de Nacimiento Acompañante " + i + "/" + (totalPersonas - 1),
                                JOptionPane.QUESTION_MESSAGE);

                        if (fNacStr == null) return;
                        try {
                            fNacAcomp = LocalDate.parse(fNacStr.trim());
                        } catch (DateTimeParseException ex) {
                            JOptionPane.showMessageDialog(this, "Formato inválido. Debe ser AAAA-MM-DD (ej: 2019-04-10).", "Error de Fecha ⚠", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    reserva.agregarOcupante(new Ocupante("Acompañante " + i, fNacAcomp));
                }
            }

            if (empresa.getListaTemporadas() != null) {
                reserva.liquidarEstancia(empresa.getListaTemporadas());
            }

            huespedSeleccionado.agregarReservaHistorial(reserva);
            empresa.getListaReservas().add(reserva);

            JOptionPane.showMessageDialog(this,
                    "¡Reserva directa registrada exitosamente!\n" +
                            "• Folio: " + idFolio + "\n" +
                            "• Personas totales: " + totalPersonas + " (Pagantes: " + reserva.contarOcupantesPagantes() + ")\n" +
                            "• Valor Total: $" + String.format("%,d", reserva.getValorTotal()),
                    "Éxito ", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error al procesar la reserva: " + ex.getMessage(),
                    "Error Interno ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}