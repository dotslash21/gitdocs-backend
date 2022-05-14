package dev.arunangshu.utils;

public class NickNameGenerator {

    public static String fromEmail(String email) {
        return email.replaceAll("[^a-zA-Z\\d]", "-");
    }
}
