package mandelbrotsetdemo;

/**
 *
 * @author HuangDiWen
 * @created 2014-1-17 10:31:25
 */
public class Complex {

    private double real;
    private double image;

    public Complex(double real, double image) {
        this.real = real;
        this.image = image;
    }

    public double getReal() {
        return real;
    }

    public void setReal(double real) {
        this.real = real;
    }

    public double getImage() {
        return image;
    }

    public void setImage(double image) {
        this.image = image;
    }

    public Complex add(Complex that) {
        double thatReal = that.getReal();
        double thatImage = that.getImage();
        double newReal = this.real + thatReal;
        double newImage = this.image + thatImage;
        Complex result = new Complex(newReal, newImage);
        return result;
    }

    public Complex multiply(Complex that) {
        double thatReal = that.getReal();
        double thatImage = that.getImage();
        double newReal = this.real * thatReal - this.image * thatImage;
        double newImage = this.image * thatReal + this.real * thatImage;
        Complex result = new Complex(newReal, newImage);
        return result;
    }

    public double abs() {
        return this.getReal() * this.getReal() + this.getImage() * this.getImage();
    }

    public static int getMandelbrotEscapeVal(Complex c, int maxEscape) {
        Complex z = new Complex(0, 0);
        int count = 0;
        while (true) {
            z = z.multiply(z).add(c);
            count++;
            if (z.abs() > 4 || count > maxEscape) {
                break;
            }
        }
        return count;
    }
}
