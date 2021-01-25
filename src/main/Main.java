package main;

import utils.MathUtils;

import javax.imageio.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String imageInput = "avatar_02";
        String imageExtension = "jpg";

        File inputImageFile = new File("res/" + imageInput + "." + imageExtension);
        BufferedImage bufferedImage;

        /* LOAD IMAGE */
        try {
            bufferedImage = ImageIO.read(inputImageFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading file: " + inputImageFile);
            return;
        }

        /* PROCESS IMAGE */
        long startTime = System.currentTimeMillis();
        bufferedImage = addNoise(bufferedImage, 10);
        bufferedImage = quantize(bufferedImage, 18);
        bufferedImage = pixelate(bufferedImage, 6);
        System.out.println("Time spent processing: " + (System.currentTimeMillis() - startTime) + " ms");

        /* STORE PROCESSED IMAGE */
        File outputImageFile = new File("res/" + imageInput + "_out." + imageExtension);
        try {
            ImageIO.write(bufferedImage, imageExtension, outputImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage pixelate(BufferedImage inputImage, int pixelSize) {
        int newWidth = inputImage.getWidth();
        while (newWidth % pixelSize != 0) newWidth++;
        int newHeight = inputImage.getHeight() + (inputImage.getHeight() % pixelSize);
        while (newHeight % pixelSize != 0) newHeight++;
        BufferedImage outputImage = new BufferedImage(newWidth, newHeight, inputImage.getType());
        for (int i = 0; i < inputImage.getWidth(); i = i + pixelSize) {
            for (int j = 0; j < inputImage.getHeight(); j = j + pixelSize) {
                for (int k = i; k < i + pixelSize; k++) {
                    for (int z = j; z < j + pixelSize; z++) {
                        Color pixelColor = new Color(inputImage.getRGB(i, j));
                        int r = pixelColor.getRed(), g = pixelColor.getGreen(), b = pixelColor.getBlue();
                        int alpha = (inputImage.getRGB(i, j) >> 24);
                        int newColor = (alpha << 24) | (r << 16) | (g << 8) | b;
                        outputImage.setRGB(k, z, newColor);
                    }
                }
            }
        }
        return outputImage;
    }

    public static BufferedImage quantize(BufferedImage inputImage, int quatizationFactor) {
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
        for (int i = 0; i < inputImage.getWidth(); i++) {
            for (int j = 0; j < inputImage.getHeight(); j++) {
                Color pixelColor = new Color(inputImage.getRGB(i, j));
                int r = quantizeInteger(pixelColor.getRed(), quatizationFactor);
                int g = quantizeInteger(pixelColor.getGreen(), quatizationFactor);
                int b = quantizeInteger(pixelColor.getBlue(), quatizationFactor);
                int alpha = (inputImage.getRGB(i, j) >> 24);
                int newColor = (alpha << 24) | (r << 16) | (g << 8) | b;
                outputImage.setRGB(i, j, newColor);
            }
        }
        return outputImage;
    }

    public static int quantizeInteger(int input) {
        return quantizeInteger(input, 36);
    }

    public static int quantizeInteger(int input, int quatizationFactor) {
        return (int) (Math.floor((double) input / (double) quatizationFactor) * (255.0 / Math.floor(255.0 / quatizationFactor)));
    }

    private static BufferedImage addNoise(BufferedImage inputImage, int noiseFactor) {
        BufferedImage outputImage = new BufferedImage(inputImage.getWidth(), inputImage.getHeight(), inputImage.getType());
        for (int i = 0; i < inputImage.getWidth(); i++) {
            for (int j = 0; j < inputImage.getHeight(); j++) {
                Color pixelColor = new Color(inputImage.getRGB(i, j));
                int r = MathUtils.clamp(pixelColor.getRed() + (int) (MathUtils.random(-noiseFactor, noiseFactor)), 0, 255);
                int g = MathUtils.clamp(pixelColor.getGreen() + (int) (MathUtils.random(-noiseFactor, noiseFactor)), 0, 255);
                int b = MathUtils.clamp(pixelColor.getBlue() + (int) (MathUtils.random(-noiseFactor, noiseFactor)), 0, 255);
                int alpha = (inputImage.getRGB(i, j) >> 24);
                int newColor = (alpha << 24) | (r << 16) | (g << 8) | b;
                outputImage.setRGB(i, j, newColor);
            }
        }
        return outputImage;
    }
}
