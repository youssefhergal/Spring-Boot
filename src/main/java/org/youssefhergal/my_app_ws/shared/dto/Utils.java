package org.youssefhergal.my_app_ws.shared.dto;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class Utils {
    
    /**
     * Génère une chaîne de caractères aléatoire selon les paramètres spécifiés
     * @param length Longueur souhaitée de la chaîne
     * @param useNumbers Inclure des chiffres
     * @param useUpperCase Inclure des majuscules
     * @return Une chaîne de caractères aléatoire
     */
    public static String generateUID(int length, boolean useNumbers, boolean useUpperCase) {
        StringBuilder uid = new StringBuilder();
        String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
        String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        
        // Construction du pool de caractères selon les paramètres
        StringBuilder characterPool = new StringBuilder(lowercaseChars);
        if (useUpperCase) {
            characterPool.append(uppercaseChars);
        }
        if (useNumbers) {
            characterPool.append(numbers);
        }
        
        // Utilisation de SecureRandom pour une meilleure génération aléatoire
        SecureRandom random = new SecureRandom();
        
        // Génération de la chaîne
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characterPool.length());
            uid.append(characterPool.charAt(randomIndex));
        }
        
        return uid.toString();
    }
}