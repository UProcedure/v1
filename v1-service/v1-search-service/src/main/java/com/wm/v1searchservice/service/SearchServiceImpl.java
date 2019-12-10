package com.wm.v1searchservice.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.wm.api.ISearchService;
import com.wm.base.entity.PageResultBean;
import com.wm.base.entity.ResultBean;
import com.wm.entity.Product;
import com.wm.mapper.ProductMapper;
import com.wm.vo.ProductSearchVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author weimin
 */
@Service
@Slf4j
public class SearchServiceImpl implements ISearchService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SolrClient solrClient;



    @Override
    public ResultBean<String> synAllData() {
        List<ProductSearchVO> list = productMapper.getSearchList();
        for (ProductSearchVO searchVO : list) {
            SolrInputDocument document = new SolrInputDocument();
            document.setField("id",searchVO.getId());
            document.setField("product_name",searchVO.getName());
            document.setField("product_price",searchVO.getPrice());
            document.setField("product_sale_price",searchVO.getSalePrice());
            document.setField("product_sale_point",searchVO.getSalePoint());
            document.setField("product_images",searchVO.getImages());
            try {
                solrClient.add(document);
            } catch (SolrServerException | IOException e) {
                e.printStackTrace();
                return new ResultBean(500,"同步数据失败！");
            }
        }
        return new ResultBean(500,"同步数据成功！");
    }

    @Override
    public ResultBean<String> synById(Long id) {
        Product product = productMapper.selectByPrimaryKey(id);
        SolrInputDocument document = new SolrInputDocument();
        //2.设置相关的属性值
        document.setField("id",product.getId());
        document.setField("product_name",product.getName());
        document.setField("product_price",product.getPrice());
        document.setField("product_sale_price",product.getSalePrice());
        document.setField("product_sale_point",product.getSalePoint());
        document.setField("product_images",product.getImages());
        //3.保存
        try {
            solrClient.add(document);
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean(500,"同步数据失败！");
        }
        return new ResultBean(200,"同步数据成功！");
    }

    @Override
    public ResultBean<String> delById(Long id) {
        try {
            solrClient.deleteById(String.valueOf(id));
            solrClient.commit();
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            //TODO log
            return new ResultBean(500,"同步数据失败！");
        }
        return new ResultBean(200,"同步数据成功！");
    }

    @Override
    public ResultBean<PageResultBean<ProductSearchVO>> queryByKeywords(PageResultBean<ProductSearchVO> page, String keywords) {
        SolrQuery queryCondition = new SolrQuery();
        queryCondition.setStart((page.getPageNum()-1)*page.getPageSize());
        queryCondition.setRows(page.getPageSize());
        queryCondition.setHighlight(true);
        queryCondition.addHighlightField("product_name");
        queryCondition.setHighlightSimplePre("<font color='red'>");
        queryCondition.setHighlightSimplePost("</font>");
        if(keywords == null || "".equals(keywords.trim())){
            queryCondition.setQuery("product_name:华为");
        }else{
            queryCondition.setQuery("product_name:"+keywords);
        }
        List<ProductSearchVO> list = null;
        try {
            QueryResponse response = solrClient.query(queryCondition);
            SolrDocumentList results = response.getResults();
            list = new ArrayList<>(results.size());
            Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
            for (SolrDocument document : results) {
                //document -> product
                ProductSearchVO product = new ProductSearchVO();
                product.setId(Long.parseLong(document.getFieldValue("id").toString()));
                //product.setName(document.getFieldValue("product_name").toString());
                product.setSalePoint(document.getFieldValue("product_sale_point").toString());
                product.setPrice(Long.parseLong(document.getFieldValue("product_price").toString()));
                product.setSalePrice(Long.parseLong(document.getFieldValue("product_sale_price").toString()));
                product.setImages(document.getFieldValue("product_images").toString());
                Map<String, List<String>> map = highlighting.get(document.getFieldValue("id"));
                List<String> productNameHighLighting = map.get("product_name");
                if(productNameHighLighting != null && productNameHighLighting.size() > 0){
                    product.setName(productNameHighLighting.get(0));
                }else{
                    product.setName(document.getFieldValue("product_name").toString());
                }
                list.add(product);
            }
            page.setTotal(results.getNumFound());
            page.setPages((int) ((results.getNumFound()+page.getPageSize())/page.getPageSize()));
            page.setList(list);
        } catch (SolrServerException | IOException e) {
            e.printStackTrace();
            return new ResultBean(500,null);
        }
        return new ResultBean(200,page);
    }
}
