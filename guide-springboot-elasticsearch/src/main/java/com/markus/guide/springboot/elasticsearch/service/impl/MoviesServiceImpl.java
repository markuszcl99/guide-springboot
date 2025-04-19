package com.markus.guide.springboot.elasticsearch.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.bulk.CreateOperation;
import co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem;
import com.markus.guide.springboot.elasticsearch.domain.MovieDO;
import com.markus.guide.springboot.elasticsearch.service.MoviesService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: markus
 * @date: 2025/4/19 21:25
 * @Description:
 * @Blog: https://markuszhang.com
 * It's my honor to share what I've learned with you!
 */
@Service
public class MoviesServiceImpl implements MoviesService {

    @Resource
    private ElasticsearchClient esClient;

    private final String INDEX_NAME = "movies";


    @Override
    public MovieDO getMovieById(String index, Long id) throws IOException {
        GetResponse<MovieDO> response = esClient.get(g -> g.index(index).id(String.valueOf(id)), MovieDO.class);
        if (response.found()) {
            return response.source();
        }
        return null;
    }

    /**
     * create 文档
     */
    @Override
    public void createMovie(MovieDO movieDO) throws IOException {
        esClient.create(es -> es.index(INDEX_NAME).id(String.valueOf(movieDO.getId())).document(movieDO));
    }


    /**
     * index 文档
     * 与 create 文档动作不同的是：
     * 如果文档不存在就索引新的文档
     * 如果文档存在，则删除旧文档，索引新的文档，并且文档 version + 1
     */
    @Override
    public void indexMovie(MovieDO movieDO) throws IOException {
        esClient.index(es -> es.index(INDEX_NAME).id(String.valueOf(movieDO.getId())).document(movieDO));
    }

    /**
     * update 文档
     * 与 index 文档动作不同的是：
     * 真正的更新文档，不会删除文档，在更新文档的时候，如果有字段变更才会 version + 1
     */
    @Override
    public void updateMovie(MovieDO movieDO) throws IOException {
        esClient.update(es -> es.index(INDEX_NAME).id(String.valueOf(movieDO.getId())).doc(movieDO), MovieDO.class);
    }

    @Override
    public List<BulkResponseItem> bulkMovies(MovieDO movieDO) throws IOException {
        List<BulkOperation> bulkOperations = new ArrayList<>();

        // 先执行删除操作（如果需要删除）
        bulkOperations.add(BulkOperation.of(bulk -> bulk.delete(delete -> delete.id(String.valueOf(movieDO.getId())))));

        // 使用 upsert 来更新或创建文档，避免同时使用 create 和 update
        bulkOperations.add(BulkOperation.of(bulk -> bulk.update(update -> update
                .id(String.valueOf(movieDO.getId()))
                .action(doc -> doc.upsert(movieDO).doc(movieDO)))));

        // 索引操作（如果需要重新索引）
        bulkOperations.add(BulkOperation.of(bulk -> bulk.index(index -> index
                .id(String.valueOf(movieDO.getId()))
                .document(movieDO))));

        // 执行批量操作
        BulkResponse bulkResponse = esClient.bulk(bulk -> bulk.index(INDEX_NAME).operations(bulkOperations));

        // 处理响应结果
        if (bulkResponse.errors()) {
            // 处理错误或失败的操作
            bulkResponse.items().forEach(item -> {
                if (item.error() != null) {
                    System.out.println("Error in operation: " + item.error());
                }
            });
        }

        return bulkResponse.items();
    }

    @Override
    public List<MovieDO> mGetMovies(List<Long> ids) throws IOException {
        MgetResponse<MovieDO> mgetResponse = esClient.mget(mget -> mget.index(INDEX_NAME).ids(ids.stream().map(String::valueOf).toList()), MovieDO.class);
        List<MultiGetResponseItem<MovieDO>> docs = mgetResponse.docs();
        List<MovieDO> movieDOS = new ArrayList<>(docs.size());
        for (MultiGetResponseItem<MovieDO> doc : docs) {
            MovieDO source = doc.result().source();
            movieDOS.add(source);
        }
        return movieDOS;
    }
}
