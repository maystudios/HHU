public class Ballistic {

    private double g = 9.81;
    private double startValue;
    private double initialSpeed;
    private double time;
    
    Ballistic(double startValue, double initialSpeed, double time) {
        this.startValue = startValue;
        this.initialSpeed = initialSpeed;
        this.time = time;   
        }

    public double getCurve() {
        return curve(startValue, initialSpeed, time, g);
        }

    private double curve(double x, double v, double t, double g) {
        return x + (v * t) - ((g * t * t) / 2);
        }
    
    public static void main(String[] args) {
        
        if(args.length != 3) {
            System.err.println("Bitte geben Sie genau drei gleitkomma Zahlen als Argument ein.");
            System.exit(1);
        }

        try {
            double x = Double.parseDouble(args[0]);
            double v = Double.parseDouble(args[1]);
            double t = Double.parseDouble(args[2]);

            Ballistic ballistic = new Ballistic(x, v, t);
            
            System.out.println(ballistic.getCurve());
        } catch(NumberFormatException e) {
            System.err.println("Das uebergebne Argumente sind keine gueltigen gleitkomma Zahlen.");
            System.exit(1);
        }

        }
    }