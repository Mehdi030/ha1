package htw.berlin.prog2.ha1;

public class Calculator {

    private String screen = "0";
    private double latestValue;
    private String latestOperation = "";

    public String readScreen() {
        return screen;
    }

    public void pressDigitKey(int digit) {
        if (digit > 9 || digit < 0) throw new IllegalArgumentException(); // Ungültige Ziffer

        // Bildschirm zurücksetzen, wenn "0" oder zuletzt eingegebener Wert
        if (screen.equals("0") || latestValue == Double.parseDouble(screen)) screen = "";
        screen = screen + digit; // Ziffer an Bildschirm anhängen
    }

    public void pressClearKey() {
        screen = "0"; // Bildschirm auf "0" setzen
        latestOperation = ""; // Letzte Operation zurücksetzen
        latestValue = 0.0; // Zuletzt eingegebener Wert zurücksetzen
    }

    public void pressBinaryOperationKey(String operation) {
        double currentNumber = Double.parseDouble(screen);

        // Überprüfen, ob es eine Division durch Null gibt
        if (operation.equals("/") && currentNumber == 0) {
            screen = "Error";
            latestOperation = "";
            latestValue = 0.0;
        } else {
            latestValue = currentNumber;
            latestOperation = operation;
            screen = "0"; // Bildschirm zurücksetzen
        }
    }

    public void pressUnaryOperationKey(String operation) {
        latestValue = Double.parseDouble(screen); // Aktuellen Wert speichern
        latestOperation = operation; // Setze die Operation

        var result = switch (operation) { // Berechnung durchführen
            case "√" -> Math.sqrt(latestValue); // Quadratwurzel
            case "%" -> latestValue / 100; // Prozent
            case "1/x" -> 1 / latestValue; // Inversion
            default -> throw new IllegalArgumentException(); // Ungültige Operation
        };
        screen = Double.toString(result); // Ergebnis auf dem Bildschirm anzeigen
        if (screen.equals("NaN")) screen = "Error"; // Fehler anzeigen
        if (screen.contains(".") && screen.length() > 11) screen = screen.substring(0, 10); // Bildschirmformatierung
    }

    public void pressDotKey() {
        if (!screen.contains(".")) screen = screen + "."; // Dezimaltrennzeichen hinzufügen
    }

    public void pressNegativeKey() {
        System.out.println("Vorzeichen vor Änderung: " + screen);
        if (screen.startsWith("-")) {
            screen = screen.substring(1);
        } else {
            screen = "-" + screen;
        }

        // Wenn der Wert -0.0 wird, setze ihn auf 0
        if (screen.equals("-0")) {
            screen = "0";
        }

        System.out.println("Vorzeichen nach Änderung: " + screen);

        if (!latestOperation.isEmpty()) {
            latestValue = Double.parseDouble(screen);
            System.out.println("Aktualisiertes latestValue (Operation läuft): " + latestValue);
        } else {
            latestValue = Double.parseDouble(screen);
            System.out.println("Aktualisiertes latestValue (keine Operation): " + latestValue);
        }
    }
    public double subtract(double a, double b) {
        return a - b; // This should work for both positive and negative numbers
    }


    public void pressEqualsKey() {
        if (!latestOperation.isEmpty()) { // Prüfen, ob eine Operation gesetzt ist
            double currentNumber;
            try {
                currentNumber = Double.parseDouble(screen); // Aktuellen Bildschirmwert parsen
            } catch (NumberFormatException e) {
                screen = "Error"; // Fehler anzeigen
                return;
            }
            double result = 0.0; // Ergebnis initialisieren

            // Berechnung basierend auf der letzten Operation
            switch (latestOperation) {
                case "+":
                    result = latestValue + currentNumber; // Addition
                    break;
                case "-":
                    result = latestValue - currentNumber; // Subtraktion
                    break;
                case "x":
                    result = latestValue * currentNumber; // Multiplikation
                    break;
                case "/":
                    if (currentNumber == 0) {
                        screen = "Error"; // Division durch Null
                        return; // Methode beenden
                    }
                    result = latestValue / currentNumber; // Division
                    break;
                default:
                    screen = "Error"; // Ungültige Operation
                    return; // Methode beenden
            }

            // Ergebnisse formatieren
            screen = formatResult(result); // Ergebnis formatieren und anzeigen
        }
    }


    private String formatResult(double result) {
        String formattedResult = Double.toString(result); // Ergebnis in String umwandeln
        if (formattedResult.equals("Infinity") || formattedResult.equals("-Infinity")) {
            return "Error"; // Fehler anzeigen
        }
        if (formattedResult.endsWith(".0")) {
            formattedResult = formattedResult.substring(0, formattedResult.length() - 2); // ".0" entfernen
        }
        if (formattedResult.contains(".") && formattedResult.length() > 11) {
            formattedResult = formattedResult.substring(0, 10); // Bildschirmformatierung
        }
        return formattedResult; // Formatiertes Ergebnis zurückgeben
    }
}