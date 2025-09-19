package org.aibles.spaced_repetition.flashcard.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.aibles.spaced_repetition.flashcard.dto.CreateFlashcardRequest;

public class FlashcardContentValidator implements ConstraintValidator<ValidFlashcardContent, CreateFlashcardRequest> {

    @Override
    public boolean isValid(CreateFlashcardRequest request, ConstraintValidatorContext context) {
        if (request == null || request.getFront() == null || request.getBack() == null) {
            return true; // Let other validators handle null checks
        }

        String front = request.getFront().trim().toLowerCase();
        String back = request.getBack().trim().toLowerCase();

        // Check if front and back are identical
        if (front.equals(back)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Front and back content cannot be identical")
                    .addPropertyNode("back")
                    .addConstraintViolation();
            return false;
        }

        // Check for minimum meaningful difference
        if (front.length() > 5 && back.length() > 5) {
            double similarity = calculateSimilarity(front, back);
            if (similarity > 0.85) { // 85% similarity threshold
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("Front and back content are too similar")
                        .addPropertyNode("back")
                        .addConstraintViolation();
                return false;
            }
        }

        return true;
    }

    private double calculateSimilarity(String s1, String s2) {
        int longer = Math.max(s1.length(), s2.length());
        if (longer == 0) return 1.0;
        return (longer - editDistance(s1, s2)) / (double) longer;
    }

    private int editDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else {
                    dp[i][j] = Math.min(
                            Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                            dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)
                    );
                }
            }
        }
        return dp[s1.length()][s2.length()];
    }
}