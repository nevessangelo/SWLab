/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ic.uff.tic10086.exercicios;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author lapaesleme
 */
public class NewClass {

    public static void main(String[] args) {
        String s = "Quinta da Boa Vista/Museu Nacional";
        StringTokenizer defaultTokenizer = new StringTokenizer(s, " ://.-");
        List<String> l = new ArrayList<>();
        while (defaultTokenizer.hasMoreTokens())
            l.add(defaultTokenizer.nextToken());
        System.out.println(String.join(" ", l));
    }
}
