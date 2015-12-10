package LootHelper;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;

		
	public static void main(String[] args) {
		new Gui();
	}

	public GridBagConstraints c;

	public static final void addToGrid(GridBagConstraints c, Component comp, int gridx, int gridy, int gridwidth, double weightx,
			Container pane) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.weightx = weightx;
		pane.add(comp, c);
	}

	private static final JLabel[] QualityLabels = { new JLabel("Poor"), new JLabel("Common"),
			new JLabel("Uncommon"), new JLabel("Rare"), new JLabel("Epic"), new JLabel("Legendary") };
	private static final JTextField[] QualityValues = { new JTextField("0"), new JTextField("100"),
			new JTextField("1000"), new JTextField("2000"), new JTextField("10000"), new JTextField("50000") };

	private static JButton ALWAYS_SHOW_ADD_BTN = Storage.getImage("add");
	private static JButton ALWAYS_SHOW_RMV_BTN = Storage.getImage("remove");
	private static final JTextField ALWAYS_SHOW_TXT_FIELD = new JTextField("");
	public static final DefaultListModel<String> ALWAYS_SHOW_MODEL = new DefaultListModel<String>();
	private static final JList<String> ALWAYS_SHOW_LIST = new JList<String>(ALWAYS_SHOW_MODEL);
	private static final JScrollPane ALWAYS_SHOW_SCROLL_PANE = new JScrollPane(ALWAYS_SHOW_LIST);

	private static JButton NEVER_SHOW_ADD_BTN = Storage.getImage("add");
	private static JButton NEVER_SHOW_RMV_BTN  = Storage.getImage("remove");
	private static final JTextField NEVER_SHOW_TXT_FIELD = new JTextField("");
	public static final DefaultListModel<String> NEVER_SHOW_MODEL = new DefaultListModel<String>();
	private static final JList<String> NEVER_SHOW_LIST = new JList<String>(NEVER_SHOW_MODEL);
	private static final JScrollPane NEVER_SHOW_SCROLL_PANE = new JScrollPane(NEVER_SHOW_LIST);

	public static int MIN_PRICE = 0;
	

	
	
	
	
	public final JPanel createPanel(JScrollPane list, final JList<String> jList,
			final DefaultListModel<String> listModel, final JTextField entryField, String header,
			final JButton addBtn, final JButton rmvBtn) {
		list.setPreferredSize(new Dimension(200, 80));
		JPanel pane = new JPanel();
		pane.setBackground(Color.BLACK);
		pane.setLayout(new GridBagLayout());
		pane.setBorder(BorderFactory.createTitledBorder(null, header, TitledBorder.DEFAULT_JUSTIFICATION,
				TitledBorder.DEFAULT_POSITION, null, Color.WHITE));
		
	
		addBtn.setPreferredSize(new Dimension(80, 30));
		rmvBtn.setPreferredSize(new Dimension(80, 30));

		c.insets = new Insets(5, 5, 5, 5);
		addToGrid(c, entryField, 0, 0, 1, 0.9, pane);
		addToGrid(c, addBtn, 1, 0, 1, 0.1, pane);
		c.gridheight = 2;
		addToGrid(c, list, 0, 1, 1, 0.9, pane);
		c.gridheight = 1;
		addToGrid(c, rmvBtn, 1, 1, 1, 0.1, pane);

		ActionListener LIST_LISTENER = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (e.getSource().equals(addBtn)) {
					listModel.addElement(entryField.getText());
					entryField.setText("");
				} else {
					final int[] indexes = jList.getSelectedIndices();
					for (int i = indexes.length - 1; i >= 0; i--) {
						listModel.remove(indexes[i]);
					}
				}

			}
		};
		addBtn.addActionListener(LIST_LISTENER);
		rmvBtn.addActionListener(LIST_LISTENER);

		return pane;
	}

	public final ActionListener START_LISTENER = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < QualityValues.length; i++) {
				Quality.values()[i].setPrice(Integer.parseInt(QualityValues[i].getText()));
			}
			MIN_PRICE = Integer.parseInt(priceTxt.getText());
			dispose();
		}
	};
	


	private static final JTextField priceTxt = new JTextField("0");

	public Gui() {
		QualityValues[0].setEditable(false);
		
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		setBackground(Color.BLACK);

		ALWAYS_SHOW_SCROLL_PANE.setPreferredSize(new Dimension(0, 80));

		JLabel header = new JLabel("Edit price indexes for Quality Values Here ");
		header.setForeground(Color.WHITE);
		c.insets = new Insets(10, 15, 10, 5);
		addToGrid(c, header, 0, 0, 2, 1.0, this);

		JPanel qualityPane = new JPanel();
		qualityPane.setForeground(Color.WHITE);
		qualityPane.setBackground(Color.BLACK);
		qualityPane.setLayout(new GridBagLayout());
		for (int i = 0; i < QualityLabels.length; i++) {
			QualityLabels[i].setBackground(Color.BLACK);
			QualityLabels[i].setForeground(Quality.values()[i].color());
			c.insets = new Insets(0, 30, 0, 0);
			addToGrid(c, QualityLabels[i], 0, i, 1, 1.0, qualityPane);
			c.insets = new Insets(0, 0, 0, 100);
			QualityValues[i].setPreferredSize(new Dimension(70, 20));
			addToGrid(c, QualityValues[i], 1, i, 1, 0.0, qualityPane);
		}
		c.insets = new Insets(0, 0, 0, 0);
		addToGrid(c, qualityPane, 0, 1, 2, 1.0, this);

		c.insets = new Insets(10, 5, 10, 5);
		JLabel priceLbl = new JLabel("Only Show loot with price greater than :");
		priceLbl.setForeground(Color.WHITE);
		priceTxt.setPreferredSize(new Dimension(55, 20));
		addToGrid(c, priceLbl, 0, 2, 1, 1.0, this);
		addToGrid(c, priceTxt, 1, 2, 1, 1.0, this);

		addToGrid(c, 
				createPanel(ALWAYS_SHOW_SCROLL_PANE, ALWAYS_SHOW_LIST, ALWAYS_SHOW_MODEL,
						ALWAYS_SHOW_TXT_FIELD, "Enter id/name of items to always show", ALWAYS_SHOW_ADD_BTN,
						ALWAYS_SHOW_RMV_BTN), 0, 3, 2, 1.0, this);
		addToGrid(c, 
				createPanel(NEVER_SHOW_SCROLL_PANE, NEVER_SHOW_LIST, NEVER_SHOW_MODEL, NEVER_SHOW_TXT_FIELD,
						"Enter id/name of items to never show", NEVER_SHOW_ADD_BTN, NEVER_SHOW_RMV_BTN), 0,
				4, 2, 1.0, this);

		
		addToGrid(c, new LootHelper.GUI.ToggleLoot(), 0, 5, 2, 1.0, this);
		
		JButton startBtn = new JButton("Start");
		startBtn.addActionListener(START_LISTENER);
		addToGrid(c, startBtn, 1, 6, 1, 1.0, this);


		// only show list  
		// always show list loot color
		// save/load button
		// start

		getContentPane().setBackground(Color.BLACK);

		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screen.width / 2 - 100, (screen.height / 2) - 200);
		pack();
		setVisible(true);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
	}

}
