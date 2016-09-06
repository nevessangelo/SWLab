/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.edu.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 *
 * @author angelo
 */
public class Unzip {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("/home/angelo/SWLab/GetFeatures/Dumps/teste.tar.gz");
            ZipInputStream zipIn = new ZipInputStream(fis);
            ZipEntry entry = zipIn.getNextEntry();
            while (zipIn.available() > 0) {
                System.out.print((char) zipIn.read());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
