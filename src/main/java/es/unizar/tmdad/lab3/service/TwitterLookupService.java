package es.unizar.tmdad.lab3.service;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.social.twitter.api.*;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import es.unizar.tmdad.lab3.utils.CountryAdapter;

@Service
public class TwitterLookupService {


	@Value("${twitter.consumerKey}")
	private String consumerKey;

	@Value("${twitter.consumerSecret}")
	private String consumerSecret;

	@Value("${twitter.accessToken}")
	private String accessToken;

	@Value("${twitter.accessTokenSecret}")
	private String accessTokenSecret;

	CountryAdapter adapter = new CountryAdapter();



	@Autowired
	private SimpMessageSendingOperations messagingTemplate;

	private ArrayList<String> queries = new ArrayList<>();
	List<StreamListener> twitterList = new ArrayList<StreamListener>();
	Stream s;


	/*public void search(String q) {

		Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		if (queries.size() >= 10) {
			queries.remove(0);
			twitterList.remove(0);
		}
		twitterList.add(new SimpleStreamListener(messagingTemplate, q));
		s = twitter.streamingOperations().filter(q, twitterList);

	}*/

	public Trends trends(String woeid) {
		Twitter twitter = new TwitterTemplate(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		return twitter.searchOperations().getLocalTrends(adapter.getWOEId(woeid));
	}

	public SearchResults emptyAnswer() {
		return new SearchResults(Collections.emptyList(), new SearchMetadata(0, 0 ));
	}

	public Trends trendsEmptyAnswer() {
		return new Trends(null, null);
	}

	private final ConcurrentMap<String, String> connections = new ConcurrentLinkedHashMap.Builder<String, String>()
			.maximumWeightedCapacity(10).build();

	public void search(String query) {
		connections.putIfAbsent(query, query);
	}

	public Set<String> getQueries() {
		return connections.keySet();
	}

}

