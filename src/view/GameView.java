package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.TextArea;
import java.util.ArrayList;

import javax.crypto.CipherInputStream;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import model.infrastructure.ResidentialBuilding;
import simulation.Address;

public class GameView extends JFrame {
	private JPanel infoPanel, rescuePanel, unitPanel, buttonsPanel, bottomPanel, citizenPanel, disaster, exec,
			excutedDisaster;
	private JTextArea infoText, unitInfo;
	private JButton[][] buttonWorld;
	private ArrayList<JButton> unitButtons;
	private JButton nextCycle;
	private JLabel currenCycle, casualtiesnum, infoDisaster;

	public GameView() {
		setTitle("EHRAAAB");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(1920, 1040);
		setBackground(Color.darkGray);
		setLayout(new BorderLayout());

		unitButtons = new ArrayList<JButton>();
		currenCycle = new JLabel();
		currenCycle.setText("Current Cycle: " + 0);
		currenCycle.setForeground(Color.WHITE);
		excutedDisaster = new JPanel();
		excutedDisaster.setBackground(Color.darkGray);
		excutedDisaster.setForeground(Color.white);

		nextCycle = new JButton("Next Cycle");

		casualtiesnum = new JLabel("Total Casualties: 0");

		infoPanel = new JPanel(new GridLayout(6, 0));
		infoPanel.setPreferredSize(new Dimension(200, getHeight()));
		infoPanel.setBackground(Color.darkGray);
		add(infoPanel, BorderLayout.WEST);

		infoText = new JTextArea();
		infoDisaster = new JLabel();

		infoText.setForeground(Color.WHITE);
		infoDisaster.setForeground(Color.white);

		infoText.setBackground(Color.darkGray);
		infoDisaster.setBackground(Color.darkGray);

		casualtiesnum.setBackground(Color.darkGray);
		casualtiesnum.setForeground(Color.white);

		infoPanel.add(infoText);
		infoPanel.add(infoDisaster);
		infoPanel.add(casualtiesnum);

		infoText.setLineWrap(true);
		infoText.setEditable(false);
		infoText.setVisible(true);

		JScrollPane scroll = new JScrollPane(infoText);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		infoPanel.add(scroll);

		infoText.setText("Info Panel:");
		infoDisaster.setText("Disasters");

		rescuePanel = new JPanel(new GridLayout(10, 10));
		rescuePanel.setPreferredSize(new Dimension(1000, 800));
		rescuePanel.setBackground(Color.black);
		add(rescuePanel, BorderLayout.CENTER);

		unitPanel = new JPanel(new GridLayout(4, 0));
		unitPanel.setPreferredSize(new Dimension(200, getHeight()));
		unitPanel.setBackground(Color.blue);
		add(unitPanel, BorderLayout.EAST);

		buttonsPanel = new JPanel();
		buttonsPanel.setBackground(Color.darkGray);
		unitPanel.add(buttonsPanel);

		bottomPanel = new JPanel(new FlowLayout());
		bottomPanel.setBackground(Color.darkGray);
		unitPanel.add(bottomPanel);

		bottomPanel.add(nextCycle);

		unitInfo = new JTextArea();
		unitInfo.setBackground(Color.darkGray);
		unitInfo.setForeground(Color.white);
		unitPanel.add(unitInfo);

		unitInfo.setLineWrap(true);
		unitInfo.setEditable(false);
		unitInfo.setVisible(true);

		citizenPanel = new JPanel();
		citizenPanel.setBackground(Color.darkGray);
		unitPanel.add(citizenPanel);

		buttonWorld = new JButton[10][10];
		for (int row = 0; row < buttonWorld.length; row++) {
			for (int col = 0; col < buttonWorld[row].length; col++) {
				JButton create = new JButton();
				buttonWorld[col][row] = create;
				create.setEnabled(false);
				rescuePanel.add(create);
			}
		}
		bottomPanel.add(currenCycle);

		unitPanel.setVisible(true);
		JScrollPane unitScroll = new JScrollPane(buttonsPanel);
		unitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JScrollPane unitScroll1 = new JScrollPane(citizenPanel);
		unitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JScrollPane unitInfoScroll = new JScrollPane(unitInfo);
		unitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		disaster = new JPanel();
		infoPanel.add(disaster);
		exec = new JPanel();
		infoPanel.add(exec);
		disaster.setBackground(Color.darkGray);
		disaster.setForeground(Color.white);
		exec.setBackground(Color.darkGray);
		exec.setForeground(Color.white);

		JScrollPane DisasterScroll = new JScrollPane(disaster);
		unitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		JScrollPane ExecScroll = new JScrollPane(exec);
		unitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		infoPanel.add(excutedDisaster);

		JScrollPane ExecutedDisasterScroll = new JScrollPane(excutedDisaster);
		unitScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		unitScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		unitPanel.add(unitScroll1);
		unitPanel.add(unitScroll);
		unitPanel.add(unitInfoScroll);
		infoPanel.add(DisasterScroll);
		infoPanel.add(ExecScroll);
		infoPanel.add(ExecutedDisasterScroll);
		validate();
		setVisible(true);
	}

	public void updateInfo(String message) {
		infoText.setText(message);
	}

	public void updateDisaster(String message) {
		infoDisaster.setText(message);
	}

	public void clearall() {
		// rescuePanel.removeAll();
		unitInfo.setText("Unit Info Panel:");
		infoText.setText("Info Panel:");
		citizenPanel.removeAll();
		revalidate();
		repaint();
		// buttonsPanel.removeAll();
	}

	public void addUnitbutton(ArrayList<JButton> unitButtons) {
		for (JButton unit : unitButtons) {
			buttonsPanel.add(unit);
		}
	}

	public void changeCurrent(int num) {
		currenCycle.setText("Current Cycle: " + num);
	}

	public JPanel getUnitPanel() {
		return unitPanel;
	}

	public JButton[][] getButtonWorld() {
		return buttonWorld;
	}

	public JPanel getButtonsPanel() {
		return buttonsPanel;
	}

	public JButton getNextCycle() {

		return nextCycle;
	}

	public void updateCasualities(int calculateCasualties) {
		casualtiesnum.setText("Total Casualties: " + calculateCasualties);

	}

	public JPanel getBottomPanel() {
		return bottomPanel;
	}

	public JTextArea getUnitInfo() {
		return unitInfo;
	}

	public void addCitizenPanel(JButton button) {

		citizenPanel.add(button);
		citizenPanel.revalidate();
		citizenPanel.repaint();
	}

	public void removeCitizenPanel() {

		citizenPanel.removeAll();
		citizenPanel.revalidate();
		citizenPanel.repaint();
	}

	public void reval() {
		validate();
	}

	public JPanel getDisaster() {
		return disaster;
	}

	public JPanel getExec() {
		return exec;
	}

	public JPanel getExcutedDisaster() {
		return excutedDisaster;
	}

//	public static void main(String[] args) {
//		GameView view = new GameView();
//		ResidentialBuilding a = new ResidentialBuilding(new Address(10, 10));
//		String s="Disasters\n Fady\n 10";
//		view.updateDisaster(s);
//		view.updateInfo(a.toString());
//	}
}
