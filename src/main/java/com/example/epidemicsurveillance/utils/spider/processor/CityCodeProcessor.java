package com.example.epidemicsurveillance.utils.spider.processor;

import com.example.epidemicsurveillance.entity.CityCode;
import com.example.epidemicsurveillance.service.ICityCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zero
 * @date 2021/10/22 15:22
 * @description
 * @since 1.8
 **/
@Component
public class CityCodeProcessor implements PageProcessor {
    @Autowired
    private ICityCodeService cityCodeService;

    @Override
    public void process(Page page) {
        final List<Selectable> nodes = page.getHtml().xpath("//tr").nodes();
        List<CityCode> list = new ArrayList<>();
        for(Selectable item : nodes) {
            final String code = item.xpath("//td[2]/text()").toString();
            final String city = item.xpath("//td[3]/text()").toString();
            System.out.println(code);
            System.out.println(city);
            if((code != null && !code.equals("")) && (city != null && !city.equals(""))) {
                CityCode cityCode = new CityCode() {{
                    setCode(code);
                    setCity(city);
                }};
                list.add(cityCode);
            }
        }
        page.putField("cityCodeList", list);
        cityCodeService.saveBatch(list,list.size());
    }

    @Override
    public Site getSite() {
        return Site.me().setSleepTime(3000).setRetryTimes(3);
    }
}
