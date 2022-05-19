package com.example.BoardGame;

import java.util.Scanner;

public class CodingTest {
    int[] numbers = new int[5];
    int[] sum=new int [1000];


    int f(int n){
        if(n==1)
            return 0;
       if( sum[n]==sum[n-1])
            return 1;
       else
           f(n-1);
        return 0;
    }

    public static void main(String[] args) {
        CodingTest ct = new CodingTest();
        int k=0;
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 5; i++) {
            ct.numbers[i] = sc.nextInt();
        }
        while ( k< 26) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    if(ct.f(k)==1)
                        continue;
                    else if (ct.f(k)==0){
                        ct.sum[k] = ct.numbers[i] + ct.numbers[j];
                        k++;

                    }
                }
            }

        }

    }
}
