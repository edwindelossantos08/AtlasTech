package com.atlastech.gestionclubdeportivos.views;
import com.atlastech.gestionclubdeportivos.dao.*;
import com.atlastech.gestionclubdeportivos.models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Vistas

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
        cargarEstadisticas(); // 🔥 NO QUEDA VACÍO
    }

    // ================= INIT =================
    private void initComponents() {
        setTitle("Panel de Administración - Club Deportivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1200, 700));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));

        // ========== HEADER ==========
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(52, 73, 94));
        headerPanel.setPreferredSize(new Dimension(0, 70));

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

        // ========== SIDEBAR ==========
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

        // ========== CONTENT ==========
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        add(mainPanel);
    }

    // ================= BOTÓN SIDEBAR =================
    private JButton crearBotonMenu(String texto, ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(250, 50));
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(44, 62, 80));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(52, 73, 94));
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(new Color(44, 62, 80));
            }
        });

        btn.addActionListener(action);
        return btn;
    }

    // ================= DASHBOARD ADMIN =================
    private void cargarEstadisticas() {
        contentPanel.removeAll();

        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setBackground(new Color(236, 240, 241));

        // ===== BIENVENIDA =====
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        JLabel lblBienvenida = new JLabel(
                "<html><h1>¡Bienvenido Administrador!</h1>" +
                        "<p style='font-size:12px;color:gray;'>Usuario: "
                        + usuarioActual.getNombreUsuario() +
                        "</p><br>" +
                        "<p>Utilice el menú lateral para administrar el sistema.</p></html>"
        );

        welcomePanel.add(lblBienvenida, BorderLayout.CENTER);

        // ===== TARJETAS =====
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(new Color(236, 240, 241));

        statsPanel.add(crearTarjeta("Socios", String.valueOf(socioDAO.contarSocios()), "👥", new Color(52, 152, 219)));
        statsPanel.add(crearTarjeta("Reservas", String.valueOf(reservaDAO.contarReservas()), "📅", new Color(155, 89, 182)));
        statsPanel.add(crearTarjeta("Pagos", String.valueOf(pagoDAO.contarPagos()), "💳", new Color(46, 204, 113)));

        mainContent.add(statsPanel, BorderLayout.NORTH);
        mainContent.add(welcomePanel, BorderLayout.CENTER);

        contentPanel.add(mainContent);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private JPanel crearTarjeta(String titulo, String valor, String emoji, Color color) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));

        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 26));
        lblValor.setForeground(color);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTitulo.setForeground(Color.GRAY);

        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(lblEmoji);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(lblValor);
        panel.add(lblTitulo);

        tarjeta.add(panel, BorderLayout.CENTER);
        return tarjeta;
    }

    // ================= ABRIR VENTANAS =================
    private void abrirGestionSocios() { new GestionSocios(this).setVisible(true); }

    private void abrirGestionUsuarios() {
        try {
            new GestionUsuarios(this).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirGestionMembresias() { new GestionMembresias(this).setVisible(true); }

    private void abrirGestionInstalaciones() { new GestionInstalaciones(this).setVisible(true); }

    private void abrirGestionPagos() { new GestionPagos(this).setVisible(true); }

    private void abrirGestionEntrenadores() { new GestionEntrenadores(this).setVisible(true); }

    private void abrirGestionDeportes() { new GestionDeportes(this).setVisible(true); }

    private void abrirGestionSuscripciones() { new GestionSuscripciones(this).setVisible(true); }

    private void abrirGestionReservas() { new GestionReservas(this).setVisible(true); }

    // ================= LOGOUT =================
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Desea cerrar sesión?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            dispose();
        }
    }
}
