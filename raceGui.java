import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class raceGui extends JFrame {
    private JTextArea raceTextArea;
    private JTextField raceLengthField;
    private ArrayList<Horse> horses;  

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new raceGui().setVisible(true);
            }
        });
    }

    public raceGui() {
        setTitle("Race GUI");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);

        horses = new ArrayList<>();

        this.raceTextArea = new JTextArea(12, 30);
        raceTextArea.setEditable(false);
        raceTextArea.setFont(new Font("Monospace", Font.PLAIN, 16));
        raceTextArea.setBackground(new Color(255, 255, 255)); // White background
        raceTextArea.setForeground(new Color(34, 34, 34)); // Almost black text

        JScrollPane scrollPane = new JScrollPane(raceTextArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(230, 230, 250)); // Lavender background
        JLabel raceLengthLabel = new JLabel("Set Race Length:");
        raceLengthField = new JTextField("20", 5);
        topPanel.add(raceLengthLabel);
        topPanel.add(raceLengthField);

        JButton addHorsesButton = new JButton("Add Horses");
        addHorsesButton.setBackground(new Color(100, 149, 237)); // Cornflower Blue
        addHorsesButton.setForeground(Color.BLACK); // Changed to black for better visibility
        addHorsesButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openHorseAdditionWindow();
            }
        });
        topPanel.add(addHorsesButton);
        getContentPane().add(topPanel, BorderLayout.NORTH);

        JButton raceButton = new JButton("Start Race");
        raceButton.setBackground(new Color(50, 205, 50)); // Lime Green
        raceButton.setForeground(Color.BLACK); // Changed to black for better visibility
        raceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int raceLength = Integer.parseInt(raceLengthField.getText().trim());
                if (horses.size() >= 2) {
                    Race race = new Race(raceLength, raceTextArea);
                    for (Horse horse : horses) {
                        race.addHorse(horse, horses.indexOf(horse) + 1);
                    }
                    new Thread(race::startRace).start();
                }
                else {
                    JOptionPane.showMessageDialog(null, "At least 2 horses are required to start the race.", "Insufficient Horses", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        getContentPane().add(raceButton, BorderLayout.SOUTH);
    }

    private void openHorseAdditionWindow() {
        HorseAdditionWindow horseAdditionWindow = new HorseAdditionWindow(this);
        horseAdditionWindow.setVisible(true);
    }

    public void updateRaceText(String raceText) {
        raceTextArea.setText(raceText);
    }

    public void addHorse(Horse horse) {
        horses.add(horse);
        updateRaceText("New horse added: " + horse.getName() + ", Symbol: " + horse.getSymbol() + ", Confidence: " + horse.getConfidence() + "\n");
    }
}

class HorseAdditionWindow extends JFrame {
    private raceGui parent;

    public HorseAdditionWindow(raceGui parent) {
        super("Add Horses");
        this.parent = parent;
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(0, 2));  // Flexible grid layout
        panel.setBackground(new Color(240, 248, 255)); // Alice Blue background

        JLabel nameLabel = new JLabel("Name:");
        JTextField nameField = new JTextField();
        JLabel symbolLabel = new JLabel("Symbol:");
        JComboBox<Character> symbolCombo = new JComboBox<>(new Character[] {'♘', '♞', '♜', '♚', '♝', '♛'});
        JLabel confidenceLabel = new JLabel("Confidence:");
        JSpinner confidenceSpinner = new JSpinner(new SpinnerNumberModel(0.5, 0.0, 1.0, 0.1));

        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(symbolLabel);
        panel.add(symbolCombo);
        panel.add(confidenceLabel);
        panel.add(confidenceSpinner);

        JButton addButton = new JButton("Add a Horse");
        addButton.setBackground(new Color(176, 224, 230)); // Powder Blue
        addButton.setForeground(Color.BLACK); // Changed to black for better visibility
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            char symbol = (char) symbolCombo.getSelectedItem();
            double confidence = (Double) confidenceSpinner.getValue();
            Horse horse = new Horse(symbol, name, confidence);
            parent.addHorse(horse);
            dispose();
        });

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(addButton, BorderLayout.SOUTH);
    }
}
