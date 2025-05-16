import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class EditorSegmentos extends JFrame {

    private JPanel painelDesenho;
    private Color corAtual = Color.BLACK;
    private Point pontoInicial = null;
    private ArrayList<Segmento> segmentos = new ArrayList<>();

    // Janela de recorte
    private final double xmin = 50, ymin = 10, xmax = 200, ymax = 200;
    private CohenSutherland recorte = new CohenSutherland(xmin, ymin, xmax, ymax);

    public EditorSegmentos() {
        super("Editor de Segmentos");

        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        painelDesenho = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Desenha janela de recorte
                g.setColor(Color.LIGHT_GRAY);
                g.drawRect((int) xmin, (int) ymin, (int) (xmax - xmin), (int) (ymax - ymin));

                // Desenha segmentos recortados
                for (Segmento s : segmentos) {
                    // Cria cópias dos pontos para recorte
                    Point2D.Double p1 = new Point2D.Double(s.getP1().x, s.getP1().y);
                    Point2D.Double p2 = new Point2D.Double(s.getP2().x, s.getP2().y);

                    if (recorte.recortarLinha(p1, p2)) {
                        g.setColor(s.getCor());
                        g.drawLine((int) p1.x, (int) p1.y, (int) p2.x, (int) p2.y);
                    }
                }
            }
        };

        painelDesenho.setBackground(Color.WHITE);
        add(painelDesenho, BorderLayout.CENTER);

        painelDesenho.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (pontoInicial == null) {
                    pontoInicial = e.getPoint();
                } else {
                    segmentos.add(new Segmento(
                            new Point2D.Double(pontoInicial.x, pontoInicial.y),
                            new Point2D.Double(e.getX(), e.getY()),
                            corAtual));
                    pontoInicial = null;
                    repaint();
                }
            }
        });

        JPanel painelControle = new JPanel();

        // Botões de cor
        JButton btnPreto = new JButton("Preto");
        btnPreto.addActionListener(e -> corAtual = Color.BLACK);
        JButton btnVermelho = new JButton("Vermelho");
        btnVermelho.addActionListener(e -> corAtual = Color.RED);
        JButton btnVerde = new JButton("Verde");
        btnVerde.addActionListener(e -> corAtual = Color.GREEN);
        JButton btnAmarelo = new JButton("Amarelo");
        btnAmarelo.addActionListener(e -> corAtual = Color.YELLOW);

        painelControle.add(new JLabel("Cor:"));
        painelControle.add(btnPreto);
        painelControle.add(btnVermelho);
        painelControle.add(btnVerde);
        painelControle.add(btnAmarelo);

        // Botões de transformação
        JButton btnTranslacao = new JButton("Translação +10px");
        btnTranslacao.addActionListener(e -> {
            for (Segmento s : segmentos) {
                s.translacao10();
            }
            repaint();
        });

        JButton btnRotacao = new JButton("Rotação 90°");
        btnRotacao.addActionListener(e -> {
            for (Segmento s : segmentos) {
                s.rotacao90();
            }
            repaint();
        });

        JButton btnEscala = new JButton("Escala x2");
        btnEscala.addActionListener(e -> {
            for (Segmento s : segmentos) {
                s.escalaDobro();
            }
            repaint();
        });

        painelControle.add(btnTranslacao);
        painelControle.add(btnRotacao);
        painelControle.add(btnEscala);

        add(painelControle, BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            EditorSegmentos editor = new EditorSegmentos();
            editor.setVisible(true);
        });
    }
}

