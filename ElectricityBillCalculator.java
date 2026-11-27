import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * Electricity Bill Calculator
 * Java GUI application using AWT + Swing, event handling and layout managers.
 *
 * Student Name : Mayur Suryavanshi
 * Roll No / PRN: IT2362
 * Class / Div  : C
 */
public class ElectricityBillCalculator extends JFrame implements ActionListener {

    // ---------- Input components ----------
    private JTextField txtName, txtConsumerNo, txtPrev, txtCurr;
    private JComboBox<String> cmbType;

    // ---------- Buttons ----------
    private JButton btnCalc, btnReset, btnExit;

    // ---------- Output ----------
    private JTextArea txtBill;
    private JLabel lblStatus;

    // ---------- Tariff (Rs per unit) ----------
    private static final String[] TYPES = {"Residential", "Commercial", "Industrial"};
    private static final double[][] RATES = {
        // slab1 (0-100), slab2 (101-300), slab3 (301-500), slab4 (500+)
        {3.50, 5.50, 7.50, 9.00},   // Residential
        {6.00, 8.00, 10.00, 12.00}, // Commercial
        {5.00, 7.00, 9.00, 11.00}   // Industrial
    };
    private static final double[] FIXED_CHARGE = {50, 150, 300};
    private static final double TAX_RATE = 0.05; // 5% electricity duty

    public ElectricityBillCalculator() {
        super("Electricity Bill Calculator");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getRootPane().setBorder(new EmptyBorder(10, 10, 10, 10));

        // ----- Header (North) -----
        JLabel title = new JLabel("Mayur Suryavanshi - IT2362", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setOpaque(true);
        title.setBackground(new Color(25, 118, 210));
        title.setForeground(Color.WHITE);
        title.setBorder(new EmptyBorder(12, 10, 12, 10));
        add(title, BorderLayout.NORTH);

        // ----- Form (West) : GridBagLayout -----
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(new TitledBorder("Consumer Details"));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.anchor = GridBagConstraints.WEST;
        g.fill = GridBagConstraints.HORIZONTAL;

        txtName = new JTextField(15);
        txtConsumerNo = new JTextField(15);
        cmbType = new JComboBox<>(TYPES);
        txtPrev = new JTextField(15);
        txtCurr = new JTextField(15);

        addRow(form, g, 0, "Customer Name:", txtName);
        addRow(form, g, 1, "Consumer No:", txtConsumerNo);
        addRow(form, g, 2, "Connection Type:", cmbType);
        addRow(form, g, 3, "Previous Reading:", txtPrev);
        addRow(form, g, 4, "Current Reading:", txtCurr);

        // ----- Buttons : FlowLayout -----
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnCalc = new JButton("Calculate");
        btnReset = new JButton("Reset");
        btnExit = new JButton("Exit");
        btnCalc.setBackground(new Color(46, 125, 50));
        btnCalc.setForeground(Color.WHITE);
        btnCalc.setOpaque(true);
        btnCalc.setBorderPainted(false);
        btnCalc.setMnemonic(KeyEvent.VK_C);
        btnReset.setMnemonic(KeyEvent.VK_R);
        btnExit.setMnemonic(KeyEvent.VK_X);
        btnPanel.add(btnCalc);
        btnPanel.add(btnReset);
        btnPanel.add(btnExit);

        g.gridx = 0; g.gridy = 5; g.gridwidth = 2;
        form.add(btnPanel, g);
        add(form, BorderLayout.WEST);

        // ----- Bill output (Center) -----
        txtBill = new JTextArea(18, 34);
        txtBill.setEditable(false);
        txtBill.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtBill.setText("\n  Enter details and click 'Calculate'\n  to generate the bill.");
        JScrollPane sp = new JScrollPane(txtBill);
        sp.setBorder(new TitledBorder("Bill Summary"));
        add(sp, BorderLayout.CENTER);

        // ----- Status bar (South) -----
        lblStatus = new JLabel("Ready");
        lblStatus.setBorder(new EmptyBorder(4, 4, 0, 4));
        add(lblStatus, BorderLayout.SOUTH);

        // ----- Menu bar -----
        setJMenuBar(buildMenu());

        // ----- Event handling -----
        btnCalc.addActionListener(this);
        btnReset.addActionListener(this);
        btnExit.addActionListener(this);
        txtCurr.addActionListener(this); // press Enter in last field = calculate

        // Allow only digits / one dot in reading fields (KeyListener)
        txtPrev.addKeyListener(numericOnly(txtPrev));
        txtCurr.addKeyListener(numericOnly(txtCurr));

        // Confirm on window close (WindowListener)
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) { confirmExit(); }
        });

        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    private void addRow(JPanel p, GridBagConstraints g, int row, String label, JComponent field) {
        g.gridy = row; g.gridwidth = 1;
        g.gridx = 0; g.weightx = 0;
        p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 1;
        p.add(field, g);
    }

    private JMenuBar buildMenu() {
        JMenuBar bar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem miReset = new JMenuItem("Reset");
        JMenuItem miExit = new JMenuItem("Exit");
        miReset.addActionListener(e -> resetForm());
        miExit.addActionListener(e -> confirmExit());
        file.add(miReset);
        file.addSeparator();
        file.add(miExit);

        JMenu help = new JMenu("Help");
        JMenuItem miTariff = new JMenuItem("View Tariff");
        JMenuItem miAbout = new JMenuItem("About");
        miTariff.addActionListener(e -> showTariff());
        miAbout.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Electricity Bill Calculator\nBuilt with Java AWT & Swing",
                "About", JOptionPane.INFORMATION_MESSAGE));
        help.add(miTariff);
        help.add(miAbout);

        bar.add(file);
        bar.add(help);
        return bar;
    }

    private KeyAdapter numericOnly(JTextField f) {
        return new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                boolean ok = Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE
                        || c == KeyEvent.VK_DELETE || c == KeyEvent.VK_ENTER
                        || (c == '.' && !f.getText().contains("."));
                if (!ok) e.consume();
            }
        };
    }

    // ---------- Event handling ----------
    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnCalc || src == txtCurr) calculateBill();
        else if (src == btnReset) resetForm();
        else if (src == btnExit) confirmExit();
    }

    private void calculateBill() {
        String name = txtName.getText().trim();
        String cNo = txtConsumerNo.getText().trim();

        if (name.isEmpty() || cNo.isEmpty()) {
            error("Please enter customer name and consumer number.");
            return;
        }
        double prev, curr;
        try {
            prev = Double.parseDouble(txtPrev.getText().trim());
            curr = Double.parseDouble(txtCurr.getText().trim());
        } catch (NumberFormatException ex) {
            error("Please enter valid numeric meter readings.");
            return;
        }
        if (curr < prev) {
            error("Current reading cannot be less than previous reading.");
            return;
        }

        int t = cmbType.getSelectedIndex();
        double units = curr - prev;
        double[] r = RATES[t];

        // slab-wise calculation
        double s1 = Math.min(units, 100);
        double s2 = Math.min(Math.max(units - 100, 0), 200);
        double s3 = Math.min(Math.max(units - 300, 0), 200);
        double s4 = Math.max(units - 500, 0);

        double c1 = s1 * r[0], c2 = s2 * r[1], c3 = s3 * r[2], c4 = s4 * r[3];
        double energy = c1 + c2 + c3 + c4;
        double fixed = FIXED_CHARGE[t];
        double tax = (energy + fixed) * TAX_RATE;
        double total = energy + fixed + tax;

        StringBuilder sb = new StringBuilder();
        sb.append("==================================\n");
        sb.append("        ELECTRICITY BILL\n");
        sb.append("==================================\n");
        sb.append(String.format("Customer   : %s%n", name));
        sb.append(String.format("Consumer No: %s%n", cNo));
        sb.append(String.format("Type       : %s%n", TYPES[t]));
        sb.append("----------------------------------\n");
        sb.append(String.format("Previous   : %.2f%n", prev));
        sb.append(String.format("Current    : %.2f%n", curr));
        sb.append(String.format("Units Used : %.2f%n", units));
        sb.append("----------------------------------\n");
        sb.append(String.format("0-100   %7.2f x %.2f = %9.2f%n", s1, r[0], c1));
        sb.append(String.format("101-300 %7.2f x %.2f = %9.2f%n", s2, r[1], c2));
        sb.append(String.format("301-500 %7.2f x %.2f = %9.2f%n", s3, r[2], c3));
        sb.append(String.format("500+    %7.2f x %.2f = %9.2f%n", s4, r[3], c4));
        sb.append("----------------------------------\n");
        sb.append(String.format("Energy Charges   : Rs %9.2f%n", energy));
        sb.append(String.format("Fixed Charges    : Rs %9.2f%n", fixed));
        sb.append(String.format("Tax (%.0f%%)        : Rs %9.2f%n", TAX_RATE * 100, tax));
        sb.append("==================================\n");
        sb.append(String.format("TOTAL PAYABLE    : Rs %9.2f%n", total));
        sb.append("==================================\n");

        txtBill.setText(sb.toString());
        txtBill.setCaretPosition(0);
        lblStatus.setText("Bill generated for " + name);
    }

    private void resetForm() {
        txtName.setText("");
        txtConsumerNo.setText("");
        txtPrev.setText("");
        txtCurr.setText("");
        cmbType.setSelectedIndex(0);
        txtBill.setText("\n  Enter details and click 'Calculate'\n  to generate the bill.");
        lblStatus.setText("Form reset");
        txtName.requestFocus();
    }

    private void showTariff() {
        StringBuilder sb = new StringBuilder("Tariff (Rs per unit)\n\n");
        sb.append(String.format("%-12s %-7s %-7s %-7s %-7s %-6s%n",
                "Type", "0-100", "101-300", "301-500", "500+", "Fixed"));
        for (int i = 0; i < TYPES.length; i++) {
            sb.append(String.format("%-12s %-7.2f %-7.2f %-7.2f %-7.2f %-6.0f%n",
                    TYPES[i], RATES[i][0], RATES[i][1], RATES[i][2], RATES[i][3], FIXED_CHARGE[i]));
        }
        sb.append("\nElectricity duty: 5% on (energy + fixed)");
        JTextArea ta = new JTextArea(sb.toString());
        ta.setFont(new Font("Monospaced", Font.PLAIN, 12));
        ta.setEditable(false);
        JOptionPane.showMessageDialog(this, ta, "Tariff", JOptionPane.PLAIN_MESSAGE);
    }

    private void error(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Input Error", JOptionPane.ERROR_MESSAGE);
        lblStatus.setText("Error: " + msg);
    }

    private void confirmExit() {
        int c = JOptionPane.showConfirmDialog(this, "Do you want to exit?",
                "Exit", JOptionPane.YES_NO_OPTION);
        if (c == JOptionPane.YES_OPTION) System.exit(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ElectricityBillCalculator().setVisible(true));
    }
}
