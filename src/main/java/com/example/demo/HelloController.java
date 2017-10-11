package com.example.demo;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;

import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class HelloController
{
    @RequestMapping("/")
    @ResponseBody
    public String home() {
        return "Hello, Spring Boot Sample Application!";
    }

    @RequestMapping("/els")
    @ResponseBody
    public String els() {

        TransportClient client;
		try {
			Settings settings = Settings.builder()
			        .put("cluster.name", "docker-cluster").build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));

			IndexResponse response = client.prepareIndex("alias_ad_search", "ad", "ad001")
			        .setSource(jsonBuilder()
			                        .startObject()
			                        .field("ad_oid", "ad001")
			                        .field("offer_oid", "001")
			                        .field("postDate", new Date())
			                        .field("message", "trying out Elasticsearch")
			                        .endObject()
			        )
			        .get();

			//bulk upsert
			BulkRequestBuilder bulkRequest = client.prepareBulk();
			bulkRequest.add(client.prepareIndex("alias_ad_search", "ad", "ad002")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("ad_oid", "ad002")
			                        .field("offer_oid", "001")
			                        .field("postDate", new Date())
			                        .field("message", "trying out Elasticsearch")
			                    .endObject()
			                  )
			        );
			bulkRequest.add(client.prepareIndex("alias_ad_search", "ad", "ad003")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("ad_oid", "ad003")
			                        .field("offer_oid", "001")
			                        .field("postDate", new Date())
			                        .field("message", "another post2")
			                    .endObject()
			                  )
			        );

			bulkRequest.add(client.prepareIndex("alias_ad_search", "ad", "ad004")
			        .setSource(jsonBuilder()
			                    .startObject()
			                        .field("ad_oid", "ad004")
			                        .field("offer_oid", "001")
			                        .field("postDate", new Date())
			                        .field("message", "another post")
			                    .endObject()
			                  )
			        );


			BulkResponse bulkResponse = bulkRequest.get();
			if (bulkResponse.hasFailures()) {
			    // process failures by iterating through each bulk response item
			}

			client.close();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}


        return "Hello2, Spring Boot Sample Application!";
    }


}