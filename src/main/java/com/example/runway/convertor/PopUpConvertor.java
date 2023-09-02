package com.example.runway.convertor;

import com.example.runway.domain.PopUp;
import com.example.runway.dto.home.HomeRes;

import java.util.ArrayList;
import java.util.List;

public class PopUpConvertor {
    public static List<HomeRes.PopUp> PopUp(List<PopUp> popUps) {
        List<HomeRes.PopUp> popUpList = new ArrayList<>();

        popUps.forEach(
                result -> popUpList.add(
                        new HomeRes.PopUp(
                                result.getId(),
                                result.getImgUrl()
                        )
                )
        );

        return popUpList;
    }
}
