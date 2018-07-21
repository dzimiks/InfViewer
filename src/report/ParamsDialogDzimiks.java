package report;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class ParamsDialogDzimiks extends JDialog {
	private Integer id = null;
	private JTextField txtUstanova = new JTextField(20);

	public ParamsDialogDzimiks() {
		super((JFrame) null, "Params dialog", true);
		setLayout(new GridLayout(2, 1));
		setSize(400, 300);
		setLocationRelativeTo(null);

		JLabel lblId = new JLabel("PS_IDENTIFIKATOR: ");
		JPanel panId = new JPanel(new FlowLayout(FlowLayout.LEFT));
		panId.add(lblId);
		panId.add(txtUstanova);

		add(panId);

		JButton btnOk = new JButton("Submit");
		JPanel panBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		panBtn.add(btnOk);
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				id = Integer.parseInt(txtUstanova.getText());
				setVisible(false);
			}
		});

		add(panBtn);
	}

	public Integer getId() {
		return id;
	}
}
