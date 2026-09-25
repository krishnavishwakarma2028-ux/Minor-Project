package com.college.fakecard.service;

import com.college.fakecard.model.DetectionResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CardAnalysisService {
    public DetectionResult analyse(MultipartFile file, String cardNumber) throws IOException {
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image == null) throw new IllegalArgumentException("Please upload a valid PNG, JPG, or WEBP image.");

        int risk = 0;
        List<String> alerts = new ArrayList<>();
        List<String> checks = new ArrayList<>();
        int w = image.getWidth(), h = image.getHeight();
        checks.add("Readable image received: " + w + " × " + h + " px");
        if (w < 500 || h < 300) { risk += 28; alerts.add("Low image resolution may hide document details."); }
        else checks.add("Resolution is suitable for visual inspection.");
        if (w > h * 3 || h > w * 2) { risk += 12; alerts.add("Unusual aspect ratio for a standard card image."); }
        else checks.add("Image aspect ratio is within an expected range.");

        double contrast = contrast(image);
        if (contrast < 24) { risk += 22; alerts.add("Low contrast suggests a blurred, washed-out, or heavily compressed image."); }
        else checks.add("Image contrast is adequate (" + Math.round(contrast) + "/255).");

        String number = cardNumber == null ? "" : cardNumber.replaceAll("[ -]", "");
        if (!number.isBlank()) {
            if (number.matches("[A-Za-z0-9]{8,20}")) checks.add("Entered identifier has a valid basic format.");
            else { risk += 18; alerts.add("Entered identifier has an unusual format."); }
        } else checks.add("No identifier supplied; identifier-format validation skipped.");

        String verdict = risk >= 45 ? "SUSPICIOUS" : risk >= 20 ? "REVIEW REQUIRED" : "LOW RISK";
        String confidence = risk >= 45 ? "High" : risk >= 20 ? "Medium" : "Moderate";
        if (alerts.isEmpty()) alerts.add("No visual quality or format anomaly was found by the demo checks.");
        return new DetectionResult(verdict, Math.min(risk, 100), confidence, w, h, alerts, checks,
                "This is an educational risk-screening result, not proof that a government or bank card is authentic.");
    }

    private double contrast(BufferedImage image) {
        int step = Math.max(1, Math.max(image.getWidth(), image.getHeight()) / 250);
        long sum = 0, sumSq = 0; int count = 0;
        for (int y = 0; y < image.getHeight(); y += step) for (int x = 0; x < image.getWidth(); x += step) {
            int rgb = image.getRGB(x, y); int gray = ((rgb >> 16 & 255) * 299 + (rgb >> 8 & 255) * 587 + (rgb & 255) * 114) / 1000;
            sum += gray; sumSq += (long) gray * gray; count++;
        }
        double mean = (double) sum / count;
        return Math.sqrt((double) sumSq / count - mean * mean);
    }
}
