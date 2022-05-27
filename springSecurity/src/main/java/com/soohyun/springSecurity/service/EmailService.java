package com.soohyun.springSecurity.service;

public interface EmailService {

    void sendMail(String to, String sub, String text);
}