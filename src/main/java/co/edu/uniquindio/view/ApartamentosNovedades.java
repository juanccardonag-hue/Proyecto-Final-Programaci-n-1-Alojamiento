package co.edu.uniquindio.view;
import co.edu.uniquindio.Administrador;
import co.edu.uniquindio.Apartamento;
import co.edu.uniquindio.Empresa;
import javax.swing.*;
import java.awt.*;

public class ApartamentosNovedades extends JDialog {
    private JPanel mainPanel;
    private JLabel lblResultado;
    private JButton btnConsultar;
    private JButton btnCerrar;
    private Empresa empresa;

    public ApartamentosNovedades(JDialog parent, Empresa empresa) {
        super(parent, "Apartamento con Más Novedades", true);
        this.empresa = empresa;

        setContentPane(mainPanel);
        setSize(550, 380);
        setResizable(false);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        if (mainPanel != null) {
            mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        }

        configurarBoton(btnConsultar, "Consultar");
        configurarBoton(btnCerrar, "Cerrar");
        initListeners();
    }
    private void initListeners() {

        if (btnConsultar != null) {
            btnConsultar.addActionListener(e -> consultar());
        }
        if (btnCerrar != null) {
            btnCerrar.addActionListener(e -> dispose());
        }
    }
    private void consultar() {

        Administrador admin = empresa.getAdministrador();
        Apartamento apto = admin.obtenerApartamentoNovedades(empresa.getListaApartamentos());
        if (apto == null) {
            lblResultado.setText("<html><div style='text-align: center;'>" +
                    "<b>No hay apartamentos o novedades registradas.</b></div></html>");
            return;
        }

        int cantidadNovedades = 0;
        if (apto.getListNovedad() != null) {
            cantidadNovedades = apto.getListNovedad().size();
        }

        lblResultado.setText("<html><div style='text-align: center;'>" +
                "<b>Apartamento con más novedades:</b><br>" +
                "<font size='6' color='#E74C3C'>Apto " + apto.getCodigo() + "</font><br><br>" +
                "<b>Total de reportes:</b> " + cantidadNovedades + "</div></html>");
    }

    private void configurarBoton(JButton boton, String titulo) {

        if (boton == null) return;
        if (titulo != null && !titulo.isEmpty()) {
            boton.setText(titulo);
        }

        Font fuenteNormal = new Font("Vensim Sans Mono", Font.BOLD, 14);
        Font fuenteZoom = new Font("Vensim Sans Mono", Font.BOLD, 17);

        boton.setFont(fuenteNormal);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteZoom);
                boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                boton.setFont(fuenteNormal);
            }
        });
    }
}