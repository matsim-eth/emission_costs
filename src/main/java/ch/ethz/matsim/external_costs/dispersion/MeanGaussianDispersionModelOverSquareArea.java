package ch.ethz.matsim.external_costs.dispersion;

public class MeanGaussianDispersionModelOverSquareArea implements DispersionModel {
    private double sigma;
    private double sideLength;

    public MeanGaussianDispersionModelOverSquareArea(double sigma, double sideLength) {
        this.sigma = sigma;
        this.sideLength = sideLength;
    }

    @Override
    public double calculate(double r) {
        double A = Math.pow(this.sigma,2) / (r * sideLength);
        double a = Math.pow( (r - sideLength/2) / (Math.sqrt(2) * sigma) , 2);
        double b = Math.pow( (r + sideLength/2) / (Math.sqrt(2) * sigma) , 2);
        return A * (Math.exp(- Math.pow(a,2)) - Math.exp(- Math.pow(b,2)));
    }
}
