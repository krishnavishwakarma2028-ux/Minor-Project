# CardShield: Fake Card Detection

CardShield is an educational B.Tech AIML minor-project web application. It screens a submitted **sample card image** for visual-quality and basic format anomalies, then displays an explainable risk score.

## What it demonstrates

- Java full-stack web development with Spring Boot and Thymeleaf
- Image input handling with Java `ImageIO`
- Feature extraction: resolution, aspect ratio, and grayscale contrast
- An explainable, weighted risk-scoring classifier
- A responsive result dashboard with reasons for every decision

The result is intentionally a screening signal—not proof of authenticity. Never upload real confidential identity, bank, or payment-card data.

## Scoring logic

| Signal | Suspicion points |
|---|---:|
| Image smaller than 500 × 300 px | 28 |
| Unusual image aspect ratio | 12 |
| Low grayscale contrast | 22 |
| Invalid entered identifier pattern | 18 |

Risk score `< 20`: **Low Risk**; `20–44`: **Review Required**; `45+`: **Suspicious**.

## Run it locally

1. Install JDK 21 and Apache Maven, then restart the terminal.
2. Open a terminal in this project directory.
3. Run `mvn spring-boot:run`.
4. Open `http://localhost:8080`.

## Future AIML enhancement

For a more advanced phase, create a labelled data set of synthetic genuine and tampered card images. Extract features such as blur variance, edge density, OCR confidence, and logo-template similarity. Train a Random Forest classifier in Java with Smile or Weka, export the model, and replace the weighted scoring method with model probability. Keep the existing reasons dashboard as the explainability layer.

## Suggested viva explanation

"The application is an explainable first-stage fraud-screening system. It converts an image into measurable features, assigns a weighted risk score, and reports the exact signals that caused the score. This makes it suitable for manual review workflows, rather than making unsupported claims of official document verification."
