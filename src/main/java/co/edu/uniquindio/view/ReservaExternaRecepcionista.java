package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;

public class ReservaExternaRecepcionista extends JDialog {
    private JPanel contentPane;
    private JTextField textCodExterno;
    private JTextField textCedulaHuesped;
    private JButton btnBuscarHuesped;
    private JLabel lblInfoHuesped;
    private JLabel lblTitulo;
    private JComboBox<String> comboApartamentos;
    private JTextField txtFechaEntrada;
    private JTextField textFechaSalida;
    private JButton btnConfirmar;
    private JButton btnVolver;

    private Empresa empresa;
    private Huesped huespedSeleccionado;

    public ReservaExternaRecepcionista(JDialog parent, Empresa empresa) {
        super(parent, "Reserva Canal Externo", true);
        this.empresa = empresa;

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
        aplicarBordeRedondo(textCodExterno);
        aplicarBordeRedondo(textCedulaHuesped);
        aplicarBordeRedondo(txtFechaEntrada);
        aplicarBordeRedondo(textFechaSalida);
        configurarBoton(btnBuscarHuesped, "Buscar");
        configurarBoton(btnConfirmar, "Confirmar Reserva");
        configurarBoton(btnVolver, "Volver al Menú");
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

    private void configurarBoton(JButton boton, String titulo) {
        if (boton == null) return;
        if (titulo != null && !titulo.isEmpty()) boton.setText(titulo);

        Font fuenteNormal = new Font("Segoe UI", Font.BOLD, 13);
        Font fuenteHover = new Font("Segoe UI", Font.BOLD, 15);
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
        if (btnConfirmar != null) btnConfirmar.addActionListener(e -> procesarReservaExterna());
        if (btnVolver != null) btnVolver.addActionListener(e -> dispose());
    }
    private void buscarOAgregarHuesped() {
        String cedula = textCedulaHuesped.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa la cédula del huésped para buscar.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        huespedSeleccionado = null;
        if (empresa.getListaHuespedes() != null) {
            for (Huesped h : empresa.getListaHuespedes()) {
                if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedula)) {
                    huespedSeleccionado = h;
                    break;
                }
            }
        }
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

                if (empresa.getListaHuespedes() != null) {
                    for (Huesped h : empresa.getListaHuespedes()) {
                        if (h.getCedula() != null && h.getCedula().equalsIgnoreCase(cedula)) {
                            huespedSeleccionado = h;
                            lblInfoHuesped.setText("Huésped: " + huespedSeleccionado.getNombre() + " (Registrado)");
                            lblInfoHuesped.setForeground(new Color(0, 128, 0));
                            break;
                        }
                    }
                }
            } else {
                lblInfoHuesped.setText("Huésped: No seleccionado");
                lblInfoHuesped.setForeground(Color.RED);
            }
        }
    }

    private void procesarReservaExterna() {
        String codExt = textCodExterno.getText().trim();
        if (codExt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa el código de la plataforma externa.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validación de código externo duplicado
        if (empresa.getListaReservas() != null) {
            for (Reserva reser : empresa.getListaReservas()) {
                if (reser.getCodigoExterno() != null && reser.getCodigoExterno().equalsIgnoreCase(codExt)) {
                    JOptionPane.showMessageDialog(this, "RECHAZADA: El código externo '" + codExt + "' ya fue registrado previamente.", "Código Duplicado ⚠", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }

        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona o registra un huésped primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (comboApartamentos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un apartamento.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemApto = (String) comboApartamentos.getSelectedItem();
        String codApto = itemApto.split(" - ")[0].trim();

        Apartamento apto = null;
        if (empresa.getListaApartamentos() != null) {
            for (Apartamento a : empresa.getListaApartamentos()) {
                if (a.getCodigo().equalsIgnoreCase(codApto)) {
                    apto = a;
                    break;
                }
            }
        }

        try {
            LocalDate entrada = LocalDate.parse(txtFechaEntrada.getText().trim());
            LocalDate salida = LocalDate.parse(textFechaSalida.getText().trim());

            if (!entrada.isBefore(salida)) {
                JOptionPane.showMessageDialog(this, "La fecha de entrada debe ser anterior a la de salida.", "Error de Fechas ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (apto != null && apto.estaBloqueadoRango(entrada, salida)) {
                JOptionPane.showMessageDialog(this, "El apartamento está fuera de servicio o bloqueado en ese período.", "Apartamento No Disponible ⚠", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (empresa.getListaReservas() != null) {
                for (Reserva reser : empresa.getListaReservas()) {
                    if (reser.getApartamento() != null && reser.getApartamento().getCodigo().equalsIgnoreCase(codApto) && reser.estaActiva()) {
                        if (reser.cruceCon(entrada, salida)) {
                            JOptionPane.showMessageDialog(this, "La reserva externa (" + codExt + ") se cruza en fechas con la reserva existente " + reser.getCodigoFolio(), "Cruce de Fechas ⚠", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
            }

            int numReserva = (empresa.getListaReservas() != null) ? empresa.getListaReservas().size() + 1 : 1;
            String idFolio = "FOL-EXT-" + numReserva;
            Reserva reserva = new Reserva(idFolio, entrada, salida, Canal.EXTERNA, huespedSeleccionado, apto, null, codExt);
            reserva.liquidarEstancia(empresa.getListaTemporadas());

            huespedSeleccionado.agregarReservaHistorial(reserva);
            if (empresa.getListaReservas() != null) {
                empresa.getListaReservas().add(reserva);
            }

            JOptionPane.showMessageDialog(this,
                    "¡Reserva externa registrada exitosamente!\nFolio: " + idFolio + "\nCódigo Externo: " + codExt + "\nValor Total: $" + reserva.getValorTotal(),
                    "Éxito 🎉", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Debe ser AAAA-MM-DD.", "Error ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}