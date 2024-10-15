package com.lzy;

import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws Exception {
//        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));
        System.out.println("Hello world! 李子园");
        String charset = System.out.charset().name();
        System.out.println("charset = " + charset);
        OutputStreamWriter outWriter = new OutputStreamWriter(System.out);
        String outEncoding = outWriter.getEncoding();
        System.out.println("encoding of System.out: " + outEncoding);
//        System.out.getOut(new PrintStream())
    }
}