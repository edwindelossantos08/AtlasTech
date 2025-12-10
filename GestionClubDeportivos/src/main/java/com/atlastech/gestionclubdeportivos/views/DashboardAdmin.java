package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.models.Usuario;
import com.atlastech.gestionclubdeportivos.dao.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


 // Dashboard Principal para Administradores
 // @author AtlasTech
 
public class DashboardAdmin extends JFrame {
    
    private Usuario usuarioActual;
    private JPanel contentPanel;
    private JLabel lblWelcome;
    
    // DAOs para estadísticas
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
        
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== HEADER ==========
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
        
        // ========== SIDEBAR ==========
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(44, 62, 80));
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Botones del menú
        sidebar.add(crearBotonMenu("📊 Dashboard", e -> cargarEstadisticas()));
        sidebar.add(crearBotonMenu("👥 Socios", e -> abrirGestionSocios()));
        sidebar.add(crearBotonMenu("🔐 Usuarios", e -> abrirGestionUsuarios()));
        sidebar.add(crearBotonMenu("💳 Membresías", e -> abrirGestionMembresias()));
        sidebar.add(crearBotonMenu("📝 Suscripciones", e -> abrirGestionSuscripciones()));
        sidebar.add(crearBotonMenu("🏟️ Instalaciones", e -> abrirGestionInstalaciones()));
        sidebar.add(crearBotonMenu("📅 Reservas", e -> abrirGestionReservas()));
        sidebar.add(crearBotonMenu("💰 Pagos", e -> abrirGestionPagos()));
        sidebar.add(crearBotonMenu("👨‍🏫 Entrenadores", e -> abrirGestionEntrenadores()));
        sidebar.add(crearBotonMenu("⚽ Deportes", e -> abrirGestionDeportes()));
        
        // ========== CONTENT PANEL ==========
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
    
    private void cargarEstadisticas() {
        contentPanel.removeAll();
        
        JPanel statsPanel = new JPanel(new GridLayout(2, 4, 20, 20));
        statsPanel.setBackground(new Color(236, 240, 241));
        
        // Obtener estadísticas
        int totalSocios = socioDAO.contarSociosActivos();
        int totalPagos = pagoDAO.contarPagosRealizados();
        int pagosPendientes = pagoDAO.contarPagosPendientes();
        
        // Tarjetas de estadísticas
        statsPanel.add(crearTarjetaEstadistica("Socios Activos", String.valueOf(totalSocios), 
            new Color(52, 152, 219), "👥"));
        statsPanel.add(crearTarjetaEstadistica("Pagos Realizados", String.valueOf(totalPagos), 
            new Color(46, 204, 113), "💰"));
        statsPanel.add(crearTarjetaEstadistica("Pagos Pendientes", String.valueOf(pagosPendientes), 
            new Color(241, 196, 15), "⏳"));
        statsPanel.add(crearTarjetaEstadistica("Reservas Hoy", "0", 
            new Color(155, 89, 182), "📅"));
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        infoPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblInfo = new JLabel("<html><h2>Bienvenido al Sistema de Gestión</h2>" +
            "<p>Utilice el menú lateral para acceder a las diferentes secciones del sistema.</p>" +
            "<br><p><b>Funcionalidades disponibles:</b></p>" +
            "<ul>" +
            "<li>Gestión completa de socios y usuarios</li>" +
            "<li>Administración de membresías y suscripciones</li>" +
            "<li>Control de instalaciones y reservas</li>" +
            "<li>Seguimiento de pagos y finanzas</li>" +
            "<li>Gestión de entrenadores y deportes</li>" +
            "</ul></html>");
        infoPanel.add(lblInfo, BorderLayout.CENTER);
        
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setBackground(new Color(236, 240, 241));
        mainContent.add(statsPanel, BorderLayout.NORTH);
        mainContent.add(infoPanel, BorderLayout.CENTER);
        
        contentPanel.add(mainContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel crearTarjetaEstadistica(String titulo, String valor, Color color, String emoji) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 36));
        lblValor.setForeground(color);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblTitulo.setForeground(Color.GRAY);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(lblEmoji, BorderLayout.WEST);
        topPanel.add(lblValor, BorderLayout.CENTER);
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(topPanel, BorderLayout.NORTH);
        contentPanel.add(lblTitulo, BorderLayout.SOUTH);
        
        tarjeta.add(contentPanel, BorderLayout.CENTER);
        return tarjeta;
    }
    
    // Métodos para abrir las diferentes gestiones
    private void abrirGestionSocios() {
        new GestionSocios(this).setVisible(true);
    }
    
    private void abrirGestionUsuarios() {
        new GestionUsuarios(this).setVisible(true);
    }
    
    private void abrirGestionMembresias() {
        new GestionMembresias(this).setVisible(true);
    }
    
    private void abrirGestionSuscripciones() {
        new GestionSuscripciones(this).setVisible(true);
    }
    
    private void abrirGestionInstalaciones() {
        new GestionInstalaciones(this).setVisible(true);
    }
    
    private void abrirGestionReservas() {
        new GestionReservas(this).setVisible(true);
    }
    
    private void abrirGestionPagos() {
        new GestionPagos(this).setVisible(true);
    }
    
    private void abrirGestionEntrenadores() {
        new GestionEntrenadores(this).setVisible(true);
    }
    
    private void abrirGestionDeportes() {
        new GestionDeportes(this).setVisible(true);
    }
    
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cerrar sesión??",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            this.dispose();
        }
    }
    
}
