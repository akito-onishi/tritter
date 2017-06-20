package com.example.tritter;

import javax.validation.constraints.Min;

import lombok.Data;

@Data
public class RtFavInputForm {
    @Min(0)
    private int fav;
    private int rt;
    

}
