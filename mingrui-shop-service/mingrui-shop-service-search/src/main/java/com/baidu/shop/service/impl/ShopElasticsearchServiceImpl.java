package com.baidu.shop.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.document.GoodsDoc;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpecParamDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.entity.BrandEntity;
import com.baidu.shop.entity.CategoryEntity;
import com.baidu.shop.entity.SpecParamEntity;
import com.baidu.shop.entity.SpuDetailEntity;
import com.baidu.shop.feign.BrandFeign;
import com.baidu.shop.feign.CategoryFeign;
import com.baidu.shop.feign.GoodsFeign;
import com.baidu.shop.feign.SpecificationFeign;
import com.baidu.shop.repository.GoodsRepository;
import com.baidu.shop.response.GoodsResponse;
import com.baidu.shop.service.ShopElasticsearchService;
import com.baidu.shop.service.SpecificationService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.ESHighLightUtil;
import com.baidu.shop.utils.JSONUtil;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.math.NumberUtils;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 2 *@ClassName ShopElasticsearchServiceImpl
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/3/4
 *
 * @Version V1.0
 * 7
 **/
@RestController
@Log4j2
public class ShopElasticsearchServiceImpl extends BaseApiService implements ShopElasticsearchService {


    @Autowired
    private GoodsFeign goodsFeign;

    @Autowired
    private SpecificationFeign specificationFeign;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    @Autowired
    private BrandFeign brandFeign;

    @Autowired
    private CategoryFeign categoryFeign;


    @Override
    public GoodsResponse search(String search , Integer page) {

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        //多字段查询
        nativeSearchQueryBuilder.withQuery(
                QueryBuilders.multiMatchQuery(search,"title","brandName","categoryName")
        );
        //结果过滤
        nativeSearchQueryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title","skus"}, null));
        //设置分页
        nativeSearchQueryBuilder.withPageable(PageRequest.of(page - 1,10));
        //设置高亮
        nativeSearchQueryBuilder.withHighlightBuilder(ESHighLightUtil.getHighlightBuilder("title"));
        //聚合 --> 品牌,分类聚合
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("agg_category").field("cid3"));
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("agg_brand").field("brandId"));

        SearchHits<GoodsDoc> searchHits = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), GoodsDoc.class);

        List<GoodsDoc> goodsDocs = ESHighLightUtil.getHighlightList(searchHits.getSearchHits());

        //获取聚合信息
        Aggregations aggregations = searchHits.getAggregations();

        Terms agg_category = aggregations.get("agg_category");
        Terms agg_brand = aggregations.get("agg_brand");

        List<? extends Terms.Bucket> categoryBuckets = agg_category.getBuckets();
        List<? extends Terms.Bucket> brandBuckets = agg_brand.getBuckets();

        List<String> categoryIdList = categoryBuckets.stream().map(categoryBucket ->
                categoryBucket.getKeyAsNumber().longValue() + "").collect(Collectors.toList());

        List<String> brandIdList = brandBuckets.stream().map(brandBucket ->
                brandBucket.getKeyAsNumber().longValue() + "").collect(Collectors.toList());

        //要将List<Long>转换成 String类型的字符串并且用,拼接

        Result<List<CategoryEntity>> categoryResult = categoryFeign.getCategoryByIdList(String.join(",", categoryIdList));
        Result<List<BrandEntity>> brandResult = brandFeign.getBrandByIdList(String.join(",", brandIdList));
        List<CategoryEntity> categoryList = null;
        if(categoryResult.isSuccess()){
            categoryList = categoryResult.getData();
        }
        List<BrandEntity> brandList = null;
        if(brandResult.isSuccess()){
            brandList = brandResult.getData();
        }

        long total = searchHits.getTotalHits();
        long totalPage = Double.valueOf(Math.ceil(Double.valueOf(total) / 10)).longValue();

        return new GoodsResponse(total,totalPage,categoryList,brandList,goodsDocs);
//        for(int i =0; i<categoryList.size();i++){
//
//            System.out.println(categoryList.get(i));
//        };

    }

    @Override
    public Result<JSONObject> initGoodsEsData() {

        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDoc.class);
        if (!indexOperations.exists()) {
            indexOperations.create();
            indexOperations.createMapping();
        }

        //查询mysql中的数据
        List<GoodsDoc> goodsDocs = this.esGoodsInfo();
        elasticsearchRestTemplate.save(goodsDocs);

        return this.setResultSuccess();
    }


    @Override
    public Result<JSONObject> clearGoodsEsData() {

        IndexOperations indexOperations = elasticsearchRestTemplate.indexOps(GoodsDoc.class);

        if (indexOperations.exists()) {
            indexOperations.delete();
        }

        return this.setResultSuccess();
    }

//    @Override
//    public Result<JSONObject> esGoodsInfo() {
//        SpuDTO spuDTO = new SpuDTO();
//        spuDTO.setPage(1);
//        spuDTO.setRows(5);
//
//        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(spuDTO);
//        log.info("goodsFeign.getSpuInfo --> {}",spuInfo);
//
//        if (spuInfo.getCode()==200) {
//                List<GoodsDoc> docList = spuInfo.getData().stream().map(spu -> {
//                GoodsDoc goodsDoc = new GoodsDoc();
//                //spu填充信息
//                goodsDoc.setId(spu.getId().longValue());
//                goodsDoc.setCid1(spu.getCid1().longValue());
//                goodsDoc.setCid2(spu.getCid2().longValue());
//                goodsDoc.setCid3(spu.getCid3().longValue());
//                goodsDoc.setCreateTime(spu.getCreateTime());
//                goodsDoc.setSubTitle(spu.getSubTitle());
//                //可搜索的数据
//                goodsDoc.setTitle(spu.getTitle());
//                goodsDoc.setBrandName(spu.getBrandName());
//                goodsDoc.setCategoryName(spu.getCategoryName());
//
//                //sku数据填充
//                    Result<List<SkuDTO>> skuResult = goodsFeign.getSkusBySpuId(spu.getId());
//                    //Result<List<SkuDTO>> skusInfo = goodsFeign.getSkusBySpuId(spu.getId());
//                if (skuResult.getCode() == 200) {
//                    List<SkuDTO> skuList = skuResult.getData();
//                    List<Long> priceList = new ArrayList<>();
//                    List<Map<String, Object>> skuListMap =
//                            skuList.stream().map(sku -> {
//                                Map<String, Object> map = new HashMap<>();
//                                map.put("id", sku.getId());
//                                map.put("title", sku.getTitle());
//                                map.put("image", sku.getImages());
//                                map.put("price", sku.getPrice());
//                                priceList.add(sku.getPrice().longValue());
//                                return map;
//                            }).collect(Collectors.toList());
//                    goodsDoc.setPrice(priceList);
//                    goodsDoc.setSkus(JSONUtil.toJsonString(skuListMap));
//                }
//
//                //规格数据填充
//                //获取规格参数
//                SpecParamDTO specParamDTO = new SpecParamDTO();
//                specParamDTO.setCid(spu.getCid3());
//                specParamDTO.setSearching(true);//只查询是为查询属性的规格参数
//
//                Result<List<SpecParamEntity>> specParamInfo = specificationFeign.getSpecParamInfo(specParamDTO);
//
//                if (specParamInfo.getCode()==200){
//                    //获取spudetail
//                    Result<SpuDetailEntity> spuDetailBySpuId = goodsFeign.getSpuDetailBySpuId(spu.getId());
//
//
//                    if(spuDetailBySpuId.getCode() == 200){
//                        SpuDetailEntity spuDetail = spuDetailBySpuId.getData();
//                        //将通用规格转换为Map
//                        String genericSpec = spuDetail.getGenericSpec();
//                        Map<String, String> genericSpecMap = JSONUtil.toMapValueString(genericSpec);
//                        //特殊规格转换为map,值为List,因为有可能会有多个例如:颜色
//                        String specialSpec = spuDetail.getSpecialSpec();
//                        Map<String, List<String>> specialSpecMap = JSONUtil.toMapValueStrList(specialSpec);
//                        //存放数据的map集合
//                        Map<String, Object> specs = new HashMap<>();
//                        specParamInfo.getData().stream().forEach(specParam -> {
//                        //将对应的规格名称和值放到Map集合中
//                            if(specParam.getGeneric() == true){//通用规格
//                                specs.put(specParam.getName(),genericSpecMap.get(specParam.getId() + ""));
//                            }else{//特殊规格
//                                specs.put(specParam.getName(),specialSpecMap.get(specParam.getId() + ""));
//                            }
//                        });
//                        goodsDoc.setSpecs(specs);
//                    }
//            }
//
//            return goodsDoc;
//
//            }).collect(Collectors.toList());
//            log.info("docListInfo -->",docList);
//        }
//        return null;
//    }



    //@Override
//    public List<GoodsDoc>  esGoodsInfo() {
//
//        SpuDTO spuDTO = new SpuDTO();
//
//        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(spuDTO);
//
//        if(spuInfo.isSuccess()){
//
//            List<SpuDTO> spuList = spuInfo.getData();
//            List<GoodsDoc> goodsDocList = spuList.stream().map(spu -> {
//                //spu
//                GoodsDoc goodsDoc = new GoodsDoc();
//                goodsDoc.setId(spu.getId().longValue());
//                goodsDoc.setTitle(spu.getTitle());
//                goodsDoc.setBrandName(spu.getBrandName());
//                goodsDoc.setCategoryName(spu.getCategoryName());
//                goodsDoc.setSubTitle(spu.getSubTitle());
//                goodsDoc.setBrandId(spu.getBrandId().longValue());
//                goodsDoc.setCid1(spu.getCid1().longValue());
//                goodsDoc.setCid2(spu.getCid2().longValue());
//                goodsDoc.setCid3(spu.getCid3().longValue());
//                goodsDoc.setCreateTime(spu.getCreateTime());
//
//                //resultType java.util.map java.util.list
//
//                //sku数据 , 通过spuid查询skus
//                Result<List<SkuDTO>> skusInfo = goodsFeign.getSkusBySpuId(spu.getId());
//                if (skusInfo.isSuccess()) {
//                    List<SkuDTO> skuList = skusInfo.getData();
//                    List<Long> priceList = new ArrayList<>();//一个spu的所有商品价格集合
//
//                    List<Map<String, Object>> skuMapList = skuList.stream().map(sku -> {
//
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("id", sku.getId());
//                        map.put("title", sku.getTitle());
//                        map.put("image", sku.getImages());
//                        map.put("price", sku.getPrice());
//
//                        priceList.add(sku.getPrice().longValue());
//                        //id ,title ,image,price
//                        return map;
//                    }).collect(Collectors.toList());
//
//                    goodsDoc.setPrice(priceList);
//                    goodsDoc.setSkus(JSONUtil.toJsonString(skuMapList));
//                }
//
//                //通过cid3查询规格参数, searching为true
//                SpecParamDTO specParamDTO = new SpecParamDTO();
//                specParamDTO.setCid(spu.getCid3());
//                specParamDTO.setSearching(true);
//                Result<List<SpecParamEntity>> specParamInfo = specificationFeign.getSpecParamInfo(specParamDTO);
//                if(specParamInfo.isSuccess()){
//
//                    List<SpecParamEntity> specParamList = specParamInfo.getData();
//                    Result<SpuDetailEntity> spuDetailInfo = goodsFeign.getSpuDetailBySpuId(spu.getId());
//                    if(spuDetailInfo.isSuccess()){
//
//                        SpuDetailEntity spuDetailEntity = spuDetailInfo.getData();
//                        //将json字符串转换成map集合
//                        Map<String, String> genericSpec = JSONUtil.toMapValueString(spuDetailEntity.getGenericSpec());
//                        Map<String, List<String>> specialSpec = JSONUtil.toMapValueStrList(spuDetailEntity.getSpecialSpec());
//
//                        //需要查询两张表的数据 spec_param(规格参数名) spu_detail(规格参数值) --> 规格参数名 : 规格参数值
//                        Map<String, Object> specMap = new HashMap<>();
//                        specParamList.stream().forEach(specParam -> {
//
//                            if (specParam.getGeneric()) {//判断从那个map集合中获取数据
//                                if(specParam.getNumeric() && !StringUtils.isEmpty(specParam.getSegments())){
//
//                                    specMap.put(specParam.getName()
//                                            , chooseSegment(genericSpec.get(specParam.getId() + ""), specParam.getSegments(), specParam.getUnit()));
//                                }else{
//
//                                    specMap.put(specParam.getName(),genericSpec.get(specParam.getId() + ""));
//                                }
//
//                            }else{
//
//                                specMap.put(specParam.getName(),specialSpec.get(specParam.getId() + ""));
//                            }
//
//                        });
//                        goodsDoc.setSpecs(specMap);
//
//                        /*specParamList.stream().forEach(specParam ->
//                                specMap.put(specParam.getName()
//                                        ,specParam.getGeneric() ? genericSpec.get(specParam.getId() + "")
//                                                : specialSpec.get(specParam.getId() + "")
//                                )
//                        );*/
//                    }
//                }
//
//                return goodsDoc;
//            }).collect(Collectors.toList());
//
//            return goodsDocList;
//        }
//
//        return null;
//    }

    //@Override
    private List<GoodsDoc> esGoodsInfo() {
        SpuDTO spuDTO = new SpuDTO();
        /*spuDTO.setPage(1);
        spuDTO.setRows(5);*/

        Result<List<SpuDTO>> spuInfo = goodsFeign.getSpuInfo(spuDTO);
        if(spuInfo.isSuccess()){
            List<SpuDTO> spuList = spuInfo.getData();
            List<GoodsDoc> goodsDocList = spuList.stream().map(spu -> {
                //spu
                GoodsDoc goodsDoc = new GoodsDoc();
                goodsDoc.setId(spu.getId().longValue());
                goodsDoc.setTitle(spu.getTitle());
                goodsDoc.setBrandName(spu.getBrandName());
                goodsDoc.setCategoryName(spu.getCategoryName());
                goodsDoc.setSubTitle(spu.getSubTitle());
                goodsDoc.setBrandId(spu.getBrandId().longValue());
                goodsDoc.setCid1(spu.getCid1().longValue());
                goodsDoc.setCid2(spu.getCid2().longValue());
                goodsDoc.setCid3(spu.getCid3().longValue());
                goodsDoc.setCreateTime(spu.getCreateTime());
                //sku数据 , 通过spuid查询skus
                Map<List<Long>, List<Map<String, Object>>> skusAndPriceMap = this.getSkusAndPriceList(spu.getId());
                skusAndPriceMap.forEach((key,value) -> {
                    goodsDoc.setPrice(key);
                    goodsDoc.setSkus(JSONUtil.toJsonString(value));
                });
                //设置规格参数
                Map<String, Object> specMap = this.getSpecMap(spu);
                goodsDoc.setSpecs(specMap);
                return goodsDoc;
            }).collect(Collectors.toList());

            return goodsDocList;
        }
        return null;
    }

    //获取规格参数map
    private Map<String, Object> getSpecMap(SpuDTO spu){

        SpecParamDTO specParamDTO = new SpecParamDTO();
        specParamDTO.setCid(spu.getCid3());
        specParamDTO.setSearching(true);
        Result<List<SpecParamEntity>> specParamInfo = specificationFeign.getSpecParamInfo(specParamDTO);
        if(specParamInfo.isSuccess()){

            List<SpecParamEntity> specParamList = specParamInfo.getData();
            Result<SpuDetailEntity> spuDetailInfo = goodsFeign.getSpuDetailBySpuId(spu.getId());
            if(spuDetailInfo.isSuccess()){

                SpuDetailEntity spuDetailEntity = spuDetailInfo.getData();
                Map<String, Object> specMap = this.getSpecMap(specParamList, spuDetailEntity);
                return specMap;
            }
        }

        return null;
    }

    private Map<String,Object> getSpecMap(List<SpecParamEntity> specParamList ,SpuDetailEntity spuDetailEntity){

        Map<String, Object> specMap = new HashMap<>();
        //将json字符串转换成map集合
        Map<String, String> genericSpec = JSONUtil.toMapValueString(spuDetailEntity.getGenericSpec());
        Map<String, List<String>> specialSpec = JSONUtil.toMapValueStrList(spuDetailEntity.getSpecialSpec());

        //需要查询两张表的数据 spec_param(规格参数名) spu_detail(规格参数值) --> 规格参数名 : 规格参数值
        specParamList.stream().forEach(specParam -> {

            if (specParam.getGeneric()) {//判断从那个map集合中获取数据
                if(specParam.getNumeric() && !StringUtils.isEmpty(specParam.getSegments())){

                    specMap.put(specParam.getName()
                            , chooseSegment(genericSpec.get(specParam.getId() + ""), specParam.getSegments(), specParam.getUnit()));
                }else{

                    specMap.put(specParam.getName(),genericSpec.get(specParam.getId() + ""));
                }

            }else{

                specMap.put(specParam.getName(),specialSpec.get(specParam.getId() + ""));
            }

        });

        return specMap;
    }

    private Map<List<Long>, List<Map<String, Object>>> getSkusAndPriceList(Integer spuId){

        Map<List<Long>, List<Map<String, Object>>> hashMap = new HashMap<>();
        Result<List<SkuDTO>> skusInfo = goodsFeign.getSkusBySpuId(spuId);
        if (skusInfo.isSuccess()) {
            List<SkuDTO> skuList = skusInfo.getData();
            List<Long> priceList = new ArrayList<>();//一个spu的所有商品价格集合

            List<Map<String, Object>> skuMapList = skuList.stream().map(sku -> {

                Map<String, Object> map = new HashMap<>();
                map.put("id", sku.getId());
                map.put("title", sku.getTitle());
                map.put("image", sku.getImages());
                map.put("price", sku.getPrice());

                priceList.add(sku.getPrice().longValue());
                //id ,title ,image,price
                return map;
            }).collect(Collectors.toList());

            hashMap.put(priceList,skuMapList);
            /*goodsDoc.setPrice(priceList);
            goodsDoc.setSkus(JSONUtil.toJsonString(skuMapList));*/
        }
        return hashMap;
    }





    private String chooseSegment(String value, String segments, String unit) {//800 -> 5000-1000
        double val = NumberUtils.toDouble(value);
        String result = "其它";
        // 保存数值段
        for (String segment : segments.split(",")) {
            String[] segs = segment.split("-");
            // 获取数值范围
            double begin = NumberUtils.toDouble(segs[0]);
            double end = Double.MAX_VALUE;
            if(segs.length == 2){
                end = NumberUtils.toDouble(segs[1]);
            }
            // 判断是否在范围内
            if(val >= begin && val < end){
                if(segs.length == 1){
                    result = segs[0] + unit + "以上";
                }else if(begin == 0){
                    result = segs[1] + unit + "以下";
                }else{
                    result = segment + unit;
                }
                break;
            }
        }
        return result;
    }


}
