package com.example.runway.service.home;

import com.example.runway.convertor.PopUpConvertor;
import com.example.runway.domain.PopUp;
import com.example.runway.dto.home.HomeRes;
import com.example.runway.repository.PopUpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PopUpServiceImpl implements PopUpService{
    private final PopUpRepository popUpRepository;
    @Override
    public List<HomeRes.PopUp> getPopUp() {
        List<PopUp> popUps = popUpRepository.findAll();
        return PopUpConvertor.PopUp(popUps);
    }
}
