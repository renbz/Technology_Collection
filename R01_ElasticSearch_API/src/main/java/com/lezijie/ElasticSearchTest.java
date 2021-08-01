package com.lezijie;

import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Ren
 */

public class ElasticSearchTest {

    // ES服务器IP
    private final static String HOST = "47.94.93.103";

    // ES服务器连接方式
    private final static String SCHEME = "http";

    // 初始化ES服务器集群
    // 参数分别为： ip，端口，连接方式(默认为http)
    private final static HttpHost[] HTTP_HOSTS = {new HttpHost(HOST, 9200, SCHEME)};

    // 客户端
    private RestHighLevelClient client = null;

    String indexName = "user";

    /**
     * 获取客户端
     */
    @Before
    public void getConnect() {
        client = new RestHighLevelClient(RestClient.builder(HTTP_HOSTS));
    }

    /**
     * 关闭连接
     */
    @After
    public void closeConnet() {
        try {
            if (null != client) {
                client.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加数据
     */
    @Test
    public void testCreate() throws IOException {
        // 准备数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("uname", "zhangsan333");
        jsonMap.put("passwd", "12345");
        jsonMap.put("introduce", "testIntroduce");
        // 指定索引库和id及数据
        IndexRequest indexRequest = new IndexRequest(indexName).id("5").source(jsonMap);
        // 执行请求
        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
    }

    /**
     * 查询数据
     */
    @Test
    public void testRetrieve() throws IOException {
        // 指定索引库和id
        GetRequest getRequest = new GetRequest(indexName, "5");
        // 执行请求
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSource());
    }


    /**
     * 修改数据
     */
    @Test
    public void testUpdate() throws IOException {
        // 准备数据
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("username", "lisi");
        jsonMap.put("age", 20);
        jsonMap.put("address", "bj");
        // 指定索引库和id及数据
        UpdateRequest updateRequest = new UpdateRequest(indexName, "5").doc(jsonMap);
        // 执行请求
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.toString());
    }


    /**
     * 删除数据
     */
    @Test
    public void testDelete() throws IOException {
        // 指定索引库和id
        DeleteRequest deleteRequest = new DeleteRequest(indexName, "5");
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        System.out.println(deleteResponse.toString());
    }


    /**
     * 批量增删改操作
     */
    @Test
    public void testCUD() throws IOException {
        // 初始化 BulkRequest
        BulkRequest request = new BulkRequest();
        // 指定索引库和id及数据
        // 批量添加
        request.add(new IndexRequest(indexName).id("6")
                .source(XContentType.JSON, "username", "zhangsan", "age", 18));
        request.add(new IndexRequest(indexName).id("7")
                .source(XContentType.JSON, "username", "lisi", "age", 20));
        // 批量修改
        request.add(new UpdateRequest(indexName, "6")
                .doc(XContentType.JSON, "", ""));
        // 批量删除
        request.add(new DeleteRequest(indexName, "6"));
        // 执行请求
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println(bulkResponse);
    }

    /**
     * 批量查询-查询所有
     */
    @Test
    public void testRetrieveAll() throws IOException {
        // 指定索引库
        SearchRequest searchRequest = new SearchRequest(indexName, "shop");
        // 构建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 添加查询条件
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        // 执行请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 总条数
        System.out.println(searchResponse.getHits().getTotalHits().value);
        // 结果数据(如果不设置返回条数，大于十条默认只返回十条)
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println("分数：" + hit.getScore());
            Map<String, Object> source = hit.getSourceAsMap();
            System.out.println("index -> " + hit.getIndex());
            System.out.println("id -> " + hit.getId());
            for (Map.Entry<String, Object> s : source.entrySet()) {
                System.out.println(s.getKey() + " -- " + s.getValue());
            }
            System.out.println("----------------------------");
        }
    }


    /**
     * 批量查询-匹配查询
     */
    @Test
    public void testRetrieveMatch() throws IOException {
        // 指定索引库
        SearchRequest searchRequest = new SearchRequest("proety");
        // 构建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 添加查询条件
        // 指定从 content 和 goodsName 字段中查询
        String key = "明月香炉";
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, "content", "author"));
        // 执行请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 总条数
        System.out.println(searchResponse.getHits().getTotalHits().value);
        // 结果数据(如果不设置返回条数，大于十条默认只返回十条)
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println("分数：" + hit.getScore());
            Map<String, Object> source = hit.getSourceAsMap();
            System.out.println("index -> " + hit.getIndex());
            System.out.println("id -> " + hit.getId());
            for (Map.Entry<String, Object> s : source.entrySet()) {
                System.out.println(s.getKey() + " -- " + s.getValue());
            }
            System.out.println("----------------------------");


        }
    }

    /**
     * 批量查询-分页查询-按分数或id排序
     */
    @Test
    public void testRetrievePage() throws IOException {
        // 指定索引库
        SearchRequest searchRequest = new SearchRequest("shop");
        // 构建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 添加分页条件，从第 0 个开始，返回 5 个
        searchSourceBuilder.from(0).size(5);
        // 添加查询条件
        // 指定从 goodsName 字段中查询
        String key = "中国移动联通电信";
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, "goodsName"));
        // 按照 score 正序排列(默认倒序)
        //searchSourceBuilder.sort(SortBuilders.scoreSort().order(SortOrder.ASC));
        // 并且按照 id 倒序排列(分数字段会失效返回 NaN)

        //searchSourceBuilder.sort(SortBuilders.fieldSort("_id").order(SortOrder.DESC))
        ;
        // 执行请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 总条数
        System.out.println(searchResponse.getHits().getTotalHits().value);
        // 结果数据(如果不设置返回条数，大于十条默认只返回十条)
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            System.out.println("分数：" + hit.getScore());
            Map<String, Object> source = hit.getSourceAsMap();
            System.out.println("index -> " + hit.getIndex());
            System.out.println("id -> " + hit.getId());
            for (Map.Entry<String, Object> s : source.entrySet()) {
                System.out.println(s.getKey() + " -- " + s.getValue());
            }
            System.out.println("----------------------------");
        }
    }


    /**
     * 批量查询-分页查询-高亮查询
     */
    @Test
    public void testHighlight() throws IOException {
        // 指定索引库
        SearchRequest searchRequest = new SearchRequest("shop");
        // 构建查询对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        // 添加分页条件，从第 0 个开始，返回 5 个
        searchSourceBuilder.from(0).size(5);
        // 构建高亮对象
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        // 指定高亮字段和高亮样式

        highlightBuilder.field("goodsName")
                .preTags("<span style='color:red;'>")
                .postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);
        // 添加查询条件
        // 指定从 goodsName 字段中查询
        String key = "中国移动联通电信";
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(key, "goodsName"));
        // 执行请求
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        // 总条数
        System.out.println(searchResponse.getHits().getTotalHits().value);
        // 结果数据(如果不设置返回条数，大于十条默认只返回十条)
        SearchHit[] hits = searchResponse.getHits().getHits();
        for (SearchHit hit : hits) {
            // 构建项目中所需的数据结果集
            String highlightMessage = String.valueOf(hit.getHighlightFields().get("goodsName").fragments()[0]);
            Integer goodsId = Integer.valueOf((Integer) hit.getSourceAsMap().get("goodsId"));
            String goodsName = String.valueOf(hit.getSourceAsMap().get("goodsName"));
            BigDecimal marketPrice = new BigDecimal(String.valueOf(hit.getSourceAsMap().get("marketPrice")));
            String originalImg = String.valueOf(hit.getSourceAsMap().get("originalImg"));
            System.out.println("goodsId -> " + goodsId);
            System.out.println("goodsName -> " + goodsName);
            System.out.println("highlightMessage -> " + highlightMessage);
            System.out.println("marketPrice -> " + marketPrice);
            System.out.println("originalImg -> " + originalImg);
            System.out.println("----------------------------");
        }
    }


}
