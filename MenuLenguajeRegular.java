import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class MenuLenguajeRegular extends JFrame {

	private JTextField txtPatron,
					   txtCadena,
					   txtNueva;
	private JLabel lbPatron,
				   lbCadena,
				   lbNueva,
				   lbNombre;
	private JButton btnEnter;
	
	public MenuLenguajeRegular() {
		super("Reemplazador con Expresiones Regulares");
		
		this.setSize(600, 800);
		this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFocusable(true);
		this.setResizable(false);
		this.getContentPane().setBackground(new Color(224,224,224));
		LineBorder border = new LineBorder(Color.BLACK, 1);
		this.lbNombre = new JLabel("Milton Eduardo Barroso Ramírez.");
		this.lbNombre.setBounds(10, -5, 300, 50);
		this.lbNombre.setFont(new Font("Century Gothic",Font.PLAIN,15));
		this.add(this.lbNombre);
		
		this.lbNombre = new JLabel("A01634505.");
		this.lbNombre.setBounds(10, 15, 300, 50);
		this.lbNombre.setFont(new Font("Century Gothic",Font.PLAIN,15));
		this.add(this.lbNombre);
		
		this.txtPatron = new JTextField(30);
		this.txtPatron.setBounds(100, 300, 400, 30);
		this.txtPatron.setFont(new Font("Century Gothic",Font.PLAIN,20));
		this.txtPatron.setBorder(border);
		this.add(this.txtPatron);
		this.lbPatron = new JLabel("Patrón");
		this.lbPatron.setBounds(100, 250, 300, 50);
		this.lbPatron.setFont(new Font("Century Gothic",Font.PLAIN,30));
		this.add(this.lbPatron);
		
		this.lbCadena = new JLabel("Cadena original");
		this.lbCadena.setBounds(100, 370, 370, 50);
		this.lbCadena.setFont(new Font("Century Gothic",Font.PLAIN,30));
		this.add(this.lbCadena);
		this.txtCadena = new JTextField(30);
		this.txtCadena.setFont(new Font("Century Gothic",Font.PLAIN,20));
		this.txtCadena.setBorder(border);
		this.txtCadena.setBounds(100, 420, 400, 30);
		this.add(this.txtCadena);
		
		this.lbNueva = new JLabel("Cadena a reemplazar");
		this.lbNueva.setBounds(100, 490, 370, 50);
		this.lbNueva.setFont(new Font("Century Gothic",Font.PLAIN,30));
		this.add(this.lbNueva);
		this.txtNueva = new JTextField(30);
		this.txtNueva.setFont(new Font("Century Gothic",Font.PLAIN,20));
		this.txtNueva.setBorder(border);
		this.txtNueva.setBounds(100, 540, 400, 30);
		this.add(this.txtNueva);
		
		JLabel titulo1=new JLabel("Reemplazador con");
		titulo1.setFont(new Font("Aharoni",Font.ITALIC,50));
		titulo1.setForeground(new Color(102,178,255));
		titulo1.setBounds(10,70,450,100);
		this.add(titulo1);
		
		JLabel titulo2=new JLabel("Expresiones Regulares");
		titulo2.setFont(new Font("Aharoni",Font.ITALIC,50));
		titulo2.setForeground(new Color(102,178,255));
		titulo2.setBounds(50,130,550,100);
		this.add(titulo2);
		
		this.btnEnter = new JButton("Procesar");
		this.btnEnter.setBounds(200, 650, 200, 50);
		this.btnEnter.setBorder(border);
		this.btnEnter.setBackground(new Color(102,178,255));
		this.btnEnter.setForeground(Color.WHITE);
		this.btnEnter.setFont(new Font("Century Gothic",Font.BOLD,20));
		this.btnEnter.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				Automata aut = new Automata(txtPatron.getText(), txtCadena.getText().toCharArray(), txtNueva.getText().toCharArray());
				showDialog(aut.getFinal());
			}
		});
		this.add(this.btnEnter);
		
		this.setVisible(true);
	}
	
	public void showDialog(char[] cadena) {
		JOptionPane.showMessageDialog(this, "Nueva cadena: "+String.copyValueOf(cadena),"Resultado",JOptionPane.INFORMATION_MESSAGE);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		MenuLenguajeRegular menu = new MenuLenguajeRegular();
	}
	
}
