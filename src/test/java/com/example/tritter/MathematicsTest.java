package com.example.tritter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;

public class MathematicsTest {

    @Test
    public void ok() {
        //System.out.println(Mathematics.sqrt(3));
        
        assertThat(Mathematics.sqrt(9),is(3d));
    }
    @Test
    public void zero(){

        assertThat(Mathematics.sqrt(0),is(0));
    }
}
