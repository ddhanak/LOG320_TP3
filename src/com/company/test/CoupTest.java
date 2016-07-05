package com.company.test;

import com.company.Coup;
import org.junit.Test;

public class CoupTest {
    @Test
    public void constructionAPartirDenTexte() {
        Coup coup = new Coup("A2 - D6");

        System.out.println(coup);
    }
}
