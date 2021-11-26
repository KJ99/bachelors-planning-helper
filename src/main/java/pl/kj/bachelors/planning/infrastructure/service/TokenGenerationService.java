package pl.kj.bachelors.planning.infrastructure.service;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class TokenGenerationService {
    public String generateToken(String prefix, String suffix, int contentLength) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<String> token = executor.submit(() -> {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            String content = this.generateContent(contentLength);
            String timestamp = String.valueOf(Calendar.getInstance().getTimeInMillis());
            String rawValue = String.format("%s%s%s%s", prefix, timestamp, content, suffix);
            md5.update(rawValue.getBytes(StandardCharsets.UTF_8));

            return DatatypeConverter.printHexBinary(md5.digest());
        });

        return token.get();
    }

    public String generateNumericToken(int length) {
        Random random = new Random();
        String[] pinElements = new String[length];
        for(int i = 0; i < pinElements.length; i++) {
            pinElements[i] = String.valueOf(random.nextInt(9));
        }

        return String.join("", pinElements);
    }

    public String generateAlphanumericToken(int length) {
        List<Character> characters = this.getAlphanumericCharset();
        Random random = new Random();
        char[] tokenElements = new char[length];
        for(int i = 0; i < tokenElements.length; i++) {
            tokenElements[i] = characters.get(random.nextInt(characters.size() - 1));
        }

        return String.valueOf(tokenElements);
    }

    private String generateContent(final int length) {
        Random random = new Random();
        byte[] bytes = new byte[length];
        random.nextBytes(bytes);
        return String.valueOf(Hex.encode(bytes));
    }

    private List<Character> getAlphanumericCharset() {
        List<Character> characters = new ArrayList<>();
        int[][] bounds = new int[][] {
                new int[] { 0x30, 0x39 },
                new int[] { 0x41, 0x5A },
                new int[] { 0x61, 0x7A }
        };

        for(int[] bound : bounds) {
            for(int code = bound[0]; code <= bound[1]; code++) {
                characters.add((char) code);
            }
        }

        return characters;
    }
}
