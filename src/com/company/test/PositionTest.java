package com.company.test;

import com.company.Position;
import org.junit.Test;

public class PositionTest {
    @Test
    public void constructionAPartirDenTexte() {
        Position position = new Position("D5");

        System.out.println(position);
    }
}
