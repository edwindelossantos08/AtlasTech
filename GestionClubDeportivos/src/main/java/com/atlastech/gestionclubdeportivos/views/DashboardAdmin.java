// DASHBOARDADMIN.JAVA - COMPLETO Y CORREGIDO

package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.*;
import com.atlastech.gestionclubdeportivos.models.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Asegúrate de importar las vistas que se abren desde el dashboard
import com.atlastech.gestionclubdeportivos.views.GestionReservas;
import com.atlastech.gestionclubdeportivos.views.GestionSuscripciones;

public class DashboardAdmin extends JFrame {

    private Usuario usuarioActual;
    private JPanel contentPanel;
    private JLabel lblWelcome;

    private SocioDAO socioDAO;
    private PagoDAO pagoDAO;
    private ReservaDAO reservaDAO;

    public DashboardAdmin(Usuario usuario) {
        this.usuarioActual = usuario;
        this.socioDAO = new SocioDAO();
        this.pagoDAO = new PagoDAO();
        this.reservaDAO = new ReservaDAO();

        initComponents();
        setLocationRelativeTo(null);
        cargarEstadisticas();
    }

    private void initComponents() {
        setTitle("Panel de Administración - Club Deportivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel("  🏆 CLUB DEPORTIVO - ADMINISTRACIÓN");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        lblWelcome = new JLabel("👤 " + usuarioActual.getNombreUsuario() + "  ");
        lblWelcome.setFont(new Font("Arial", Font.PLAIN, 14));
        lblWelcome.setForeground(Color.WHITE);

        JButton btnLogout = new JButton("Cerrar Sesión");
        btnLogout.setBackground(new Color(231, 76, 60));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> logout());

        userPanel.add(lblWelcome);
        userPanel.add(btnLogout);
        headerPanel.add(userPanel, BorderLayout.EAST);

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));

        sidebar.add(crearBotonMenu("📊 Dashboard", e -> cargarEstadisticas()));
        sidebar.add(crearBotonMenu("👥 Socios", e -> abrirGestionSocios()));
        sidebar.add(crearBotonMenu("🔐 Usuarios", e -> abrirGestionUsuarios()));
        sidebar.add(crearBotonMenu("💳 Membresías", e -> abrirGestionMembresias()));
        sidebar.add(crearBotonMenu("📝 Suscripciones", e -> abrirGestionSuscripciones()));
        sidebar.add(crearBotonMenu("🏟️ Instalaciones", e -> abrirGestionInstalaciones()));
        sidebar.add(crearBotonMenu("📅 Reservas", e -> abrirGestionReservas()));
        sidebar.add(crearBotonMenu("💰 Pagos", e -> abrirGestionPagos()));
        sidebar.add(crearBotonMenu("👨‍🏋 Entrenadores", e -> abrirGestionEntrenadores()));
        sidebar.add(crearBotonMenu("⚽ Deportes", e -> abrirGestionDeportes()));

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    private JButton crearBotonMenu(String texto, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(52, 73, 94));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(44, 62, 80));
            }
        });

        btn.addActionListener(action);
        return btn;
    }

    private void cargarEstadisticas() {
        contentPanel.removeAll();
        // Aquí puedes mantener el código de tarjetas de estadísticas
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void abrirGestionSocios() { new GestionSocios(this).setVisible(true); }
    private void abrirGestionUsuarios() {
    try {
        new GestionUsuarios(this).setVisible(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al abrir Gestión de Usuarios: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        e.printStackTrace(); // Esto mostrará el error en la consola del IDE
    }
}

    
    private void abrirGestionMembresias() { new GestionMembresias(this).setVisible(true); }
    
    private void abrirGestionInstalaciones() { new GestionInstalaciones(this).setVisible(true); }
    
    private void abrirGestionPagos() {
    try {
        GestionPagos ventanaPagos = new GestionPagos(this);
        ventanaPagos.setVisible(true);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al abrir Gestión de Pagos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    private void abrirGestionEntrenadores() { new GestionEntrenadores(this).setVisible(true); }
    
    private void abrirGestionDeportes() { new GestionDeportes(this).setVisible(true); }

    // MÉTODOS CORREGIDOS
    private void abrirGestionSuscripciones() {
        try {
            GestionSuscripciones ventanaSuscripciones = new GestionSuscripciones(this);
            ventanaSuscripciones.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir Gestión de Suscripciones: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirGestionReservas() {
        try {
            GestionReservas ventanaReservas = new GestionReservas(this);
            ventanaReservas.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al abrir Gestión de Reservas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            this.dispose();
        }
    }
}
