package com.seojey.controller.v1;

import com.seojey.controller.type.RequestRestParam;
import com.seojey.domain.service.ServiceManager;
import com.seojey.domain.service.form.RecommendDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recommend/v1")
public class RecommendRestController {
    @Autowired
    ServiceManager recommendationService ;

    @RequestMapping(value = "/{serviceName}", method = RequestMethod.POST)
    public RecommendDTO recommend(@PathVariable String serviceName ,
                                  @RequestBody RequestRestParam param) {

        return recommendationService.recommend(serviceName, param);
    }


}
