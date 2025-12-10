package com.atlastech.gestionclubdeportivos.views;

import com.atlastech.gestionclubdeportivos.dao.UsuarioDAO;
import com.atlastech.gestionclubdeportivos.models.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//Pantalla de Login del Sistema
 // @author AtlasTech

public class Login extends JFrame {
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnCancelar;
    private UsuarioDAO usuarioDAO;
    
    public Login() {
        usuarioDAO = new UsuarioDAO();
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        setTitle("Sistema de Gestión Club Deportivo - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 550);
        setResizable(false);
        
        // Panel principal con gradiente
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(41, 128, 185);
                Color color2 = new Color(109, 213, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(null);
        
        // Panel del formulario
        JPanel formPanel = new JPanel();
        formPanel.setBackground(Color.WHITE);
        formPanel.setBounds(50, 80, 350, 380);
        formPanel.setLayout(null);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Título
        JLabel lblTitulo = new JLabel("BIENVENIDO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(41, 128, 185));
        lblTitulo.setBounds(90, 20, 200, 35);
        formPanel.add(lblTitulo);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Club Deportivo AtlasTech");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.GRAY);
        lblSubtitulo.setBounds(80, 55, 200, 20);
        formPanel.add(lblSubtitulo);
        
        // Icono de usuario
        JLabel iconoUsuario = new JLabel("👤");
        iconoUsuario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
        iconoUsuario.setBounds(150, 85, 60, 60);
        formPanel.add(iconoUsuario);
        
        // Label Username
        JLabel lblUsername = new JLabel("Usuario:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 13));
        lblUsername.setBounds(30, 160, 100, 25);
        formPanel.add(lblUsername);
        
        // Campo Username
        txtUsername = new JTextField();
        txtUsername.setBounds(30, 185, 290, 35);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtUsername);
        
        // Label Password
        JLabel lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 13));
        lblPassword.setBounds(30, 230, 100, 25);
        formPanel.add(lblPassword);
        
        // Campo Password
        txtPassword = new JPasswordField();
        txtPassword.setBounds(30, 255, 290, 35);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(txtPassword);
        
        // Botón Login
        btnLogin = new JButton("INICIAR SESIÓN");
        btnLogin.setBounds(30, 310, 290, 40);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 13));
        btnLogin.setBackground(new Color(46, 204, 113));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> login());
        formPanel.add(btnLogin);
        
        // Enter key para login
        txtPassword.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    login();
                }
            }
        });
        
        mainPanel.add(formPanel);
        add(mainPanel);
        
        // Efecto hover en botones
        btnLogin.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btnLogin.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(MouseEvent e) {
                btnLogin.setBackground(new Color(46, 204, 113));
            }
        });
    }
    
    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese usuario y contraseña",
                "Campos Vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Autenticar usuario
        Usuario usuario = usuarioDAO.autenticar(username, password);
        
        if (usuario != null) {
            JOptionPane.showMessageDialog(this,
                "¡Bienvenido " + usuario.getNombreUsuario() + "!",
                "Login Exitoso",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Abrir dashboard según tipo de usuario
            if (usuario.isAdministrador()) {
                new DashboardAdmin(usuario).setVisible(true);
            } else if (usuario.isSocio()) {
                new DashboardSocio(usuario).setVisible(true);
            }
            
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this,
                "Usuario o contraseña incorrectos",
                "Error de Autenticación",
                JOptionPane.ERROR_MESSAGE);
            txtPassword.setText("");
            txtUsername.requestFocus();
        }
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}