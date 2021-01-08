package com.baidu.shop.servce.impl;

import com.alibaba.fastjson.JSONObject;
import com.baidu.shop.base.BaseApiService;
import com.baidu.shop.base.Result;
import com.baidu.shop.dto.SkuDTO;
import com.baidu.shop.dto.SpuDTO;
import com.baidu.shop.dto.SpuDetailDTO;
import com.baidu.shop.entity.*;
import com.baidu.shop.mapper.*;
import com.baidu.shop.service.GoodsService;
import com.baidu.shop.status.HTTPStatus;
import com.baidu.shop.utils.BaiduBeanUtil;
import com.baidu.shop.utils.ObjectUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2 *@ClassName GoodsServiceImpl
 * 3 *@Description: TODO
 * 4 *@Author 王振方
 * 5 *@Date 2021/1/5
 *
 * @Version V1.0
 * 7
 **/
@RestController
public class GoodsServiceImpl extends BaseApiService implements GoodsService {



    @Resource
    private BrandMapper brandMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SpuMapper spuMapper;

    @Resource
    private SpuDetailMapper spuDetailMapper;

    @Resource
    private SkuMapper skuMapper;

    @Resource
    private StockMapper  stockMapper;

    @Transactional
    @Override
    public Result<JSONObject> GoodsUpdateSaleable(SpuDTO spuDTO) {

        final SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);

        if(ObjectUtil.isNotNull(spuEntity.getSaleable()) && spuEntity.getSaleable()==1){
            spuEntity.setSaleable(0);
            spuMapper.updateByPrimaryKeySelective(spuEntity);
        }else{
            spuEntity.setSaleable(1);
            spuMapper.updateByPrimaryKeySelective(spuEntity);
        }
        return this.setResultSuccess();
    }


    @Override
    public Result<SpuDetailEntity> getSpuDetailBySpuId(Integer spuId) {
        SpuDetailEntity spuDetailEntity = spuDetailMapper.selectByPrimaryKey(spuId);
        return this.setResultSuccess(spuDetailEntity);
    }

    @Override
    public Result<SkuDTO> getSkusBySpuId(Integer spuId) {
        List<SkuDTO> skusAndStockBySpuId = skuMapper.getSkusAndStockBySpuId(spuId);
        return this.setResultSuccess(skusAndStockBySpuId);
    }



    @Override
    public Result<List<SpuDTO>> getSpuInfo(SpuDTO spuDTO) {
        //分页
        if(ObjectUtil.isNotNull(spuDTO.getPage()) && ObjectUtil.isNotNull(spuDTO.getRows()))
            PageHelper.startPage(spuDTO.getPage(),spuDTO.getRows());
        Example example = new Example(SpuEntity.class);
        Example.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(spuDTO.getSort()) && !StringUtils.isEmpty(spuDTO.getOrder())){
            PageHelper.orderBy(spuDTO.getOrderByClause());
        }
        //是否上架
        if(ObjectUtil.isNotNull(BaiduBeanUtil.copyProperties(spuDTO,SpuEntity.class).getSaleable())
                && BaiduBeanUtil.copyProperties(spuDTO,SpuEntity.class).getSaleable() < 2){
            criteria.andEqualTo("saleable",spuDTO.getSaleable());
        }
        //模糊查询
        if(!StringUtils.isEmpty(spuDTO.getTitle())) criteria.andLike("title","%" + spuDTO.getTitle() + "%");

        List<SpuEntity> spuEntities = spuMapper.selectByExample(example);

        List<SpuDTO> spuDTOList = spuEntities.stream().map(spuEntity ->{
            SpuDTO spuDTO1 = BaiduBeanUtil.copyProperties(spuEntity, SpuDTO.class);
            //通过分类id集合查询数据
            List<CategoryEntity> categoryEntities  = categoryMapper.selectByIdList(Arrays.asList(spuEntity.getCid1(), spuEntity.getCid2(), spuEntity.getCid3()));
            //便利集合并且将分类名称用/拼接
            String categoryName  = categoryEntities.stream().map(categoryEntity -> categoryEntity.getName()).collect(Collectors.joining("/"));
            spuDTO1.setCategoryName(categoryName);

            BrandEntity brandEntity = brandMapper.selectByPrimaryKey(spuEntity.getBrandId());
            spuDTO1.setBrandName(brandEntity.getName());

            return spuDTO1;
        }).collect(Collectors.toList());


        PageInfo<SpuEntity>  pageInfo = new PageInfo<>(spuEntities);
        return  this.setResult(HTTPStatus.OK,pageInfo.getTotal()+"",spuDTOList);
        //return this.setResultSuccess(pageInfo);

    }

    @Override
    @Transactional
    public Result<JSONObject> saveGoods(SpuDTO spuDTO) {
        System.out.println(spuDTO);
        //新增spu
        final Date date = new Date();
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setSaleable(1);
        spuEntity.setValid(1);
        spuEntity.setCreateTime(date);
        spuEntity.setLastUpdateTime(date);
        spuMapper.insertSelective(spuEntity);
        //新增spuDetail、
        SpuDetailDTO spuDetail = spuDTO.getSpuDetail();
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDetail, SpuDetailEntity.class);
        spuDetailEntity.setSpuId(spuEntity.getId());
        spuDetailMapper.insertSelective(spuDetailEntity);
        //新增sku list插入顺序有序 b,a set a,b treeSet b,a

        List<SkuDTO> skus = spuDTO.getSkus();
        skus.stream().forEach(skuDTO ->{
            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setSpuId(spuEntity.getId());
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuMapper.insertSelective(skuEntity);

            //新增stock
            StockEntity stockEntity = new StockEntity();
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);

        });
        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JSONObject> editGoods(SpuDTO spuDTO) {

        final Date date = new Date();
        //先修改spu
        SpuEntity spuEntity = BaiduBeanUtil.copyProperties(spuDTO, SpuEntity.class);
        spuEntity.setLastUpdateTime(date);
        spuMapper.updateByPrimaryKeySelective(spuEntity);
        //在修改spuDetail
        SpuDetailEntity spuDetailEntity = BaiduBeanUtil.copyProperties(spuDTO.getSpuDetail(), SpuDetailEntity.class);
        spuDetailMapper.updateByPrimaryKeySelective(spuDetailEntity);
        //然后修改skus 最后修改 stock   //通过spuId查询sku信息

        final Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuEntity.getId());
        List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        //得到skuId集合
        List<Long> skuIdList = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
        skuMapper.deleteByIdList(skuIdList);//通过skuId集合删除sku信息
        stockMapper.deleteByIdList(skuIdList);//通过skuId集合删除stock信息

        List<SkuDTO> skus = spuDTO.getSkus();
        skus.stream().forEach(skuDTO -> {

            SkuEntity skuEntity = BaiduBeanUtil.copyProperties(skuDTO, SkuEntity.class);
            skuEntity.setCreateTime(date);
            skuEntity.setLastUpdateTime(date);
            skuEntity.setSpuId(spuEntity.getId());
            skuMapper.insertSelective(skuEntity);
            //新增stock
            StockEntity stockEntity = BaiduBeanUtil.copyProperties(skuDTO, StockEntity.class);
            stockEntity.setSkuId(skuEntity.getId());
            stockEntity.setStock(skuDTO.getStock());
            stockMapper.insertSelective(stockEntity);

        });


        return this.setResultSuccess();
    }
    @Transactional
    @Override
    public Result<JSONObject> GoodsDelete(Integer spuId) {
        //删除spu
        spuMapper.deleteByPrimaryKey(spuId);
        //删除spuDetail
        spuDetailMapper.deleteByPrimaryKey(spuId);
        //删除skus和stock

        //通过spuId 查询sku信息
        final Example example = new Example(SkuEntity.class);
        example.createCriteria().andEqualTo("spuId",spuId);
        final List<SkuEntity> skuEntities = skuMapper.selectByExample(example);
        //得到skuId集合
        final List<Long> skuIdList = skuEntities.stream().map(skuEntity -> skuEntity.getId()).collect(Collectors.toList());
        skuMapper.deleteByIdList(skuIdList);
        stockMapper.deleteByIdList(skuIdList);

        return  this.setResultSuccess();

    }


}
