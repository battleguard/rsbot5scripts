package Miner;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.powerbot.game.api.methods.interactive.Players;

public class MiningGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	public static final String[] ORE_TYPES = { "Clay", "Copper", "Tin", "Iron", "Silver", "Coal", "Gold",
			"Mithril", "Adamantite", "Runite" };

	private static final DefaultListModel<String> ORE_TYPES_MODEL = new DefaultListModel<String>();
	private static final JList<String> ORE_LIST = new JList<String>(ORE_TYPES_MODEL);
	private static final JScrollPane SCROLL_PANE = new JScrollPane(ORE_LIST);

	private static final DefaultListModel<String> SELECTED_ORE_TYPES_MODEL = new DefaultListModel<String>();
	private static final JList<String> SELECT_ORE_LIST = new JList<String>(SELECTED_ORE_TYPES_MODEL);
	private static final JScrollPane SCROLL_PANE2 = new JScrollPane(SELECT_ORE_LIST);

	private static final String[] GEM_TYPES = { "Sapphire", "Emerald", "Ruby", "Diamond" };
	private static final int[] GEM_IDS = { 1623, 1621, 1619, 1617 };

	private static final DefaultListModel<String> GEMS_MODEL = new DefaultListModel<String>();
	private static final JList<String> GEMS_LIST = new JList<String>(GEMS_MODEL);
	private static final JScrollPane GEMS_SCROLL_PANE = new JScrollPane(GEMS_LIST);

	private static final DefaultListModel<String> SELECTED_GEMS_MODEL = new DefaultListModel<String>();
	private static final JList<String> SELECTED_GEMS_LIST = new JList<String>(SELECTED_GEMS_MODEL);
	private static final JScrollPane SELECTED_GEMS_SCROLL_PANE = new JScrollPane(SELECTED_GEMS_LIST);

	private static final JButton GEM_ADD_BTN = new JButton("Add");
	private static final JButton GEM_REMOVE_BTN = new JButton("Remove");

	private static final JButton ADD_BTN = new JButton("Add");
	private static final JButton REMOVE_BTN = new JButton("Remove");

	private static final JSlider MINING_AREA_SLIDER = new JSlider(JSlider.HORIZONTAL, 1, 15, 3);
	private static final JLabel MINING_AREA_LBL = new JLabel("Mining area size to search for rocks");

	private static final JCheckBox MOUSE_KEYS_BOX = new JCheckBox("Use mouse key dropping");
	private static final JCheckBox RESOURCE_DUNG_BOX = new JCheckBox("Use Dwarven Resource Dungeon");
	private static final JButton START_BTN = new JButton("Start");

	public static JPanel GEM_PANEL;
	private static GridBagConstraints c;

	public final void addToGrid(Component comp, int gridx, int gridy, int gridwidth, double weightx,
			JPanel panel) {
		c.gridx = gridx;
		c.gridy = gridy;
		c.gridwidth = gridwidth;
		c.weightx = weightx;
		panel.add(comp, c);
	}

	public static final void updateList(JList<String> selectedList, DefaultListModel<String> removing, DefaultListModel<String> adding) {
		final int[] indexes = selectedList.getSelectedIndices();
		for (int i : indexes) {
			adding.addElement(removing.get(i));
		}
		for (int i = indexes.length - 1; i >= 0; i--) {
			removing.remove(indexes[i]);
		}
	}

	public static final void setEnable(final boolean Enable, Component... comps) {
		for (Component comp : comps) {
			comp.setEnabled(Enable);
		}
	}

	public final ActionListener ALL_LISTENER = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			final Object obj = e.getSource();
			if (obj.equals(ADD_BTN)) {
				SELECTED_ORE_TYPES_MODEL.removeElement("Empty");
				updateList(ORE_LIST, ORE_TYPES_MODEL, SELECTED_ORE_TYPES_MODEL);
			} else if (obj.equals(REMOVE_BTN)) {
				updateList(SELECT_ORE_LIST, SELECTED_ORE_TYPES_MODEL, ORE_TYPES_MODEL);
			} else if (obj.equals(GEM_ADD_BTN)) {
				SELECTED_GEMS_MODEL.removeElement("None");
				updateList(GEMS_LIST, GEMS_MODEL, SELECTED_GEMS_MODEL);
			} else if (obj.equals(GEM_REMOVE_BTN)) {
				updateList(SELECTED_GEMS_LIST, SELECTED_GEMS_MODEL, GEMS_MODEL);
			} else {
				RockTimer.rockNames = new String[SELECTED_ORE_TYPES_MODEL.size()];
				for (int i = 0; i < SELECTED_ORE_TYPES_MODEL.size(); i++) {
					RockTimer.rockNames[i] = SELECTED_ORE_TYPES_MODEL.get(i);
				}

				Mine.Dropper.skiplistArray = new int[SELECTED_GEMS_MODEL.size()];
				for (int i = 0; i < SELECTED_GEMS_MODEL.size(); i++) {
					for (int j = 0; j < GEM_TYPES.length; j++) {
						if (GEM_TYPES[j].equals(SELECTED_GEMS_MODEL.get(i))) {
							Mine.Dropper.skiplistArray[i] = GEM_IDS[j];
							break;
						}
					}
				}
				Mine.Dropper.hop = MOUSE_KEYS_BOX.isSelected();
				dispose();
				Mine.guiDone = true;
			}
		}
	};

	public final ActionListener RESOURCE_LISTER = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			Mine.useResource = RESOURCE_DUNG_BOX.isSelected();
			setEnable(!Mine.useResource, GEM_ADD_BTN, GEM_REMOVE_BTN, MINING_AREA_LBL, MINING_AREA_SLIDER, MOUSE_KEYS_BOX);
			if (Mine.useResource) {
				for (int i = ORE_TYPES_MODEL.size() - 1; i >= 0; i--) {
					final String ore = (String) ORE_TYPES_MODEL.get(i);
					if (!ore.equals("Silver") && !ore.equals("Coal") && !ore.equals("Mithril")) {
						ORE_TYPES_MODEL.remove(i);
					}
				}

				Mine.updateArea = 20;
			} else {
				for (String ore : ORE_TYPES) {
					if (!ORE_TYPES_MODEL.contains(ore) && !SELECTED_ORE_TYPES_MODEL.contains(ore)) {
						ORE_TYPES_MODEL.addElement(ore);
					}
				}
				Mine.updateArea = MINING_AREA_SLIDER.getValue();
			}
		}
	};

	public final ChangeListener MINING_AREA_LISTENER = new ChangeListener() {

		@Override
		public void stateChanged(ChangeEvent paramChangeEvent) {
			Mine.updateArea = MINING_AREA_SLIDER.getValue();
		}
	};

	public static final void setButtonSizes(JButton... buttons) {
		final Dimension ButtonSize = new Dimension(50, 30);
		for (JButton button : buttons) {
			button.setPreferredSize(ButtonSize);
		}
	}

	public final JPanel createListPanel(final String Name, Component... comps) {
		final JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(Name));
		panel.setLayout(new GridBagLayout());
		for (int i = 0; i < comps.length; i++) {
			addToGrid(comps[i], i % 2, i / 2, 1, 0.5, panel);
		}
		return panel;
	}

	public MiningGUI() {
		super("Battleguard's Power Miner v1.8");

		SELECTED_ORE_TYPES_MODEL.addElement("Empty");
		SELECTED_GEMS_MODEL.addElement("None");

		for (String gem : GEM_TYPES) {
			GEMS_MODEL.addElement(gem);
		}

		for (String ore : ORE_TYPES) {
			ORE_TYPES_MODEL.addElement(ore);
		}

		setButtonSizes(ADD_BTN, REMOVE_BTN, GEM_ADD_BTN, GEM_REMOVE_BTN, START_BTN);

		RockTimer.makeMiningArea(3, Players.getLocal().getLocation());
		MINING_AREA_SLIDER.setMajorTickSpacing(1);
		MINING_AREA_SLIDER.setPaintTicks(true);
		MINING_AREA_SLIDER.setPaintLabels(true);

		final JPanel Pane = new JPanel();
		Pane.setLayout(new GridBagLayout());
		c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		final JPanel ORE_PANEL = createListPanel("Select Ores", SCROLL_PANE, SCROLL_PANE2, ADD_BTN,
				REMOVE_BTN);

		GEM_PANEL = createListPanel("Select Gems To Keep", GEMS_SCROLL_PANE, SELECTED_GEMS_SCROLL_PANE,
				GEM_ADD_BTN, GEM_REMOVE_BTN);

		c.insets = new Insets(3, 3, 3, 3);
		addToGrid(ORE_PANEL, 0, 0, 4, 1.0, Pane);
		addToGrid(GEM_PANEL, 0, 1, 4, 1.0, Pane);

		addToGrid(MINING_AREA_LBL, 0, 2, 4, 1.0, Pane);
		addToGrid(MINING_AREA_SLIDER, 0, 3, 4, 1.0, Pane);

		c.insets = new Insets(10, 10, 5, 10);

		addToGrid(MOUSE_KEYS_BOX, 0, 4, 2, 1.0, Pane);
		addToGrid(RESOURCE_DUNG_BOX, 0, 5, 2, 1.0, Pane);
		addToGrid(START_BTN, 3, 5, 1, 1.0, Pane);

		START_BTN.addActionListener(ALL_LISTENER);
		MINING_AREA_SLIDER.addChangeListener(MINING_AREA_LISTENER);
		ADD_BTN.addActionListener(ALL_LISTENER);
		REMOVE_BTN.addActionListener(ALL_LISTENER);
		GEM_ADD_BTN.addActionListener(ALL_LISTENER);
		GEM_REMOVE_BTN.addActionListener(ALL_LISTENER);
		RESOURCE_DUNG_BOX.addActionListener(RESOURCE_LISTER);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				Mine.killScript = Mine.guiDone = true;
			}
		});

		add(Pane);
		setPreferredSize(new Dimension(400, 600));
		final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(screen.width / 2 - 100, (screen.height / 2) - 200);
		pack();
		setVisible(true);
	}

}
