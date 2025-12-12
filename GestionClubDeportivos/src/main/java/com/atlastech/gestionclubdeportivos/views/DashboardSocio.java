
package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.models.*;
import com.atlastech.gestionclubdeportivos.dao.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;


 //Dashboard para Socios
 // @author AtlasTech
 
public class DashboardSocio extends JFrame {
    
    private Usuario usuarioActual;
    private Socio socioActual;
    private JPanel contentPanel;
    
    private SocioDAO socioDAO;
    private ReservaDAO reservaDAO;
    private PagoDAO pagoDAO;
    
    public DashboardSocio(Usuario usuario) {
        this.usuarioActual = usuario;
        this.socioDAO = new SocioDAO();
        this.reservaDAO = new ReservaDAO();
        this.pagoDAO = new PagoDAO();
        
        // Cargar datos del socio
        if (usuario.getIdSocio() != null) {
            this.socioActual = socioDAO.obtenerSocioPorId(usuario.getIdSocio());
        }
        
        initComponents();
        setLocationRelativeTo(null);
        cargarDashboard();
    }
    
    private void initComponents() {
        setTitle("Mi Panel - Club Deportivo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 600));
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(236, 240, 241));
        
        // ========== HEADER ==========
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(41, 128, 185));
        headerPanel.setPreferredSize(new Dimension(0, 70));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel lblTitle = new JLabel("  🏆 MI PANEL - CLUB DEPORTIVO");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);
        headerPanel.add(lblTitle, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        
        String nombreCompleto = socioActual != null ? socioActual.getNombreCompleto() : usuarioActual.getNombreUsuario();
        JLabel lblWelcome = new JLabel("👤 " + nombreCompleto + "  ");
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
        sidebar.setBackground(new Color(52, 73, 94));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        sidebar.add(crearBotonMenu("🏠 Inicio", e -> cargarDashboard()));
        sidebar.add(crearBotonMenu("📅 Mis Reservas", e -> verMisReservas()));
        sidebar.add(crearBotonMenu("💳 Mis Pagos", e -> verMisPagos()));
        sidebar.add(crearBotonMenu("👤 Mi Perfil", e -> verMiPerfil()));
        sidebar.add(crearBotonMenu("🏟️ Instalaciones", e -> verInstalaciones()));
        
        // ========== CONTENT PANEL ==========
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(236, 240, 241));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);
    }
    
    private JButton crearBotonMenu(String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setMaximumSize(new Dimension(220, 50));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 73, 94));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(41, 128, 185));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(52, 73, 94));
            }
        });
        
        btn.addActionListener(action);
        return btn;
    }
    
    private void cargarDashboard() {
        contentPanel.removeAll();
        
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setBackground(new Color(236, 240, 241));
        
        // Panel de bienvenida
        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(Color.WHITE);
        welcomePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        String info = socioActual != null ? 
            String.format("<html><h1>¡Bienvenido, %s!</h1>" +
                "<p style='font-size: 12px; color: gray;'>Email: %s | Teléfono: %s</p>" +
                "<p style='font-size: 12px; color: gray;'>Miembro desde: %s | Categoría: %s</p>" +
                "<br><p>Utilice el menú lateral para acceder a las diferentes opciones.</p></html>",
                socioActual.getNombreCompleto(),
                socioActual.getEmail(),
                socioActual.getTelefono(),
                socioActual.getFechaRegistro(),
                socioActual.obtenerCategoria()) :
            "<html><h1>¡Bienvenido!</h1><p>Complete su perfil de socio para acceder a todas las funcionalidades.</p></html>";
        
        JLabel lblWelcome = new JLabel(info);
        welcomePanel.add(lblWelcome, BorderLayout.CENTER);
        
        // Panel de estadísticas rápidas
        if (socioActual != null) {
            JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
            statsPanel.setBackground(new Color(236, 240, 241));
            
            List<Reserva> misReservas = reservaDAO.obtenerReservasPorSocio(socioActual.getId());
            
            statsPanel.add(crearTarjeta("Mis Reservas", String.valueOf(misReservas.size()), 
                new Color(52, 152, 219), "📅"));
            statsPanel.add(crearTarjeta("Estado", socioActual.isEstado() ? "Activo" : "Inactivo", 
                new Color(46, 204, 113), "✓"));
            statsPanel.add(crearTarjeta("Antigüedad", socioActual.getMesesAntiguedad() + " meses", 
                new Color(155, 89, 182), "📆"));
            
            mainContent.add(statsPanel, BorderLayout.NORTH);
        }
        
        mainContent.add(welcomePanel, BorderLayout.CENTER);
        
        contentPanel.add(mainContent, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private JPanel crearTarjeta(String titulo, String valor, Color color, String emoji) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBackground(Color.WHITE);
        tarjeta.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(189, 195, 199)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel lblEmoji = new JLabel(emoji);
        lblEmoji.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Arial", Font.BOLD, 28));
        lblValor.setForeground(color);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblTitulo.setForeground(Color.GRAY);
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        contentPanel.add(lblEmoji);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(lblValor);
        contentPanel.add(lblTitulo);
        
        tarjeta.add(contentPanel, BorderLayout.CENTER);
        return tarjeta;
    }
    
    private void verMisReservas() {
        if (socioActual == null) {
            JOptionPane.showMessageDialog(this, "No se encontró información del socio", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(new Color(236, 240, 241));
        
        JLabel lblTitulo = new JLabel("📅 MIS RESERVAS");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(lblTitulo, BorderLayout.NORTH);
        
        // Tabla de reservas
        String[] columnas = {"ID", "Instalación", "Fecha", "Hora Inicio", "Hora Fin", "Estado", "Costo"};
        DefaultTableModel model = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        List<Reserva> reservas = reservaDAO.obtenerReservasPorSocio(socioActual.getId());
        for (Reserva r : reservas) {
            model.addRow(new Object[]{
                r.getId(),
                r.getIdInstalacion(),
                r.getFecha(),
                r.getHoraInicio(),
                r.getHoraFin(),
                r.getEstado(),
                "$" + r.getCosto()
            });
        }
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
   private void verMisPagos() {
    if (socioActual == null) {
        JOptionPane.showMessageDialog(this, "No se encontró información del socio",
                "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    MisPagos ventanaPagos = new MisPagos(socioActual);
    ventanaPagos.setVisible(true);
}

    private void verMiPerfil() {
        if (socioActual == null) {
            JOptionPane.showMessageDialog(this, "No se encontró información del socio", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        contentPanel.removeAll();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        String perfil = String.format(
            "<html><h2>👤 MI PERFIL</h2><br>" +
            "<table cellpadding='5'>" +
            "<tr><td><b>Nombre Completo:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Email:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Teléfono:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Dirección:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Fecha Nacimiento:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Edad:</b></td><td>%d años</td></tr>" +
            "<tr><td><b>Categoría:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Fecha Registro:</b></td><td>%s</td></tr>" +
            "<tr><td><b>Estado:</b></td><td>%s</td></tr>" +
            "</table></html>",
            socioActual.getNombreCompleto(),
            socioActual.getEmail(),
            socioActual.getTelefono(),
            socioActual.getDireccion() != null ? socioActual.getDireccion() : "No especificado",
            socioActual.getFechaNacimiento(),
            socioActual.calcularEdad(),
            socioActual.obtenerCategoria(),
            socioActual.getFechaRegistro(),
            socioActual.isEstado() ? "Activo" : "Inactivo"
        );
        
        JLabel lblPerfil = new JLabel(perfil);
        panel.add(lblPerfil, BorderLayout.CENTER);
        
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
    
    private void verInstalaciones() {
        JOptionPane.showMessageDialog(this, "Funcionalidad en desarrollo", 
            "Información", JOptionPane.INFORMATION_MESSAGE);
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