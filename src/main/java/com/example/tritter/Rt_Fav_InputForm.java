package com.example.tritter;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class Rt_Fav_InputForm {
    @Min(0)
    private int fav;
    private int rt;
    

}
