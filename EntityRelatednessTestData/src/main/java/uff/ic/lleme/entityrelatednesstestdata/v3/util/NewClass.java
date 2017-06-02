/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uff.ic.lleme.entityrelatednesstestdata.v3.util;

/**
 *
 * @author lapaesleme
 */
public class NewClass {

    public static void main(String[] args) {
        String linha = "mvo:Director 	dbo:MovieDirector 	Artist ";
        System.out.printf("\\u%04x \n", (int) linha.charAt(linha.length() - 1));
        System.out.printf("\\u%04x \n", (int) linha.charAt(11));
        System.out.printf("\\u%04x \n", (int) linha.charAt(12));
        System.out.printf("\\u%04x \n", (int) linha.charAt(13));
        System.out.printf("\\u%04x \n", (int) linha.charAt(14));
        linha = linha.replace('\u00a0', '?').replace('\u0009', '@');
        System.out.println(linha);
    }
}
