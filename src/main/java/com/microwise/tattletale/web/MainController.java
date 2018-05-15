package com.microwise.tattletale.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by lijianfei on 2017/11/7.
 *
 * @author li.jianfei
 * @since 2017/11/7
 */
@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
