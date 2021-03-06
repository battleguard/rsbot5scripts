package com.battleguard.scripts.pathmaker;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.powerbot.event.PaintListener;
import org.powerbot.script.AbstractScript;
import org.powerbot.script.Manifest;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.Tile;
import org.powerbot.script.wrappers.TileMatrix;
import org.powerbot.script.wrappers.TilePath;

@Manifest(authors = { "battleguard" }, description = "path makeing utility for script writers", name = "TilePath creater")
public class PathMaker extends PollingScript implements PaintListener {
	
	private final JButton addTileButton = new JButton("Add Tile");
	private final JButton removeTileButton = new JButton("Remove Selected Tiles");
	private final JButton resetButton = new JButton("Reset");
	private final JButton printButton = new JButton("Print To Console");
	
	private final JCheckBox traversePathCheckBox = new JCheckBox("Traverse Path");
	private final JCheckBox autoAddtilesCheckBox = new JCheckBox("Auto Add Tiles");
	private JComboBox<Integer> distanceComboBox = new JComboBox<Integer>(); 	
	
	private final DefaultListModel<Tile> tileArray = new DefaultListModel<Tile>();	
	private final JList<Tile> tileList = new JList<Tile>(tileArray); 
    private final JScrollPane scrollPane = new JScrollPane(tileList);    
   
    private final JFrame frame = new JFrame();        
    private TilePath currentPath;	
	private final JTextArea copyPathTextArea = new JTextArea();	
	
	public PathMaker() {				
		for(int i = 1; i <= 20; i++) {
			distanceComboBox.addItem(i);
		}
		distanceComboBox.setSelectedIndex(4);
		
		copyPathTextArea.setFont(new Font("Verdana", Font.PLAIN, 11));
		copyPathTextArea.setEditable(false);
		
		frame.setPreferredSize(new Dimension(800, 300));
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				getController().stop();
			}
		});
		
		tileArray.addListDataListener(dataListener);
		
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));		
		frame.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout());
		
		panel.add(addTileButton);
		panel.add(removeTileButton);
		panel.add(addTileButton);
		panel.add(resetButton);
		panel.add(printButton);
		panel.add(traversePathCheckBox);
		panel.add(autoAddtilesCheckBox);
		panel.add(distanceComboBox);
		
		addTileButton.addActionListener(addTileListener);
		printButton.addActionListener(printButtonListener);
		removeTileButton.addActionListener(removeButtonListener);
		resetButton.addActionListener(resetButtonListener);			
		frame.add(panel);				
		frame.add(copyPathTextArea);		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	private final ActionListener addTileListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			tileArray.addElement(ctx.players.local().getLocation());
			currentPath = getTilePath();
			frame.pack();			
		}
	};	
	
	private final ActionListener printButtonListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {			
			System.out.println(copyPathTextArea.getText());			
		}
	};	
	
	private final ActionListener removeButtonListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(!tileList.isSelectionEmpty()) 
				tileArray.removeElement(tileList.getSelectedValue());
		}
	};	
	
	private final ActionListener resetButtonListener = new ActionListener() {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			tileArray.clear();
		}
	};
	
	private final ListDataListener dataListener = new ListDataListener() {		

		@Override public void intervalRemoved(ListDataEvent e) {
			String tilePathCode = TilePathCodeMaker.create(tileArray.elements());
			copyPathTextArea.setText(tilePathCode);
		}
		
		@Override public void intervalAdded(ListDataEvent e) {
			String tilePathCode = TilePathCodeMaker.create(tileArray.elements());
			copyPathTextArea.setText(tilePathCode);
		}
		
		@Override public void contentsChanged(ListDataEvent e) {			
			
		}
	};
	
	
	

	
	public Tile[] getTiles() {
		final Tile[] tiles = new Tile[tileArray.getSize()];
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = tileArray.get(i);
		}
		return tiles;
	}	
	
	public Tile getSelectedTile() {
		return tileList.getSelectedValue();		
	}
	
	public TilePath getTilePath() {
		return new TilePath(ctx, getTiles());
	}
	
	@Override
	public int poll() {
		if(traversePathCheckBox.isSelected() && currentPath != null) {
			if(currentPath.getEnd().distanceTo(ctx.players.local()) < 3) {
				System.out.println("reversing path");
				currentPath.reverse();
			}
			currentPath.traverse();
			return 100;
		}
		
		if(autoAddtilesCheckBox.isSelected()) {			
			if(tileArray.isEmpty() || tileArray.lastElement().distanceTo(ctx.players.local()) >= (Integer) distanceComboBox.getSelectedItem()) {
				currentPath = getTilePath();
				tileArray.addElement(ctx.players.local().getLocation());
			}
		}
		
		return 100;
	}

	@Override
	public void repaint(Graphics g) {
		final Tile selectedtile = getSelectedTile();
		for (Tile tile : getTiles()) {
			final TileMatrix matrix = tile.getMatrix(ctx);
			if(matrix.isOnScreen()) {
				if(selectedtile != null && selectedtile.equals(tile)) {
					g.setColor(Color.RED);				
					g.drawPolygon(matrix.getBounds());
					g.setColor(Color.WHITE);
				} else {
					g.drawPolygon(matrix.getBounds());
				}	
			}
		}
	}
}


