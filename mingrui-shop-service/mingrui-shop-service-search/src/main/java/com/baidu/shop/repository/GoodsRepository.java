package com.baidu.shop.repository;

import com.baidu.shop.document.GoodsDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * 2 *@ClassName GoodsRepository
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/5
 *
 * @Version V1.0
 * 7
 **/

public interface GoodsRepository extends ElasticsearchRepository<GoodsDoc,Long> {

}
