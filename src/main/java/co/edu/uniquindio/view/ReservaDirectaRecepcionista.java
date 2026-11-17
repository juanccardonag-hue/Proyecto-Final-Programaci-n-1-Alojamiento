package co.edu.uniquindio.view;

import co.edu.uniquindio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

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

        configurarBoton(btnBuscarHuesped, "/buscar.png","Buscar");
        configurarBoton(btnReservar, "","Confirmar ");
        configurarBoton(btnCancelar, "","Cancelar");
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

    private void configurarBoton(JButton boton, String rutaIcono, String titulo) {
        if (boton == null) return;

        int tamNormalIcono = 32;
        int tamZoomIcono = 40;

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setText(titulo);
        boton.setFont(fuenteNormal);
        boton.setForeground(new Color(0x2C3E50));
        boton.setHorizontalTextPosition(SwingConstants.RIGHT);
        boton.setMargin(new Insets(5, 10, 5, 10));

        Image imgBase = null;
        if (rutaIcono != null && !rutaIcono.trim().isEmpty()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(getClass().getResource(rutaIcono));
                imgBase = iconOriginal.getImage();
                boton.setIcon(new ImageIcon(imgBase.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
            } catch (Exception e) {
                System.err.println("No se encontró la imagen: " + rutaIcono);
            }
        }

        final Image imagenOriginal = imgBase;

        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamZoomIcono, tamZoomIcono, Image.SCALE_SMOOTH)));
                }

                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                boton.revalidate();
                boton.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);

                if (imagenOriginal != null) {
                    boton.setIcon(new ImageIcon(imagenOriginal.getScaledInstance(tamNormalIcono, tamNormalIcono, Image.SCALE_SMOOTH)));
                }

                boton.revalidate();
                boton.repaint();
            }
        });
    }
    private void cargarApartamentos() {
        if (comboApartamentos == null) return;
        comboApartamentos.removeAllItems();
        if (empresa != null && empresa.getListaApartamentos() != null) {
            for (Apartamento apto : empresa.getListaApartamentos()) {
                comboApartamentos.addItem(apto.getCodigo() + " - " + apto.getNombre());
            }
        }
    }

    private void initListeners() {
        if (btnBuscarHuesped != null) {
            btnBuscarHuesped.addActionListener(e -> buscarOAgregarHuesped());
        }
        if (btnReservar != null) {
            btnReservar.addActionListener(e -> procesarReserva());
        }
        if (btnCancelar != null) {
            btnCancelar.addActionListener(e -> dispose());
        }
    }

    private void buscarOAgregarHuesped() {
        String cedula = txtCedulaHuesped.getText().trim();
        if (cedula.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingresa la cédula del huésped para buscar.", "Campo Requerido ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Huesped> listaHuespedes = null;
        if (empresa != null) {
            listaHuespedes = empresa.getListaHuespedes();
        }

        huespedSeleccionado = Recepcionista.buscarHuespedPorCedula(listaHuespedes, cedula);

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

                List<Huesped> listaActualizada = null;
                if (empresa != null) {
                    listaActualizada = empresa.getListaHuespedes();
                }

                huespedSeleccionado = Recepcionista.buscarHuespedPorCedula(listaActualizada, cedula);
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

    private void procesarReserva() {
        if (huespedSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Selecciona o registra un huésped primero.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (comboApartamentos.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Selecciona un apartamento.", "Atención ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int totalPersonas = 0;
        if (numeroPersonas != null && numeroPersonas.getValue() != null) {
            totalPersonas = (Integer) numeroPersonas.getValue();
        }

        if (totalPersonas <= 0) {
            JOptionPane.showMessageDialog(this, "El número de personas debe ser al menos 1 (incluyendo al titular).", "Cantidad Inválida ⚠", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String itemApto = (String) comboApartamentos.getSelectedItem();
        String codApto = itemApto.split("-")[0].trim();

        List<Apartamento> listaApartamentos = null;
        if (empresa != null) {
            listaApartamentos = empresa.getListaApartamentos();
        }

        Apartamento apto = Recepcionista.buscarApartamentoPorCodigo(listaApartamentos, codApto);

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

        List<Ocupante> ocupantes = new ArrayList<>();
        LocalDate fNacTitular = LocalDate.of(2000, 1, 1);
        if (huespedSeleccionado.getFechaNacimiento() != null) {
            fNacTitular = huespedSeleccionado.getFechaNacimiento();
        }
        ocupantes.add(new Ocupante(huespedSeleccionado.getNombre(), fNacTitular));

        if (totalPersonas > 1) {
            for (int i = 1; i < totalPersonas; i++) {
                LocalDate fNacAcomp = null;
                while (fNacAcomp == null) {
                    String fNacStr = JOptionPane.showInputDialog(this,
                            "Ingresa la fecha de nacimiento del acompañante #" + i + " (AAAA-MM-DD):\n",
                            "Fecha de Nacimiento Acompañante " + i + "/" + (totalPersonas - 1),
                            JOptionPane.QUESTION_MESSAGE);

                    if (fNacStr == null) {
                        return;
                    }
                    try {
                        fNacAcomp = LocalDate.parse(fNacStr.trim());
                    } catch (DateTimeParseException ex) {
                        JOptionPane.showMessageDialog(this, "Formato inválido. Debe ser AAAA-MM-DD (ej: 2019-04-10).", "Error de Fecha ⚠", JOptionPane.ERROR_MESSAGE);
                    }
                }
                ocupantes.add(new Ocupante("Acompañante " + i, fNacAcomp));
            }
        }
        Reserva reserva = null;
        if (recepcionistaActual != null && empresa != null) {
            reserva = recepcionistaActual.registrarReservaDirecta(
                    huespedSeleccionado, apto, entrada, salida, ocupantes,
                    empresa.getListaReservas(), empresa.getListaTemporadas()
            );
        }
        if (reserva != null) {
            JOptionPane.showMessageDialog(this,
                    "¡Reserva directa registrada exitosamente!\n" +
                            "• Folio: " + reserva.getCodigoFolio() + "\n" +
                            "• Personas totales: " + totalPersonas + " (Pagantes: " + reserva.contarOcupantesPagantes() + ")\n" +
                            "• Valor Total: $" + String.format("%,d", reserva.getValorTotal()),
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                    "El apartamento ya cuenta con una reserva activa en ese rango de fechas o los datos no son válidos.",
                    "Cruce de Fechas ⚠", JOptionPane.ERROR_MESSAGE);
        }
    }
}