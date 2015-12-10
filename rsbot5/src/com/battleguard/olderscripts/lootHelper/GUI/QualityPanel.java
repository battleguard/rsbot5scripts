package LootHelper.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import LootHelper.Quality;

public class QualityPanel extends Panel {
	private static final long serialVersionUID = 9022663076437281656L;
	
	private static final JLabel[] QualityLabels = { new JLabel("Poor"), new JLabel("Common"),
		new JLabel("Uncommon"), new JLabel("Rare"), new JLabel("Epic"), new JLabel("Legendary") };
private static final JTextField[] QualityValues = { new JTextField("0"), new JTextField("100"),
		new JTextField("1000"), new JTextField("2000"), new JTextField("10000"), new JTextField("50000") };


	public QualityPanel() {
		QualityValues[0].setEditable(false);
		
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
	}
}
