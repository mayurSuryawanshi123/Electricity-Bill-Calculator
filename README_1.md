# Electricity Bill Calculator

**Student Name:** Mayur Suryavanshi
**Roll No:** IT2362
**Application:** Electricity Bill Calculator (Java GUI)

## About
A desktop GUI application built with Java AWT and Swing that calculates an electricity bill from meter readings using slab-wise tariff rates for Residential, Commercial and Industrial connections.

## Java GUI Concepts Used
| Concept | Where it is used |
|---|---|
| **Swing components** | JFrame, JPanel, JLabel, JTextField, JComboBox, JButton, JTextArea, JScrollPane, JMenuBar, JOptionPane |
| **AWT** | Color, Font, Insets, event classes (`java.awt.event.*`) |
| **Layout managers** | `BorderLayout` (main frame), `GridBagLayout` (input form), `FlowLayout` (button panel) |
| **Event handling** | `ActionListener` (buttons, Enter key), `KeyListener` (numeric-only readings), `WindowListener` (confirm on close), lambda listeners (menu items) |

## Features
- Inputs: customer name, consumer number, connection type, previous and current meter reading
- Slab-wise billing: 0-100, 101-300, 301-500, 500+ units
- Fixed charge per connection type plus 5% electricity duty
- Itemised bill summary with total payable
- Input validation with error dialogs (empty fields, non-numeric readings, current < previous)
- Reset and Exit buttons, File and Help menus (View Tariff, About), keyboard mnemonics (Alt+C, Alt+R, Alt+X)

## Tariff (Rs per unit)
| Type | 0-100 | 101-300 | 301-500 | 500+ | Fixed (Rs) |
|---|---|---|---|---|---|
| Residential | 3.50 | 5.50 | 7.50 | 9.00 | 50 |
| Commercial | 6.00 | 8.00 | 10.00 | 12.00 | 150 |
| Industrial | 5.00 | 7.00 | 9.00 | 11.00 | 300 |

`Total = Energy charges + Fixed charge + 5% duty on (Energy + Fixed)`

## How to Run
Requires JDK 8 or above.

```
javac ElectricityBillCalculator.java
java ElectricityBillCalculator
```

## How to Use
1. Enter the customer name and consumer number.
2. Choose the connection type.
3. Enter the previous and current meter readings.
4. Click **Calculate** (or press Enter in the Current Reading field).
5. The bill appears in the Bill Summary panel. Use **Reset** to clear the form.

## Sample
Residential, previous 1200, current 1450 (250 units):
Energy = 100 x 3.50 + 150 x 5.50 = Rs 1175.00, Fixed = Rs 50.00, Duty = Rs 61.25, **Total = Rs 1286.25**

## Files
- `ElectricityBillCalculator.java` - complete source code
- `Output_ElectricityBillCalculator.pdf` - screenshots and sample outputs
- `README.md` - this file
